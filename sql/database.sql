CREATE SCHEMA cast_xapp_tools
  AUTHORIZATION operator;

CREATE TABLE cast_xapp_tools.ari_master
(
  source_opt character varying(30),
  create_date date,
  run_status character varying(30),
  schema_name character varying(200),
  application_id integer,
  application_name character varying(200),
  health_factor_id integer,
  tech_crit_id integer,
  snapshot_id integer,
  snapshot_date date,
  object_id integer,
  object_full_name character varying(255),
  ap_priority integer,
  object_name character varying(255),
  metric_id integer,
  aggregate_weight integer,
  risk_propagation_factor bigint,
  violation_index bigint,
  propagated_risk_index bigint,
  violation_status character varying(50),
  object_status character varying(50)
);

ALTER TABLE cast_xapp_tools.ari_master
  OWNER TO operator;

CREATE OR REPLACE FUNCTION cast_xapp_tools.ari_create_ap_items(IN source_schema character varying)
  RETURNS TABLE(source_opt character varying, create_date date, run_status character varying, schema_name character varying, application_id integer, application_name character varying, health_factor_id integer, tech_crit_id integer, snapshot_id integer, snapshot_date date, object_id integer, object_full_name character varying, ap_priority integer, object_name character varying, metric_id integer, aggregate_weight integer, risk_propagation_factor bigint, violation_index bigint, propagated_risk_index bigint, violation_status character varying, object_status character varying) AS
$BODY$

DECLARE   

   v_search_path varchar(200);
BEGIN

   v_search_path := (select set_search_path from set_search_path('cast_xapp_tools,' || source_schema));

   delete from ari_master where ari_master.schema_name = source_schema;

   insert into ari_master
	select distinct
	'ari' as source_opt,
	current_timestamp at time zone 'us/eastern' as create_date,
	'prod' as run_status,
	source_schema,
	(select dss_objects.object_id from dss_objects where object_type_id = -102) as application_id,
	(select dss_objects.object_name from dss_objects where object_type_id = -102)application_name,
	dqt.b_criterion_id health_factor_id,
	dqt.t_criterion_id tech_crit_id,
	dss.snapshot_id ,
	dss.snapshot_date ,
	dmr.object_id ,
	dso.object_full_name ,
	1 as ap_priority ,
	dso.object_name ,
	dqt.metric_id ,
	dqt. t_weight aggregate_weight,
	dsr.risk_propagation_factor ,
	dsr.violation_index ,
	dsr.propagated_risk_index ,
	cvs.violation_status,
	cos.object_status 
   from
	dss_metric_results dmr,
	dss_objects dso,
	dss_quality_tree dqt,
	dss_snapshots dss,
	dss_snapshot_ranking dsr,
	csv_objects_statuses cos,
	csv_violation_statuses cvs
   where dmr.metric_value_index = 1
	and dso.object_id=dmr.object_id
	and dmr.metric_id=dqt.metric_id+1
	and dss.snapshot_id=(SELECT MAX(DSS.SNAPSHOT_ID) FROM DSS_SNAPSHOTS dss) 
	and dss.snapshot_id=dmr.snapshot_id
	and dmr.object_id=dsr.object_id
	and dmr.snapshot_id=dsr.snapshot_id
	and dsr.business_criterion_id=dqt.b_criterion_id
	and dmr.object_id=cvs.object_id
	and dmr.snapshot_id=cvs.snaphot_id
	and dmr.metric_id=cvs.diag_id+1
	and dmr.object_id=cos.object_id
	and dmr.snapshot_id=cos.snaphot_id
	and dqt.b_criterion_id in (60011,60012,60013,60014,60016)
   order by snapshot_id,metric_id,propagated_risk_index desc;

   RETURN QUERY
	SELECT * FROM ari_master;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION cast_xapp_tools.ari_create_ap_items(character varying)
  OWNER TO operator;

CREATE OR REPLACE FUNCTION cast_xapp_tools.ari_insert_ap_items(
	source_schema character varying,
	health_factors integer[],
	ap_limit integer DEFAULT 99999)
RETURNS SETOF "TABLE(metric_id integer, object_id integer, first_snapshot_date timestamp without time zone, last_snapshot_date timestamp without time zone, user_name character varying, sel_date timestamp without time zone, priority integer, action_def character varying)"
    LANGUAGE 'plpgsql'
    COST 100.0
    VOLATILE NOT LEAKPROOF 
    ROWS 1000.0
AS $function$

DECLARE   
   v_sql varchar(2000);
   v_hf varchar(200);
   v_limit integer;
   v_search_path varchar(200);
BEGIN

   v_search_path := (select set_search_path from set_search_path('cast_xapp_tools,' || source_schema));

   v_limit := (ap_limit -
	       (select count(*) from viewer_action_plans vap, dss_violation_statuses dvs
		where 1=1
		  and dvs.snapshot_id = (SELECT MAX(DSS.SNAPSHOT_ID) FROM DSS_SNAPSHOTS DSS)
		  and dvs.object_id = vap.object_id  
		  and dvs.diag_id = vap.metric_id 
		  and dvs.violation_status <> 2 
  		) + (select count(*) from viewer_action_plans where viewer_action_plans.last_snapshot_date < now()::date));

   IF v_limit < 0 THEN 
      v_limit:=0;
   END IF;

   insert into viewer_action_plans 
     select sub.metric_id,sub.object_id,sub.snapshot_date,sub.last_snapshot_date,'input',sub.sel_date,sub.ap_priority,sub.source_opt 
from 
 (select ari.metric_id,row_number() over (partition by metric_id) rownum,ari.object_id,ari.snapshot_date,ari.create_date as last_snapshot_date,'input',ari.create_date as sel_date,ari.ap_priority,ari.source_opt 
	   from ari_master ari
	  where not exists (select 1 from viewer_action_plans vap 
			     where vap.metric_id = ari.metric_id
			       and vap.object_id = ari.object_id)
	    and ari.health_factor_id = ANY(ARRAY[$2])
       ) sub 
       where rownum<=10 limit(v_limit); 
	
   RETURN QUERY
	SELECT * FROM viewer_action_plans;
	
END

$function$;

ALTER FUNCTION cast_xapp_tools.ari_insert_ap_items(character varying, integer[], integer)
    OWNER TO operator;

CREATE OR REPLACE FUNCTION cast_xapp_tools.set_search_path(path text, is_local boolean DEFAULT false)
  RETURNS text AS
$BODY$
    SELECT set_config('search_path', regexp_replace(path, '[^\w ,]', '', 'g'), is_local);
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION cast_xapp_tools.set_search_path(text, boolean)
  OWNER TO operator;
  


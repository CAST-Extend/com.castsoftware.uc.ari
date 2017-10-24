package com.castsoftware.tools.bean;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.castsoftware.tools.ARI;
import com.castsoftware.tools.util.InputParameters;

public class AriStagingBean implements BaseBean
{
	private Logger logger = Logger.getLogger(AriStagingBean.class);

	private String sourceOpt;
	private Date createDate;
	private String runStatus;
	private String schemaName;
	private int applicationId;
	private String applicationName;
	private int healthFactorId;
	private int techCriteriaId;
	private int snapshotId;
	private Date snapshotDate;
	private int objectId;
	private String ObjectFullName;
	private int apPriority;
	private String objectName;
	private int metricId;
	private int aggregateWeight;
	private long riskPropagationFactor;
	private long violationIndex;
	private long propagatedRiskIndex;
	private String violationStatus;
	private String objectStatus;

	public AriStagingBean()
	{
		super();
	};

	public AriStagingBean(ResultSet rs) throws SQLException
	{
		setSourceOpt(rs.getString("source_opt"));
		setCreateDate(rs.getDate("create_date"));
		setRunStatus(rs.getString("run_status"));
		setSchemaName(rs.getString("schema_name"));
		setApplicationId(rs.getInt("application_id"));
		setApplicationName(rs.getString("application_name"));
		setHealthFactorId(rs.getInt("health_factor_id"));
		setTechCriteriaId(rs.getInt("tech_crit_id"));
		setSnapshotId(rs.getInt("snapshot_id"));
		setSnapshotDate(rs.getDate("snapshot_date"));
		setObjectId(rs.getInt("object_id"));
		setObjectFullName(rs.getString("object_full_name"));
		setApPriority(rs.getInt("ap_priority"));
		setMetricId(rs.getInt("metric_id"));
		setAggregateWeight(rs.getInt("aggregate_weight"));
		setRiskPropagationFactor(rs.getLong("risk_propagation_factor"));
		setViolationIndex(rs.getLong("violation_index"));
		setRiskPropagationFactor(rs.getLong("propagated_risk_index"));
		setViolationStatus(rs.getString("violation_status"));
		setObjectStatus(rs.getString("object_status"));

	};

	public AriStagingBean(String sourceOpt, Date createDate, String runStatus, String schemaName, int applicationId,
			String applicationName, int healthFactorId, int techCriteriaId, int snapshotId, Date snapshotDate,
			int objectId, String ObjectFullName, int apPriority, String objectName, int metricId, int aggregateWeight,
			long riskPropagationFactor, long violationIndex, long propagatedRiskIndex, String violationStatus,
			String objectStatus)
	{
		this.sourceOpt = sourceOpt;
		this.createDate = createDate;
		this.runStatus = runStatus;
		this.schemaName = schemaName;
		this.applicationId = applicationId;
		this.applicationName = applicationName;
		this.healthFactorId = healthFactorId;
		this.techCriteriaId = techCriteriaId;
		this.snapshotId = snapshotId;
		this.snapshotDate = snapshotDate;
		this.objectId = objectId;
		this.ObjectFullName = ObjectFullName;
		this.apPriority = apPriority;
		this.objectName = objectName;
		this.metricId = metricId;
		this.aggregateWeight = aggregateWeight;
		this.riskPropagationFactor = riskPropagationFactor;
		this.violationIndex = violationIndex;
		this.propagatedRiskIndex = propagatedRiskIndex;
		this.violationStatus = violationStatus;
		this.objectStatus = objectStatus;

	}
	
	public static String header()
	{
		return "source Opt\tCreate Date\tRun Status\tSchema Name\tApp Id\tApp Name\tHealth Factor Id\t"+
				"Tech Criteria Id\tSnapshot Id\tSnapshot Date\tObject Id\tObject Full Name\tPriority\tObject Name\t" +
				"Metric Id\tAggregate Weight\tRisk Propagation Factor\tViolation Index\tPropagated Risk Index\t" +
				"ViolationStatus\tObject Status\n";

	}
	
//	@Override
	public String toString()
	{
		return String.format("%s\t%s\t%s\t%s\t%d\t%s\t%d\t%d\t%d\t%s\t%d\t%s\t%d\t%s\t%d\t%d\t%d\t%d\t%d\t%s\t%s\n", 
				sourceOpt,createDate,runStatus,schemaName,applicationId,applicationName,healthFactorId,techCriteriaId,snapshotId,
				snapshotDate,objectId,ObjectFullName,apPriority,objectName,metricId,aggregateWeight,riskPropagationFactor,
				violationIndex,propagatedRiskIndex,violationStatus,objectStatus);

				
				
//		return String.format(
//				"Staging[sourceOpt=%s, createDate='%s', runStatus='%s', schemaName='%s', "
//						+ "applicationId=%d, applicationName='%s', healthFactorId=%d, techCriteriaId=%d, "
//						+ "snapshotId=%d]",
//				sourceOpt, createDate, runStatus, schemaName, applicationId, applicationName, healthFactorId,
//				techCriteriaId, snapshotId);
	}

	public String getSourceOpt()
	{
		return sourceOpt;
	}

	public void setSourceOpt(String sourceOpt)
	{
		this.sourceOpt = sourceOpt;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date date)
	{
		this.createDate = date;
	}

	public String getRunStatus()
	{
		return runStatus;
	}

	public void setRunStatus(String runStatus)
	{
		this.runStatus = runStatus;
	}

	public String getSchemaName()
	{
		return schemaName;
	}

	public void setSchemaName(String schemaName)
	{
		this.schemaName = schemaName;
	}

	public int getApplicationId()
	{
		return applicationId;
	}

	public void setApplicationId(int applicationId)
	{
		this.applicationId = applicationId;
	}

	public String getApplicationName()
	{
		return applicationName;
	}

	public void setApplicationName(String applicationName)
	{
		this.applicationName = applicationName;
	}

	public int getHealthFactorId()
	{
		return healthFactorId;
	}

	public void setHealthFactorId(int healthFactorId)
	{
		this.healthFactorId = healthFactorId;
	}

	public int getTechCriteriaId()
	{
		return techCriteriaId;
	}

	public void setTechCriteriaId(int techCriteriaId)
	{
		this.techCriteriaId = techCriteriaId;
	}

	public int getSnapshotId()
	{
		return snapshotId;
	}

	public void setSnapshotId(int snapshotId)
	{
		this.snapshotId = snapshotId;
	}

	public Date getSnapshotDate()
	{
		return snapshotDate;
	}

	public void setSnapshotDate(Date snapshotDate)
	{
		this.snapshotDate = snapshotDate;
	}

	public int getObjectId()
	{
		return objectId;
	}

	public void setObjectId(int objectId)
	{
		this.objectId = objectId;
	}

	public String getObjectFullName()
	{
		return ObjectFullName;
	}

	public void setObjectFullName(String objectFullName)
	{
		ObjectFullName = objectFullName;
	}

	public int getApPriority()
	{
		return apPriority;
	}

	public void setApPriority(int apPriority)
	{
		this.apPriority = apPriority;
	}

	public String getObjectName()
	{
		return objectName;
	}

	public void setObjectName(String objectName)
	{
		this.objectName = objectName;
	}

	public int getMetricId()
	{
		return metricId;
	}

	public void setMetricId(int metricId)
	{
		this.metricId = metricId;
	}

	public int getAggregateWeight()
	{
		return aggregateWeight;
	}

	public void setAggregateWeight(int aggregateWeight)
	{
		this.aggregateWeight = aggregateWeight;
	}

	public long getRiskPropagationFactor()
	{
		return riskPropagationFactor;
	}

	public void setRiskPropagationFactor(long riskPropagationFactor)
	{
		this.riskPropagationFactor = riskPropagationFactor;
	}

	public long getViolationIndex()
	{
		return violationIndex;
	}

	public void setViolationIndex(long violationIndex)
	{
		this.violationIndex = violationIndex;
	}

	public long getPropagatedRiskIndex()
	{
		return propagatedRiskIndex;
	}

	public void setPropagatedRiskIndex(long propagatedRiskIndex)
	{
		this.propagatedRiskIndex = propagatedRiskIndex;
	}

	public String getViolationStatus()
	{
		return violationStatus;
	}

	public void setViolationStatus(String violationStatus)
	{
		this.violationStatus = violationStatus;
	}

	public String getObjectStatus()
	{
		return objectStatus;
	}

	public void setObjectStatus(String objectStatus)
	{
		this.objectStatus = objectStatus;
	}

}

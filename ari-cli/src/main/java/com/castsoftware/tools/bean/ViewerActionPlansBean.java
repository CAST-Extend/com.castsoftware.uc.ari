package com.castsoftware.tools.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ViewerActionPlansBean implements BaseBean
{
	private int metricId;
	private int objectId;
	private Date firstSnapshotDate;
	private Date lastSnapshotDate;
	private String userName;
	private Date SelDate;
	private int priority;
	private String actionDef;

	public ViewerActionPlansBean()
	{
		super();
	}

	public ViewerActionPlansBean(ResultSet rs) throws SQLException
	{
		setMetricId(rs.getInt("metric_id"));
		setObjectId(rs.getInt("object_id"));
		setFirstSnapshotDate(rs.getDate("first_snapshot_date"));
		setLastSnapshotDate(rs.getDate("last_snapshot_date"));
		setUserName(rs.getString("user_name"));
		setSelDate(rs.getDate("sel_date"));
		setPriority(rs.getInt("priority"));
		setActionDef(rs.getString("action_def"));
	}

	public int getMetricId()
	{
		return metricId;
	}

	public void setMetricId(int metricId)
	{
		this.metricId = metricId;
	}

	public int getObjectId()
	{
		return objectId;
	}

	public void setObjectId(int objectId)
	{
		this.objectId = objectId;
	}

	public Date getFirstSnapshotDate()
	{
		return firstSnapshotDate;
	}

	public void setFirstSnapshotDate(Date firstSnapshotDate)
	{
		this.firstSnapshotDate = firstSnapshotDate;
	}

	public Date getLastSnapshotDate()
	{
		return lastSnapshotDate;
	}

	public void setLastSnapshotDate(Date lastSnapshotDate)
	{
		this.lastSnapshotDate = lastSnapshotDate;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public Date getSelDate()
	{
		return SelDate;
	}

	public void setSelDate(Date selDate)
	{
		SelDate = selDate;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	public String getActionDef()
	{
		return actionDef;
	}

	public void setActionDef(String actionDef)
	{
		this.actionDef = actionDef;
	}

	
	
}

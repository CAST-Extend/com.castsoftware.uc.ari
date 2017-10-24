package com.castsoftware.jenkins.CastARIConnector;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class ARILinkage extends AbstractDescribableImpl<ARILinkage>
{
	private String operation;
	private String appName;
	private String schemaName;
	private String limit;
	private boolean changablity;
	private boolean efficiency;
	private boolean transferability;
	private boolean security;
	private boolean robustness;
	
	@DataBoundConstructor
	public ARILinkage(String operation, String schemaName, String appName, String limit, boolean changablity,
			boolean efficiency, boolean transferability, boolean security, boolean robustness)
	{
		setOperation(operation);
		setAppName(appName);
		setSchemaName(schemaName);
		setLimit(limit);
		setChangablity(changablity);
		setEfficiency(efficiency);
		setTransferability(transferability);
		setSecurity(security);
		setRobustness(robustness);
	}

	public String getOperation()
	{
		return operation;
	}

	public void setOperation(String operation)
	{
		this.operation = operation;
	}

	public String getAppName()
	{
		return appName;
	}

	public void setAppName(String appName)
	{
		this.appName = appName;
	}

	public String getSchemaName()
	{
		return schemaName;
	}

	public void setSchemaName(String schemaName)
	{
		this.schemaName = schemaName;
	}

	public String getLimit()
	{
		return limit;
	}

	public void setLimit(String limit)
	{
		this.limit = limit;
	}

	public boolean isChangablity()
	{
		return changablity;
	}

	public void setChangablity(boolean changablity)
	{
		this.changablity = changablity;
	}

	public boolean isEfficiency()
	{
		return efficiency;
	}

	public void setEfficiency(boolean efficiency)
	{
		this.efficiency = efficiency;
	}

	public boolean isTransferability()
	{
		return transferability;
	}

	public void setTransferability(boolean transferability)
	{
		this.transferability = transferability;
	}

	public boolean isSecurity()
	{
		return security;
	}

	public void setSecurity(boolean security)
	{
		this.security = security;
	}

	public boolean isRobustness()
	{
		return robustness;
	}

	public void setRobustness(boolean robustness)
	{
		this.robustness = robustness;
	}

	@Override
	public Descriptor<ARILinkage> getDescriptor()
	{
		return (DescriptorImpl) super.getDescriptor();
	}

	@Extension
	public static class DescriptorImpl extends Descriptor<ARILinkage>
	{
		public DescriptorImpl()
		{
			load();
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject formData) throws FormException
		{
			req.bindJSON(this, formData);
			save();
			return true;
			// return super.configure(req, formData);
		}

		@Override
		public String getDisplayName()
		{
			return "";
		}

	}

}

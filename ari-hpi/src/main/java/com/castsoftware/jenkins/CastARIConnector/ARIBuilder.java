package com.castsoftware.jenkins.CastARIConnector;

import hudson.Launcher;
import hudson.RelativePath;
import hudson.Extension;
import hudson.FilePath;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Builder;
import hudson.tasks.Shell;
import hudson.tasks.BatchFile;
import hudson.tasks.BuildStepDescriptor;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import com.castsoftware.tools.ARI;
import com.castsoftware.tools.util.Constants;
import com.castsoftware.tools.util.DatabaseConnection;

import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Sample {@link Builder}.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked and a new
 * {@link HelloWorldBuilder} is created. The created instance is persisted to
 * the project configuration XML by using XStream, so this allows you to use
 * instance fields (like {@link #name}) to remember the configuration.
 *
 * <p>
 * When a build is performed, the {@link #perform} method will be invoked.
 *
 * @author Kohsuke Kawaguchi
 */
public class ARIBuilder extends Builder implements SimpleBuildStep
{
	private ArrayList<ARILinkage> ariLinkage;

	String databaseHost;
	String databaseName;
	String databasePort;
	String castUserName;
	String castUserPassword;

	// Fields in config.jelly must match the parameter names in the
	// "DataBoundConstructor"
	@DataBoundConstructor
	public ARIBuilder(String databaseHost, String databaseName, String databasePort, String castUserName,
			String castUserPassword, ArrayList<ARILinkage> ariLinkage)
	{
		this.databaseHost = databaseHost;
		this.databaseName = databaseName;
		this.databasePort = databasePort;
		this.castUserName = castUserName;
		this.castUserPassword = castUserPassword;

		this.ariLinkage = ariLinkage;

	}

	public ArrayList<ARILinkage> getAriLinkage()
	{
		return ariLinkage;
	}

	public void setAriLinkage(ArrayList<ARILinkage> ariLinkage)
	{
		this.ariLinkage = ariLinkage;
	}

	public String getDatabaseHost()
	{
		return databaseHost;
	}

	public void setDatabaseHost(String databaseHost)
	{
		this.databaseHost = databaseHost;
	}

	public String getDatabaseName()
	{
		return databaseName;
	}

	public void setDatabaseName(String databaseName)
	{
		this.databaseName = databaseName;
	}

	public String getDatabasePort()
	{
		return databasePort;
	}

	public void setDatabasePort(String databasePort)
	{
		this.databasePort = databasePort;
	}

	public String getCastUserName()
	{
		return castUserName;
	}

	public void setCastUserName(String castUserName)
	{
		this.castUserName = castUserName;
	}

	public String getCastUserPassword()
	{
		return castUserPassword;
	}

	public void setCastUserPassword(String castUserPassword)
	{
		this.castUserPassword = castUserPassword;
	}

	private static String OS = System.getProperty("os.name").toLowerCase();

	public static boolean isWindows()
	{
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isUnix()
	{
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}

	@Override
	public void perform(Run<?, ?> build, FilePath workspace, Launcher launcher, TaskListener listener)
	{
		ArrayList<String> args = new ArrayList<String>();

		String extractorLocation = this.getDescriptor().getARIExportLoc();
		if (extractorLocation == null || extractorLocation.isEmpty()) {
			listener.getLogger().println("Extraction location is empty");
			build.setResult(Result.FAILURE);
			return;
		}
		extractorLocation = new File(extractorLocation + "/ARI-CLI.jar").toString();

		for (ARILinkage link : getAriLinkage()) {
			args.clear();
			String limit = link.getLimit();

			switch (link.getOperation()) {
				case "Generate":
					args.add("-" + Constants.CMD_GENERATE);
					listener.getLogger().print("Generating new action items ");
					break;

				case "Publish":
					args.add("-" + Constants.CMD_PUBLISH);
					listener.getLogger().format("Publishing the next %s new action items ", limit);
					break;

				case "Both generate and publish":
				default:
					args.add("-" + Constants.CMD_GENERATE);
					args.add("-" + Constants.CMD_PUBLISH);
					listener.getLogger().format("Generating then Publishing the next %s new action items ", limit);
					break;
			}
			listener.getLogger().format("for %s on %s\n", link.getAppName(), getDatabaseHost());

			args.add("-" + Constants.CMD_HOST);
			args.add(getDatabaseHost());

			args.add("-" + Constants.CMD_DATABASE);
			args.add(getDatabaseName());

			args.add("-" + Constants.CMD_PORT);
			args.add(getDatabasePort());

			args.add("-" + Constants.CMD_USER);
			args.add(getCastUserName());

			args.add("-" + Constants.CMD_PASSWORD);
			args.add(getCastUserPassword());

			args.add("-" + Constants.CMD_CENTRAL_DB);
			args.add(link.getSchemaName());

			args.add("-" + Constants.CMD_LIMIT);
			args.add(link.getLimit());

			boolean changablity = link.isChangablity();
			boolean efficiency = link.isEfficiency();
			boolean transferability =  link.isTransferability();
			boolean security = link.isSecurity();
			boolean robustness = link.isRobustness();
 
			String hf = "";
			if (!changablity && !efficiency && !transferability && !security && !robustness)
			{
				hf="all";
			} else {
				if (changablity) hf = String.format("%s %s", hf,Constants.CMD_HF_CHANGABLITY);
				if (efficiency) hf = String.format("%s %s", hf,Constants.CMD_HF_EFFICIENCY);
				if (transferability) hf = String.format("%s %s", hf,Constants.CMD_HF_TRANSFERABILITY);
				if (security) hf = String.format("%s %s", hf,Constants.CMD_HF_SECURITY);
				if (robustness) hf = String.format("%s %s", hf,Constants.CMD_HF_ROBUSTNESS);
			}
			
			args.add("-hf");
			args.add(hf);
			
			String command = String.format("java -jar %s ", extractorLocation);
			for (String s : args) {
				command = String.format("%s %s", command, s);
			}

			boolean rslt = true;
			try {
				if (isUnix()) {
					listener.getLogger().println("Executing Unix Shell");
					Shell shell = new Shell(command);
					rslt = shell.perform((AbstractBuild<?, ?>) build, launcher, listener);

				} else if (isWindows()) {
					listener.getLogger().println("Executing Windows Batch");
					
					command = String.format("@echo off\n%s", command);
					
					BatchFile batchFileRefresh = new BatchFile(command);
					rslt = batchFileRefresh.perform((AbstractBuild<?, ?>) build, launcher, listener);
				} else {
					listener.getLogger().println("Error - Unsupported Operating System");
				}
			} catch (InterruptedException e) {
				listener.getLogger().println(e.toString());
				rslt = false;
			}
			if (!rslt) {
				build.setResult(Result.UNSTABLE);
			}

		}

		build.setResult(Result.SUCCESS);
	}

	// Overridden for better type safety.
	// If your plugin doesn't really define any property on Descriptor,
	// you don't have to do this.
	@Override
	public DescriptorImpl getDescriptor()
	{
		return (DescriptorImpl) super.getDescriptor();
	}

	/**
	 * Descriptor for {@link HelloWorldBuilder}. Used as a singleton. The class
	 * is marked as public so that it can be accessed from views.
	 *
	 * <p>
	 * See
	 * {@code src/main/resources/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly}
	 * for the actual HTML fragment for the configuration screen.
	 */
	@Extension // This indicates to Jenkins that this is an implementation of an
				// extension point.
	public static final class DescriptorImpl extends BuildStepDescriptor<Builder>
	{
		String ARIExportLoc;

		/**
		 * In order to load the persisted global configuration, you have to call
		 * load() in the constructor.
		 */
		public DescriptorImpl()
		{
			load();
		}

		private FormValidation validateField(String value, String message)
		{
			if (value.length() == 0) return FormValidation.error(message);
			return FormValidation.ok();
		}

		public FormValidation doCheckDatabaseHost(@QueryParameter String value) throws IOException, ServletException
		{
			return validateField(value, "Please set the Database Host. It is mandatory");
		}

		public FormValidation doCheckDatabaseName(@QueryParameter String value) throws IOException, ServletException
		{
			return validateField(value, "Please set the Database Name. It is mandatory");
		}

		public FormValidation doCheckDatabasePort(@QueryParameter String value) throws IOException, ServletException
		{
			if (value.length() == 0) return FormValidation.error("Please set the Database Port. It is mandatory");
			else if (!NumberUtils.isNumber(value)) return FormValidation.error("Database Port must be a number");
			else
				return FormValidation.ok();
		}

		public FormValidation doCheckCastUserName(@QueryParameter String value) throws IOException, ServletException
		{
			return validateField(value, "Please set the CAST User Name. It is mandatory");
		}

		public FormValidation doCheckCastUserPassword(@QueryParameter String value) throws IOException, ServletException
		{
			return validateField(value, "Please set the CAST User Password. It is mandatory");
		}

		public FormValidation doTestDatabaseConnection(@QueryParameter("useDatabase") final String useDatabase,
				@QueryParameter("databaseHost") final String databaseHost,
				@QueryParameter("databaseName") final String databaseName,
				@QueryParameter("databasePort") final String databasePort,
				@QueryParameter("castUserName") final String castUserName,
				@QueryParameter("castUserPassword") final String castUserPassword) throws IOException, ServletException
		{
			if (castUserName.isEmpty()) return FormValidation.error("Please fill-in the Cast User Name");
			if (castUserPassword.isEmpty()) return FormValidation.error("Please fill-in the Cast User Password");
			if (databaseHost.isEmpty()) return FormValidation.error("Please fill-in the database host");
			if (databaseName.isEmpty()) return FormValidation.error("Please fill-in the database name");
			if (databasePort.isEmpty()) return FormValidation.error("Please fill-in the database port");
			try {
				System.out.println(castUserName);

				DatabaseConnection conn = new DatabaseConnection(databaseHost, databaseName, databasePort, castUserName,
						castUserPassword);

				if (conn.getDBConnection() == null) {
					return FormValidation.error("Connection Failed");
				} else {
					conn.closeConnection();
				}
			} catch (Exception e) {
				return FormValidation.error("Connection Failed: " + e.getMessage());
			}
			return FormValidation.ok("Connection Test Successful");
		}

		public ListBoxModel doFillSchemaNameItems(
				@QueryParameter("databaseHost") @RelativePath("..") final String databaseHost,
				@QueryParameter("databaseName") @RelativePath("..") final String databaseName,
				@QueryParameter("databasePort") @RelativePath("..") final String databasePort,
				@QueryParameter("castUserName") @RelativePath("..") final String castUserName,
				@QueryParameter("castUserPassword") @RelativePath("..") final String castUserPassword,
				@QueryParameter("useDatabase") @RelativePath("..") final String useDatabase)
		{
			ListBoxModel m = new ListBoxModel();
			DatabaseConnection conn = null;

			if (databaseHost != null && databaseName != null && databasePort != null && castUserName != null
					&& castUserPassword != null) {
				try {
					conn = new DatabaseConnection(databaseHost, databaseName, databasePort, castUserName,
							castUserPassword);
					String sql = "select schema_name from information_schema.schemata where schema_name like '%_central' order by schema_name";
					PreparedStatement pst = conn.getDBConnection().prepareStatement(sql);
					ResultSet rs = pst.executeQuery();
					while (rs.next()) {
						m.add(rs.getString("schema_name"));
					}
				} catch (SQLException e) {
					return m;
				} finally {
					if (conn != null) conn.closeConnection();
				}
			}
			return m;
		}

		public ListBoxModel doFillAppNameItems(
				@QueryParameter("databaseHost") @RelativePath("..") final String databaseHost,
				@QueryParameter("databaseName") @RelativePath("..") final String databaseName,
				@QueryParameter("databasePort") @RelativePath("..") final String databasePort,
				@QueryParameter("castUserName") @RelativePath("..") final String castUserName,
				@QueryParameter("castUserPassword") @RelativePath("..") final String castUserPassword,
				@QueryParameter("useDatabase") @RelativePath("..") final String useDatabase,
				@QueryParameter("schemaName") final String schemaName

		)
		{
			ListBoxModel m = new ListBoxModel();
			DatabaseConnection conn = null;

			if (databaseHost != null && databaseName != null && databasePort != null && castUserName != null
					&& castUserPassword != null && schemaName != null) {
				try {
					conn = new DatabaseConnection(databaseHost, databaseName, databasePort, castUserName,
							castUserPassword);
					String sql = new StringBuffer().append("select distinct app_name from ").append(schemaName)
							.append(".csv_portf_tree").toString();
					PreparedStatement pst = conn.getDBConnection().prepareStatement(sql);
					ResultSet rs = pst.executeQuery();
					while (rs.next()) {
						m.add(rs.getString("app_name"));
					}
				} catch (SQLException e) {
					return m;
				} finally {
					if (conn != null) conn.closeConnection();
				}
			}
			return m;
		}

		public boolean isApplicable(Class<? extends AbstractProject> aClass)
		{
			// Indicates that this builder can be used with all kinds of project
			// types
			return true;
		}

		/**
		 * This human readable name is used in the configuration screen.
		 */
		public String getDisplayName()
		{
			return "CAST ARI Connector Plugin";
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject formData) throws FormException
		{
			ARIExportLoc = formData.getString("ARIExportLoc");
			save();
			return super.configure(req, formData);
		}

		public String getARIExportLoc()
		{
			return ARIExportLoc;
		}

		public void setARIExportLoc(String ARIExportLoc)
		{
			ARIExportLoc = ARIExportLoc;
		}

	}
}

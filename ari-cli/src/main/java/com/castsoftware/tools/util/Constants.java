package com.castsoftware.tools.util;

import org.apache.commons.cli.Option;

public interface Constants
{
    //Command Line Options
    public final String CMD_HELP = "help";
    public final String CMD_VERSION = "version";
    public final String CMD_QUIET = "quiet";
    public final String CMD_VERBOSE = "verbose";
    public final String CMD_DEBUG = "debug";
 
    public final String CMD_HOST = "host";
    public final String CMD_PORT = "port";
    public final String CMD_DATABASE = "database";
    public final String CMD_USER = "user";
    public final String CMD_PASSWORD = "password";
    
    public final String CMD_GENERATE = "generate";
    public final String CMD_LIST = "list";
    public final String CMD_PUBLISH = "publish";

    public final String CMD_CENTRAL_DB = "central_db";
    public final String CMD_HF = "hf";
    public final String CMD_HF_CHANGABLITY = "changablity";
    public final String CMD_HF_EFFICIENCY = "efficiency";
    public final String CMD_HF_TRANSFERABILITY = "transferability";
    public final String CMD_HF_SECURITY = "security";
    public final String CMD_HF_ROBUSTNESS = "robustness";

    public final String CMD_LIMIT = "limit";
    
    // health factor translation
    public final int CMD_HF_VALUE_CHANGABLITY = 60012;
    public final int CMD_HF_VALUE_EFFICIENCY = 60014;
    public final int CMD_HF_VALUE_TRANSFERABILITY = 60011;
    public final int CMD_HF_VALUE_SECURITY = 60016;
    public final int CMD_HF_VALUE_ROBUSTNESS = 60013;
    public final int [] CMD_HF_ALL = {CMD_HF_VALUE_CHANGABLITY, CMD_HF_VALUE_EFFICIENCY, CMD_HF_VALUE_TRANSFERABILITY, CMD_HF_VALUE_SECURITY, CMD_HF_VALUE_ROBUSTNESS};
    
	// Database 
	public static final String DB_JDBC_DRIVER_CSS = "org.postgresql.Driver";
	public static final String DB_CONN_STRING_CSS = "jdbc:postgresql://";

	
	
}

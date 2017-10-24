package com.castsoftware.tools.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.castsoftware.tools.util.Constants;

public abstract class BaseDAO
{
	private Logger logger = Logger.getLogger(BaseDAO.class);

	private static Connection conn = null;

	private String dbUser;
	private String dbPassword;
	private String dbHost;
	private String dbSchema;
	private int dbPort;
	private String dbName;

	public BaseDAO()
	{

	}

	public BaseDAO(String dbUser, String dbPassword, String dbHost, String dbSchema, int dbPort, String dbName)
	{
		super();
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
		this.dbHost = dbHost;
		this.dbSchema = dbSchema;
		this.dbPort = dbPort;
		this.dbName = dbName;
	}

	public Connection getConnection() throws SQLException
	{
		if (conn == null) {
			String connectionString = String.format("%s%s:%d/%s", Constants.DB_CONN_STRING_CSS, getDbHost(),
					getDbPort(), getDbName());
			logger.debug(String.format("DB: %s", connectionString));

			conn = DriverManager.getConnection(connectionString, getDbUser(), getDbPassword());

			PreparedStatement pstmt;
			try {
				pstmt = conn.prepareStatement("set search_path=cast_xapp_tools");
				pstmt.execute();
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}

		}
		return conn;
	}

	public String getDbUser()
	{
		return dbUser;
	}

	public void setDbUser(String dbUser)
	{
		this.dbUser = dbUser;
	}

	public void setDbPassword(String dbPassword)
	{
		this.dbPassword = dbPassword;
	}

	public void setDbHost(String dbHost)
	{
		this.dbHost = dbHost;
	}

	public void setDbSchema(String dbSchema)
	{
		this.dbSchema = dbSchema;
	}

	public void setDbPort(int dbPort)
	{
		this.dbPort = dbPort;
	}

	public void setDbName(String dbName)
	{
		this.dbName = dbName;
	}

	public String getDbPassword()
	{
		return dbPassword;
	}

	public String getDbHost()
	{
		return dbHost;
	}

	public int getDbPort()
	{
		return dbPort;
	}

	public String getDbSchema()
	{
		return dbSchema;
	}

	public String getDbName()
	{
		return dbName;
	}

}

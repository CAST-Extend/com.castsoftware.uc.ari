package com.castsoftware.tools.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.postgresql.jdbc2.optional.SimpleDataSource;

/**
 * The Class DatabaseConnection establishes the database connection depending on
 * the parameters provided in the command line
 * 
 * @author FME
 * @version 1.1
 */

public class DatabaseConnection implements Constants
{
	/** The log. */
	private Logger log = Logger.getLogger(DatabaseConnection.class);

	/** The database user. */
	private String databaseUser;

	/** The database password. */
	private String databasePassword;

	/** The database host. */
	private String databaseHost;

	/** The database name. */
	private String databaseName;

	/** The database port. */
	private String databasePort;

	/** The database provider. */
	private String databaseProvider;

	/** The connection. */
	private Connection connection = null;

	public DatabaseConnection(InputParameters in) throws SQLException
	{
		this(in.getDbHost(), in.getDbDatabase(), in.getDbPort(), in.getDbUser(), in.getDbPassword());
	}	
	
	/**
	 * Instantiates a new database connection.
	 * 
	 * @param databaseUser
	 *            the database user
	 * @param databasePassword
	 *            the database password
	 * @param databaseHost
	 *            the database host
	 * @param databaseName
	 *            the database name
	 * @param databasePort
	 *            the database port
	 * @param databaseProvider
	 *            the database provider
	 * @throws Exception
	 *             the exception
	 * @throws SQLException
	 *             the SQL exception
	 */
	public DatabaseConnection(String dbHost, String dbDatabase, String dbPort, String dbUser, String dbPassword) throws SQLException
	{
		setDatabaseUser(dbUser);
		setDatabasePassword(dbPassword);
		setDatabaseHost(dbHost);
		setDatabaseName(dbDatabase);
		setDatabasePort(dbPort);

		try {
			Class.forName(Constants.DB_JDBC_DRIVER_CSS);

			if  (log.isDebugEnabled())
			{
				log.debug(getDatabaseProvider() + " JDBC Driver Registered!");
			}
			
			setCreateDBConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Gets the DB connection.
	 * 
	 * @return the DB connection
	 */
	public Connection getDBConnection()
	{
		return connection;
	}

	/**
	 * Sets the create db connection.
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
	public void setCreateDBConnection() throws SQLException
	{
		try {
			StringBuffer connectionString = new StringBuffer();
			connectionString.append(Constants.DB_CONN_STRING_CSS);
			connectionString.append(getDatabaseHost());
			connectionString.append(":");
			connectionString.append(getDatabasePort());
			connectionString.append("/");
			connectionString.append(getDatabaseName());

			log.debug("setCreateDBConnection() - Connection String: " + connectionString.toString());
			connection = DriverManager.getConnection(connectionString.toString(), getDatabaseUser(),
					getDatabasePassword());
		} catch (SQLException e) {
			log.fatal("setCreateDBConnection() - Connection Failed! Check output log file" + e.getMessage());
			throw e;
		}
		log.debug(" setCreateDBConnection() - Connection done!");
	}

	/**
	 * Close connection.
	 * 
	 * @param connection
	 *            the connection
	 */
	public void closeConnection()
	{
		Connection connection = getDBConnection();
		try {
			if (connection != null) {
				connection.close();
				log.debug("Connection closed!");
			}
		} catch (SQLException e) {
			log.error("Close Connection Error" + e.getMessage());
		}
	}

	/**
	 * Gets the database user.
	 * 
	 * @return the databaseUser
	 */
	public String getDatabaseUser()
	{
		return databaseUser;
	}

	/**
	 * Sets the database user.
	 * 
	 * @param databaseUser
	 *            the databaseUser to set
	 */
	public void setDatabaseUser(String databaseUser)
	{
		this.databaseUser = databaseUser;
	}

	/**
	 * Gets the database password.
	 * 
	 * @return the databasePassword
	 */
	public String getDatabasePassword()
	{
		return databasePassword;
	}

	/**
	 * Sets the database password.
	 * 
	 * @param databasePassword
	 *            the databasePassword to set
	 */
	public void setDatabasePassword(String databasePassword)
	{
		this.databasePassword = databasePassword;
	}

	/**
	 * Gets the database host.
	 * 
	 * @return the databaseHost
	 */
	public String getDatabaseHost()
	{
		return databaseHost;
	}

	/**
	 * Sets the database host.
	 * 
	 * @param databaseHost
	 *            the databaseHost to set
	 */
	public void setDatabaseHost(String databaseHost)
	{
		this.databaseHost = databaseHost;
	}

	/**
	 * Gets the database name.
	 * 
	 * @return the databaseName
	 */
	public String getDatabaseName()
	{
		return databaseName;
	}

	/**
	 * Sets the database name.
	 * 
	 * @param databaseName
	 *            the databaseName to set
	 */
	public void setDatabaseName(String databaseName)
	{
		this.databaseName = databaseName;
	}

	/**
	 * Gets the database port.
	 * 
	 * @return the databasePort
	 */
	public String getDatabasePort()
	{
		return databasePort;
	}

	/**
	 * Sets the database port.
	 * 
	 * @param databasePort
	 *            the databasePort to set
	 */
	public void setDatabasePort(String databasePort)
	{
		this.databasePort = databasePort;
	}

	/**
	 * Gets the database provider.
	 * 
	 * @return the databaseProvider
	 */
	public String getDatabaseProvider()
	{
		return databaseProvider;
	}

	/**
	 * Sets the database provider.
	 * 
	 * @param databaseProvider
	 *            the databaseProvider to set
	 */
	public void setDatabaseProvider(String databaseProvider)
	{
		this.databaseProvider = databaseProvider;
	}

}

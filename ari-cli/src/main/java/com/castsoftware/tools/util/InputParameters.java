package com.castsoftware.tools.util;

import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;

import com.castsoftware.tools.dao.BaseDAO;

public class InputParameters implements Constants
{
	private Logger log = Logger.getLogger(InputParameters.class);

	private CommandLineParser parser;
	private CommandLine line;

	private String dbHost;

	private String dbDatabase;

	private String dbPort;

	private String dbUser;

	private String dbPassword;

	private boolean generate;
	private boolean list;
	private boolean publish;

	private String centralDB;
	private int [] healthFactor;
	private String inputHealthFactor;
	private int limit;

	public InputParameters(String[] args) throws IllegalArgumentException
	{

		parser = new DefaultParser();

		Options options = createOptions();
		try {
			line = parser.parse(options, args);

			if (line.hasOption("h")) {
				throw new ParseException("Help");
			}

			dbHost = line.getOptionValue(CMD_HOST, "");
			dbDatabase = line.getOptionValue(CMD_DATABASE, "postgres");
			dbPort = line.getOptionValue(CMD_PORT, "2280");
			dbUser = line.getOptionValue(CMD_USER, "operator");
			dbPassword = line.getOptionValue(CMD_PASSWORD);

			generate = line.hasOption(CMD_GENERATE);
			list = line.hasOption(CMD_LIST);
			publish = line.hasOption(CMD_PUBLISH);

			if (!generate && !list && !publish) {
				throw new ParseException(String.format("\nArgument list must include  %s, %s or %s", CMD_GENERATE,
						CMD_LIST, CMD_PUBLISH));
			}

			centralDB = line.getOptionValue(CMD_CENTRAL_DB);
			if (centralDB == null || centralDB.isEmpty()) {
				throw new ParseException(String.format("\nMissine Required Argument CentralDB", CMD_CENTRAL_DB,
						CMD_LIST, CMD_PUBLISH));
			}

			if (publish) {
				try
				{
					limit = Integer.parseInt(line.getOptionValue(CMD_LIMIT));
				} catch (NumberFormatException e) {
					limit = 50;
				}
				
				String[] factors = line.getOptionValues(CMD_HF);
				inputHealthFactor = Arrays.toString(factors);
				
				if (factors == null) {
					healthFactor = CMD_HF_ALL;
				} else {
					int idx=0;
					healthFactor = new int[factors.length];
					for (String hf : factors) {
						switch (hf.toLowerCase()) {
							case CMD_HF_CHANGABLITY:
								healthFactor[idx++] = CMD_HF_VALUE_CHANGABLITY;
								break;
							case CMD_HF_EFFICIENCY:
								healthFactor[idx++] = CMD_HF_VALUE_EFFICIENCY;
								break;
							case CMD_HF_ROBUSTNESS:
								healthFactor[idx++] = CMD_HF_VALUE_ROBUSTNESS;
								break;
							case CMD_HF_SECURITY:
								healthFactor[idx++] = CMD_HF_VALUE_SECURITY;
								break;
							case CMD_HF_TRANSFERABILITY:
								healthFactor[idx++] = CMD_HF_VALUE_TRANSFERABILITY;
								break;
							default:
								StringBuffer s = new StringBuffer();
								s.append(
										String.format("\nInvalid health factor id [%s]\n\tValid health factors:  ", hf))
										.append(CMD_HF_CHANGABLITY).append(", ").append(CMD_HF_EFFICIENCY).append(", ")
										.append(CMD_HF_SECURITY).append(", ").append(CMD_HF_TRANSFERABILITY)
										.append(", ").append(CMD_HF_ROBUSTNESS);

								throw new ParseException(s.toString());
						}
					}
				}

				if (log.isDebugEnabled())
					log.debug(String.format("Generate Action Plan using HealthFactors [%s] ", healthFactor));

			}

		} catch (ParseException e) {
			HelpFormatter hf = new HelpFormatter();

			hf.printHelp(
					String.format("ari [OPTION] -%s <%s> -%s <%s> -%s <%s>\n\n", CMD_HOST, CMD_HOST.toUpperCase(),
							CMD_CENTRAL_DB, CMD_CENTRAL_DB.toUpperCase(), CMD_PASSWORD, CMD_PASSWORD.toUpperCase()),
					"", options, e.getMessage());

			throw new IllegalArgumentException(e.getMessage());

		}

	}

	private Options createOptions()
	{
		Options options = new Options();

		Option o = new Option(CMD_HOST, true, "Database Host");
		o.setRequired(true);
		options.addOption(o);

		options.addOption(CMD_PORT, true, "Database Port");
		options.addOption(CMD_DATABASE, true, "Database Name");
		options.addOption(CMD_USER, true, "Database User Id");

		o = new Option(CMD_CENTRAL_DB, true, "CAST Central Database to be reviewed");
		o.setRequired(true);
		options.addOption(o);

		o = new Option(CMD_PASSWORD, true, "Database User Password");
		o.setRequired(true);
		options.addOption(o);

		options.addOption(CMD_GENERATE, false, "Generate Action Items");
		options.addOption(CMD_LIST, false, "List Action Items");
		options.addOption(CMD_PUBLISH, false, "Publish Action Items");

		String hfDesc = String.format("List of one or more health factors or \"all\"\npossible health factors:  %s, %s, %s, %s, %s", 
				CMD_HF_CHANGABLITY, CMD_HF_EFFICIENCY, CMD_HF_TRANSFERABILITY, 
				CMD_HF_SECURITY, CMD_HF_ROBUSTNESS);
		
		options.addOption(
				Option.builder(CMD_HF).desc( hfDesc )
						.hasArgs().build()
				);
		
		options.addOption(CMD_LIMIT, true, "Limit number of action items - default 50");
	
		return options;
	}

	public String getDbHost()
	{
		return dbHost;
	}

	public String getDbDatabase()
	{
		return dbDatabase;
	}

	public String getDbPort()
	{
		return dbPort;
	}

	public String getDbUser()
	{
		return dbUser;
	}

	public String getDbPassword()
	{
		return dbPassword;
	}

	public String getCentralDB()
	{
		return centralDB;
	}

	public boolean isGenerate()
	{
		return generate;
	}

	public boolean isPublish()
	{
		return publish;
	}

	public int[] getHealthFactor()
	{
		return healthFactor;
	}

	public String getInputHealthFactor()
	{
		return inputHealthFactor;
	}

	public boolean isList()
	{
		return list;
	}

	public int getLimit()
	{
		return limit;
	}

}

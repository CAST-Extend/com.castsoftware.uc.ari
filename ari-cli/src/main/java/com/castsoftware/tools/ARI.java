package com.castsoftware.tools;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.castsoftware.tools.bean.AriStagingBean;
import com.castsoftware.tools.bean.ViewerActionPlansBean;
import com.castsoftware.tools.dao.AriStagingDao;
import com.castsoftware.tools.util.InputParameters;

public class ARI
{
	private Logger logger = Logger.getLogger(ARI.class);

	private InputParameters parms;

	public ARI(String... args) throws Exception
	{
		try {
			PropertyConfigurator.configure("log4j.properties");
			System.out.println("ARI-CLI v" + ARI.class.getPackage().getImplementationVersion() + "\n(C) 2017, CAST Software, All Rights Reserved" );

			parms = new InputParameters(args);

			AriStagingDao dao = new AriStagingDao(parms);
			List<AriStagingBean> actionItems = null;

			if (parms.isGenerate()) {
				System.out.print(String.format("Generating Action Items for %s ... ", parms.getCentralDB()));
				actionItems = dao.generateActionItems(parms.getCentralDB());

				System.out.println(String.format("%d items generated", actionItems.size()));
			}

			if (parms.isList() && !parms.isGenerate()) {
				actionItems = dao.getActionItems(parms.getCentralDB());
			}

			if (parms.isList()) {
				System.out.print(AriStagingBean.header());
				for (AriStagingBean b : actionItems) {
					System.out.print(b);
				}
			}

			if (parms.isPublish()) {
				System.out.println(String.format("Publishing %d action items", parms.getLimit()));
				System.out.println(String.format("Filtering with %s Health Factors", parms.getInputHealthFactor()));

				List<ViewerActionPlansBean> actionItemList = dao.publishActionItems(parms.getCentralDB(),
						parms.getHealthFactor(), parms.getLimit());

				System.out.println(String.format("There are now %d Action Items ", actionItemList.size()));

			}
		} catch (IllegalArgumentException ia) {
			// nothing to do here
		}

	}

	public static void main(String... args)
	{
		try {
			new ARI(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

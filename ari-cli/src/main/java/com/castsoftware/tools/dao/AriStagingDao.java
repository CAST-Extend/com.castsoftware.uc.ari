package com.castsoftware.tools.dao;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.castsoftware.tools.bean.AriStagingBean;
import com.castsoftware.tools.bean.ViewerActionPlansBean;
import com.castsoftware.tools.util.InputParameters;

public class AriStagingDao extends BaseDAO
{
	private Logger logger = Logger.getLogger(AriStagingDao.class);

	public AriStagingDao(InputParameters parms)
	{
		super(parms.getDbUser(), parms.getDbPassword(), parms.getDbHost(), parms.getCentralDB(),
				Integer.parseInt(parms.getDbPort()), parms.getDbDatabase());
	}

	public List<AriStagingBean> getActionItems(String centralDB)
	{
		List rslt = new ArrayList();

		PreparedStatement pstmt;
		try {
			pstmt = getConnection().prepareStatement(
					"select * FROM cast_xapp_tools.ari_master WHERE schema_name = ?");
			pstmt.setString(1, centralDB);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				rslt.add(new AriStagingBean(rs));
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		}

		return rslt;
	}

	public List<AriStagingBean> generateActionItems(String centralDB)
	{
		List rslt = new ArrayList();

		PreparedStatement pstmt;
		try {
			pstmt = getConnection().prepareStatement("select * FROM ari_create_ap_items(?)");
			pstmt.setString(1, centralDB);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				rslt.add(new AriStagingBean(rs));
			}
		} catch (SQLException e) {
			if (!e.getMessage().contains("No results were returned by the query"))
				logger.error(e.getMessage());
		}

		return rslt;
	}

	//select * FROM cast_xapp_tools.ari_insert_ap_items('webgoat_central',array[60012, 60014],10);
	public List<ViewerActionPlansBean> publishActionItems(String centralDB, int[] healthFactors, int apLimit)
	{
		List rslt = new ArrayList();
		PreparedStatement pstmt;
		try {
			Connection conn = getConnection();

			Integer hfs[] = new Integer[healthFactors.length];
			int cnt=0;
			for (int h: healthFactors)
			{
				hfs[cnt++]=new Integer(h);
			}
			
			pstmt = conn.prepareStatement("select * FROM ari_insert_ap_items(?,?,?);");
			pstmt.setString(1, centralDB);
			pstmt.setArray(2, conn.createArrayOf("int",hfs));
			pstmt.setInt(3, apLimit);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				rslt.add(new ViewerActionPlansBean(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

		return rslt;
	}

}

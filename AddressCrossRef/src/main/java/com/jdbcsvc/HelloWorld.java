package com.jdbcsvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.api.transport.PropertyScope;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloWorld implements Callable {

	public Object onCall(MuleEventContext eventContext) throws Exception {
		eventContext.getMessage().getProperty("header1", PropertyScope.INBOUND);
		MuleMessage muleMessage = eventContext.getMessage();
		Map<String, String> queryParams = muleMessage.getInboundProperty("http.query.params");
		String ZipCode = queryParams.get("zipcode");
		//System.out.println(ZipCode);

        ConnectionPool SQLConnPoolManager = new ConnectionPool(
                "com.microsoft.sqlserver.jdbc.SQLServerDriver",
                "jdbc:sqlserver://LOUSQLWTS405:1433;DatabaseName=ELog_DEV", "ELog", "i#9QjJueWj",
                5, 10, true);

        ConnectionPool DB2ConnPoolManager = new ConnectionPool(
                "com.ibm.db2.jcc.DB2Driver",
                "jdbc:db2://dbt1dist.humana.com:5027/DBT1", "DB2EIT0", "mrvwk75#",
                5, 10, true);

		
		List<Map<String, Object>> queueListDtl = 
				new ArrayList<Map<String, Object>>();
		Map<String, Object> iMap = new HashMap<String, Object>();
		Map<String, Object> iMap1 = new HashMap<String, Object>();
		

		
		Connection db2connection = null;
		Connection SQLconnection = null;
		try {
					
			/*
			 * --Zipcode SELECT CITY.ZIP_CD, CITY.STATE_CD, CITY.CNTY_CD,
			 * CITY.CITY_NAME, CITY.PREFERED_CITY_NAME , CNTY.CNTY_NAME FROM
			 * HUM.TEXCITY CITY, HUM.TEXSTCN CNTY WHERE CITY.ZIP_CD = ? AND
			 * CITY.STATE_CD = CNTY.STATE_CD AND CITY.CNTY_CD = CNTY.CNTY_CD
			 * WITH UR;
			 * 
			 * --County/State SELECT CITY.ZIP_CD, CITY.STATE_CD, CITY.CNTY_CD,
			 * CITY.CITY_NAME, CITY.PREFERED_CITY_NAME , CNTY.CNTY_NAME FROM
			 * HUM.TEXCITY CITY, HUM.TEXSTCN CNTY WHERE CNTY.CNTY_NAME = ? AND
			 * CNTY.STATE_CD = ? AND CITY.STATE_CD = CNTY.STATE_CD AND
			 * CITY.CNTY_CD = CNTY.CNTY_CD WITH UR;
			 */

			db2connection = DB2ConnPoolManager.getConnection();
			SQLconnection = SQLConnPoolManager.getConnection();
			String selectDB2TableSQL = "SELECT CITY.ZIP_CD, CITY.STATE_CD, CITY.CNTY_CD, CITY.CITY_NAME, "
					+ "CITY.PREFERED_CITY_NAME , CNTY.CNTY_NAME " + "FROM HUM.TEXCITY CITY, HUM.TEXSTCN CNTY "
					+ "WHERE CITY.ZIP_CD = '" + ZipCode + "'" + "AND CITY.STATE_CD = CNTY.STATE_CD "
					+ "AND CITY.CNTY_CD = CNTY.CNTY_CD " + "WITH UR;";

			Statement db2statement = db2connection.createStatement();
			ResultSet db2rs = db2statement.executeQuery(selectDB2TableSQL);

			
			String SQLQuery = "SELECT *  FROM ELog_Dev.Location.ZipCodeCrossRef WHERE COUNTY = ? AND STATE = ? ";
			
			PreparedStatement SQLstatement = SQLconnection.prepareStatement(SQLQuery);
			ResultSet SQLrs = null;
			

			while (db2rs.next()) {
				
				iMap = new HashMap();
				iMap.put("ZIP_CD", db2rs.getString("ZIP_CD"));
				iMap.put("STATE_CD", db2rs.getString("STATE_CD"));
				iMap.put("CNTY_CD", db2rs.getString("CNTY_CD"));
				iMap.put("CITY_NAME", db2rs.getString("CITY_NAME"));
				iMap.put("PREFERED_CITY_NAME", db2rs.getString("PREFERED_CITY_NAME"));
				iMap.put("CNTY_NAME", db2rs.getString("CNTY_NAME"));

				SQLstatement.setString(1, db2rs.getString("CNTY_NAME"));
				SQLstatement.setString(2, db2rs.getString("STATE_CD"));
				SQLrs = SQLstatement.executeQuery();
				while (SQLrs.next()) {
					iMap1 = new HashMap();
					iMap1.put("SSACode", SQLrs.getString("SSACode"));
					iMap1.put("FIPSCode", SQLrs.getString("FIPSCode"));
					iMap.put("Code", iMap1);
				}
				queueListDtl.add(iMap);
			}

			SQLstatement.close();
			db2statement.close();

			SQLrs.close();
			db2rs.close();

			SQLConnPoolManager.free(SQLconnection);
			DB2ConnPoolManager.free(db2connection);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (db2connection != null) {
				System.out.println("Connected successfully.");
				try {
					db2connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			if (SQLconnection != null) {
				System.out.println("Connected successfully.");
				
				try {
					SQLconnection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		}

		//return "Hello " + ZipCode;
		return queueListDtl;

	}

}

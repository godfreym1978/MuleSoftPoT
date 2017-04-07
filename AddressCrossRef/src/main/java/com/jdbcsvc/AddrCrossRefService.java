package com.jdbcsvc;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AddrCrossRefService implements IAddrCrossRefService{

	private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String DB_CONNECTION = "jdbc:oracle:thin:@localhost:1521:XE";
	private static final String DB_USER = "system";
	private static final String DB_PASSWORD = "passw0rd";

	private static Connection getDBConnection() {
		Connection dbConnection = null;
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,
					DB_PASSWORD);
			return dbConnection;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return dbConnection;
	}
	
	@Override
	public ArrayList<EmpResponse> getEmployee(EmpRequest request) {
		
		Connection dbConnection = null;
		Statement statement = null;

		String selectTableSQL = "SELECT * from SYSTEM.EMPLOYEES where MANAGER_ID="+Integer.parseInt(request.getMgrID().toString());
				
		// TODO Auto-generated method stub
		EmpResponse response;// = new EmpResponse();
		
		ArrayList<EmpResponse> queueList = new ArrayList<EmpResponse>();
		
		try {
			dbConnection = getDBConnection();
			statement = dbConnection.createStatement();

			System.out.println(selectTableSQL);

			// execute select SQL stetement
			ResultSet rs = statement.executeQuery(selectTableSQL);

			while (rs.next()) {
				response = new EmpResponse();
				response.setEmpID(rs.getInt("EMPLOYEE_ID"));
				response.setfName(rs.getString("FIRST_NAME"));
				response.setlName(rs.getString("LAST_NAME"));
				response.seteMail(rs.getString("EMAIL"));
				response.setPhoneNumber(rs.getString("PHONE_NUMBER"));
				response.setHireDate(rs.getDate("HIRE_DATE").toString());
				response.setJobID(rs.getString("JOB_ID"));
				response.setSalary(rs.getInt("SALARY")  );
				response.setCommPct(rs.getInt("COMMISSION_PCT"));
				response.setMgrID(rs.getInt("MANAGER_ID"));
				response.setDeptID(rs.getInt("DEPARTMENT_ID"));
				
				queueList.add(response);

			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		System.out.println(queueList.size());
		
		//return response;
		return queueList;
	}
}




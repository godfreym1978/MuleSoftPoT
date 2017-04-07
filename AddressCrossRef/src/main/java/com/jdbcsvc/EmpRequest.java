package com.jdbcsvc;

public class EmpRequest {
	
	private String empID;
	private String fName;
	private String lName;
	private String jobID;
	private String mgrID;
	private String deptID;

	public String getEmpID() {
		return empID;
	}
	public void setEmpID(String empID) {
		this.empID = empID;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getlName() {
		return lName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}
	
	public String getJobID() {
		return jobID;
	}
	public void setJobID(String jobID) {
		this.jobID = jobID;
	}
	public String getMgrID() {
		return mgrID;
	}
	public void setMgrID(String mgrID) {
		this.mgrID = mgrID;
	}
	public String getDeptID() {
		return deptID;
	}
	public void setDeptID(String deptID) {
		this.deptID = deptID;
	}
	


}

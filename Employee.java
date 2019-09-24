package com.dev.beans;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee implements Serializable {
	@Id
	@Column(name = "Empno")
	private Integer empno;
	@Column(name = "Ename")
	private String ename;
	@Column(name = "Job")
	private String job;
	@Column(name = "MgrNo")
	private Integer mgrno;
	@Column(name = "HireDate")
	private String hiredate;
	@Column(name = "Dept_ID")
	private Integer deptid;

	public Integer getEmpno() {
		return empno;
	}

	public void setEmpno(Integer empno) {
		this.empno = empno;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public Integer getMgrno() {
		return mgrno;
	}

	public void setMgrno(Integer mgrno) {
		this.mgrno = mgrno;
	}

	public String getHiredate() {
		return hiredate;
	}

	public void setHiredate(String hiredate) {
		this.hiredate = hiredate;
	}

	public Integer getDeptid() {
		return deptid;
	}

	public void setDeptid(Integer deptid) {
		this.deptid = deptid;
	}

	@Override
	public String toString() {
		return "Employee [empno=" + empno + ", ename=" + ename + ", job=" + job + ", mgrno=" + mgrno + ", hiredate="
				+ hiredate + ", deptid=" + deptid + "]";
	}

}

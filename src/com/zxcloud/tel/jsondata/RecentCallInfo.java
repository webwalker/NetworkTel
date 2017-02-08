package com.zxcloud.tel.jsondata;

import java.util.Date;

/**
 * @author xu.jian
 * 
 */
public class RecentCallInfo {
	private int userId;
	private String userName;
	private String department;
	private String userIcon;
	private String phone;
	private String extensionNo;
	private int callTimes;
	private String ext;
	private Date lastCallDate;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getExtensionNo() {
		return extensionNo;
	}

	public void setExtensionNo(String extensionNo) {
		this.extensionNo = extensionNo;
	}

	public int getCallTimes() {
		return callTimes;
	}

	public void setCallTimes(int callTimes) {
		this.callTimes = callTimes;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public Date getLastCallDate() {
		return lastCallDate;
	}

	public void setLastCallDate(Date lastCallDate) {
		this.lastCallDate = lastCallDate;
	}

}

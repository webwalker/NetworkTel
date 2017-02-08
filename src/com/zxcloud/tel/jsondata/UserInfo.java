package com.zxcloud.tel.jsondata;

import java.io.Serializable;

/**
 * @author xu.jian
 * 
 */
public class UserInfo implements Serializable {
	private String userId = "-1";
	private String userName;
	private String companyName;
	private String companyId;
	private String departmentName;
	private String email;
	private String mobileNo;
	private String companyBalance;
	private String picUrl;
	private String introduce;
	private String extensionNo;
	private String loginName;
	private int status;
	private String tempPass;

	public UserInfo() {
	}

	public UserInfo(String userName, String departmentName, String mobileNo,
			String picUrl, String extensionNo) {
		this.userName = userName;
		this.departmentName = departmentName;
		this.mobileNo = mobileNo;
		this.picUrl = picUrl;
		this.extensionNo = extensionNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getCompanyBalance() {
		return companyBalance;
	}

	public void setCompanyBalance(String companyBalance) {
		this.companyBalance = companyBalance;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getExtensionNo() {
		return extensionNo;
	}

	public void setExtensionNo(String extensionNo) {
		this.extensionNo = extensionNo;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTempPass() {
		return tempPass;
	}

	public void setTempPass(String tempPass) {
		this.tempPass = tempPass;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("UserID:" + userId);
		sb.append("UserName:" + userName);
		sb.append("Department:" + departmentName);
		sb.append("Phone:" + mobileNo);
		sb.append("ExtensionNo:" + extensionNo);
		return sb.toString();
	}
}

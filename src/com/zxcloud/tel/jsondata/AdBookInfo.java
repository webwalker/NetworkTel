package com.zxcloud.tel.jsondata;

import java.util.List;

/**
 * @author xu.jian
 * 
 */
public class AdBookInfo {
	private String departmentId;
	private String departmentName;
	private int memberCount;
	private List<UserInfo> members;

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public int getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}

	public List<UserInfo> getMembers() {
		return members;
	}

	public void setMembers(List<UserInfo> members) {
		this.members = members;
	}

}

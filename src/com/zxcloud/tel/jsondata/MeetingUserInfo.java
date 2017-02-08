package com.zxcloud.tel.jsondata;

/**
 * @author xu.jian
 * 
 */
public class MeetingUserInfo {
	private String userId;
	private String mobileNo;
	// 分机号
	private String extensionNo;
	private String name;
	// 1：可发言，2：禁言
	private int level;
	private String callId;
	// 1成功，2失败
	private int result;
	private String reason;

}

package com.zxcloud.tel.model;

/**
 * @author xu.jian
 * 
 */
public class UserEntry extends BaseRequest {
	public UserEntry() {
		super(true);
	}

	public class UserResp {
		private boolean recorded;
		private String companyName;
		private String userName;
		private int isSetPwd;

		public boolean isRecorded() {
			return recorded;
		}

		public void setRecorded(boolean recorded) {
			this.recorded = recorded;
		}

		public String getCompanyName() {
			return companyName;
		}

		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public int getIsSetPwd() {
			return isSetPwd;
		}

		public void setIsSetPwd(int isSetPwd) {
			this.isSetPwd = isSetPwd;
		}
	}

	public class UserReq {
		private String companyId;
		private String extensionNo;
		private String secretCode;
		private String userName;
		private String password;

		public String getCompanyId() {
			return companyId;
		}

		public void setCompanyId(String companyId) {
			this.companyId = companyId;
		}

		public String getExtensionNo() {
			return extensionNo;
		}

		public void setExtensionNo(String extensionNo) {
			this.extensionNo = extensionNo;
		}

		public String getSecretCode() {
			return secretCode;
		}

		public void setSecretCode(String secretCode) {
			this.secretCode = secretCode;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}

	public class UserToken {
		private String token;

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}
	}
}

package com.zxcloud.tel.model;

/**
 * @author xujian
 * 
 */
public class LoginEntry extends BaseRequest {
	public LoginEntry() {
		super(true);
	}

	public class LoginResp<T> {
		private T userInfo;

		public T getUserInfo() {
			return userInfo;
		}

		public void setUserInfo(T userInfo) {
			this.userInfo = userInfo;
		}
	}

	public class LoginReq {
		public String loginNo;
		public String password;

		public String getLoginNo() {
			return loginNo;
		}

		public void setLoginNo(String loginNo) {
			this.loginNo = loginNo;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}
}

package com.zxcloud.tel.model;

/**
 * @author xujian
 * 
 */
public class PasswordEntry extends BaseRequest {

	public class SetPassReq {
		private String mobileNo;
		private String oldPassword;
		private String newPassword;
		private int optype;

		public String getMobileNo() {
			return mobileNo;
		}

		public void setMobileNo(String mobileNo) {
			this.mobileNo = mobileNo;
		}

		public String getOldPassword() {
			return oldPassword;
		}

		public void setOldPassword(String oldPassword) {
			this.oldPassword = oldPassword;
		}

		public String getNewPassword() {
			return newPassword;
		}

		public void setNewPassword(String newPassword) {
			this.newPassword = newPassword;
		}

		public int getOptype() {
			return optype;
		}

		public void setOptype(int optype) {
			this.optype = optype;
		}

	}

	public class SmsReq {
		private String mobileNo;

		public String getMobileNo() {
			return mobileNo;
		}

		public void setMobileNo(String mobileNo) {
			this.mobileNo = mobileNo;
		}

	}

	public class ValidateSmsReq {
		private String mobileNo;
		private String code;

		public String getMobileNo() {
			return mobileNo;
		}

		public void setMobileNo(String mobileNo) {
			this.mobileNo = mobileNo;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

	}
}

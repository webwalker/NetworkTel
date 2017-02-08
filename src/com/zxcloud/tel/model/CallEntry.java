package com.zxcloud.tel.model;

/**
 * @author xu.jian
 * 
 */
public class CallEntry extends BaseRequest {

	public class CallReq {
		// 本机号码
		private String caller;
		private String callNo;
		// 拨打类型 1wifi直播，2免流量拨打
		private int callType;

		public String getCaller() {
			return caller;
		}

		public void setCaller(String caller) {
			this.caller = caller;
		}

		public String getCallNo() {
			return callNo;
		}

		public void setCallNo(String callNo) {
			this.callNo = callNo;
		}

		public int getCallType() {
			return callType;
		}

		public void setCallType(int callType) {
			this.callType = callType;
		}

	}
}

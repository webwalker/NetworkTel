package com.zxcloud.tel.model;

import com.zxcloud.tel.jsondata.SipInfo;

/**
 * @author xu.jian
 * 
 */
public class AppEntry extends BaseRequest {
	public class SipResp {
		private SipInfo sipInfo;

		public SipInfo getSipInfo() {
			return sipInfo;
		}

		public void setSipInfo(SipInfo sipInfo) {
			this.sipInfo = sipInfo;
		}

	}
}

package com.zxcloud.tel.model;

/**
 * @author xujian
 * 
 */
public class FeedBackEntry extends BaseRequest {
	public FeedBackEntry() {
		super(true);
	}

	public class ContentReq {
		public String content;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}
}

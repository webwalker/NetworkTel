package com.zxcloud.tel.model;

import java.util.List;

import com.zxcloud.tel.jsondata.MeetingUserInfo;

/**
 * @author xu.jian
 * 
 */
public class MeetingEntry extends BaseRequest {
	public MeetingEntry() {
		super(true);
	}

	public class MeetingSendReq {
		// 1：立即发起 2：预约
		private int type;
		// yyyy-mm-dd hh:mm:ss
		private String startTime;
		// yyyy-mm-dd hh:mm:ss
		private String expectEndTime;
		private String meetTitle;
		private String meetContent;
		private List<MeetingUserInfo> calls;

	}

	public class MeetingSendResp {
		private String meetId;
		private String userId;
		private String callStatus;
		private String answerTime;
		private String callId;
		private List<MeetingUserInfo> calls;
	}

	public class MeetingCommonReq {
		private String meetId;
		private List<MeetingUserInfo> calls;
	}

	public class MeetingCommonResp {
		private List<MeetingUserInfo> calls;
	}

	public class MeetingEndReq {
		private String meetingId;

		public String getMeetingId() {
			return meetingId;
		}

		public void setMeetingId(String meetingId) {
			this.meetingId = meetingId;
		}
	}

	public class MeetingOperationReq {
		private String meetId;
		// 0拒绝，1接收2取消
		private int optype;

		public String getMeetId() {
			return meetId;
		}

		public void setMeetId(String meetId) {
			this.meetId = meetId;
		}

		public int getOptype() {
			return optype;
		}

		public void setOptype(int optype) {
			this.optype = optype;
		}

	}

	public class MeetingNoticeInfo {
		private String initiatorName;
		private String noticeTime;
		// yyyy-mm-dd hh:mm:ss
		private String startTime;
		private String expectEndTime;
		private String meetTitle;
		private String members;
	}

	public class MeetingNoticeResp {
		private List<MeetingNoticeInfo> notice;
	}
}

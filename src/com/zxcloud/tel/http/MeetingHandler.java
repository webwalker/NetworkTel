package com.zxcloud.tel.http;

import org.json.JSONObject;

import webwalker.framework.http.MyJsonObjectRequest;
import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.zxcloud.tel.jsondata.MeetingHistoryInfo;
import com.zxcloud.tel.model.BaseResponse;
import com.zxcloud.tel.model.MeetingEntry;
import com.zxcloud.tel.model.MeetingEntry.MeetingCommonReq;
import com.zxcloud.tel.model.MeetingEntry.MeetingCommonResp;
import com.zxcloud.tel.model.MeetingEntry.MeetingEndReq;
import com.zxcloud.tel.model.MeetingEntry.MeetingNoticeResp;
import com.zxcloud.tel.model.MeetingEntry.MeetingOperationReq;
import com.zxcloud.tel.model.MeetingEntry.MeetingSendReq;
import com.zxcloud.tel.model.MeetingEntry.MeetingSendResp;

/**
 * @author xu.jian
 * 
 */
public class MeetingHandler extends BaseHandler {
	final static String CREATEMEET = "/meet/createMeet";
	final static String SETLEVEL = "/meet/setLevel";
	final static String LEAVEMEET = "/meet/leaveMeet";
	final static String JOINMEET = "/meet/joinMeet";
	final static String ENDMEETTING = "/meet/endMeetting";
	final static String MEETOPERATION = "/meet/meetOperation";
	final static String GETINVITELIST = "/meet/getInviteList";
	final static String GETAPPOINTLIST = "/meet/getAppointList";
	final static String GETMEETHISTORY = "/meet/getMeetHistory";

	public MeetingHandler(Context c) {
		super(c);
	}

	public void createMeet(MeetingSendReq request) {
		MeetingEntry entry = new MeetingEntry();
		// TO-DO add params
		entry.setBody(request);

		MyJsonObjectRequest req = baseRequest(CREATEMEET, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						BaseResponse<MeetingSendResp> resp = fromJson(arg0);
						if (!haSuccess(resp))
							return;

						// TO-DO
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});
		commit(req);
	}

	public void setLevel(MeetingCommonReq request) {
		MeetingEntry entry = new MeetingEntry();
		// TO-DO add params
		entry.setBody(request);

		MyJsonObjectRequest req = baseRequest(SETLEVEL, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						BaseResponse<MeetingSendResp> resp = fromJson(arg0);
						if (!haSuccess(resp))
							return;

						// TO-DO
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});
		commit(req);
	}

	public void leaveMeet(MeetingCommonReq request) {
		MeetingEntry entry = new MeetingEntry();
		// TO-DO add params
		entry.setBody(request);

		MyJsonObjectRequest req = baseRequest(LEAVEMEET, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						BaseResponse<MeetingCommonResp> resp = fromJson(arg0);
						if (!haSuccess(resp))
							return;

						// TO-DO
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});
		commit(req);
	}

	public void joinMeet(MeetingCommonReq request) {
		MeetingEntry entry = new MeetingEntry();
		// TO-DO add params
		entry.setBody(request);

		MyJsonObjectRequest req = baseRequest(JOINMEET, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						BaseResponse<MeetingCommonResp> resp = fromJson(arg0);
						if (!haSuccess(resp))
							return;

						// TO-DO
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});
		commit(req);
	}

	public void endMeetting(String meetingId) {
		MeetingEntry entry = new MeetingEntry();
		MeetingEndReq body = entry.new MeetingEndReq();
		body.setMeetingId(meetingId);
		entry.setBody(body);

		MyJsonObjectRequest req = baseRequest(ENDMEETTING, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						BaseResponse<Object> resp = fromJson(arg0);
						if (!haSuccess(resp))
							return;

						// TO-DO
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});
		commit(req);
	}

	public void meetOperation(String meetingId, int optype) {
		MeetingEntry entry = new MeetingEntry();
		MeetingOperationReq body = entry.new MeetingOperationReq();
		body.setMeetId(meetingId);
		body.setOptype(optype);
		entry.setBody(body);

		MyJsonObjectRequest req = baseRequest(MEETOPERATION, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						BaseResponse<Object> resp = fromJson(arg0);
						if (!haSuccess(resp))
							return;

						// TO-DO
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});
		commit(req);
	}

	public void getInviteList() {
		MeetingEntry entry = new MeetingEntry();
		entry.setBody(null);

		MyJsonObjectRequest req = baseRequest(GETINVITELIST, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						BaseResponse<MeetingNoticeResp> resp = fromJson(arg0);
						if (!haSuccess(resp))
							return;

						// TO-DO
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});
		commit(req);
	}

	public void getAppointList() {
		MeetingEntry entry = new MeetingEntry();
		entry.setBody(null);

		MyJsonObjectRequest req = baseRequest(GETAPPOINTLIST, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						BaseResponse<Object> resp = fromJson(arg0);
						if (!haSuccess(resp))
							return;

						// TO-DO
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});
		commit(req);
	}

	public void getMeetHistory() {
		MeetingEntry entry = new MeetingEntry();
		entry.setBody(null);

		MyJsonObjectRequest req = baseRequest(GETMEETHISTORY, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						BaseResponse<MeetingHistoryInfo> resp = fromJson(arg0);
						if (!haSuccess(resp))
							return;

						// TO-DO
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});
		commit(req);
	}
}

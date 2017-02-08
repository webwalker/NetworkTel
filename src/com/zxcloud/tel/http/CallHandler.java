package com.zxcloud.tel.http;

import org.json.JSONObject;

import webwalker.framework.http.MyJsonObjectRequest;
import webwalker.framework.interfaces.ICallback;
import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.zxcloud.tel.model.BaseResponse;
import com.zxcloud.tel.model.CallEntry;
import com.zxcloud.tel.model.CallEntry.CallReq;

/**
 * @author xu.jian
 * 
 */
public class CallHandler extends BaseHandler {
	final static String CALLNO = "/call/callNo";

	public CallHandler(Context c) {
		super(c);
	}

	/**
	 * 呼叫
	 * 
	 * @param callback
	 * @param caller
	 * @param callNo
	 * @param callType
	 */
	public void callNumber(final ICallback<Boolean> callback, String caller,
			String callNo, int callType) {
		CallEntry entry = new CallEntry();
		CallReq body = entry.new CallReq();
		body.setCaller(caller);
		body.setCallNo(callNo);
		body.setCallType(callType);
		entry.setBody(body);

		MyJsonObjectRequest req = baseRequest(CALLNO, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						BaseResponse<Object> resp = fromJson(arg0);
						boolean success = haSuccess(resp);
						if (callback != null)
							callback.action(success);
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						showHttpError(arg0);
					}
				});
		commit(req);
	}
}

package com.zxcloud.tel.http;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import webwalker.framework.http.BaseVolleyHandler;
import webwalker.framework.http.MyJsonObjectRequest;
import webwalker.framework.utils.JsonUtil;
import webwalker.framework.utils.Loggers;
import webwalker.framework.utils.MessageUtil;
import webwalker.framework.utils.NetworkUtils;
import webwalker.framework.utils.UriUtil;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.reflect.TypeToken;
import com.zxcloud.tel.MessageActivity;
import com.zxcloud.tel.common.AppContext;
import com.zxcloud.tel.common.SecurityMgr;
import com.zxcloud.tel.jsondata.ResponseInfo;
import com.zxcloud.tel.model.BaseResponse;

/**
 * @author xu.jian
 * 
 */
public class BaseHandler extends BaseVolleyHandler {
	protected final int SUCCESS = 0;

	public BaseHandler(Context c) {
		super(c);
	}

	@Override
	public <T> void commit(Request<T> req) {
		// if (!checkNetWork())
		// return;
		super.commit(req);
	}

	protected boolean checkNetWork() {
		if (!NetworkUtils.isNetworkAvailable(context)) {
			MessageUtil.showShortToast(context, "网络不可用");
			return false;
		}
		return true;
	}

	@Override
	protected String getUrl(String url) {
		return AppContext.BASE_URL + url;
	}

	protected Map<String, String> getHeaders(String url) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Authorization",
				SecurityMgr.signHeaders(AppContext.USERID, url));
		return map;
	}

	protected MyJsonObjectRequest baseRequest(String url, Object request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		return getRequest(url, request, getHeaders(url), listener,
				errorListener);
	}

	protected <T> BaseResponse<T> fromJson(String json, Class cls) {
		return fromJson(json, cls);
	}

	protected <T> BaseResponse<T> fromJson(JSONObject json) {
		return fromJson(json.toString());
	}

	protected <T> BaseResponse<T> fromJson(JSONObject json, Class cls) {
		Type objectType = JsonUtil.type(BaseResponse.class, cls);
		return JsonUtil.gson.fromJson(json.toString(), objectType);
	}

	/**
	 * 反序列化
	 * 
	 * @param json
	 * @return
	 */
	protected <T> BaseResponse<T> fromJson(String json) {
		if (json == null)
			return null;
		try {
			Loggers.d(TAG, "http response:" + json);
			return JsonUtil.gson.fromJson(json,
					new TypeToken<BaseResponse<T>>() {
					}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected boolean haSuccess(BaseResponse resp) {
		return haSuccess(resp, "");
	}

	protected boolean haSuccess(BaseResponse resp, String errMessage) {
		if (resp == null || resp.getStatus() == null) {
			if (!errMessage.equals("")) {
				showActMessage(errMessage);
			}
			return false;
		}
		ResponseInfo status = resp.getStatus();
		if (status.getCode() != 0) {
			if (status.getShowMsg() != null && !status.getShowMsg().equals(""))
				showActMessage(status.getShowMsg());
			return false;
		}
		return true;
	}

	protected void showActMessage(String msg) {
		UriUtil.startMsgActivity(context, MessageActivity.class, msg);
	}

}

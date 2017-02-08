package com.zxcloud.tel.http;

import java.util.List;

import org.json.JSONObject;

import webwalker.framework.http.MyJsonObjectRequest;
import webwalker.framework.interfaces.ICallback;
import webwalker.framework.utils.JsonUtil;
import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.zxcloud.tel.common.SecurityMgr;
import com.zxcloud.tel.jsondata.AdBookInfo;
import com.zxcloud.tel.model.AdBookEntry;
import com.zxcloud.tel.model.AdBookEntry.AdBookResp;
import com.zxcloud.tel.model.BaseResponse;

/**
 * @author xu.jian
 * 
 */
public class AdBookHandler extends BaseHandler {
	final static String ADBOOK_LIST = "/mails/getCompanyMailList";

	public AdBookHandler(Context c) {
		super(c);
	}

	public void getAdBookList(final ICallback<List<AdBookInfo>> callback) {
		final AdBookEntry entry = new AdBookEntry();
		entry.setBody(null);

		MyJsonObjectRequest req = baseRequest(ADBOOK_LIST, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						try {
							// 解密重新构造JSON
							JSONObject data = arg0.getJSONObject("data");
							String json = SecurityMgr.decode3DES(data
									.getString("departmentList"));
							data.remove("departmentList");

							//
							BaseResponse<AdBookResp> resp = fromJson(arg0,
									AdBookResp.class);
							AdBookResp bResp = entry.new AdBookResp();
							List<AdBookInfo> books = JsonUtil.gson.fromJson(
									json, new TypeToken<List<AdBookInfo>>() {
									}.getType());
							bResp.setDepartmentList(books);
							resp.setData(bResp);

							if (!haSuccess(resp))
								return;
							AdBookResp list = resp.getData();
							if (list == null) {
								// MessageUtil
								// .showShortToast(context, "同步通讯录信息出错");
								return;
							}
							callback.action(list.getDepartmentList());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						arg0.printStackTrace();
					}
				});
		commit(req);
	}
}

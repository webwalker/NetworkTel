package com.zxcloud.tel.http;

import org.json.JSONObject;

import webwalker.framework.http.MyJsonObjectRequest;
import webwalker.framework.interfaces.ICallback;
import webwalker.framework.interfaces.ICallback1;
import webwalker.framework.utils.Encrypter;
import webwalker.framework.utils.MessageUtil;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.zxcloud.tel.common.AppContext;
import com.zxcloud.tel.common.SipContext;
import com.zxcloud.tel.jsondata.AboutInfo;
import com.zxcloud.tel.jsondata.SipInfo;
import com.zxcloud.tel.model.AboutEntry;
import com.zxcloud.tel.model.AppEntry;
import com.zxcloud.tel.model.AppEntry.SipResp;
import com.zxcloud.tel.model.BaseResponse;
import com.zxcloud.tel.model.FeedBackEntry;
import com.zxcloud.tel.model.FeedBackEntry.ContentReq;
import com.zxcloud.tel.model.PasswordEntry;
import com.zxcloud.tel.model.PasswordEntry.SetPassReq;
import com.zxcloud.tel.model.PasswordEntry.SmsReq;
import com.zxcloud.tel.model.PasswordEntry.ValidateSmsReq;

/**
 * @author xu.jian
 * 
 */
public class AppHandler extends BaseHandler {
	final static String ABOUT = "/app/about";
	final static String FEEDBACK = "/app/feedback";
	final static String SEND_VALIDATE_MSG = "/user/sendValidateMsg";
	final static String SET_PASSWORD = "/user/setPassword";
	final static String VALIDATE_SMS_CODE = "/user/validate";
	final static String GET_SIP_PARAMS = "/app/getSipParams";

	public AppHandler(Context c) {
		super(c);
	}

	public void getAbout(final ICallback<AboutInfo> callback) {
		AboutEntry body = new AboutEntry();
		MyJsonObjectRequest req = baseRequest(ABOUT, body,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						BaseResponse<AboutInfo> resp = fromJson(arg0,
								AboutInfo.class);
						if (!haSuccess(resp))
							return;
						AboutInfo about = resp.getData();
						if (about != null)
							callback.action(about);
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						arg0.printStackTrace();
					}
				});
		commit(req);
	}

	public void postFeedBack(final ICallback1 callback, String text) {
		FeedBackEntry entry = new FeedBackEntry();
		ContentReq body = entry.new ContentReq();
		body.setContent(text);
		entry.setBody(body);

		MyJsonObjectRequest req = baseRequest(FEEDBACK, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						BaseResponse<Object> resp = fromJson(arg0);
						if (!haSuccess(resp))
							return;
						if (callback != null)
							callback.action();
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});
		commit(req);
	}

	public void sendSmsCode(String mobile) {
		PasswordEntry entry = new PasswordEntry();
		SmsReq body = entry.new SmsReq();
		body.setMobileNo(mobile);
		entry.setBody(body);

		MyJsonObjectRequest req = baseRequest(SEND_VALIDATE_MSG, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						BaseResponse<Object> resp = fromJson(arg0);
						if (!haSuccess(resp))
							return;
						MessageUtil.showShortToast(context, "验证码已发送");
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});
		commit(req);
	}

	public class PasswordType {
		public final static int RESET = 1;
		public final static int REGIST = 2;
		public final static int MODIFY = 3;
	}

	/**
	 * 
	 * @param oldPassword
	 * @param newPassword
	 * @param optype
	 *            1：忘记密码后重设密码，2：注册时的密码设置，3：修改密码
	 */
	public void setPassword(final ICallback1 callback, String user,
			String phoneNo, String oldPassword, String newPassword, int optype) {
		PasswordEntry entry = new PasswordEntry();
		SetPassReq body = entry.new SetPassReq();
		body.setMobileNo(phoneNo);
		if (!TextUtils.isEmpty(oldPassword))
			body.setOldPassword(Encrypter.encryptMD5(oldPassword));
		else
			body.setOldPassword("");
		body.setNewPassword(Encrypter.encryptMD5(newPassword));
		body.setOptype(optype);
		entry.setBody(body);
		AppContext.USERID = user;

		MyJsonObjectRequest req = baseRequest(SET_PASSWORD, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						BaseResponse<Object> resp = fromJson(arg0);
						if (!haSuccess(resp))
							return;
						callback.action();
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});
		commit(req);
	}

	// 短信验证码校验
	public void validateSmsCode(final ICallback1 callback, String mobile,
			String code) {
		PasswordEntry entry = new PasswordEntry();
		ValidateSmsReq body = entry.new ValidateSmsReq();
		body.setMobileNo(mobile);
		body.setCode(code);
		entry.setBody(body);

		MyJsonObjectRequest req = baseRequest(VALIDATE_SMS_CODE, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						BaseResponse<Object> resp = fromJson(arg0);
						if (!haSuccess(resp))
							return;
						callback.action();
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});
		commit(req);
	}

	public void getSipParams() {
		MyJsonObjectRequest req = baseRequest(GET_SIP_PARAMS, new AppEntry(),
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						try {
							BaseResponse<SipResp> resp = fromJson(arg0,
									SipResp.class);
							if (!haSuccess(resp))
								return;
							SipResp sipResp = resp.getData();
							if (sipResp == null || sipResp.getSipInfo() == null) {
								MessageUtil.showShortToast(context,
										"初始化失败, 请重启尝试。");
								return;
							}

							// 解密并存储SIP信息
							SipInfo sip = sipResp.getSipInfo();
							String v1 = sip.getV1();
							String v2 = sip.getV2();
							// sip.setUserName(SecurityMgr.decode3DES(v1));
							// sip.setPassword(SecurityMgr.decode3DES(v2));
							sip.setIp("111.13.51.28");
							sip.setPort("6001");
							sip.setUserName("31296937");
							sip.setPassword("jsxx@6937");
							SipContext.setConfig(sip);

							SipContext.setSip(context, true);
						} catch (Exception e) {
							showHttpError(e);
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

package com.zxcloud.tel.http;

import java.io.File;

import org.apache.http.Header;
import org.json.JSONObject;

import webwalker.framework.http.MyJsonObjectRequest;
import webwalker.framework.interfaces.ICallback;
import webwalker.framework.interfaces.ICallback1;
import webwalker.framework.utils.Encrypter;
import webwalker.framework.utils.JsonUtil;
import webwalker.framework.utils.Loggers;
import webwalker.framework.utils.MessageUtil;
import webwalker.framework.utils.UriUtil;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.message.UmengRegistrar;
import com.zxcloud.tel.activity.LoginActivity;
import com.zxcloud.tel.common.AppContext;
import com.zxcloud.tel.common.DataSyncMgr;
import com.zxcloud.tel.common.SecurityMgr;
import com.zxcloud.tel.jsondata.UserInfo;
import com.zxcloud.tel.model.BaseResponse;
import com.zxcloud.tel.model.LoginEntry;
import com.zxcloud.tel.model.LoginEntry.LoginReq;
import com.zxcloud.tel.model.LoginEntry.LoginResp;
import com.zxcloud.tel.model.UserEntry;
import com.zxcloud.tel.model.UserEntry.UserReq;
import com.zxcloud.tel.model.UserEntry.UserResp;
import com.zxcloud.tel.model.UserEntry.UserToken;

/**
 * @author xu.jian
 * 
 */
public class UserHandler extends BaseHandler {
	final static String LOGIN = "/user/login";
	final static String GET_USER_INFO = "/user/getUserInfo";
	final static String USER_REGISIT = "/user/register";
	final static String REGISTER_RECORD = "/user/registerRecord";
	final static String UPDATE_USER_INFO = "/user/updateUserInfo";
	final static String SAVE_PUSH_TOKEN = "/user/save4PushToken";
	final static String UPLOAD_USER_ICON = "/user/upload";
	private int tryTimes = 1;

	public UserHandler(Context c) {
		super(c);
	}

	public void login(final ICallback<UserInfo> callback, final String user,
			final String pass) {
		LoginEntry entry = new LoginEntry();
		final LoginReq body = entry.new LoginReq();
		body.setLoginNo(SecurityMgr.encode3DES(user));
		body.setPassword(pass);
		entry.setBody(body);
		AppContext.USERID = user;

		MyJsonObjectRequest req = baseRequest(LOGIN, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						try {
							BaseResponse<LoginResp> resp = fromJson(arg0,
									LoginResp.class);
							if (!haSuccess(resp, "您输入的用户名或密码可能有误，请重新输入。"))
								return;

							LoginResp loginResp = resp.getData();
							if (user == null) {
								MessageUtil.showShortToast(context,
										"您输入的用户名或密码可能有误，请重新输入。");
								return;
							}
							// session
							String userInfo = SecurityMgr.decode3DES(loginResp
									.getUserInfo().toString());
							AppContext.LOGIN_USER = JsonUtil.gson.fromJson(
									userInfo, UserInfo.class);
							if (AppContext.LOGIN_USER == null) {
								MessageUtil.showShortToast(context,
										"登录未能成功，部分使用功能可能会受限。");
								if (tryTimes <= 1) {
									login(callback, user, pass);
									tryTimes++;
								}
								return;
							}
							// save state
							AppContext.updateLogin(AppContext.LOGIN_USER, user);
							// get company adbook
							DataSyncMgr.getInstance().queryCompanyList();
							// get sip info
							DataSyncMgr.getInstance().getSipParams();
							// 绑定token，用于发送消息推送
							bindUserToken();

							if (callback != null) {
								AppContext.LOGIN_USER.setTempPass(pass);
								callback.action(AppContext.LOGIN_USER);
							}
						} catch (Exception e) {
							showHttpError(e);
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						showHttpError(arg0);
					}
				});
		commit(req);
	}

	public void getUserInfo(final ICallback<UserInfo> callback,
			final String user) {
		final LoginEntry entry = new LoginEntry();
		LoginReq body = entry.new LoginReq();
		body.setLoginNo(user);
		entry.setBody(body);

		MyJsonObjectRequest req = baseRequest(GET_USER_INFO, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						try {
							// 解密重新构造JSON
							JSONObject data = arg0.getJSONObject("data");
							String json = SecurityMgr.decode3DES(data
									.getString("userInfo"));
							data.remove("userInfo");

							BaseResponse<LoginResp<UserInfo>> resp = JsonUtil.gson.fromJson(
									arg0.toString(),
									new TypeToken<BaseResponse<LoginResp<UserInfo>>>() {
									}.getType());
							LoginResp<UserInfo> bResp = entry.new LoginResp<UserInfo>();
							UserInfo userInfo = JsonUtil.gson.fromJson(json,
									new TypeToken<UserInfo>() {
									}.getType());
							bResp.setUserInfo(userInfo);
							resp.setData(bResp);

							if (!haSuccess(resp, "获取个人信息出错啦，请返回重新尝试。"))
								return;
							LoginResp<UserInfo> user = resp.getData();
							if (user == null) {
								MessageUtil.showShortToast(context,
										"获取个人信息出错啦，请返回重新尝试。");
								return;
							}
							AppContext.LOGIN_USER = user.getUserInfo();
							callback.action(AppContext.LOGIN_USER);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});
		commit(req);
	}

	public void registUser(final ICallback<UserResp> callback,
			String companyId, String extensionNo, String secretCode) {
		UserEntry entry = new UserEntry();
		UserReq body = entry.new UserReq();
		body.setCompanyId(companyId);
		body.setExtensionNo(extensionNo);
		body.setSecretCode(SecurityMgr.encode3DES(secretCode));
		entry.setBody(body);

		MyJsonObjectRequest req = baseRequest(USER_REGISIT, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						try {
							BaseResponse<UserResp> resp = fromJson(arg0,
									UserResp.class);
							if (!haSuccess(resp, "对不起，您提交的信息不正确，请重新尝试。"))
								return;
							UserResp result = resp.getData();
							if (result == null) {
								MessageUtil.showShortToast(context,
										"注册失败了，请重新尝试。");
								return;
							}
							// if (!result.isRecorded()) {
							// MessageUtil.showShortToast(context,
							// "注册失败了，请重新尝试。");
							// return;
							// }
							callback.action(result);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});
		commit(req);
	}

	public void registUserRecord(String companyId, String userName, String pass) {
		UserEntry entry = new UserEntry();
		UserReq body = entry.new UserReq();
		body.setCompanyId(companyId);
		body.setUserName(userName);
		body.setPassword(Encrypter.encryptMD5(pass));
		entry.setBody(body);

		MyJsonObjectRequest req = baseRequest(REGISTER_RECORD, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						try {
							BaseResponse<UserResp> resp = fromJson(arg0);
							if (!haSuccess(resp, "处理出错了"))
								return;
							MessageUtil.showShortToast(context, "恭喜,注册成功！");
							UriUtil.startActivity(context, LoginActivity.class);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});
		commit(req);
	}

	public void updateUserInfo(final ICallback1 callback, UserInfo user) {
		UserEntry entry = new UserEntry();
		String userName = SecurityMgr.encode3DES(user.getUserName());
		String userMobile = "";
		if (!TextUtils.isEmpty(userMobile))
			userMobile = SecurityMgr.encode3DES(user.getMobileNo());
		String email = SecurityMgr.encode3DES(user.getEmail());
		user.setUserName(userName);
		user.setMobileNo(userMobile);
		user.setEmail(email);
		entry.setBody(user);

		MyJsonObjectRequest req = baseRequest(UPDATE_USER_INFO, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						try {
							BaseResponse<UserResp> resp = fromJson(arg0);
							if (!haSuccess(resp, "亲，更新您的资料信息出错了。"))
								return;
							if (callback != null)
								callback.action();
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

	/**
	 * 用于更新客户端与用户的绑定关系
	 * 
	 * @param info
	 */
	public void bindUserToken() {
		if (true)
			return;

		UserEntry entry = new UserEntry();
		UserToken token = entry.new UserToken();
		token.setToken(UmengRegistrar.getRegistrationId(context));
		entry.setBody(token);

		MyJsonObjectRequest req = baseRequest(SAVE_PUSH_TOKEN, entry,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						try {
							BaseResponse<UserResp> resp = fromJson(arg0);
							if (!haSuccess(resp)) {
								Loggers.d("bindUserToken: failed");
								return;
							}
							Loggers.d("bindUserToken: success");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});
		commit(req);
	}

	public void uploadUserIcon(String path) {
		// 异步的客户端对象
		AsyncHttpClient client = new AsyncHttpClient();
		// 指定url路径
		String url = getUrl(UPLOAD_USER_ICON);
		// 封装文件上传的参数
		RequestParams params = new RequestParams();
		// 根据路径创建文件
		File file = new File(path);
		try {
			params.put("file", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 执行post请求

		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				if (statusCode == 200)
					MessageUtil.showShortToast(context, "上传成功");
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				error.printStackTrace();
			}

			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				super.onProgress(bytesWritten, totalSize);
				// 上传进度显示
				int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
				Loggers.e("上传 Progress>>>>>", bytesWritten + " / " + totalSize);
			}

			@Override
			public void onRetry(int retryNo) {
				super.onRetry(retryNo);
			}

		});
	}
}

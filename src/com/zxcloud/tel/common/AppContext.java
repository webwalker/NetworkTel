package com.zxcloud.tel.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import webwalker.framework.beans.PinYinItem;
import webwalker.framework.http.BasicVolleyHttpClient;
import webwalker.framework.utils.ImageUnity;
import webwalker.framework.utils.Loggers;
import webwalker.framework.utils.MessageUtil;
import webwalker.framework.utils.SharedUtil;
import android.content.Context;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.zxcloud.tel.jsondata.UserInfo;
import com.zxcloud.tel.model.SearchPerson;

/**
 * 当前用户的上下文信息
 * 
 * @author xu.jian
 * 
 */
public class AppContext {
	public static final long clickTimes = 800;
	public static String TAG = "ZXCloud";
	public static String USERID = "-1";
	public static UserInfo LOGIN_USER;
	public static boolean needUpdateUserInfo = false;
	private static boolean isAppCall = true; // 是否采用App进行呼叫
	public static final String BASE_URL = "http://60.191.151.58:8081/NP";
	private static Set<String> searchMap = Collections
			.synchronizedSet(new HashSet<String>());
	public static List<PinYinItem> SEARCH_ITEM = Collections
			.synchronizedList(new ArrayList<PinYinItem>());

	public static void updateLogin(UserInfo user, String userId) {
		if (user == null)
			return;
		LOGIN_USER = user;
		USERID = userId;
	}

	/**
	 * 是否已登录
	 * 
	 * @return
	 */
	public static boolean hasLogin() {
		if (LOGIN_USER != null && !USERID.equals("-1"))
			return true;
		return false;
	}

	public static void searchAdd(SearchPerson sp) {
		if (sp.getTag() == null)
			return;
		String set = sp.getChineseFont() + sp.getTag();
		if (searchMap.contains(set))
			return;
		searchMap.add(set);
		SEARCH_ITEM.add(sp);
	}

	public static void recycle() {
		try {
			Loggers.d(TAG, "recycle resource...");
			CacheMgr.clear();
			searchMap.clear();
			SEARCH_ITEM.clear();
			ImageUnity.getLoader().clearMemoryCache();
			// cancel request
			BasicVolleyHttpClient client = BasicVolleyHttpClient.getInstance();
			client.cancelPendingRequests(client.TAG);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isAppCall() {
		return isAppCall;
	}

	public static void setAppCall(boolean isApp) {
		isAppCall = isApp;
	}

	public static void clearLoginCache(Context context) {
		// 移除本地缓存
		SharedUtil.getInstance(context).remove("pass");
		SharedUtil.getInstance(context).remove("logindate");
	}

	public static void setUpdateListener(final Context context,
			final boolean showMessage) {
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				String msg = "";
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					UmengUpdateAgent.showUpdateDialog(context, updateInfo);
					break;
				case UpdateStatus.No: // has no update
					msg = "当前没有可更新的版本。";
					break;
				case UpdateStatus.NoneWifi: // none wifi
					msg = "wifi没有连接， 需要在wifi下进行更新。";
					break;
				case UpdateStatus.Timeout: // time out
					msg = "更新超时。";
					break;
				}
				if (showMessage && !msg.equals("")) {
					MessageUtil.showShortToast(context, msg);
				}
			}
		});
	}
}

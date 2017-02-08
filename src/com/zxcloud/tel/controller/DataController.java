package com.zxcloud.tel.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import webwalker.framework.utils.Loggers;
import webwalker.framework.utils.SQLiteUtil;
import android.content.Context;
import android.database.Cursor;

import com.zxcloud.tel.common.AppContext;
import com.zxcloud.tel.common.CacheMgr;
import com.zxcloud.tel.jsondata.RecentCallInfo;
import com.zxcloud.tel.jsondata.UserInfo;

/**
 * @author xu.jian
 * 
 */
public class DataController extends BaseController {
	static MyDataHelper helper = null;
	static Pattern pattern = Pattern.compile("^(?:17951|12593)");
	RecentCallHelper recentHelper = null;

	public DataController(Context context) {
		super(context);
		helper = new MyDataHelper(context);
		recentHelper = new RecentCallHelper(context);
	}

	public static String getArea(String phoneNumber) {
		if (phoneNumber.length() < 11)
			return "";
		phoneNumber = getFormatNumbers(phoneNumber);
		Cursor cursor = null;
		if (phoneNumber.startsWith("0")) { // 固话
			cursor = helper.getTelArea(phoneNumber);
		} else if (phoneNumber.startsWith("1")) {
			cursor = helper.getMobileArea(phoneNumber);
		}
		try {
			while (cursor != null && cursor.moveToNext()) {
				return SQLiteUtil.getString(cursor, "MobileArea");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			helper.close();
		}
		return "";
	}

	private static String getFormatNumbers(String number) {
		String result = number;
		Matcher matcher = pattern.matcher(number);
		if (matcher.find()) {
			result = matcher.replaceFirst("");
		}
		return result;
	}

	// 从缓存中查询常用联系人
	public List<RecentCallInfo> getCachedRecentCalls() {
		Object data = CacheMgr.get(CacheMgr.RECENT_CALLS);
		if (data != null)
			return (List<RecentCallInfo>) data;
		List<RecentCallInfo> calls = getRecentCalls();
		if (calls != null && calls.size() > 0) {
			CacheMgr.set(CacheMgr.RECENT_CALLS, calls);
		}
		return calls;
	}

	// 查询SQLITE最新常用联系人
	private List<RecentCallInfo> getRecentCalls() {
		List<RecentCallInfo> list = new ArrayList<RecentCallInfo>();
		try {
			Cursor cursor = recentHelper.getRecentCalls(Integer.MAX_VALUE);
			while (cursor.moveToNext()) {
				RecentCallInfo call = new RecentCallInfo();
				call.setUserId(cursor.getInt(cursor.getColumnIndex("UserID")));
				call.setUserName(cursor.getString(cursor
						.getColumnIndex("UserName")));
				call.setDepartment(cursor.getString(cursor
						.getColumnIndex("Department")));
				call.setUserIcon(cursor.getString(cursor
						.getColumnIndex("UserIcon")));
				call.setPhone(cursor.getString(cursor.getColumnIndex("Phone")));
				call.setExtensionNo(cursor.getString(cursor
						.getColumnIndex("ExtensionNO")));
				call.setCallTimes(cursor.getInt(cursor
						.getColumnIndex("CallTimes")));
				call.setExt(cursor.getString(cursor.getColumnIndex("Ext")));

				list.add(call);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			recentHelper.close();
		}
		return list;
	}

	// 是否已存在常用联系人
	public boolean hasExistRecentCall(String userName, String phone) {
		try {
			Cursor cursor = recentHelper.getSingleRecentCalls(userName, phone);
			if (cursor.moveToNext())
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			recentHelper.close();
		}
		return false;
	}

	// 更新次数
	public boolean updateRecentCalls(UserInfo user) {
		try {
			boolean ret = recentHelper.updateRecentCalls(user);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 新增常用联系人
	public boolean insertRecentCalls(UserInfo user) {
		try {
			boolean ret = recentHelper.insertRecentCalls(user);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 保存最近通话人记录
	public static void setRecentCalls(final UserInfo userInfo,
			final Context context) {
		if (userInfo == null || userInfo.getMobileNo() == null
				|| userInfo.getMobileNo().equals("")) {
			return;
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Loggers.d(AppContext.TAG,
							"setRecentCalls:" + userInfo.toString());
					DataController controller = new DataController(context);
					boolean exist = controller.hasExistRecentCall(
							userInfo.getUserName(), userInfo.getMobileNo());
					if (exist) {
						controller.updateRecentCalls(userInfo);
					} else {
						controller.insertRecentCalls(userInfo);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}

package com.zxcloud.tel.controller;

import webwalker.framework.common.AbstractDBHelper;
import webwalker.framework.utils.DateUtil;
import android.content.Context;
import android.database.Cursor;

import com.zxcloud.tel.R;
import com.zxcloud.tel.jsondata.UserInfo;

/**
 * @author xu.jian
 * 
 */
public class RecentCallHelper extends AbstractDBHelper {

	public RecentCallHelper(Context context) {
		super(context);
	}

	@Override
	protected String getTag() {
		return "RecentCallHelper";
	}

	@Override
	protected String getDbName() {
		return "inc.db";
	}

	@Override
	protected int getDatabaseVersion() {
		return 1;
	}

	@Override
	protected String[] createDBTables() {
		return null;
	}

	@Override
	protected String[] dropDBTables() {
		return null;
	}

	// 获取常用通话联系人
	public Cursor getRecentCalls(int number) {
		return Query(R.string.select_recent_calls, new String[] { number + "" });
	}

	// 判断是否已存在联系人
	public Cursor getSingleRecentCalls(String userName, String phone) {
		return Query(R.string.select_single_recent_calls, new String[] {
				userName, phone });
	}

	// 插入常用通话联系人
	public boolean insertRecentCalls(UserInfo user) {
		if (user == null)
			return false;
		return super.execAutoClose(R.string.insert_recent_calls, new Object[] {
				user.getUserId(), user.getUserName(), user.getDepartmentName(),
				user.getPicUrl(), user.getMobileNo(), user.getExtensionNo(), 1,
				"", DateUtil.getCurrentTime() });
	}

	// 更新最近通话联系人呼叫信息
	public boolean updateRecentCalls(UserInfo user) {
		if (user == null)
			return false;
		return super.execAutoClose(R.string.update_recent_calls, new Object[] {
				user.getUserName(), user.getMobileNo() });
	}
}

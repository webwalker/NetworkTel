package com.zxcloud.tel.controller;

import webwalker.framework.common.AbstractDBHelper;
import android.content.Context;
import android.database.Cursor;

import com.zxcloud.tel.R;

/**
 * @author xu.jian
 * 
 */
public class MyDataHelper extends AbstractDBHelper {

	public MyDataHelper(Context context) {
		super(context);
	}

	@Override
	protected String getTag() {
		return "MyDataHelper";
	}

	@Override
	protected String getDbName() {
		return "locations.db";
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

	// 获取手机号归属地
	public Cursor getMobileArea(String phoneNumber) {
		if (phoneNumber.length() <= 7)
			return null;
		return Query(R.string.select_mobile_area,
				new String[] { phoneNumber.substring(0, 7) });
	}

	// 获取手机号归属地
	public Cursor getTelArea(String phoneNumber) {
		if (phoneNumber.length() <= 4)
			return null;
		return Query(
				R.string.select_tel_area,
				new String[] { phoneNumber.substring(0, 3),
						phoneNumber.substring(0, 4) });
	}
}

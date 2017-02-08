package com.zxcloud.tel.activity;

import webwalker.framework.utils.AppUtil;
import webwalker.framework.utils.UIUtils;
import webwalker.framework.utils.UriUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zxcloud.tel.BaseActivity;
import com.zxcloud.tel.GuideActivity;
import com.zxcloud.tel.R;
import com.zxcloud.tel.common.AppContext;

public class SettingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		initTopBar(this, "设置");

		String version = AppUtil.getVersionName(this);
		aq.id(R.id.tvVersion).format(version);
		// 显示退出登录按钮
		if (AppContext.hasLogin())
			aq.id(R.id.btnExit).visible();
		else
			aq.id(R.id.btnExit).invisible();

		aq.id(R.id.lAbout).clicked(this);
		aq.id(R.id.lVersion).clicked(this);
		aq.id(R.id.lSetting).clicked(this);
		aq.id(R.id.lProtocal).clicked(this);
		aq.id(R.id.lFeedback).clicked(this);
		aq.id(R.id.lHelp).clicked(this);
		aq.id(R.id.btnExit).clicked(this);
		aq.id(R.id.tvVersion).format(AppUtil.getVersionName(getBaseContext()));
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		switch (id) {
		case R.id.lAbout:
			UriUtil.startActivity(this, AboutActivity.class);
			break;
		case R.id.lVersion:
			UriUtil.startActivity(this, UpdateActivity.class);
			break;
		case R.id.lSetting:
			break;
		case R.id.lProtocal:
			UriUtil.startActivity(this, ProtocalActivity.class);
			break;
		case R.id.lFeedback:
			UriUtil.startActivity(this, FeedbackActivity.class);
			break;
		case R.id.lHelp:
			Intent it = new Intent(this, GuideActivity.class);
			it.putExtra("forceShow", true);
			startActivity(it);
			break;
		case R.id.btnExit:
			if (UIUtils.isFastDoubleClick())
				return;
			AppContext.LOGIN_USER = null;
			AppContext.clearLoginCache(this);
			MainActivity.instance.finish();
			UriUtil.startActivityClearTask(this, LoginActivity.class);
			finish();
			break;
		}
	}
}

package com.zxcloud.tel.activity;

import org.sipdroid.sipua.ui.InCallScreen;

import webwalker.framework.uiquery.AQuery;
import webwalker.framework.utils.MessageUtil;
import webwalker.framework.utils.NetworkUtils;
import webwalker.framework.utils.UriUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.zxcloud.tel.R;
import com.zxcloud.tel.common.AppContext;
import com.zxcloud.tel.controller.DataController;
import com.zxcloud.tel.jsondata.UserInfo;

public class CallSelectorActivity extends Activity {
	private AQuery aq;
	private String number = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_call_selector);

		aq = new AQuery(this);
		number = getIntent().getStringExtra("number");
		String location = DataController.getArea(number);
		if (!TextUtils.isEmpty(location)) {
			location = "(" + location + ")";
		}
		aq.id(R.id.tv_title).text(number + location);
		aq.id(R.id.lWifi).clicked(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dial(CallSelectorActivity.this, 1);
			}
		});
		aq.id(R.id.lFree).clicked(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dial(CallSelectorActivity.this, 2);
			}
		});
		aq.id(R.id.lSim).clicked(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dial(CallSelectorActivity.this, 3);
			}
		});
	}

	private void dial(Activity act, int type) {
		UserInfo user = new UserInfo();
		user.setUserName(number);
		user.setMobileNo(number);
		DataController.setRecentCalls(user, act);

		switch (type) {
		case 1:
			if (!NetworkUtils.isWifi(act)) {
				MessageUtil.showShortToast(act, "WIFI没有连接。");
				break;
			}
			Intent it = new Intent(act, InCallScreen.class);
			it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			it.putExtra("type", type);
			it.putExtra("user", user);
			act.startActivity(it);
			finish();
		case 2:
			MessageUtil.showShortToast(this, "此功能暂时还不支持。");
			break;
		case 3: // 手机卡呼叫
			UriUtil.callPhone(act, user.getMobileNo());
			AppContext.setAppCall(false);
			finish();
			break;
		}
	}
}

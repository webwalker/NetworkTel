package com.zxcloud.tel;

import org.sipdroid.sipua.ui.Receiver;
import org.sipdroid.sipua.ui.RegisterService;
import org.sipdroid.sipua.ui.Settings;

import webwalker.framework.utils.UriUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.zxcloud.tel.common.SipContext;
import com.zxcloud.tel.jsondata.SipInfo;

public class SipTestActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sip_test);

		SipInfo sip = new SipInfo();
		sip.setIp("111.13.51.28");
		sip.setPort("6001");
		sip.setUserName("31296937");
		sip.setPassword("jsxx@6937");
		SipContext.setConfig(sip);

		// 初始化sip
		// SipMgr.init(this);
		on(this, true);

		aq.id(R.id.button1).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String number = aq.id(R.id.editText1).getText().toString();
				// Receiver.engine(SipTestActivity.this).call(number, true);
				UriUtil.callPhone(SipTestActivity.this, number);
			}
		});
		aq.id(R.id.button2).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				on(SipTestActivity.this, false);
				Receiver.pos(true);
				Receiver.engine(SipTestActivity.this).halt();
				Receiver.mSipdroidEngine = null;
				Receiver.reRegister(0);
				stopService(new Intent(SipTestActivity.this,
						RegisterService.class));
				finish();
			}
		});
	}

	public static boolean on(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(Settings.PREF_ON, Settings.DEFAULT_ON);
	}

	public static void on(Context context, boolean on) {
		Editor edit = PreferenceManager.getDefaultSharedPreferences(context)
				.edit();
		edit.putBoolean(Settings.PREF_ON, on);
		edit.commit();
		if (on)
			Receiver.engine(context).isRegistered();
	}
}

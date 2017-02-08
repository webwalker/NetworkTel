package com.zxcloud.tel.activity;

import org.sipdroid.sipua.ui.InCallScreen;
import org.sipdroid.sipua.ui.Receiver;

import webwalker.framework.interfaces.ICallback;
import webwalker.framework.system.telephone.TelephoneMgr;
import webwalker.framework.uiquery.AQuery;
import webwalker.framework.utils.ImageUnity;
import webwalker.framework.utils.UriUtil;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.zxcloud.tel.R;
import com.zxcloud.tel.common.DataSyncMgr;
import com.zxcloud.tel.jsondata.UserInfo;

public class CallActivity extends InCallScreen {
	private int type;// 1wifi，2免流量
	private UserInfo user;
	private AQuery aq;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call);

		bindEvent();
		init();
		call();
	}

	private void bindEvent() {
		aq = new AQuery(this);
		type = UriUtil.getIntValue(this, "type");
		user = (UserInfo) UriUtil.getSerializableValue(this, "user");

		aq.id(R.id.ivBack).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// 静音
		aq.id(R.id.ivSilence).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TelephoneMgr.silence(CallActivity.this);
			}
		});
		// 免提
		aq.id(R.id.ivSpeaker).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TelephoneMgr.speaker(CallActivity.this);
			}
		});
		// 拨号键盘
		aq.id(R.id.ivDial).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!aq.id(R.id.dial_keyboard_mini).isVisible()) {
					aq.id(R.id.dial_keyboard_mini).visible();
				} else {
					aq.id(R.id.dial_keyboard_mini).gone();
				}
			}
		});
		// 通信录
		aq.id(R.id.ivContact).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cancelCall();
				Intent it = new Intent(CallActivity.this, MainActivity.class);
				it.putExtra("tab", 1);
				startActivity(it);
			}
		});
		// 取消呼叫
		aq.id(R.id.ibCancel).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cancelCall();
			}
		});
	}

	private void init() {
		UserInfo userInfo = DataSyncMgr.getInstance().getCompanyPerson(
				user.getMobileNo());
		if (userInfo == null)
			return;
		aq.id(R.id.txtUser).text(userInfo.getUserName());
		if (TextUtils.isEmpty(userInfo.getPicUrl()))
			ImageUnity.getLoader().displayImage(userInfo.getPicUrl(),
					aq.id(R.id.txtUser).getImageView());
	}

	private void call() {
		ICallback<Boolean> callback = new ICallback<Boolean>() {
			@Override
			public void action(Boolean data) {
				if (!data)
					// finish();
					return;
				aq.id(R.id.tvCallStatus).text("通话中");
			}
		};

		String number = "10086";// user.getMobileNo();
		Receiver.engine(CallActivity.this).call(number, true);

		// calling
		// CallHandler call = new CallHandler(this);
		// call.callNumber(callback, AppContext.USERID, user.getMobileNo(),
		// type);
	}

	private void cancelCall() {

	}

	// 发送DTMF
	public void keyNum(View v) {
		EditText etNumber = aq.id(R.id.etDialNum).getEditText();
		String tag = v.getTag().toString();
		String numbers = etNumber.getText().toString() + tag;
		etNumber.setText(numbers);
		etNumber.setSelection(numbers.length());

		// int index = etNumber.getSelectionStart();
		// Editable editable = etNumber.getText();
		// editable.insert(index, tag);
	}
}

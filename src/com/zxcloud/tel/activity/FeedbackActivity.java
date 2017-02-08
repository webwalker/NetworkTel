package com.zxcloud.tel.activity;

import webwalker.framework.interfaces.ICallback1;
import webwalker.framework.utils.DialogUtil;
import webwalker.framework.utils.MessageUtil;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.zxcloud.tel.BaseActivity;
import com.zxcloud.tel.R;
import com.zxcloud.tel.http.AppHandler;

public class FeedbackActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		initTopBar(this, "意见反馈");

		aq.id(R.id.btnSubmit).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = aq.id(R.id.etFeedback).getText().toString();
				if (TextUtils.isEmpty(text)) {
					showActMessage("请输入您的反馈意见。");
					return;
				}

				ICallback1 callback = new ICallback1() {
					@Override
					public void action() {
						MessageUtil.showShortToast(context, "已成功提交您的反馈，非常感谢!");
						finish();
					}
				};
				AppHandler app = new AppHandler(getBaseContext());
				app.postFeedBack(callback, text);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_BACK) {
			String text = aq.id(R.id.etFeedback).getText().toString();
			if (!TextUtils.isEmpty(text)) {
				android.content.DialogInterface.OnClickListener okListener = new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				};
				android.content.DialogInterface.OnClickListener cancelListener = new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};
				Dialog dialog = DialogUtil.createConfirmDialog(this, "",
						"是否放弃修改", "确定", "取消", okListener, cancelListener,
						DialogUtil.NO_ICON);
				dialog.show();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}

package com.zxcloud.tel;

import webwalker.framework.common.BaseFrameworkActivity;
import webwalker.framework.utils.UriUtil;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.zxcloud.tel.ui.MessageDialog;
import com.zxcloud.tel.ui.TopNavBar;

public class BaseActivity extends BaseFrameworkActivity {
	private Handler uiHandler = null;
	protected Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 不管用户如何旋转设备显示方向都不会随着改变
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		super.onCreate(savedInstanceState);

		context = this;
		uiHandler = getUIHandler();
	}

	protected TopNavBar initTopBar(String title) {
		return initTopBar(null, title);
	}

	protected TopNavBar initTopBar(Activity act, String title) {
		TopNavBar bar = (TopNavBar) findViewById(R.id.inc_navbar);
		bar.setActivity(act);
		bar.setTv1(title);
		return bar;
	}

	protected TopNavBar initTopBar(Activity act, View v, String title) {
		TopNavBar bar = (TopNavBar) v.findViewById(R.id.inc_navbar);
		bar.setActivity(act);
		bar.setTv1(title);
		return bar;
	}

	protected void showDialogMessage(String msg) {
		MessageDialog dialog = new MessageDialog(context, msg);
		dialog.show();
	}

	protected void showActMessage(String msg) {
		UriUtil.startMsgActivityNewTask(context, MessageActivity.class, msg);
	}

	protected void asyncMessage(Context c, int resId) {
		asyncMessage(c, getString(resId));
	}

	protected void asyncMessage(Context c, String data) {
		Message msg = new Message();
		msg.obj = data;
		getUIHandler().sendMessage(msg);
	}

	protected Handler getUIHandler() {
		if (uiHandler == null) {
			uiHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					MessageDialog dialog = new MessageDialog(context,
							msg.obj.toString());
					dialog.show();
				}
			};
		}

		return uiHandler;
	}
}

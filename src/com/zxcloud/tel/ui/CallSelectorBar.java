package com.zxcloud.tel.ui;

import org.sipdroid.sipua.ui.InCallScreen;

import webwalker.framework.interfaces.ICallback1;
import webwalker.framework.system.contact.DialRecordWrapper;
import webwalker.framework.uiquery.AQuery;
import webwalker.framework.utils.MessageUtil;
import webwalker.framework.utils.NetworkUtils;
import webwalker.framework.utils.UIUtils;
import webwalker.framework.utils.UriUtil;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.zxcloud.tel.R;
import com.zxcloud.tel.common.AppContext;
import com.zxcloud.tel.common.DataSyncMgr;
import com.zxcloud.tel.common.SipContext;
import com.zxcloud.tel.controller.DataController;
import com.zxcloud.tel.jsondata.UserInfo;

/**
 * @author xu.jian
 * 
 */
public class CallSelectorBar {
	private UserInfo userInfo;
	private PopupWindow popupWindow = null;
	private AQuery aq = null;
	private static Activity act;
	private ICallback1 callback = null;
	private ViewGroup parent;
	private View mask;

	public CallSelectorBar(final Activity act, ICallback1 cb) {
		this.act = act;
		this.callback = cb;
		if (null == popupWindow) {
			parent = (ViewGroup) act.getWindow().getDecorView()
					.findViewById(android.R.id.content);
			mask = UIUtils.getView(act, R.layout.call_selector_bar);
			parent.removeView(mask);
			parent.addView(mask);

			View v = UIUtils.getView(act, R.layout.pop_dial);
			aq = new AQuery(v);
			aq.id(R.id.lWifi).clicked(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dial(act, 1);
				}
			});
			aq.id(R.id.lFree).clicked(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dial(act, 2);
				}
			});
			aq.id(R.id.lSim).clicked(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dial(act, 3);
				}
			});
			popupWindow = new PopupWindow(v, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT, true);
			popupWindow.setAnimationStyle(R.style.PopupAnimation);
		}
		// for dismiss
		popupWindow.setBackgroundDrawable(act.getResources().getDrawable(
				R.drawable.ic_launcher));
		popupWindow.setOutsideTouchable(true);
		popupWindow.showAtLocation(act.getWindow().getDecorView(),
				Gravity.BOTTOM, 0, 0);
		popupWindow.update();
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				parent.findViewById(R.id.popMask).setVisibility(View.GONE);
			}
		});
	}

	public void setMaskView(View v) {
		this.mask = v;
	}

	public void showPopWindow(UserInfo user) {
		this.userInfo = user;
		parent.findViewById(R.id.popMask).setVisibility(View.VISIBLE);
	}

	private void dial(Activity act, int type) {
		String msg = "暂时无法接通，请稍候重试！";
		if (userInfo == null || TextUtils.isEmpty(userInfo.getMobileNo())) {
			MessageUtil.showShortToast(act, msg);
			return;
		}
		if (TextUtils.isEmpty(SipContext.getIp())) {
			DataSyncMgr.getInstance().getSipParams();
			SipContext.setSip(act, true);
			// MessageUtil.showShortToast(act, msg);
			// return;
		}

		DataController.setRecentCalls(userInfo, act);

		switch (type) {
		case 1:
			if (!NetworkUtils.isWifi(act)) {
				MessageUtil.showShortToast(act, "WIFI没有连接");
				break;
			}
			Intent it = new Intent(act, InCallScreen.class);
			it.putExtra("type", type);
			it.putExtra("user", userInfo);
			act.startActivity(it);

			// 保存通话记录
			DialRecordWrapper.getInstance().saveCalllog(act,
					userInfo.getMobileNo(), userInfo.getUserName());
			// 更新通话记录缓存
			DataSyncMgr.getInstance().queryDialRecord();

			break;
		case 2:
			MessageUtil.showShortToast(act, "此功能暂时还不支持。");
			break;
		case 3:
			UriUtil.callPhone(act, userInfo.getMobileNo());
			AppContext.setAppCall(false);
			break;
		}
		popupWindow.dismiss();
		if (callback != null) {
			callback.action();
		}
	}
}

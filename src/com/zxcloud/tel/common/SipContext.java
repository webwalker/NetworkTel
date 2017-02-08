package com.zxcloud.tel.common;

import org.sipdroid.sipua.ui.Receiver;
import org.sipdroid.sipua.ui.RegisterService;
import org.sipdroid.sipua.ui.Settings;

import webwalker.framework.utils.Loggers;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.zxcloud.tel.jsondata.SipInfo;

/**
 * @author xu.jian
 * 
 */
public class SipContext {
	private static SipInfo sipInfo = new SipInfo();

	public static void setConfig(SipInfo info) {
		sipInfo = info;
	}

	public static String getIp() {
		return sipInfo.getIp();
	}

	public static String getPort() {
		return sipInfo.getPort();
	}

	public static String getUserName() {
		return sipInfo.getUserName();
	}

	public static String getPassword() {
		return sipInfo.getPassword();
	}

	/**
	 * 是否已开启SIP客户端服务
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasSipOpen(Context context) {
		boolean isOn = PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(Settings.PREF_ON, false);
		return isOn;
	}

	/**
	 * 开关SIP客户端
	 * 
	 * @param context
	 * @param on
	 */
	public static void setSip(Context context, boolean on) {
		Loggers.d(AppContext.TAG, "setSip:" + on);
		Editor edit = PreferenceManager.getDefaultSharedPreferences(context)
				.edit();
		edit.putBoolean(Settings.PREF_ON, on);
		edit.commit();
		if (on) {
			if (AppContext.LOGIN_USER == null
					|| TextUtils.isEmpty(SipContext.getIp())) {
				Loggers.d(AppContext.TAG, "preparing sip failed");
				return;
			}
			Receiver.engine(context).isRegistered();
		} else {
			Receiver.pos(true);
			Receiver.engine(context).halt();
			Receiver.mSipdroidEngine = null;
			Receiver.reRegister(0);
			context.stopService(new Intent(context, RegisterService.class));
		}
	}
}

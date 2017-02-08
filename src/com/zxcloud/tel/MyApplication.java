package com.zxcloud.tel;

import webwalker.framework.common.BaseApplication;
import webwalker.framework.http.BasicVolleyHttpClient;
import webwalker.framework.utils.ImageUnity;
import webwalker.framework.utils.MessageUtil;
import android.app.PendingIntent;

import com.zxcloud.tel.common.AppContext;
import com.zxcloud.tel.common.DeviceMgr;

/**
 * @author xujian
 * 
 */
public class MyApplication extends BaseApplication {
	PendingIntent restartIntent;

	@Override
	public void onCreate() {
		super.onCreate();
		// 上下文
		MessageUtil.setContext(getContext());
		// image loader
		ImageUnity.initLoader(getContext());
		// http
		BasicVolleyHttpClient.getInstance().setContext(getContext());
		// 设备信息
		DeviceMgr device = new DeviceMgr();
		device.init(getContext());

		// 异常后重启
		// initUncaughtHandler(AppContext.TAG, SplashActivity.class);
	}

}

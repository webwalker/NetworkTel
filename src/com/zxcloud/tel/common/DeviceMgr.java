package com.zxcloud.tel.common;

import webwalker.framework.utils.AppUtil;
import webwalker.framework.utils.SharedUtil;
import webwalker.framework.utils.SystemUtil;
import webwalker.framework.utils.UIUtils;
import android.content.Context;

import com.zxcloud.tel.jsondata.DeviceInfo;

/**
 * @author xujian
 * 
 */
public class DeviceMgr {
	private Context context;
	private static final DeviceInfo device = new DeviceInfo();

	public void init(Context c) {
		try {
			context = c;
			device.setMode(SystemUtil.getModel());
			device.setDpi(UIUtils.getDpi(context));
			device.setOsVersion(SystemUtil.getOSVersion());

			String version = AppUtil.getVersionName(context);
			version = version.replaceAll("v|V", "");
			device.setAppVersion(version);

			// 首次运行
			boolean firstRun = SharedUtil.getInstance(context).getBoolean(
					"firstRun");
			device.setFirstRun(firstRun);
			SharedUtil.getInstance(context).set("firstRun", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static DeviceInfo getDeviceInfo() {
		return device;
	}
}

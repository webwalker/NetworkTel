package com.zxcloud.tel.jsondata;

/**
 * @author xu.jian
 * 
 */
public class DeviceInfo {
	private String mode;
	private float dpi;
	private String osVersion;
	private String os = "Android";
	private String appVersion;
	private boolean firstRun;

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public float getDpi() {
		return dpi;
	}

	public void setDpi(float dpi) {
		this.dpi = dpi;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public boolean isFirstRun() {
		return firstRun;
	}

	public void setFirstRun(boolean firstRun) {
		this.firstRun = firstRun;
	}

}

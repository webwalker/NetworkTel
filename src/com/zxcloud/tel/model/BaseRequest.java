package com.zxcloud.tel.model;

import com.zxcloud.tel.common.DeviceMgr;
import com.zxcloud.tel.jsondata.DeviceInfo;

/**
 * @author xu.jian
 * 
 */
public abstract class BaseRequest {
	private Object body;
	private DeviceInfo device;

	public BaseRequest() {
	}

	public BaseRequest(boolean addDevice) {
		this.setDevice(DeviceMgr.getDeviceInfo());
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public DeviceInfo getDevice() {
		return device;
	}

	public void setDevice(DeviceInfo device) {
		this.device = device;
	}

}

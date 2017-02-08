package com.zxcloud.tel.model;

import com.zxcloud.tel.jsondata.ResponseInfo;

/**
 * @author xu.jian
 * @param <T>
 * 
 */
public class BaseResponse<T> {
	private ResponseInfo status;
	private T data;

	public T getResponseData() {
		if (data != null)
			return (T) data;
		return null;
	}

	public ResponseInfo getStatus() {
		return status;
	}

	public void setStatus(ResponseInfo status) {
		this.status = status;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}

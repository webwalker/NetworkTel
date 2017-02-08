package com.zxcloud.tel.controller;

import android.content.Context;

/**
 * @author 
 * 
 */
public abstract class BaseController {
	Context context;

	public BaseController() {
	}

	public BaseController(Context context) {
		this.context = context;
	}

}

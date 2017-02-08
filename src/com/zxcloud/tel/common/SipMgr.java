package com.zxcloud.tel.common;

import org.sipdroid.sipua.ui.Receiver;

import android.content.Context;

/**
 * Sip 初始化
 * 
 * @author xu.jian
 * 
 */
public class SipMgr {
	public static void init(Context context) {
		Receiver.engine(context).registerMore();
	}
}

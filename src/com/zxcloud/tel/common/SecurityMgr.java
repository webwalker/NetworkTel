package com.zxcloud.tel.common;

import webwalker.framework.utils.DateUtil;
import webwalker.framework.utils.Encrypter3DES;

/**
 * @author xujian
 * 
 */
public class SecurityMgr {

	private final static String SECRETKEY = "netPhone.des3@88881234.com";
	private final static String SPLITER = ":";

	public static String signHeaders(String userId, String url) {
		String signStr = userId + SPLITER + DateUtil.getCurrentyyyyMMddHHmmss()
				+ SPLITER + url;
		String result = "";
		try {
			result = Encrypter3DES.encode(signStr, SECRETKEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String encode3DES(String str) {
		try {
			return Encrypter3DES.encode(str, SECRETKEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String decode3DES(String str) {
		try {
			return Encrypter3DES.decode(str, SECRETKEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}

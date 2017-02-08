package com.zxcloud.tel.common;

import webwalker.framework.utils.CacheUtil;

/**
 * @author xu.jian
 * 
 */
public class CacheMgr {
	public final static String CALL_LOGS = "CALL_LOGS";
	public final static String CONTRACT_PERSON = "CONTRACT_PERSON";
	public final static String COMPANY_BOOK = "COMPANY_BOOK";
	public final static String RECENT_CALLS = "RECENT_CALLS";
	public final static String ABOUT_ACT = "ABOUT_ACT";
	public final static String COMPANY_BOOK_LOCAL = "COMPANY_BOOK_LOCAL";
	public final static String USER_INFO = "USER_INFO";

	public static Object get(String key) {
		return CacheUtil.get(key);
	}

	public static void set(String key, Object data) {
		CacheUtil.set(key, data);
	}

	public static void clear() {
		CacheUtil.remove(CALL_LOGS);
		CacheUtil.remove(CONTRACT_PERSON);
		CacheUtil.remove(RECENT_CALLS);
		CacheUtil.remove(ABOUT_ACT);
		// CacheUtil.remove(COMPANY_BOOK);
	}
}

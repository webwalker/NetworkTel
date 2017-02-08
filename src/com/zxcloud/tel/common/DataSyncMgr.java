package com.zxcloud.tel.common;

import java.util.List;

import webwalker.framework.beans.BaseContract;
import webwalker.framework.beans.CallLogBean;
import webwalker.framework.beans.ContractPersion;
import webwalker.framework.beans.PinYinItem;
import webwalker.framework.cache.ACache;
import webwalker.framework.interfaces.ICallback;
import webwalker.framework.system.contact.ContactWrapper;
import webwalker.framework.system.contact.DialRecordWrapper;
import webwalker.framework.utils.JsonUtil;
import webwalker.framework.utils.Loggers;
import webwalker.framework.utils.PinYin4JUtil;
import webwalker.framework.utils.ValidatorUtil;
import android.content.Context;
import android.text.TextUtils;

import com.zxcloud.tel.http.AdBookHandler;
import com.zxcloud.tel.http.AppHandler;
import com.zxcloud.tel.jsondata.AdBookInfo;
import com.zxcloud.tel.jsondata.UserInfo;
import com.zxcloud.tel.model.SearchPerson;

/**
 * 数据同步管理
 * 
 * @author xu.jian
 * 
 */
public class DataSyncMgr {
	private Context context;
	private static DataSyncMgr _instance = null;

	// forbid
	private DataSyncMgr() {
	}

	public synchronized static DataSyncMgr getInstance() {
		if (_instance == null) {
			_instance = new DataSyncMgr();
		}
		return _instance;
	}

	public void setContext(Context c) {
		this.context = c;
	}

	/**
	 * 启动异步查询
	 */
	public void syncData() {
		// get call logs
		queryDialRecord();
		// get contract persons
		queryContracts();
	}

	/**
	 * 数据初始化
	 * 
	 * @return
	 */
	public boolean getStatus() {
		try {
			Loggers.d("waiting query over.");
			// check dial and contract person
			List<CallLogBean> logs = getRecords();
			List<ContractPersion> persons = getPersons();
			List<AdBookInfo> coms = getCompanyList();
			if (logs == null || logs.size() == 0) {
				Loggers.d(AppContext.TAG, "preparing dial records list.");
				// return false;
			}
			if (persons == null || persons.size() == 0) {
				Loggers.d(AppContext.TAG, "preparing contract person list.");
				// return false;
			}
			if (coms == null || coms.size() == 0) {
				Loggers.d(AppContext.TAG, "preparing company list.");
				// return false;
			}
			// 构造搜索链表
			buildCallLogs();
			buildContractPersons();
			queryCompanyList();
			// 计算全拼和简写
			PinYin4JUtil.getPinYinAndFirst(AppContext.SEARCH_ITEM, true);
			Loggers.d(AppContext.TAG, "total record count:"
					+ AppContext.SEARCH_ITEM.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public void getSipParams() {
		Loggers.d(AppContext.TAG, "getSipParams.");
		if (!TextUtils.isEmpty(SipContext.getIp())) {
			Loggers.d(AppContext.TAG, "getSipParams from cache.");
			return;
		}
		if (AppContext.USERID.equals("-1"))
			return;
		Loggers.d(AppContext.TAG, "getSipParams..");
		AppHandler appHandler = new AppHandler(context);
		appHandler.getSipParams();
	}

	/**
	 * 拼音转化是否结束
	 * 
	 * @return
	 */
	public boolean isPinYinConverted() {
		return PinYin4JUtil.pool.isTerminated();
	}

	/**
	 * 是否是公司人员
	 * 
	 * @param phone
	 * @return
	 */
	public boolean isCompanyPerson(String phone) {
		List<AdBookInfo> list = getCompanyList();
		String userNumber = "";
		for (AdBookInfo user : list) {
			List<UserInfo> users = user.getMembers();
			for (UserInfo u : users) {
				userNumber = u.getMobileNo();
				if (!TextUtils.isEmpty(userNumber)
						&& phone.contains(userNumber))
					return true;
			}
		}
		return false;
	}

	/**
	 * 是否是公司人员
	 * 
	 * @param phone
	 * @return
	 */
	public UserInfo getCompanyPerson(String phone) {
		List<AdBookInfo> list = getCompanyList();
		if (list == null)
			return null;
		String userNumber = "";
		for (AdBookInfo user : list) {
			List<UserInfo> users = user.getMembers();
			for (UserInfo u : users) {
				userNumber = u.getMobileNo();
				if (!TextUtils.isEmpty(userNumber)
						&& phone.contains(userNumber))
					return u;
			}
		}
		return null;
	}

	/**
	 * 获取通话记录
	 * 
	 * @return
	 */
	public List<CallLogBean> getRecords() {
		Object data = CacheMgr.get(CacheMgr.CALL_LOGS);
		if (data != null)
			return (List<CallLogBean>) data;
		// refresh
		List<CallLogBean> logs = DialRecordWrapper.getInstance().getLogs();
		if (logs != null && logs.size() > 0) {
			CacheMgr.set(CacheMgr.CALL_LOGS, logs);
		}
		return logs;
	}

	/**
	 * 获取联系人
	 * 
	 * @return
	 */
	public List<ContractPersion> getPersons() {
		Object data = CacheMgr.get(CacheMgr.CONTRACT_PERSON);
		if (data != null)
			return (List<ContractPersion>) data;
		// refresh
		List<ContractPersion> persons = ContactWrapper.getInstance()
				.getPersons();
		if (persons != null && persons.size() > 0) {
			CacheMgr.set(CacheMgr.CONTRACT_PERSON, persons);
		}
		return persons;
	}

	/**
	 * 获取企业通信录
	 * 
	 * @return
	 */
	public List<AdBookInfo> getCompanyList() {
		Object data = CacheMgr.get(CacheMgr.COMPANY_BOOK);
		if (data != null)
			return (List<AdBookInfo>) data;
		return null;
	}

	public void queryDialRecord() {
		DialRecordWrapper.getInstance().queryDialRecord(context);
	}

	public void queryContracts() {
		ContactWrapper.getInstance().queryContracts(context);
	}

	/**
	 * 企业通信录
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void queryCompanyList() {
		if (AppContext.USERID.equals("-1"))
			return;
		ICallback<List<AdBookInfo>> callback = new ICallback<List<AdBookInfo>>() {
			@Override
			public void action(List<AdBookInfo> data) {
				CacheMgr.set(CacheMgr.COMPANY_BOOK, data);
				buildCompanyList();

				// 保存到本地缓存
				ACache cache = ACache.get(context);
				String json = JsonUtil.gson.toJson(data);
				cache.put(CacheMgr.COMPANY_BOOK_LOCAL, json);
			}
		};
		AdBookHandler ad = new AdBookHandler(context);
		ad.getAdBookList(callback);
	}

	@SuppressWarnings("unchecked")
	private void buildCallLogs() {
		List<CallLogBean> logs = getRecords();
		if (logs == null) {
			Loggers.d(AppContext.TAG, "dial logs is empty.");
			return;
		}
		for (CallLogBean log : logs) {
			AppContext.searchAdd(searchItem(log));
		}
	}

	@SuppressWarnings("unchecked")
	private void buildContractPersons() {
		List<ContractPersion> persons = getPersons();
		if (persons == null) {
			Loggers.d(AppContext.TAG, "contracts list is empty.");
			return;
		}
		for (ContractPersion p : persons) {
			AppContext.searchAdd(searchItem(p));

			// 如果通过系统取得的sort_key没有自动转换为拼音，则手动转换
			if (!ValidatorUtil.isLetter(p.getSortKey())) {
				PinYinItem item = PinYin4JUtil.getSinglePinYinAndFirst(p
						.getName());
				p.setSortKey(item.getFirstLetter());
			}
		}
	}

	public void refreshWhenCompanyIsNull() {
		List<AdBookInfo> list = getCompanyList();
		if (list == null) {
			queryCompanyList();
		}
	}

	@SuppressWarnings("unchecked")
	private void buildCompanyList() {
		try {
			List<AdBookInfo> list = getCompanyList();
			if (list == null) {
				Loggers.d(AppContext.TAG, "company list is empty.");
				return;
			}
			for (AdBookInfo bi : list) {
				for (UserInfo u : bi.getMembers()) {
					BaseContract bc = new BaseContract();
					bc.setName(u.getUserName());
					bc.setPhone(u.getMobileNo());
					bc.setIcon(u.getPicUrl());
					// build search list
					SearchPerson s = new SearchPerson();
					s.setChineseFont(u.getUserName());
					s.setTag(u.getMobileNo());
					s.setPerson(bc);
					s.setExtansionNo(u.getExtensionNo());

					PinYin4JUtil.getSinglePinYinAndFirst(s);
					AppContext.searchAdd(s);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	SearchPerson searchItem(BaseContract p) {
		SearchPerson s = new SearchPerson();
		s.setChineseFont(p.getName());
		s.setTag(p.getPhone());
		s.setPerson(p);
		return s;
	}
}

package com.zxcloud.tel.fragment;

import java.util.List;

import webwalker.framework.cache.ACache;
import webwalker.framework.uiquery.AQuery;
import webwalker.framework.utils.JsonUtil;
import webwalker.framework.utils.NetworkUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.zxcloud.tel.R;
import com.zxcloud.tel.adapter.CompanyContactListAdapter;
import com.zxcloud.tel.common.AppContext;
import com.zxcloud.tel.common.CacheMgr;
import com.zxcloud.tel.common.DataSyncMgr;
import com.zxcloud.tel.jsondata.AdBookInfo;

/**
 * @author xu.jian
 * 
 */
public class FragmentContactCompany extends BaseFragment {
	private View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (v == null) {
			v = inflater.inflate(R.layout.fragment_contact_company, container,
					false);
			aq = new AQuery(v);
			init();
		}
		ViewGroup parent = (ViewGroup) v.getParent();
		if (parent != null) {
			parent.removeView(v);
		}
		return v;
	}

	@Override
	public void init() {
		if (!AppContext.hasLogin()
				|| !NetworkUtils.isNetworkAvailable(getActivity())) {
			loadCacheData();
			return;
		}
		setAdapter();
	}

	// 获取本地缓存数据
	private void loadCacheData() {
		ACache mCache = ACache.get(getActivity());
		String json = mCache.getAsString(CacheMgr.COMPANY_BOOK_LOCAL);
		List<AdBookInfo> list = JsonUtil.gson.fromJson(json,
				new TypeToken<List<AdBookInfo>>() {
				}.getType());
		if (list != null)
			aq.id(R.id.elv_company).adapter(
					new CompanyContactListAdapter(getActivity(), list));
	}

	private void setAdapter() {
		try {
			DataSyncMgr sync = DataSyncMgr.getInstance();
			List<AdBookInfo> list = sync.getCompanyList();
			if (list == null) {
				// MessageUtil.showShortToast(getActivity(),
				// "同步企业通信录数据错误，您可以退出重新尝试!");
				sync.refreshWhenCompanyIsNull();
				return;
			}
			if (list != null)
				aq.id(R.id.elv_company).adapter(
						new CompanyContactListAdapter(getActivity(), list));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

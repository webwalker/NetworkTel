package com.zxcloud.tel.fragment;

import java.util.List;

import webwalker.framework.beans.ContractPersion;
import webwalker.framework.widget.LetterIndexBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zxcloud.tel.R;
import com.zxcloud.tel.adapter.ContactListAdapter;
import com.zxcloud.tel.common.DataSyncMgr;

/**
 * 手机通信录
 * 
 * @author xu.jian
 * 
 */
public class FragmentContactPhone extends BaseFragment {
	private ContactListAdapter adapter;
	private ListView contactList;
	private View v;
	private LetterIndexBar alphabeticBar; // 快速索引条

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (v == null) {
			v = inflater.inflate(R.layout.fragment_contact_phone, container,
					false);
			contactList = (ListView) v.findViewById(R.id.contact_list);
			alphabeticBar = (LetterIndexBar) v
					.findViewById(R.id.fast_scroller);
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
		setAdapter();
	}

	private void setAdapter() {
		List<ContractPersion> list = DataSyncMgr.getInstance().getPersons();

		adapter = new ContactListAdapter(getActivity(), list, alphabeticBar);
		contactList.setAdapter(adapter);
		alphabeticBar.init(v, R.id.fast_position);
		alphabeticBar.setListView(contactList);
		alphabeticBar.setHight(alphabeticBar.getHeight());
		alphabeticBar.setVisibility(View.VISIBLE);
	}
}

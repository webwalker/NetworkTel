package com.zxcloud.tel.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zxcloud.tel.R;
import com.zxcloud.tel.adapter.RecentCallsAdapter;
import com.zxcloud.tel.controller.DataController;
import com.zxcloud.tel.jsondata.RecentCallInfo;

/**
 * @author xu.jian
 * 
 */
public class FragmentContactRecent extends BaseFragment {
	private ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_contact_recent, container,
				false);
		listView = (ListView) v.findViewById(R.id.recent_list);

		return v;
	}

	@Override
	public void init() {
		DataController controller = new DataController(getActivity());
		List<RecentCallInfo> list = controller.getCachedRecentCalls();

		RecentCallsAdapter adapter = new RecentCallsAdapter(getActivity(), list);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View arg0) {
	}

	@Override
	public void onResume() {
		super.onResume();
		this.init();
	}
}

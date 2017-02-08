package com.zxcloud.tel.fragment;

import webwalker.framework.uiquery.AQuery;
import webwalker.framework.utils.UriUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxcloud.tel.R;
import com.zxcloud.tel.activity.MeetingAppointActivity;
import com.zxcloud.tel.activity.MeetingCurrentActivity;
import com.zxcloud.tel.activity.MeetingHistoryActivity;
import com.zxcloud.tel.activity.MeetingNotifyActivity;
import com.zxcloud.tel.activity.MeetingSendActivity;

public class FragmentMeeting extends BaseFragment {
	private View saveView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (saveView == null) {
			saveView = inflater.inflate(R.layout.fragment_meeting, container,
					false);
			initTopBar(saveView, "会议");
			aq = new AQuery(saveView);
			aq.id(R.id.lSender).clicked(this);
			aq.id(R.id.lAppoint).clicked(this);
			aq.id(R.id.lNotify).clicked(this);
			aq.id(R.id.lCurrent).clicked(this);
			aq.id(R.id.lHistory).clicked(this);
		}
		ViewGroup parent = (ViewGroup) saveView.getParent();
		if (parent != null) {
			parent.removeView(saveView);
		}
		return saveView;
	}

	@Override
	public void init() {
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		switch (id) {
		case R.id.lSender:
			UriUtil.startActivity(getActivity(), MeetingSendActivity.class);
			break;
		case R.id.lAppoint:
			UriUtil.startActivity(getActivity(), MeetingAppointActivity.class);
			break;
		case R.id.lNotify:
			UriUtil.startActivity(getActivity(), MeetingNotifyActivity.class);
			break;
		case R.id.lCurrent:
			UriUtil.startActivity(getActivity(), MeetingCurrentActivity.class);
			break;
		case R.id.lHistory:
			UriUtil.startActivity(getActivity(), MeetingHistoryActivity.class);
			break;
		}
	}

}

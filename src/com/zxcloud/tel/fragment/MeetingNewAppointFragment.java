package com.zxcloud.tel.fragment;

import webwalker.framework.widget.MyDateTimePicker;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxcloud.tel.R;

public class MeetingNewAppointFragment extends BaseFragment {
	MyDateTimePicker dtPicker = new MyDateTimePicker(getActivity());

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_meeting, container, false);

		return v;
	}

	@Override
	public void init() {
	}

	@Override
	public void onClick(View arg0) {
	}

}

package com.zxcloud.tel.activity;

import android.os.Bundle;

import com.zxcloud.tel.R;
import com.zxcloud.tel.BaseActivity;

public class MeetingNotifyActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_notify);
		
		initTopBar(this, "会议通知");
	}

}

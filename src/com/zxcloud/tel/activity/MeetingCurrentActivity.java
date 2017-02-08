package com.zxcloud.tel.activity;

import android.os.Bundle;

import com.zxcloud.tel.R;
import com.zxcloud.tel.BaseActivity;

public class MeetingCurrentActivity extends BaseActivity {

	// 根据会议主持人是否为自己，确定加载不同的layout
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_current);

		initTopBar(this, "正在进行的会议");

		// 判断会议是否为自己创建，显示不同的界面
	}
}

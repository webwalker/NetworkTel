package com.zxcloud.tel.activity;

import android.os.Bundle;

import com.zxcloud.tel.R;
import com.zxcloud.tel.BaseActivity;

/**
 * 从通信录中发起会议
 * 
 * @author xu.jian
 * 
 */
public class MeetingSendActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_send);

		initTopBar(this, "发起会议");
	}

}

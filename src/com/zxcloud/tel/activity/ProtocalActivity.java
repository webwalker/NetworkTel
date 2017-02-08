package com.zxcloud.tel.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.zxcloud.tel.R;
import com.zxcloud.tel.BaseActivity;

public class ProtocalActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_protocal);

		initTopBar(this, "阅读用户协议");

		aq.id(R.id.btnAgree).clicked(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}

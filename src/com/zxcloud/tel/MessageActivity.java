package com.zxcloud.tel;

import com.zxcloud.tel.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MessageActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

		Intent it = getIntent();
		String msg = it.getStringExtra("msg");
		if (msg.equals("")) {
			finish();
			return;
		}

		aq.id(R.id.txtMsg).text(msg);
		aq.id(R.id.txtOK).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}

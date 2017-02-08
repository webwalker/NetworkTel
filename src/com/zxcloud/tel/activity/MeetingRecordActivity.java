package com.zxcloud.tel.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.zxcloud.tel.R;
import com.zxcloud.tel.BaseActivity;

public class MeetingRecordActivity extends BaseActivity {
	private MediaPlayer player = new MediaPlayer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_record);
		initTopBar(this, "会议录音");

		Intent it = getIntent();
		final String url = it.getStringExtra("url");
		aq.id(R.id.imgRecord).clicked(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				play(url);
			}
		});
	}

	private void play(String url) {
		try {
			player.setDataSource(getBaseContext(), Uri.parse(url));
			player.prepare();
			player.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			player.stop();
			player.release();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

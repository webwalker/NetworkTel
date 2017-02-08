package com.zxcloud.tel.activity;

import java.util.HashMap;
import java.util.Map;

import webwalker.framework.utils.HttpUtil;
import webwalker.framework.utils.MessageUtil;
import webwalker.framework.utils.UriUtil;
import webwalker.framework.utils.UserIconUtil;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.zxcloud.tel.BaseActivity;
import com.zxcloud.tel.R;
import com.zxcloud.tel.common.AppContext;
import com.zxcloud.tel.ui.TopNavBar;

public class UploadUserPicActivity extends BaseActivity {

	private UserIconUtil icon = null;
	private ImageView iv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_user_pic);

		iv = (ImageView) findViewById(R.id.ivUserIcon);
		icon = new UserIconUtil(UploadUserPicActivity.this);
		icon.setSaveFile(true);

		int type = UriUtil.getIntValue(UploadUserPicActivity.this, "type");
		switch (type) {
		case 1:
			icon.getFromCamera();
			break;
		case 2:
			icon.getFromImage();
			break;
		}

		TopNavBar bar = initTopBar(this, "选择头像");
		bar.setTv2("使用");
		bar.setTv2Listener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("userId", AppContext.USERID);
				String imgUrl = HttpUtil.uploadFile(AppContext.BASE_URL
						+ "/user/upload", "file", maps, icon.getTempFile());
				if (TextUtils.isEmpty(imgUrl)) {
					MessageUtil.showShortToast(UploadUserPicActivity.this,
							"头像更新失败了");
					finish();
					return;
				}
				AppContext.LOGIN_USER.setPicUrl(imgUrl);
				MessageUtil
						.showShortToast(UploadUserPicActivity.this, "头像更新成功");
				finish();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		icon.onActivityResult(iv, requestCode, resultCode, data);
	}
}

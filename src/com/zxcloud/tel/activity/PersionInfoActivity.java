package com.zxcloud.tel.activity;

import webwalker.framework.interfaces.ICallback1;
import webwalker.framework.utils.MessageUtil;
import webwalker.framework.utils.NetworkUtils;
import webwalker.framework.utils.UIUtils;
import webwalker.framework.utils.ValidatorUtil;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zxcloud.tel.BaseActivity;
import com.zxcloud.tel.R;
import com.zxcloud.tel.common.AppContext;
import com.zxcloud.tel.controller.ImageController;
import com.zxcloud.tel.http.UserHandler;
import com.zxcloud.tel.jsondata.UserInfo;

public class PersionInfoActivity extends BaseActivity {
	private View parentView = null;
	private PopupWindow popupWindow = null;
	private UserInfo user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parentView = UIUtils.getView(this, R.layout.activity_persion_info);
		setContentView(parentView);

		user = (UserInfo) getIntent().getSerializableExtra("user");
		if (user == null || !NetworkUtils.isNetworkAvailable(this)) {
			finish();
			AppContext.needUpdateUserInfo = true;
			return;
		}
		aq.id(R.id.lEditUser).invisible();
		aq.id(R.id.tvName).text(user.getUserName());
		aq.id(R.id.tvComNumber).text(user.getCompanyId());
		aq.id(R.id.tvExtNumber).text(user.getLoginName());
		aq.id(R.id.lUserIcon).clicked(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				aq.id(R.id.popMask).visible();
				if (null == popupWindow) {
					View v = UIUtils.getView(PersionInfoActivity.this,
							R.layout.pop_camera);
					LinearLayout lCamera = (LinearLayout) v
							.findViewById(R.id.lCamera);
					LinearLayout lGallery = (LinearLayout) v
							.findViewById(R.id.lGallery);
					LinearLayout lCancel = (LinearLayout) v
							.findViewById(R.id.lCancel);
					lCamera.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							Intent it = new Intent(PersionInfoActivity.this,
									UploadUserPicActivity.class);
							it.putExtra("type", 1);
							startActivity(it);
							popupWindow.dismiss();
							aq.id(R.id.popMask).gone();
						}
					});
					lGallery.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							Intent it = new Intent(PersionInfoActivity.this,
									UploadUserPicActivity.class);
							it.putExtra("type", 2);
							startActivity(it);
							popupWindow.dismiss();
							aq.id(R.id.popMask).gone();
						}
					});
					lCancel.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							popupWindow.dismiss();
							aq.id(R.id.popMask).gone();
						}
					});
					popupWindow = new PopupWindow(v, LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT, true);
					// popupWindow.setBackgroundDrawable(getResources().getDrawable(
					// R.drawable.btn_style_alert_dialog_background));
					popupWindow.setAnimationStyle(R.style.PopupAnimation);
				}
				popupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				popupWindow.update();
			}
		});
		aq.id(R.id.lUserName).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				aq.id(R.id.lEditUser).visible();
			}
		});
		aq.id(R.id.btnSubmit).clicked(AppContext.clickTimes,
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						String userName = aq.id(R.id.etName).getText()
								.toString();
						String email = aq.id(R.id.etEmail).getText().toString();
						UserInfo user = new UserInfo();
						user.setUserName(userName);
						// 手机号必须验证短信码才可以修改，这里不支持
						// user.setMobileNo("13818668209");
						user.setEmail(email);

						if (TextUtils.isEmpty(userName)) {
							showActMessage("对不起，您的姓名输入错误。");
							return;
						}
						if (!ValidatorUtil.isEmail(email)) {
							showActMessage("对不起，您的邮箱输入错误。");
							return;
						}
						ICallback1 callback = new ICallback1() {
							@Override
							public void action() {
								AppContext.needUpdateUserInfo = true;
								MessageUtil.showShortToast(context, "更新成功");
								finish();
							}
						};
						UserHandler handler = new UserHandler(
								PersionInfoActivity.this);
						handler.updateUserInfo(callback, user);
						aq.id(R.id.lEditUser).invisible();
					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateUserIcon();
	}

	private void updateUserIcon() {
		if (TextUtils.isEmpty(user.getPicUrl()))
			return;
		ImageController.loadImage(user.getPicUrl(), aq.id(R.id.user_icon)
				.getImageView());
		AppContext.needUpdateUserInfo = true;
	}
}

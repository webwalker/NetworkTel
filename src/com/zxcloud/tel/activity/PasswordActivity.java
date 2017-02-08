package com.zxcloud.tel.activity;

import webwalker.framework.interfaces.IAction;
import webwalker.framework.interfaces.ICallback1;
import webwalker.framework.utils.MessageUtil;
import webwalker.framework.utils.SharedUtil;
import webwalker.framework.utils.StringUtil;
import webwalker.framework.utils.UriUtil;
import webwalker.framework.widget.ButtonSender;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.zxcloud.tel.BaseActivity;
import com.zxcloud.tel.R;
import com.zxcloud.tel.common.AppContext;
import com.zxcloud.tel.http.AppHandler;
import com.zxcloud.tel.http.AppHandler.PasswordType;
import com.zxcloud.tel.ui.TopNavBar;

public class PasswordActivity extends BaseActivity {

	LinearLayout lPassInput, lNewPass;
	ButtonSender<Object> sender = new ButtonSender("获取验证码", "{0}秒后重新发送",
			R.drawable.corner_green_button, R.drawable.corner_blue_button_press);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password);
		lPassInput = aq.id(R.id.lPassInput).getLinearLayout();
		lNewPass = aq.id(R.id.lNewPass).getLinearLayout();

		sender.setTimeCounter(60);

		TopNavBar topBar = initTopBar("找回密码");
		topBar.setIv1Listener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (lPassInput.getVisibility() != View.VISIBLE) {
					lPassInput.setVisibility(View.VISIBLE);
					lNewPass.setVisibility(View.GONE);
					return;
				}
				finish();
			}
		});

		aq.id(R.id.btnCode).clicked(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				final String phone = getPhone();
				if (!StringUtil.isMobile(phone)) {
					showActMessage("手机号码输入不正确。");
					return;
				}
				// 发送验证码
				sender.sendSmsCode(aq.id(R.id.btnCode).getButton(),
						aq.id(R.id.etCode).getEditText(), aq.id(R.id.etPhone)
								.getText().toString(),
						new IAction<Object, String>() {
							@Override
							public Void action(String data) {
								AppHandler handler = new AppHandler(
										PasswordActivity.this);
								handler.sendSmsCode(phone);
								return null;
							}
						}, null);
				aq.id(R.id.btnCode).text("30秒后重新发送");
				aq.id(R.id.btnCode).enabled(false);
			}
		});
		aq.id(R.id.btnNext).clicked(AppContext.clickTimes,
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						sender.setRunning(false);
						String code = aq.id(R.id.etCode).getText().toString();
						if (!StringUtil.isMobile(getPhone())) {
							showActMessage("手机号码输入不正确。");
							return;
						}
						if (code.length() != 6) {
							showActMessage("验证码输入不正确。");
							return;
						}

						ICallback1 callback = new ICallback1() {
							@Override
							public void action() {
								aq.id(R.id.lPassInput).gone();
								aq.id(R.id.lNewPass).visible();
							}
						};
						AppHandler handler = new AppHandler(
								PasswordActivity.this);
						handler.validateSmsCode(callback, getPhone(), code);
					}
				});
		aq.id(R.id.btnSubmit).clicked(AppContext.clickTimes,
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						sender.setRunning(false);
						String p1 = aq.id(R.id.etNewPass).getText().toString();
						String p2 = aq.id(R.id.etConfirmPass).getText()
								.toString();
						if (p1.length() < 6) {
							showActMessage("密码输入不正确(6-12位字母、数字组合)。");
							return;
						}
						if (!p2.equals(p1)) {
							showActMessage("两次输入的密码不匹配。");
							return;
						}

						ICallback1 callback = new ICallback1() {
							@Override
							public void action() {
								AppContext
										.clearLoginCache(PasswordActivity.this);
								MessageUtil.showShortToast(context, "密码更新成功!");
								UriUtil.startActivity(PasswordActivity.this,
										LoginActivity.class);
								finish();
							}
						};
						AppHandler handler = new AppHandler(
								PasswordActivity.this);
						handler.setPassword(callback, "", getPhone(), "", p1,
								PasswordType.RESET);
					}
				});
	}

	// 获取手机号码
	private String getPhone() {
		String phone = aq.id(R.id.etPhone).getText().toString();
		return phone;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sender.setRunning(false);
	}

}

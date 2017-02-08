package com.zxcloud.tel.activity;

import webwalker.framework.interfaces.ICallback;
import webwalker.framework.interfaces.ICallback1;
import webwalker.framework.utils.MessageUtil;
import webwalker.framework.utils.UriUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.zxcloud.tel.BaseActivity;
import com.zxcloud.tel.R;
import com.zxcloud.tel.common.AppContext;
import com.zxcloud.tel.http.AppHandler;
import com.zxcloud.tel.http.AppHandler.PasswordType;
import com.zxcloud.tel.http.UserHandler;
import com.zxcloud.tel.model.UserEntry.UserResp;
import com.zxcloud.tel.ui.TopNavBar;

public class RegistActivity extends BaseActivity {
	LinearLayout lRegInput, lNewPass;
	String companyId, extentionNo, secretCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);

		TopNavBar topBar = initTopBar("注册新用户");
		topBar.setIv1Listener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (lRegInput.getVisibility() != View.VISIBLE) {
					lRegInput.setVisibility(View.VISIBLE);
					lNewPass.setVisibility(View.GONE);
					return;
				}
				finish();
			}
		});

		lRegInput = aq.id(R.id.lRegInput).getLinearLayout();
		lNewPass = aq.id(R.id.lRegPass).getLinearLayout();
		aq.id(R.id.txtProtocal).clicked(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(RegistActivity.this,
						ProtocalActivity.class));
			}
		});
		aq.id(R.id.btnNext).clicked(AppContext.clickTimes,
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						clickNext();
					}
				});
		aq.id(R.id.btnSubmit).clicked(AppContext.clickTimes,
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						clickSubmit();
					}
				});
	}

	void clickNext() {
		companyId = aq.id(R.id.etComCode).getText().toString();
		extentionNo = aq.id(R.id.etPhone).getText().toString();
		secretCode = aq.id(R.id.etCode).getText().toString();
		if (companyId.length() < 8) {
			showActMessage("您输入的企业号不正确(8位数字)。");
			return;
		}
		if (extentionNo.length() < 8) {
			showActMessage("您输入的分机号不正确(8位数字)。");
			return;
		}
		if (secretCode.length() < 4) {
			showActMessage("您输入的验证码不正确(4位数字)。");
			return;
		}

		ICallback<UserResp> callback = new ICallback<UserResp>() {
			@Override
			public void action(UserResp resp) {
				if (resp.getIsSetPwd() == 1) {
					MessageUtil.showShortToast(getBaseContext(),
							"分机号已注册，您可以直接登录啦！");
					finish();
					return;
				}
				lRegInput.setVisibility(View.GONE);
				lNewPass.setVisibility(View.VISIBLE);
				return;
			}
		};
		UserHandler user = new UserHandler(this);
		user.registUser(callback, companyId, extentionNo, secretCode);
	}

	void clickSubmit() {
		if (!aq.id(R.id.cbProtocal).isChecked()) {
			showActMessage("请先同意用户协议。");
			return;
		}
		String p1 = aq.id(R.id.etPass).getText().toString();
		String p2 = aq.id(R.id.etConfirmPass).getText().toString();
		if (p1.length() < 6) {
			showActMessage("密码设置不合规范：\n6-20位之间的数字、字母和组合)。");
			return;
		}
		if (!p2.equals(p1)) {
			showActMessage("两次输入的密码不匹配。");
			return;
		}

		ICallback1 callback = new ICallback1() {
			@Override
			public void action() {
				MessageUtil.showShortToast(context, "注册成功!");
				UriUtil.startActivity(RegistActivity.this, LoginActivity.class);
				finish();
			}
		};

		AppHandler handler = new AppHandler(this);
		handler.setPassword(callback, extentionNo, "", "", p1,
				PasswordType.REGIST);
	}
}

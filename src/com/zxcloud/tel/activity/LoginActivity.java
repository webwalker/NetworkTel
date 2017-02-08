package com.zxcloud.tel.activity;

import webwalker.framework.interfaces.ICallback;
import webwalker.framework.utils.DateUtil;
import webwalker.framework.utils.Encrypter;
import webwalker.framework.utils.SharedUtil;
import webwalker.framework.utils.UIUtils;
import webwalker.framework.utils.UriUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.zxcloud.tel.BaseActivity;
import com.zxcloud.tel.R;
import com.zxcloud.tel.common.AppContext;
import com.zxcloud.tel.common.SecurityMgr;
import com.zxcloud.tel.http.UserHandler;
import com.zxcloud.tel.jsondata.UserInfo;

public class LoginActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// check login
		if (AppContext.hasLogin()) {
			UriUtil.startActivity(this, MainActivity.class);
			finish();
			return;
		}

		aq.id(R.id.btnLogin).clicked(this);
		aq.id(R.id.lReg).clicked(this);
		aq.id(R.id.txtPass).clicked(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btnLogin:
			if (UIUtils.isFastDoubleClick())
				return;
			clickLogin();
			break;
		case R.id.txtPass:
			UriUtil.startActivityNoHistory(this, PasswordActivity.class);
			break;
		case R.id.lReg:
			UriUtil.startActivityNoHistory(this, RegistActivity.class);
			break;
		}
	}

	void clickLogin() {
		// 您输入的用户名或密码有误，请重新输入。
		final String extNo = aq.id(R.id.etPhone).getText().toString();
		String pass = aq.id(R.id.etPass).getText().toString();
		if (extNo.length() < 8) {
			showActMessage("分机号输入不正确(8位数字)。");
			return;
		}
		if (pass.length() < 6) {
			showActMessage("密码输入不正确(6-12位字母、数字组合)。");
			return;
		}

		ICallback<UserInfo> callback = new ICallback<UserInfo>() {
			@Override
			public void action(UserInfo user) {
				// auto login
				String userValue = SecurityMgr.encode3DES(extNo);
				String passValue = SecurityMgr.encode3DES(user.getTempPass());
				String date = SecurityMgr.encode3DES(DateUtil
						.getCurrentyyyy_MM_dd_HHmmss());
				SharedUtil.getInstance(context).set("uid", userValue);
				SharedUtil.getInstance(context).set("pass", passValue);
				SharedUtil.getInstance(context).set("logindate", date);
			}
		};

		pass = Encrypter.encryptMD5(pass);
		UserHandler user = new UserHandler(this);
		user.login(callback, extNo, pass);

		// 无法登录时也可以使用部分功能
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				UriUtil.startActivity(LoginActivity.this, MainActivity.class);
				finish();
			}
		}, 500);
	}
}

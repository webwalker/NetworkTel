package com.zxcloud.tel;

import webwalker.framework.interfaces.ICallback;
import webwalker.framework.utils.AppUtil;
import webwalker.framework.utils.DateUtil;
import webwalker.framework.utils.Loggers;
import webwalker.framework.utils.SQLiteUtil;
import webwalker.framework.utils.SharedUtil;
import webwalker.framework.utils.UriUtil;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.umeng.message.PushAgent;
import com.zxcloud.tel.common.AppContext;
import com.zxcloud.tel.common.DataSyncMgr;
import com.zxcloud.tel.common.SecurityMgr;
import com.zxcloud.tel.http.UserHandler;
import com.zxcloud.tel.jsondata.UserInfo;

public class SplashActivity extends Activity {
	final static int delayTime = 500;
	private Context context;
	DataSyncMgr sync = DataSyncMgr.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		Loggers.d(AppContext.TAG, "SplashActivity onCreate.");

		context = getApplicationContext();

		new Thread(new prepare()).start();
		sync.setContext(this);
		sync.syncData();
	}

	public class prepare implements Runnable {
		@Override
		public void run() {
			// 启用调试日志
			// LogcatUtil.start(SplashActivity.this);
			// 检测自动登录
			checkAutoLogin();
			// 检查数据是否已复制
			checkLocalDb();

			Loggers.d(AppContext.TAG, "YouMent init..");
			// 友盟消息推送，不可放Application中
			PushAgent mPushAgent = PushAgent.getInstance(context);
			mPushAgent.enable();
			// 友盟设备ID
			// String device_token = UmengRegistrar
			// .getRegistrationId(context);
			// Loggers.d(AppContext.TAG, "YouMent device token:" +
			// device_token);
			// 统计应用启动数据
			// PushAgent.getInstance(context).onAppStart();
		}
	};

	void checkLocalDb() {
		String packageName = AppUtil.getPackageName(context);
		String DB_PATH = "/data/data/" + packageName + "/databases/";
		boolean isValid = SQLiteUtil.prepareDatabase(context, DB_PATH,
				"locations.db", "select 1 from t_mobile_area");
		Loggers.d(AppContext.TAG, "check locations db ret:" + isValid);

		isValid = SQLiteUtil.prepareDatabase(context, DB_PATH, "inc.db",
				"select 1 from t_recent_calls");
		Loggers.d(AppContext.TAG, "check app db ret:" + isValid);
	}

	void checkAutoLogin() {
		try {
			String date = SharedUtil.getInstance(this).getString("logindate");
			if (TextUtils.isEmpty(date)) {
				handler.sendEmptyMessageDelayed(0, delayTime);
				return;
			}
			date = SecurityMgr.decode3DES(date);
			if (DateUtil.getDaysFromNow(date, DateUtil.defaultTimeFormat) > 7) {
				handler.sendEmptyMessageDelayed(0, delayTime);
				return;
			}
			String user = SharedUtil.getInstance(this).getString("uid");
			if (TextUtils.isEmpty(user)) {
				handler.sendEmptyMessageDelayed(0, delayTime);
				return;
			}

			ICallback<UserInfo> callback = new ICallback<UserInfo>() {
				@Override
				public void action(UserInfo data) {
				}
			};
			String pass = SharedUtil.getInstance(this).getString("pass");
			user = SecurityMgr.decode3DES(user);
			pass = SecurityMgr.decode3DES(pass);
			UserHandler userHandler = new UserHandler(this);
			userHandler.login(callback, user, pass);
			handler.sendEmptyMessageDelayed(0, delayTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (!sync.getStatus()) {
				handler.sendEmptyMessageDelayed(0, delayTime);
				return;
			}
			handler.removeMessages(0);

			// redirect
			// UriUtil.startActivityNoHistory(SplashActivity.this,
			// Sipdroid.class);
			UriUtil.startActivityNoHistory(SplashActivity.this,
					GuideActivity.class);// SipTestActivity
			finish();
		}
	};

}

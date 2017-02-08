package com.zxcloud.tel.activity;

import java.util.Timer;
import java.util.TimerTask;

import webwalker.framework.common.DoubleClickExitHelper;
import webwalker.framework.interfaces.ICallback;
import webwalker.framework.ui.MyFragmentTabHost;
import webwalker.framework.ui.MyFragmentTabHost.MyFragmentItem;
import webwalker.framework.uiquery.AQuery;
import webwalker.framework.utils.Loggers;
import webwalker.framework.utils.UriUtil;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;
import com.zxcloud.tel.R;
import com.zxcloud.tel.common.AppContext;
import com.zxcloud.tel.common.SipContext;
import com.zxcloud.tel.fragment.FragmentAdBook;
import com.zxcloud.tel.fragment.FragmentDial;
import com.zxcloud.tel.fragment.FragmentMe;
import com.zxcloud.tel.fragment.FragmentMeeting;

public class MainActivity extends FragmentActivity {
	private MyFragmentTabHost nsTabHost;
	private FragmentTabHost tabHost;
	private AQuery aq;
	private int tabIndex;// 默认显示的tab页
	private DoubleClickExitHelper exitHelper = null;
	public static MainActivity instance = null;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Loggers.d(AppContext.TAG, "MainActivity onCreate");

		init();

		tabIndex = UriUtil.getIntValue(this, "tab");
		if (tabIndex >= 0) {
			tabHost.setCurrentTab(tabIndex);
		}
	}

	@SuppressLint("ResourceAsColor")
	private void init() {
		exitHelper = new DoubleClickExitHelper(MainActivity.this,
				R.string.exit_msg);
		// exitHelper.iSystemExit(true);
		instance = this;
		aq = new AQuery(this);
		tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		nsTabHost = new MyFragmentTabHost(tabHost, this, R.id.tab_container);
		nsTabHost.setImgHeight(40);
		nsTabHost.setImgWidth(40);
		nsTabHost.setDownColor(R.color.common_blue);
		nsTabHost.setUpColor(R.color.black);
		final View i1 = nsTabHost.newTabView(R.layout.tab_item,
				R.drawable.nav_dial_gray, "拨号");
		View i2 = nsTabHost.newTabView(R.layout.tab_item,
				R.drawable.nav_txl_gray, "通讯录");
		View i3 = nsTabHost.newTabView(R.layout.tab_item,
				R.drawable.nav_meet_gray, "会议");
		View i4 = nsTabHost.newTabView(R.layout.tab_item,
				R.drawable.nav_me_gray, "我");
		nsTabHost.addTab(new MyFragmentItem(i1, "1", FragmentDial.class,
				R.drawable.nav_dial, R.drawable.nav_dial_gray));
		nsTabHost.addTab(new MyFragmentItem(i2, "2", FragmentAdBook.class,
				R.drawable.nav_txl, R.drawable.nav_txl_gray));
		nsTabHost.addTab(new MyFragmentItem(i3, "3", FragmentMeeting.class,
				R.drawable.nav_meet, R.drawable.nav_meet_gray));
		nsTabHost.addTab(new MyFragmentItem(i4, "4", FragmentMe.class,
				R.drawable.nav_me, R.drawable.nav_me_gray));
		tabHost.getTabWidget().getChildAt(0)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						View kb = findViewById(R.id.dial_keyboard);
						View dcb = findViewById(R.id.dial_callbar_bottom);
						EditText tvNumber = (EditText) findViewById(R.id.etDialNum);
						if (kb == null) {
							tabHost.setCurrentTab(0);
							return;
						}
						TextView tv = (TextView) arg0;
						if (kb.getVisibility() != View.VISIBLE) {
							kb.setVisibility(View.VISIBLE);
							nsTabHost.setBoundDrawable(tv,
									R.drawable.kb_collpse);
						} else {
							kb.setVisibility(View.INVISIBLE);
							nsTabHost.setBoundDrawable(tv, R.drawable.nav_dial);
						}
						if (!TextUtils.isEmpty(tvNumber.getText())) {
							dcb.setVisibility(View.VISIBLE);
						}
					}
				});

		// 避免登录返回没执行到SIP客户端服务开启
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				SipContext.setSip(MainActivity.this, true);
			}
		}, 3000);

		// 友盟自动更新
		AppContext.setUpdateListener(this, false);
		UmengUpdateAgent.update(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * 处理Fragment异常问题
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// No call for super(). Bug on API Level > 11.

		// outState.putString("WORKAROUND_FOR_BUG_19917_KEY",
		// "WORKAROUND_FOR_BUG_19917_VALUE");
		// super.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Loggers.d(AppContext.TAG, "MainActivity onDestroy");
		AppContext.recycle();
		SipContext.setSip(MainActivity.this, false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		ICallback<Void> callback = new ICallback<Void>() {
			@Override
			public void action(Void data) {
				// 是否关闭离线消息
				PushAgent mPushAgent = PushAgent
						.getInstance(getApplicationContext());
				mPushAgent.disable();
			}
		};
		if (exitHelper.onKeyDown(keyCode, event, callback))
			return true;
		return super.onKeyDown(keyCode, event);
	}

}

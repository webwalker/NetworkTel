package org.sipdroid.sipua.ui;

import java.util.HashMap;

import org.sipdroid.media.RtpStreamReceiver;
import org.sipdroid.sipua.UserAgent;
import org.sipdroid.sipua.phone.Call;

import webwalker.framework.uiquery.AQuery;
import webwalker.framework.utils.ImageUnity;
import webwalker.framework.utils.Loggers;
import webwalker.framework.utils.UIUtils;
import webwalker.framework.utils.UriUtil;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.zxcloud.tel.R;
import com.zxcloud.tel.activity.CallSelectorActivity;
import com.zxcloud.tel.common.AppContext;
import com.zxcloud.tel.common.DataSyncMgr;
import com.zxcloud.tel.jsondata.UserInfo;

public class InCallScreen extends CallScreen implements View.OnClickListener,
		SensorEventListener {
	private int type;// 1wifi，2免流量
	private UserInfo user;
	private AQuery aq;
	final int MSG_ANSWER = 1;
	final int MSG_ANSWER_SPEAKER = 2;
	final int MSG_BACK = 3;
	final int MSG_TICK = 4;
	final int MSG_POPUP = 5;
	final int SCREEN_OFF_TIMEOUT = 12000;
	public static Activity fromIntent = null;
	int oldtimeout;
	SensorManager sensorManager;
	Sensor proximitySensor;
	boolean first;
	Thread t;
	EditText etDialNum;
	boolean running;
	public static boolean started;
	private static final HashMap<Character, Integer> mToneMap = new HashMap<Character, Integer>();

	void screenOff(boolean off) {
		ContentResolver cr = getContentResolver();

		if (proximitySensor != null)
			return;
		if (off) {
			if (oldtimeout == 0) {
				oldtimeout = Settings.System.getInt(cr,
						Settings.System.SCREEN_OFF_TIMEOUT, 60000);
				Settings.System.putInt(cr, Settings.System.SCREEN_OFF_TIMEOUT,
						SCREEN_OFF_TIMEOUT);
			}
		} else {
			if (oldtimeout == 0
					&& Settings.System.getInt(cr,
							Settings.System.SCREEN_OFF_TIMEOUT, 60000) == SCREEN_OFF_TIMEOUT)
				oldtimeout = 60000;
			if (oldtimeout != 0) {
				Settings.System.putInt(cr, Settings.System.SCREEN_OFF_TIMEOUT,
						oldtimeout);
				oldtimeout = 0;
			}
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		mHandler.removeMessages(MSG_BACK);
		if (Receiver.call_state == UserAgent.UA_STATE_IDLE)
			finish();
		sensorManager.unregisterListener(this);
		started = false;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (Receiver.call_state == UserAgent.UA_STATE_IDLE)
			mHandler.sendEmptyMessageDelayed(MSG_BACK,
					Receiver.call_end_reason == -1 ? 2000 : 5000);
		first = true;
		pactive = false;
		pactivetime = SystemClock.elapsedRealtime();
		sensorManager.registerListener(this, proximitySensor,
				SensorManager.SENSOR_DELAY_NORMAL);
		started = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (!Sipdroid.release)
			Log.i("SipUA:", "on pause");
		switch (Receiver.call_state) {
		case UserAgent.UA_STATE_INCOMING_CALL:
			if (!RtpStreamReceiver.isBluetoothAvailable())
				Receiver.moveTop();
			break;
		case UserAgent.UA_STATE_IDLE:
			mHandler.sendEmptyMessageDelayed(MSG_BACK,
					Receiver.call_end_reason == -1 ? 2000 : 5000);
			break;
		}
		if (t != null) {
			running = false;
			t.interrupt();
		}
		screenOff(false);
	}

	void moveBack() {
		if (Receiver.ccConn != null && !Receiver.ccConn.isIncoming()) {
			// after an outgoing call don't fall back to the contact
			// or call log because it is too easy to dial accidentally from
			// there
			startActivity(Receiver.createHomeIntent());
		}
		onStop();
	}

	Context mContext = this;

	@Override
	public void onResume() {
		super.onResume();
		if (!Sipdroid.release)
			Log.i("SipUA:", "on resume");
		switch (Receiver.call_state) {
		case UserAgent.UA_STATE_INCOMING_CALL:
			if (Receiver.pstn_state == null
					|| Receiver.pstn_state.equals("IDLE"))
				if (PreferenceManager.getDefaultSharedPreferences(mContext)
						.getBoolean(
								org.sipdroid.sipua.ui.Settings.PREF_AUTO_ON,
								org.sipdroid.sipua.ui.Settings.DEFAULT_AUTO_ON)
						&& !mKeyguardManager.inKeyguardRestrictedInputMode())
					mHandler.sendEmptyMessageDelayed(MSG_ANSWER, 1000);
				else if ((PreferenceManager.getDefaultSharedPreferences(
						mContext).getBoolean(
						org.sipdroid.sipua.ui.Settings.PREF_AUTO_ONDEMAND,
						org.sipdroid.sipua.ui.Settings.DEFAULT_AUTO_ONDEMAND) && PreferenceManager
						.getDefaultSharedPreferences(mContext)
						.getBoolean(
								org.sipdroid.sipua.ui.Settings.PREF_AUTO_DEMAND,
								org.sipdroid.sipua.ui.Settings.DEFAULT_AUTO_DEMAND))
						|| (PreferenceManager
								.getDefaultSharedPreferences(mContext)
								.getBoolean(
										org.sipdroid.sipua.ui.Settings.PREF_AUTO_HEADSET,
										org.sipdroid.sipua.ui.Settings.DEFAULT_AUTO_HEADSET) && Receiver.headset > 0))
					mHandler.sendEmptyMessageDelayed(MSG_ANSWER_SPEAKER, 10000);
			break;
		case UserAgent.UA_STATE_INCALL:
			if (Receiver.docked <= 0)
				screenOff(true);
			break;
		case UserAgent.UA_STATE_IDLE:
			if (!mHandler.hasMessages(MSG_BACK))
				moveBack();
			break;
		}
		mHandler.sendEmptyMessage(MSG_TICK);
		mHandler.sendEmptyMessage(MSG_POPUP);
		if (t == null && Receiver.call_state != UserAgent.UA_STATE_IDLE) {
			etDialNum.setText("");
			running = true;
			(t = new Thread() {
				public void run() {
					int len = 0;
					long time;
					ToneGenerator tg = null;

					if (Settings.System.getInt(getContentResolver(),
							Settings.System.DTMF_TONE_WHEN_DIALING, 1) == 1)
						tg = new ToneGenerator(
								AudioManager.STREAM_VOICE_CALL,
								(int) (ToneGenerator.MAX_VOLUME * 2 * org.sipdroid.sipua.ui.Settings
										.getEarGain()));
					for (;;) {
						if (!running) {
							t = null;
							break;
						}
						if (len != etDialNum.getText().length()) {
							time = SystemClock.elapsedRealtime();
							if (tg != null)
								tg.startTone(mToneMap.get(etDialNum.getText()
										.charAt(len)));
							Receiver.engine(Receiver.mContext).info(
									etDialNum.getText().charAt(len++), 250);
							time = 250 - (SystemClock.elapsedRealtime() - time);
							try {
								if (time > 0)
									sleep(time);
							} catch (InterruptedException e) {
							}
							if (tg != null)
								tg.stopTone();
							try {
								if (running)
									sleep(250);
							} catch (InterruptedException e) {
							}
							continue;
						}
						mHandler.sendEmptyMessage(MSG_TICK);
						try {
							sleep(1000);
						} catch (InterruptedException e) {
						}
					}
					if (tg != null)
						tg.release();
				}
			}).start();
		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_ANSWER:
				if (Receiver.call_state == UserAgent.UA_STATE_INCOMING_CALL)
					answer();
				break;
			case MSG_ANSWER_SPEAKER:
				if (Receiver.call_state == UserAgent.UA_STATE_INCOMING_CALL) {
					answer();
					Receiver.engine(mContext).speaker(AudioManager.MODE_NORMAL);
				}
				break;
			case MSG_BACK:
				moveBack();
				break;
			case MSG_TICK:
				break;
			case MSG_POPUP:
				break;
			}
		}
	};

	public void initInCallScreen() {
		// Have the WindowManager filter out touch events that are "too fat".
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES);

		etDialNum = (EditText) findViewById(R.id.etDialNum);

		mToneMap.put('1', ToneGenerator.TONE_DTMF_1);
		mToneMap.put('2', ToneGenerator.TONE_DTMF_2);
		mToneMap.put('3', ToneGenerator.TONE_DTMF_3);
		mToneMap.put('4', ToneGenerator.TONE_DTMF_4);
		mToneMap.put('5', ToneGenerator.TONE_DTMF_5);
		mToneMap.put('6', ToneGenerator.TONE_DTMF_6);
		mToneMap.put('7', ToneGenerator.TONE_DTMF_7);
		mToneMap.put('8', ToneGenerator.TONE_DTMF_8);
		mToneMap.put('9', ToneGenerator.TONE_DTMF_9);
		mToneMap.put('0', ToneGenerator.TONE_DTMF_0);
		mToneMap.put('#', ToneGenerator.TONE_DTMF_P);
		mToneMap.put('*', ToneGenerator.TONE_DTMF_S);
	}

	public void onClick(View v) {
		int viewId = v.getId();
	}

	void appendDigit(final String c) {
		etDialNum.getText().append(c);
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.incall);

		bindEvent();
		init();
		call();

		initInCallScreen();

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

		if (!android.os.Build.BRAND.equalsIgnoreCase("archos"))
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
	}

	public void silence() {
		// TelephoneMgr.silence(InCallScreen.this);
		(new Thread() {
			public void run() {
				Receiver.engine(mContext).togglemute();
			}
		}).start();
	}

	public void speaker() {
		// TelephoneMgr.speaker(InCallScreen.this);
		(new Thread() {
			public void run() {
				Receiver.engine(mContext)
						.speaker(
								RtpStreamReceiver.speakermode == AudioManager.MODE_NORMAL ? AudioManager.MODE_IN_CALL
										: AudioManager.MODE_NORMAL);
			}
		}).start();
	}

	public void reject() {
		if (Receiver.ccCall != null) {
			Receiver.stopRingtone();
			Receiver.ccCall.setState(Call.State.DISCONNECTED);
		}
		(new Thread() {
			public void run() {
				Receiver.engine(mContext).rejectcall();
				// Intent it = new Intent(mContext, MainActivity.class);
				// it.putExtra("tab", 1);
				// startActivity(it);
				finish();
			}
		}).start();
	}

	public void answer() {
		(new Thread() {
			public void run() {
				Receiver.engine(mContext).answercall();
			}
		}).start();
		if (Receiver.ccCall != null) {
			aq.id(R.id.tvCallStatus).text("通话中");
			Receiver.ccCall.setState(Call.State.ACTIVE);
			Receiver.ccCall.base = SystemClock.elapsedRealtime();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			if (Receiver.call_state == UserAgent.UA_STATE_INCOMING_CALL) {
				answer();
				return true;
			}
			break;

		case KeyEvent.KEYCODE_CALL:
			switch (Receiver.call_state) {
			case UserAgent.UA_STATE_INCOMING_CALL:
				answer();
				break;
			case UserAgent.UA_STATE_INCALL:
			case UserAgent.UA_STATE_HOLD:
				Receiver.engine(this).togglehold();
				break;
			}
			// consume KEYCODE_CALL so PhoneWindow doesn't do anything with it
			return true;

		case KeyEvent.KEYCODE_BACK:
			back();
			return true;

		case KeyEvent.KEYCODE_CAMERA:
			// Disable the CAMERA button while in-call since it's too
			// easy to press accidentally.
			return true;

		case KeyEvent.KEYCODE_VOLUME_DOWN:
		case KeyEvent.KEYCODE_VOLUME_UP:
			if (Receiver.call_state == UserAgent.UA_STATE_INCOMING_CALL) {
				Receiver.stopRingtone();
				return true;
			}
			RtpStreamReceiver.adjust(keyCode, true);
			return true;
		}
		if (Receiver.call_state == UserAgent.UA_STATE_INCALL) {
			char number = event.getNumber();
			if (Character.isDigit(number) || number == '*' || number == '#') {
				appendDigit(String.valueOf(number));
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void back() {
		// if (Receiver.call_state == UserAgent.UA_STATE_INCOMING_CALL)
		reject();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);

		MenuItem m = menu.add(0, DTMF_MENU_ITEM, 0, R.string.menu_dtmf);
		m.setIcon(R.drawable.ic_menu_dial_pad);
		return result;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean result = super.onPrepareOptionsMenu(menu);

		menu.findItem(DTMF_MENU_ITEM).setVisible(
				Receiver.call_state == UserAgent.UA_STATE_INCALL);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DTMF_MENU_ITEM:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_DOWN:
		case KeyEvent.KEYCODE_VOLUME_UP:
			RtpStreamReceiver.adjust(keyCode, false);
			return true;
		case KeyEvent.KEYCODE_ENDCALL:
			if (Receiver.pstn_state == null
					|| (Receiver.pstn_state.equals("IDLE") && (SystemClock
							.elapsedRealtime() - Receiver.pstn_time) > 3000)) {
				reject();
				return true;
			}
			break;
		}
		Receiver.pstn_time = 0;
		return false;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	void setScreenBacklight(float a) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = a;
		getWindow().setAttributes(lp);
	}

	static final float PROXIMITY_THRESHOLD = 5.0f;
	public static boolean pactive;
	public static long pactivetime;

	@Override
	public void onSensorChanged(SensorEvent event) {
		boolean keepon = PreferenceManager
				.getDefaultSharedPreferences(mContext).getBoolean(
						org.sipdroid.sipua.ui.Settings.PREF_KEEPON,
						org.sipdroid.sipua.ui.Settings.DEFAULT_KEEPON);
		if (first) {
			first = false;
			return;
		}
		float distance = event.values[0];
		boolean active = (distance >= 0.0 && distance < PROXIMITY_THRESHOLD && distance < event.sensor
				.getMaximumRange());
		if (!(keepon && Receiver.on_wlan)
				|| Receiver.call_state == UserAgent.UA_STATE_INCOMING_CALL
				|| Receiver.call_state == UserAgent.UA_STATE_HOLD)
			active = false;
		pactive = active;
		pactivetime = SystemClock.elapsedRealtime();
		setScreenBacklight((float) (active ? 0.1 : -1));
	}

	private void bindEvent() {
		aq = new AQuery(this);
		ViewGroup keyBoard = aq.id(R.id.dial_keyboard_mini).getViewGroup();
		type = UriUtil.getIntValue(this, "type");
		user = (UserInfo) UriUtil.getSerializableValue(this, "user");

		aq.id(R.id.ivBack).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				back();
			}
		});
		// 静音
		aq.id(R.id.ivSilence).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				silence();
			}
		});
		// 免提
		aq.id(R.id.ivSpeaker).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				speaker();
			}
		});
		// 拨号键盘
		aq.id(R.id.ivDial).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!aq.id(R.id.dial_keyboard_mini).isVisible()) {
					aq.id(R.id.dial_keyboard_mini).visible();
				} else {
					aq.id(R.id.dial_keyboard_mini).gone();
				}
			}
		});
		UIUtils.bindTagEvent(keyBoard, new OnClickListener() {
			@Override
			public void onClick(View v) {
				keyNum(v);
			}
		});
		// 通信录
		aq.id(R.id.ivContact).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reject();
			}
		});
		// 取消呼叫
		aq.id(R.id.ibCancel).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reject();
			}
		});
	}

	private void init() {
		if (user == null) {
			UriUtil.startActivity(this, CallSelectorActivity.class);
			finish();
			return;
		}
		UserInfo userInfo = DataSyncMgr.getInstance().getCompanyPerson(
				user.getMobileNo());
		if (userInfo == null)
			return;
		aq.id(R.id.txtUser).text(userInfo.getUserName());
		if (TextUtils.isEmpty(userInfo.getPicUrl()))
			ImageUnity.getLoader().displayImage(userInfo.getPicUrl(),
					aq.id(R.id.ivUserIcon).getImageView());
	}

	private void call() {
		// String number = "10086";// user.getMobileNo();
		Loggers.d(AppContext.TAG, "call:" + user.getMobileNo());
		Receiver.engine(InCallScreen.this).call(user.getMobileNo(), true);
	}

	// 按键、发送DTMF
	public void keyNum(View v) {
		// EditText etNumber = aq.id(R.id.etDialNum).getEditText();
		String tag = v.getTag().toString();
		// String numbers = etNumber.getText().toString() + tag;
		// etNumber.setText(numbers);
		// etNumber.setSelection(numbers.length());

		appendDigit(tag);
	}
}

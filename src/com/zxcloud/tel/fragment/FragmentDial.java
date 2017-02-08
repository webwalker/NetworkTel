package com.zxcloud.tel.fragment;

import java.util.List;

import webwalker.framework.beans.CallLogBean;
import webwalker.framework.interfaces.ICallback;
import webwalker.framework.interfaces.ICallback1;
import webwalker.framework.system.contact.DialRecordWrapper;
import webwalker.framework.uiquery.AQuery;
import webwalker.framework.utils.StringUtil;
import webwalker.framework.utils.UIUtils;
import android.content.AsyncQueryHandler;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.zxcloud.tel.R;
import com.zxcloud.tel.adapter.DialAdapter;
import com.zxcloud.tel.adapter.SearchDialAdapter;
import com.zxcloud.tel.common.CacheMgr;
import com.zxcloud.tel.common.DataSyncMgr;
import com.zxcloud.tel.common.SearchMgr;
import com.zxcloud.tel.jsondata.UserInfo;
import com.zxcloud.tel.model.SearchPerson;
import com.zxcloud.tel.ui.CallSelectorBar;
import com.zxcloud.tel.ui.TopNavBar;

public class FragmentDial extends BaseFragment {
	private ListView callLogListView;
	private AsyncQueryHandler asyncQuery;
	private DialAdapter adapter;
	private List<CallLogBean> callLogs;
	private ViewGroup keyBoard;
	private EditText etNumber;
	private View v;
	private ToneGenerator tg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_dial, container, false);
		TopNavBar bar = initTopBar(v, "通话记录");
		bar.showIv1(false);

		aq = new AQuery(getParent());

		init();

		return v;
	}

	@Override
	public void init() {
		callLogListView = (ListView) v.findViewById(R.id.call_log_list);
		keyBoard = (ViewGroup) v.findViewById(R.id.dial_keyboard);
		callLogListView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				keyBoard.setVisibility(View.INVISIBLE);
				aq.id(R.id.dial_callbar_bottom).gone();
				return false;
			}
		});
		UIUtils.bindTagEvent(keyBoard, new OnClickListener() {
			@Override
			public void onClick(View v) {
				keyNum(v);
			}
		});

		aq.id(R.id.l_callbar_del).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = getCallNumber();
				if (text.length() > 0) {
					text = text.substring(0, text.length() - 1);
				}
				setCallNumber(text);
				if (text.length() == 0)
					showBottomBar(false);
				refreshSearch();
			}
		});
		aq.id(R.id.l_callbar_call).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (getCallNumber().length() > 0) {
					CallSelectorBar call = new CallSelectorBar(getActivity(),
							callback);
					call.showPopWindow(new UserInfo(getCallNumber(), "",
							getCallNumber(), "", ""));
				}
			}
		});
		etNumber = (EditText) v.findViewById(R.id.etDialNum);
		etNumber.addTextChangedListener(textWatcher);

		// 设置拨号音量
		if (Settings.System.getInt(getActivity().getContentResolver(),
				Settings.System.DTMF_TONE_WHEN_DIALING, 1) == 1)
			tg = new ToneGenerator(AudioManager.STREAM_VOICE_CALL,
					(int) (ToneGenerator.MAX_VOLUME * 2));

		showDialRecords();
	}

	private TextWatcher textWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			refreshSearch();
			if (TextUtils.isEmpty(getCallNumber())) {
				aq.id(R.id.callbar_clear).invisible();
			}
		}
	};

	private void showBottomBar(boolean visible) {
		if (visible) {
			aq.id(R.id.dial_callbar_bottom).visible();
			// aq.id(R.id.dial_callbar_top).visible();
		} else {
			// aq.id(R.id.dial_callbar_top).gone();
			aq.id(R.id.dial_callbar_bottom).gone();
		}
	}

	public void keyNum(View v) {
		etNumber.append(v.getTag().toString());
		aq.id(R.id.callbar_clear).visible();
		aq.id(R.id.callbar_clear).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setCallNumber("");
				showBottomBar(false);
			}
		});
		showBottomBar(true);

		int tone = StringUtil.getInt(v.getTag(), 1);
		playTone(tone);
	}

	private void playTone(int number) {
		if (tg != null) {
			tg.startTone(number);
			tg.stopTone();
		}
	}

	private String getCallNumber() {
		if (TextUtils.isEmpty(etNumber.getText().toString()))
			return "";
		return etNumber.getText().toString();
	}

	private void setCallNumber(String number) {
		etNumber.setText(number);
	}

	ICallback1 callback = new ICallback1() {
		@Override
		public void action() {
			showBottomBar(false);
			setCallNumber("");
			refreshSearch();
		}
	};

	void refreshSearch() {
		if (TextUtils.isEmpty(getCallNumber())) {
			showDialRecords();
		} else {
			searchContracts();
		}
	}

	// 搜索与高亮处理
	private void searchContracts() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 验证企业通信录是否已同步
				DataSyncMgr.getInstance().refreshWhenCompanyIsNull();

				// 开始搜索
				String numbers = getCallNumber();
				SearchMgr searcher = new SearchMgr();
				List<SearchPerson> persons = searcher.searchContracts(numbers);
				Message msg = new Message();
				msg.obj = persons;
				searchHandler.sendMessage(msg);
			}
		}).start();
	}

	private Handler searchHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			List<SearchPerson> persons = (List<SearchPerson>) msg.obj;
			SearchDialAdapter searchAdapter = new SearchDialAdapter(
					getActivity(), persons);
			callLogListView.setAdapter(searchAdapter);
		}
	};

	private void showDialRecords() {
		List<CallLogBean> logs = DataSyncMgr.getInstance().getRecords();
		if (logs != null) {
			setAdapter(logs);
			return;
		}

		ICallback<List<CallLogBean>> callback = new ICallback<List<CallLogBean>>() {
			@Override
			public void action(List<CallLogBean> data) {
				if (data.size() == 0)
					return;
				setAdapter(data);
				CacheMgr.set(CacheMgr.CALL_LOGS, data);
			}
		};
		DialRecordWrapper.getInstance()
				.queryDialRecord(getActivity(), callback);
	}

	private void setAdapter(List<CallLogBean> callLogs) {
		adapter = new DialAdapter(getActivity(), (ViewGroup) v, callLogs);
		callLogListView.setAdapter(adapter);
	}
}

package com.zxcloud.tel.activity;

import java.util.List;

import webwalker.framework.beans.PinYinItem;
import webwalker.framework.interfaces.ICallback1;
import webwalker.framework.utils.Loggers;
import webwalker.framework.utils.PinYin4JUtil;
import webwalker.framework.utils.UriUtil;
import webwalker.framework.widget.ListViewController;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.zxcloud.tel.BaseActivity;
import com.zxcloud.tel.R;
import com.zxcloud.tel.adapter.MeetingSendAdapter;
import com.zxcloud.tel.common.AppContext;
import com.zxcloud.tel.common.SearchMgr;
import com.zxcloud.tel.model.SearchPerson;

public class ContactSearchActivity extends BaseActivity {
	private EditText etSearchText;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_search);

		init();
	}

	private void init() {
		etSearchText = aq.id(R.id.etSearchText).getEditText();
		etSearchText.addTextChangedListener(textWatcher);
		listView = aq.id(R.id.lv_meeting_search).getListView();
		aq.id(R.id.tvCancel).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		aq.id(R.id.btnMeeting).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UriUtil.startActivity(ContactSearchActivity.this,
						MeetingCurrentActivity.class);
			}
		});
	}

	// 将输入内容转换为字母及对应的数字索引
	private String getSearchNumbers() {
		String inputs = etSearchText.getText().toString();
		if (!TextUtils.isEmpty(inputs)) {
			try {
				PinYinItem item = PinYin4JUtil.getSinglePinYinAndFirst(inputs);
				return item.getIndex();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	private Handler searchHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String numbers = getSearchNumbers();
			Loggers.d(AppContext.TAG, "search numbers:" + numbers);
			if (TextUtils.isEmpty(numbers))
				return;
			List<SearchPerson> persons = new SearchMgr()
					.searchContracts(numbers);
			MeetingSendAdapter adapter = new MeetingSendAdapter(
					ContactSearchActivity.this, persons);
			adapter.setCallback(callback);
			listView.setAdapter(adapter);
		}
	};

	private int getSelectedCount() {
		ListViewController controller = new ListViewController(
				ContactSearchActivity.this, listView);
		int count = controller.getCheckedCount(R.id.cbMeeting);
		return count;
	}

	private ICallback1 callback = new ICallback1() {
		@Override
		public void action() {
			int count = getSelectedCount();
			if (count > 0) {
				aq.id(R.id.l_bottom_action).visible();
				aq.id(R.id.tvSelectCount).text("共" + count + "人");
			} else {
				aq.id(R.id.l_bottom_action).gone();
			}
		}
	};

	// 搜索框输入监听
	private TextWatcher textWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			// 搜索与高亮处理
			new Thread(new Runnable() {
				@Override
				public void run() {
					searchHandler.sendEmptyMessageDelayed(0, 500);
				}
			}).start();
		}
	};
}

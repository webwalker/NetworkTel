package com.zxcloud.tel.adapter;

import java.util.Date;
import java.util.List;

import webwalker.framework.beans.CallLogBean;
import webwalker.framework.utils.DateUtil;
import webwalker.framework.utils.DateUtil.DayType;
import webwalker.framework.utils.StringUtil;
import android.app.Activity;
import android.graphics.Color;
import android.provider.CallLog.Calls;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxcloud.tel.R;
import com.zxcloud.tel.controller.DataController;
import com.zxcloud.tel.jsondata.UserInfo;
import com.zxcloud.tel.ui.CallSelectorBar;

/**
 * 最近通话记录
 * 
 * @author xu.jian
 * 
 */
public class DialAdapter extends BaseAdapter {
	private List<CallLogBean> callLogs;
	private LayoutInflater inflater;
	private Date nowDate = null;
	private int numberLength, nameLength;
	private String numberText, nameText;
	private DataController controller = null;
	private Activity context;
	private ViewGroup parent;

	public DialAdapter(Activity act, ViewGroup vg, List<CallLogBean> callLogs) {
		this.context = act;
		this.parent = vg;
		this.callLogs = callLogs;
		this.inflater = LayoutInflater.from(context);
		nowDate = new Date();
	}

	@Override
	public int getCount() {
		return callLogs.size();
	}

	@Override
	public Object getItem(int position) {
		return callLogs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_callrecord, null);
			holder = new ViewHolder();
			holder.call_type = (ImageView) convertView
					.findViewById(R.id.dial_status);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.number = (TextView) convertView.findViewById(R.id.number);
			holder.count = (TextView) convertView.findViewById(R.id.tvCount);
			holder.time = (TextView) convertView.findViewById(R.id.tvTime);
			holder.location = (TextView) convertView
					.findViewById(R.id.tvLocation);
			holder.call_btn = (LinearLayout) convertView
					.findViewById(R.id.call_btn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		CallLogBean callLog = callLogs.get(position);
		holder.name.setTextColor(Color.BLACK);
		switch (callLog.getType()) {
		case Calls.INCOMING_TYPE:
			holder.call_type.setBackgroundResource(R.drawable.dial_in);
			break;
		case Calls.OUTGOING_TYPE:
			holder.call_type.setBackgroundResource(R.drawable.dial_out);
			break;
		case Calls.MISSED_TYPE:
			holder.call_type.setBackgroundResource(R.drawable.dial_in_unanswer);
			holder.name.setTextColor(Color.RED);
			break;
		}
		nameLength = callLog.getName().length();
		numberLength = callLog.getPhone().length();
		// name
		nameText = callLog.getName();
		if (nameLength > 11)
			nameText = StringUtil.left(nameText, 11) + ".";
		if (nameText.equals("-1") || nameText.equals("0"))
			nameText = "未知";
		// number
		numberText = callLog.getPhone();
		if (numberLength > 11)
			numberText = StringUtil.left(numberText, 11) + ".";
		else if (numberLength <= 8)
			numberText = "本地";

		holder.name.setText(nameText);
		holder.number.setText(numberText);
		holder.count.setText("(" + callLog.getCount() + ")");

		// async update list info
		convertView.post(new adapterRunnable(holder, callLog));

		addViewListener(holder.call_btn, callLog, position);
		return convertView;
	}

	private class adapterRunnable implements Runnable {
		private ViewHolder holder;
		private CallLogBean log;

		public adapterRunnable(ViewHolder vh, CallLogBean l) {
			this.holder = vh;
			this.log = l;
		}

		@Override
		public void run() {
			// 格式化时间
			holder.time.setText(formatCallTime(log.getDate()));
			holder.time.postInvalidate();
			// 处理地理位置
			controller = new DataController(context);
			String area = controller.getArea(log.getPhone());
			holder.location.setText(area);
			holder.location.postInvalidate();
		}
	}

	private String formatCallTime(Date date) {
		long time = date.getTime();
		DayType type = DateUtil.getDayType(date.getTime());
		switch (type) {
		case Today:
			return DateUtil.formatDate(time, DateUtil.HHmm);
		case Yesterday:
			return "昨天";
		default:
			return DateUtil.formatDate(time, DateUtil.MMdd_Line);
		}
	}

	private static class ViewHolder {
		ImageView call_type;
		TextView name;
		TextView number;
		TextView count;
		TextView time;
		TextView location;
		LinearLayout call_btn;
	}

	private void addViewListener(View view, final CallLogBean callLog,
			final int position) {
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// UriUtil.callPhone(ctx, callLog.getPhone());
				CallSelectorBar call = new CallSelectorBar(context, null);
				call.showPopWindow(new UserInfo(callLog.getName(), "", callLog
						.getPhone(), callLog.getIcon(), ""));

			}
		});
	}
}

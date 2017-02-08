package com.zxcloud.tel.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxcloud.tel.R;
import com.zxcloud.tel.controller.ImageController;
import com.zxcloud.tel.jsondata.RecentCallInfo;
import com.zxcloud.tel.jsondata.UserInfo;
import com.zxcloud.tel.ui.CallSelectorBar;

/**
 * 常用联系人
 * 
 * @author xu.jian
 * 
 */
public class RecentCallsAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<RecentCallInfo> list;
	private Activity act;

	public RecentCallsAdapter(Activity context, List<RecentCallInfo> list) {
		this.act = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void remove(int position) {
		list.remove(position);
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_txl, null);
			holder = new ViewHolder();
			holder.user_icon = (ImageView) convertView
					.findViewById(R.id.user_icon);
			holder.name = (TextView) convertView.findViewById(R.id.tvName);
			holder.number1 = (TextView) convertView
					.findViewById(R.id.tvNumber1);
			holder.number2 = (TextView) convertView
					.findViewById(R.id.tvNumber2);
			holder.department = (TextView) convertView
					.findViewById(R.id.tvDesc);
			holder.call_btn = (LinearLayout) convertView
					.findViewById(R.id.call_btn);
			holder.number2.setVisibility(View.VISIBLE);
			holder.department.setVisibility(View.VISIBLE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final RecentCallInfo call = list.get(position);
		ImageController.loadImage(call.getUserIcon(), holder.user_icon);
		holder.name.setText(call.getUserName());
		String extensionNo = call.getExtensionNo();
		String phone = call.getPhone();
		String department = call.getDepartment();
		if (!TextUtils.isEmpty(extensionNo)) {
			holder.number1.setText("分机号:" + extensionNo);
		}
		if (!TextUtils.isEmpty(phone)) {
			holder.number2.setText("手机号:" + phone);
		}
		if (!TextUtils.isEmpty(department)) {
			holder.department.setText(department);
		}
		holder.call_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CallSelectorBar bar = new CallSelectorBar(act, null);
				bar.showPopWindow(new UserInfo(call.getUserName(), call
						.getDepartment(), call.getPhone(), call.getUserIcon(),
						call.getExtensionNo()));
			}
		});

		return convertView;
	}

	private static class ViewHolder {
		ImageView user_icon;
		TextView name;
		TextView number1;
		TextView number2;
		TextView department;
		LinearLayout call_btn;
	}
}

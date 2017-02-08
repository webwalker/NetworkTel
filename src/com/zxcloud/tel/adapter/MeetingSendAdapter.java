package com.zxcloud.tel.adapter;

import java.util.List;

import webwalker.framework.beans.BaseContract;
import webwalker.framework.interfaces.ICallback1;
import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxcloud.tel.R;
import com.zxcloud.tel.controller.ImageController;
import com.zxcloud.tel.model.SearchPerson;

/**
 * 通信录搜索-会议 适配
 * 
 * @author xu.jian
 * 
 */
public class MeetingSendAdapter extends BaseAdapter {
	private Activity act;
	private List<SearchPerson> persons;
	private LayoutInflater inflater;
	private ICallback1 callback;

	public MeetingSendAdapter(Activity act, List<SearchPerson> persons) {
		this.act = act;
		this.persons = persons;
		this.inflater = LayoutInflater.from(act);
	}

	@Override
	public int getCount() {
		return persons.size();
	}

	@Override
	public Object getItem(int position) {
		return persons.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_meeting, null);
			holder = new ViewHolder();
			holder.checkbox = (CheckBox) convertView
					.findViewById(R.id.cbMeeting);
			holder.user_icon = (ImageView) convertView
					.findViewById(R.id.user_icon);
			holder.name = (TextView) convertView.findViewById(R.id.tvName);
			holder.number1 = (TextView) convertView
					.findViewById(R.id.tvNumber1);
			holder.number2 = (TextView) convertView
					.findViewById(R.id.tvNumber2);
			holder.number1.setVisibility(View.VISIBLE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean needChecked = !holder.checkbox.isChecked();
				holder.checkbox.setChecked(needChecked);
				if (callback != null)
					callback.action();
			}
		});

		SearchPerson person = persons.get(position);
		BaseContract base = person.getPerson();
		if (base != null)
			ImageController.loadImage(base.getIcon(), holder.user_icon);
		holder.name.setText(person.getChineseFont());

		if (person.isMatchName()) {
			holder.name.setText(person.getSpanable());
			holder.number1.setText("手机号:" + person.getTag());
		} else {
			holder.name.setText(person.getChineseFont());
			holder.number1.setText("手机号:" + person.getSpanable());
		}
		if (!TextUtils.isEmpty(person.getExtansionNo())) {
			holder.number2.setVisibility(View.VISIBLE);
			holder.number2.setText("分机号:" + person.getExtansionNo());
		}

		return convertView;
	}

	private static class ViewHolder {
		ImageView user_icon;
		TextView name;
		TextView number1;
		TextView number2;
		CheckBox checkbox;
	}

	public ICallback1 getCallback() {
		return callback;
	}

	public void setCallback(ICallback1 callback) {
		this.callback = callback;
	}

}

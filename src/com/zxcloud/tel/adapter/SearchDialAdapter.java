package com.zxcloud.tel.adapter;

import java.util.List;

import webwalker.framework.utils.UriUtil;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxcloud.tel.R;
import com.zxcloud.tel.jsondata.UserInfo;
import com.zxcloud.tel.model.SearchPerson;
import com.zxcloud.tel.ui.CallSelectorBar;

/**
 * 拨号搜索适配
 * 
 * @author xu.jian
 * 
 */
public class SearchDialAdapter extends BaseAdapter {
	private Activity ctx;
	private List<SearchPerson> persons;
	private LayoutInflater inflater;

	public SearchDialAdapter(Activity act, List<SearchPerson> persons) {
		this.ctx = act;
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
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_txl, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.tvName);
			holder.number = (TextView) convertView.findViewById(R.id.tvNumber1);
			holder.number.setVisibility(View.VISIBLE);
			holder.call_btn = (LinearLayout) convertView
					.findViewById(R.id.call_btn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		SearchPerson person = persons.get(position);
		if (person.isMatchName()) {
			holder.name.setText(person.getSpanable());
			holder.number.setText(person.getTag());
		} else {
			holder.name.setText(person.getChineseFont());
			holder.number.setText(person.getSpanable());
		}
		addViewListener(holder.call_btn, person, position);
		return convertView;
	}

	private void addViewListener(View view, final SearchPerson person,
			final int position) {
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// UriUtil.callPhone(ctx, person.getTag());
				CallSelectorBar call = new CallSelectorBar(ctx, null);
				call.showPopWindow(new UserInfo(person.getChineseFont(), "",
						person.getTag(), "", person.getExtansionNo()));
			}
		});
	}

	private static class ViewHolder {
		TextView name;
		TextView number;
		LinearLayout call_btn;
	}
}

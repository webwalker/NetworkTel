package com.zxcloud.tel.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxcloud.tel.R;
import com.zxcloud.tel.jsondata.AdBookInfo;
import com.zxcloud.tel.jsondata.UserInfo;
import com.zxcloud.tel.ui.CallSelectorBar;

/**
 * 手机联系人
 * 
 * @author xu.jian
 * 
 */
public class CompanyContactListAdapter extends BaseExpandableListAdapter {
	private LayoutInflater inflater;
	private Activity act; // 上下文
	private List<AdBookInfo> groups;

	public CompanyContactListAdapter(Activity context, List<AdBookInfo> list) {
		this.act = context;
		this.inflater = LayoutInflater.from(context);
		this.groups = list;
	}

	private static class ViewHolder {
		TextView alpha;
		TextView name;
		TextView number;
		TextView number2;
		LinearLayout call_btn;
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).getMemberCount();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).getMembers().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		AdBookInfo book = groups.get(groupPosition);
		return createGroupView(convertView, book);
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		AdBookInfo group = groups.get(groupPosition);
		UserInfo user = group.getMembers().get(childPosition);
		user.setDepartmentName(group.getDepartmentName());
		return createChildView(convertView, user);
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	private View createGroupView(View convertView, AdBookInfo book) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_groups, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView
					.findViewById(R.id.txtGroupName);
			holder.number = (TextView) convertView
					.findViewById(R.id.txtMemberCount);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(book.getDepartmentName());
		holder.number.setText(book.getMemberCount() + "人");
		return convertView;
	}

	private View createChildView(View convertView, final UserInfo user) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_txl, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.tvName);
			holder.number = (TextView) convertView.findViewById(R.id.tvNumber1);
			holder.number2 = (TextView) convertView
					.findViewById(R.id.tvNumber2);
			holder.call_btn = (LinearLayout) convertView
					.findViewById(R.id.call_btn);
			holder.number2.setVisibility(View.VISIBLE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final String number = user.getExtensionNo();
		final String number2 = user.getMobileNo();
		holder.name.setText(user.getUserName());
		if (!TextUtils.isEmpty(number))
			holder.number.setText("分机号：" + number);
		if (!TextUtils.isEmpty(number2))
			holder.number2.setText("手机号：" + number2);
		holder.call_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CallSelectorBar call = new CallSelectorBar(act, null);
				call.showPopWindow(user);
			}
		});
		return convertView;

	}
}

package com.zxcloud.tel.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import webwalker.framework.beans.ContractPersion;
import webwalker.framework.widget.LetterIndexBar;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxcloud.tel.R;
import com.zxcloud.tel.jsondata.UserInfo;
import com.zxcloud.tel.ui.CallSelectorBar;

/**
 * 手机联系人
 * 
 * @author xu.jian
 * 
 */
public class ContactListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<ContractPersion> list;
	private HashMap<String, Integer> alphaIndexer; // 字母索引
	private String[] sections; // 存储每个章节
	private Activity act; // 上下文
	final static Pattern pattern = Pattern.compile("^[A-Za-z]+$");

	public ContactListAdapter(Activity context, List<ContractPersion> list,
			LetterIndexBar alpha) {
		this.act = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.alphaIndexer = new HashMap<String, Integer>();
		this.sections = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			// 得到字母
			String name = getAlpha(list.get(i).getSortKey());
			if (!alphaIndexer.containsKey(name)) {
				alphaIndexer.put(name, i);
			}
		}

		Set<String> sectionLetters = alphaIndexer.keySet();
		ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
		Collections.sort(sectionList);
		sections = new String[sectionList.size()];
		sectionList.toArray(sections);

		alpha.setAlphaIndexer(alphaIndexer);

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
			convertView = inflater.inflate(R.layout.list_item_contact_phone,
					null);
			holder = new ViewHolder();
			holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
			holder.name = (TextView) convertView.findViewById(R.id.tvName);
			holder.number = (TextView) convertView.findViewById(R.id.tvNumber1);
			holder.call_btn = (LinearLayout) convertView
					.findViewById(R.id.call_btn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ContractPersion contact = list.get(position);
		final String name = contact.getName();
		final String number = contact.getPhone();
		holder.name.setText(name);
		holder.number.setText(number);
		holder.call_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CallSelectorBar call = new CallSelectorBar(act, null);
				call.showPopWindow(new UserInfo(name, "", number, contact
						.getIcon(), ""));
			}
		});
		// 当前字母
		String currentStr = getAlpha(contact.getSortKey());
		// 前面的字母
		String previewStr = (position - 1) >= 0 ? getAlpha(list.get(
				position - 1).getSortKey()) : " ";

		if (!previewStr.equals(currentStr)) {
			holder.alpha.setVisibility(View.VISIBLE);
			holder.alpha.setText(currentStr);
		} else {
			holder.alpha.setVisibility(View.GONE);
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView alpha;
		TextView name;
		TextView number;
		LinearLayout call_btn;
	}

	/**
	 * 获取首字母
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式匹配
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(); // 将小写字母转换为大写
		} else {
			return "#";
		}
	}
}

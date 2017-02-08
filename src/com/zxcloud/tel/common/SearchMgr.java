package com.zxcloud.tel.common;

import java.util.ArrayList;
import java.util.List;

import webwalker.framework.beans.PinYinItem;
import webwalker.framework.utils.UIUtils;
import android.text.SpannableStringBuilder;

import com.zxcloud.tel.model.SearchPerson;

/**
 * @author xu.jian
 * 
 */
public class SearchMgr {
	private List<SearchPerson> nameList = new ArrayList<SearchPerson>();
	private List<SearchPerson> phoneList = new ArrayList<SearchPerson>();

	public synchronized List<SearchPerson> searchContracts(String keys) {
		nameList.clear();
		phoneList.clear();
		List<PinYinItem> list = AppContext.SEARCH_ITEM;
		String indexs = "";
		for (PinYinItem p : list) {
			try {
				SearchPerson sp = (SearchPerson) p;
				indexs = sp.getIndex();
				if (sp == null || indexs == null) {
					// Loggers.d("PinYinUtil", "----" + sp.getFirstLetter() +
					// ","
					// + indexs);
					continue;
				}
				if (indexs.indexOf(keys) > -1) {
					SpannableStringBuilder span = UIUtils.getHighLightText(
							sp.getChineseFont(), indexs, keys);
					sp.setSpanable(span);
					sp.setMatchName(true);
					nameList.add(sp);
				} else if (sp.getTag().indexOf(keys) > -1) {
					SpannableStringBuilder span = UIUtils.getHighLightText(
							sp.getTag(), sp.getTag(), keys);
					sp.setSpanable(span);
					sp.setMatchName(false);
					phoneList.add(sp);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		nameList.addAll(phoneList);
		return nameList;
	}
}

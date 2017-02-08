package com.zxcloud.tel.model;

import webwalker.framework.beans.BaseContract;
import webwalker.framework.beans.PinYinItem;
import android.text.SpannableStringBuilder;

/**
 * @author xu.jian
 * 
 */
public class SearchPerson extends PinYinItem {
	private String extansionNo;// 分机号
	private BaseContract person;
	private SpannableStringBuilder spanable;
	private boolean matchName; //true匹配姓名、false匹配电话 

	public String getExtansionNo() {
		return extansionNo;
	}

	public void setExtansionNo(String extansionNo) {
		this.extansionNo = extansionNo;
	}

	public BaseContract getPerson() {
		return person;
	}

	public void setPerson(BaseContract person) {
		this.person = person;
	}

	public SpannableStringBuilder getSpanable() {
		return spanable;
	}

	public void setSpanable(SpannableStringBuilder spanable) {
		this.spanable = spanable;
	}

	public boolean isMatchName() {
		return matchName;
	}

	public void setMatchName(boolean matchName) {
		this.matchName = matchName;
	}
}

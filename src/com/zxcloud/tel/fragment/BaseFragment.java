package com.zxcloud.tel.fragment;

import webwalker.framework.common.BaseFrameworkFragment;
import webwalker.framework.utils.Loggers;
import android.view.View;

import com.zxcloud.tel.R;
import com.zxcloud.tel.activity.MainActivity;
import com.zxcloud.tel.ui.TopNavBar;

/**
 * @author xu.jian
 * 
 */
public abstract class BaseFragment extends BaseFrameworkFragment {

	protected MainActivity getParent() {
		return (MainActivity) getActivity();
	}

	protected TopNavBar initTopBar(View v, String title) {
		TopNavBar bar = (TopNavBar) v.findViewById(R.id.inc_navbar);
		bar.setActivity(getActivity());
		bar.setTv1(title);
		return bar;
	}

	@Override
	public void onClick(View arg0) {

	}

	public void init() {
		Loggers.d("current fragment:" + this.getClass().getName());
	}
}

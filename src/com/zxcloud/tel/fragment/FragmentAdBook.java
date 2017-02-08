package com.zxcloud.tel.fragment;

import webwalker.framework.utils.UriUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.viewpagerindicator.TabPageIndicator;
import com.zxcloud.tel.R;
import com.zxcloud.tel.activity.ContactSearchActivity;
import com.zxcloud.tel.ui.TopNavBar;

public class FragmentAdBook extends BaseFragment implements OnClickListener {

	private ViewPager pager;
	private MyPagerAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_adbook, container, false);

		TopNavBar bar = initTopBar(v, "通信录");
		bar.showIv1(false);
		bar.setIv2Image(R.drawable.search);
		bar.setIv2Listener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UriUtil.startActivity(getActivity(),
						ContactSearchActivity.class);
			}
		});

		adapter = new MyPagerAdapter(this.getChildFragmentManager());
		pager = (ViewPager) v.findViewById(R.id.pager);
		pager.setAdapter(adapter);

		TabPageIndicator indicator = (TabPageIndicator) v
				.findViewById(R.id.indicator);
		indicator.setViewPager(pager);

		return v;
	}

	@Override
	public void init() {
	}

	@Override
	public void onClick(View arg0) {
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {
		private final String[] TITLES = { "手机通讯录", "企业通讯录", "常用联系人" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new FragmentContactPhone();
			case 1:
				return new FragmentContactCompany();
			case 2:
				return new FragmentContactRecent();
			}
			return new FragmentContactPhone();
		}
	}
}

package com.zxcloud.tel.fragment;

import webwalker.framework.cache.ACache;
import webwalker.framework.interfaces.ICallback;
import webwalker.framework.system.log.LogcatUtil;
import webwalker.framework.uiquery.AQuery;
import webwalker.framework.utils.ImageUnity;
import webwalker.framework.utils.JsonUtil;
import webwalker.framework.utils.NetworkUtils;
import webwalker.framework.utils.UriUtil;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxcloud.tel.R;
import com.zxcloud.tel.activity.PersionInfoActivity;
import com.zxcloud.tel.activity.SettingActivity;
import com.zxcloud.tel.common.AppContext;
import com.zxcloud.tel.common.CacheMgr;
import com.zxcloud.tel.http.UserHandler;
import com.zxcloud.tel.jsondata.UserInfo;

public class FragmentMe extends BaseFragment {
	private AQuery aq = null;
	private UserInfo userInfo;
	private View saveView;
	private boolean hasInit = false;
	private ACache cache;

	/**
	 * 缓存视图
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		cache = ACache.get(getActivity());
		if (saveView == null || hasInit == false) {
			firstInit(inflater, container);
		}

		ViewGroup parent = (ViewGroup) saveView.getParent();
		if (parent != null) {
			parent.removeView(saveView);
		}
		return saveView;
	}

	private void firstInit(LayoutInflater inflater, ViewGroup container) {
		saveView = inflater.inflate(R.layout.fragment_me, container, false);
		aq = new AQuery(saveView);
		aq.id(R.id.imgSettings).clicked(this);
		aq.id(R.id.lMyInfo).clicked(this);
		aq.id(R.id.lDeposit).clicked(this);
		aq.id(R.id.lEmail).clicked(this);
		aq.id(R.id.lPhone).clicked(this);
		aq.id(R.id.button1).clicked(this);
		aq.id(R.id.button2).clicked(this);
		init();
	}

	@Override
	public void init() {
		if (!AppContext.hasLogin()
				|| !NetworkUtils.isNetworkAvailable(getActivity())) {
			// 预加载缓存数据
			loadCacheData();
			return;
		}

		aq.id(R.id.tvNumber).visible();
		aq.id(R.id.tvExtNumber).visible();

		showLoading();

		ICallback<UserInfo> callback = new ICallback<UserInfo>() {
			@Override
			public void action(UserInfo user) {
				try {
					hasInit = true;
					updateUI(user);

					// 保存到本地缓存, 方便不联网时显示
					cache.put(CacheMgr.USER_INFO, JsonUtil.gson.toJson(user));

					hideLoading();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		UserHandler user = new UserHandler(getActivity());
		user.getUserInfo(callback, AppContext.USERID);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (AppContext.needUpdateUserInfo) {
			AppContext.needUpdateUserInfo = false;
			init();
		}
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		switch (id) {
		case R.id.imgSettings:
			UriUtil.startActivity(getActivity(), SettingActivity.class);
			break;
		case R.id.lMyInfo:
			Intent it = new Intent(getActivity(), PersionInfoActivity.class);
			it.putExtra("user", userInfo);
			getActivity().startActivity(it);
			break;
		case R.id.lDeposit:
			// UriUtil.startActivity(getActivity(), DepositActivity.class);
			break;
		case R.id.lEmail:
			String email = aq.id(R.id.tvEmail).getText().toString();
			if (email.equals("-") || email.equals(""))
				return;
			UriUtil.sendMail(getActivity(), email, "", "");
			break;
		case R.id.lPhone:
			String phone = aq.id(R.id.tvPhone).getText().toString();
			if (phone.equals("-") || phone.equals(""))
				return;
			UriUtil.callPhone(getActivity(), phone);
			break;
		// 真机调试使用
		case R.id.button1:
			LogcatUtil.start(getActivity());
			break;
		case R.id.button2:
			LogcatUtil.showActLogs(getActivity());
			break;
		}
	}

	public void loadCacheData() {
		try {
			// show cache data
			String json = cache.getAsString(CacheMgr.USER_INFO);
			if (!TextUtils.isEmpty(json)) {
				UserInfo user = JsonUtil.gson.fromJson(json, UserInfo.class);
				if (user == null)
					return;
				updateUI(user);
				aq.id(R.id.tvMoney).text("未知");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateUI(UserInfo user) {
		userInfo = user;
		// 获取远程头像
		if (!TextUtils.isEmpty(user.getPicUrl()))
			ImageUnity.getLoader().displayImage(user.getPicUrl(),
					aq.id(R.id.user_icon).getImageView());

		aq.id(R.id.tvName).text(user.getUserName());
		if (!TextUtils.isEmpty(user.getCompanyId())) {
			aq.id(R.id.tvNumber).format(user.getCompanyId());
			aq.id(R.id.tvExtNumber).visible();
		}
		if (!TextUtils.isEmpty(user.getLoginName())) {
			aq.id(R.id.tvExtNumber).format(user.getLoginName());
			aq.id(R.id.tvExtNumber).visible();
		}
		aq.id(R.id.tvMoney).format(user.getCompanyBalance());
		aq.id(R.id.tvDept).text(user.getDepartmentName());
		aq.id(R.id.tvEmail).text(user.getEmail());
		aq.id(R.id.tvPhone).text(user.getMobileNo());
		aq.id(R.id.tvIntro).text(user.getIntroduce());
	}
}

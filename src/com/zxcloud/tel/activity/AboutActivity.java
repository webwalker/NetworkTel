package com.zxcloud.tel.activity;

import webwalker.framework.interfaces.ICallback;
import webwalker.framework.utils.UIUtils;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.zxcloud.tel.BaseActivity;
import com.zxcloud.tel.R;
import com.zxcloud.tel.common.CacheMgr;
import com.zxcloud.tel.http.AppHandler;
import com.zxcloud.tel.jsondata.AboutInfo;

public class AboutActivity extends BaseActivity {
	private View v;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Object data = CacheMgr.get(CacheMgr.ABOUT_ACT);
		if (data != null) {
			v = (View) data;
			ViewGroup parent = (ViewGroup) v.getParent();
			if (parent != null)
				parent.removeView(v);
		}
		if (v == null) {
			v = UIUtils.getView(getBaseContext(), R.layout.activity_about);
			showLoading();

			ICallback<AboutInfo> callback = new ICallback<AboutInfo>() {
				@Override
				public void action(AboutInfo about) {
					hideLoading();

					String intro = about.getIntroduce();
					String contactInfo = about.getContactInfo();
					aq.id(R.id.txtIntroduce).text(intro);
					aq.id(R.id.txtContactInfo).text(contactInfo);

					CacheMgr.set(CacheMgr.ABOUT_ACT, v);
				}
			};

			AppHandler app = new AppHandler(getBaseContext());
			app.getAbout(callback);
		}

		initTopBar(this, v, "关于网络电话");
		setContentView(v);
	}
}

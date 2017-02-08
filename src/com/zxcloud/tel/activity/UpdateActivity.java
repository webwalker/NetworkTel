package com.zxcloud.tel.activity;

import webwalker.framework.utils.AppUtil;
import webwalker.framework.utils.MessageUtil;
import webwalker.framework.utils.MetaDataUtil;
import webwalker.framework.utils.NetworkUtils;
import webwalker.framework.utils.StringUtil;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.umeng.update.UmengUpdateAgent;
import com.zxcloud.tel.BaseActivity;
import com.zxcloud.tel.R;
import com.zxcloud.tel.common.AppContext;

public class UpdateActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);

		initTopBar(this, "网络电话");

		String version = AppUtil.getVersionName(this);
		MetaDataUtil data = new MetaDataUtil(this);
		aq.id(R.id.tvVersion).text(
				StringUtil.format(aq.id(R.id.tvVersion).getText().toString(),
						version));
		aq.id(R.id.tvReleaseTime).text(
				data.getApplicationMetaData("VERSION_DATE"));
		aq.id(R.id.btnUpdate).clicked(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!NetworkUtils.isNetworkAvailable(getBaseContext())) {
					MessageUtil.showShortToast(getBaseContext(),
							"连接不到服务器，请稍后再试！");
					return;
				}

				UmengUpdateAgent.setUpdateAutoPopup(false);
				AppContext.setUpdateListener(UpdateActivity.this, true);
				UmengUpdateAgent.forceUpdate(UpdateActivity.this);
			}
		});
	}
}

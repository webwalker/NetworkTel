package com.zxcloud.tel.ui;

import webwalker.framework.common.BaseFrameworkDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zxcloud.tel.R;

/**
 * @author xu.jian
 * 
 */
public class MessageDialog extends BaseFrameworkDialog {

	TextView txtMsg;

	public MessageDialog(Context context, String message) {
		super(context, R.style.dialog);
		View v = getLayoutInflater().inflate(R.layout.dialog_msg, null);
		setContentView(v);

		txtMsg = (TextView) v.findViewById(R.id.txtMsg);
		TextView txtOK = (TextView) v.findViewById(R.id.txtButton);
		txtMsg.setText(message);
		txtOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
	}

	public void showMsg(String message) {
		if (txtMsg != null)
			txtMsg.setText(message);
		this.show();
	}

	public void setMessage(String message) {
		if (txtMsg != null)
			txtMsg.setText(message);
	}
}

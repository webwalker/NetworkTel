package com.zxcloud.tel.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxcloud.tel.R;

/**
 * @author xujian
 * 
 */
public class TopNavBar extends LinearLayout {
	private Activity activity;
	private ImageButton iv1, iv2;
	private TextView tv1, tv2;
	private RelativeLayout rLine;
	private OnClickListener iv1Listener, iv2Listener;

	public TopNavBar(Context context) {
		super(context);
	}

	public void init() {
		iv2.setVisibility(View.INVISIBLE);
		tv2.setVisibility(View.INVISIBLE);
		tv2.setText("");
	}

	public TopNavBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.inc_navbar, this);

		iv1 = (ImageButton) v.findViewById(R.id.nav_back);
		iv2 = (ImageButton) v.findViewById(R.id.nav_img);
		tv1 = (TextView) v.findViewById(R.id.nav_title);
		tv2 = (TextView) v.findViewById(R.id.nav_right_text);
		rLine = (RelativeLayout) v.findViewById(R.id.inc_line);

		init();

		// default event
		if (iv1Listener == null)
			iv1Listener = new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (activity != null)
						activity.finish();
				}
			};
		setIv1Listener(iv1Listener);
	}

	public void setActivity(Activity act) {
		activity = act;
	}

	public void setTv1(String title) {
		tv1.setText(title);
		tv1.setVisibility(View.VISIBLE);
	}

	public void setTv2(String tip) {
		tv2.setText(tip);
		tv2.setVisibility(View.VISIBLE);
	}

	public void showIv1(boolean visible) {
		if (visible)
			iv1.setVisibility(View.VISIBLE);
		else
			iv1.setVisibility(View.GONE);
	}

	public void showLine(boolean visible) {
		if (visible)
			rLine.setVisibility(View.VISIBLE);
		else
			rLine.setVisibility(View.GONE);
	}

	public void setIv1Listener(OnClickListener iv1Listener) {
		if (iv1 != null)
			iv1.setOnClickListener(iv1Listener);
	}

	public void setIv2Listener(OnClickListener iv2Listener) {
		if (iv2 != null)
			iv2.setOnClickListener(iv2Listener);
	}

	public void setTv1Listener(OnClickListener iv1Listener) {
		if (tv1 != null)
			tv1.setOnClickListener(iv1Listener);
	}

	public void setTv2Listener(OnClickListener iv2Listener) {
		if (tv2 != null)
			tv2.setOnClickListener(iv2Listener);
	}

	public void setIv2Image(int id) {
		if (iv2 != null) {
			iv2.setVisibility(View.VISIBLE);
			iv2.setImageResource(id);
		}
	}
}

package com.zxcloud.tel;

import java.util.ArrayList;

import webwalker.framework.utils.MessageUtil;
import webwalker.framework.utils.NetworkUtils;
import webwalker.framework.utils.UIUtils;
import webwalker.framework.utils.UriUtil;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;

import com.zxcloud.tel.activity.LoginActivity;
import com.zxcloud.tel.activity.MainActivity;

public class GuideActivity extends Activity {
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private ImageView imageView;
	private ImageView[] imageViews;
	// 包裹小圆点的LinearLayout
	private ViewGroup group;
	// 当前页码
	private int currentIndex;
	// ImageView的alpha值
	private int mAlpha = 0;
	private boolean isHide;
	private boolean isNetworkAvailable = true;
	private Class mainClass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		isNetworkAvailable = NetworkUtils.isNetworkAvailable(this);
		if (!isNetworkAvailable) {
			MessageUtil.showShortToast(this, "没有连接到服务器，只能使用部分功能！");
			mainClass = MainActivity.class;
		} else
			mainClass = LoginActivity.class;

		// 是否强制显示了引导界面
		boolean show = getIntent().getBooleanExtra("forceShow", false);
		if (!show && !UIUtils.isShowSplashUI(GuideActivity.this, mainClass)) {
			return;
		}

		// 将要显示的图片放到ArrayList当中，存到适配器中
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.guide_03, null);

		Button btn = (Button) v.findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UriUtil.startActivityNoHistory(GuideActivity.this, mainClass);
				finish();
			}
		});

		pageViews = new ArrayList<View>();
		pageViews.add(inflater.inflate(R.layout.guide_01, null));
		pageViews.add(inflater.inflate(R.layout.guide_02, null));
		pageViews.add(v);
		imageViews = new ImageView[pageViews.size()];
		group = (ViewGroup) findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.guidePages);

		// 将小圆点放到imageView数组当中
		for (int i = 0; i < pageViews.size(); i++) {
			imageView = new ImageView(GuideActivity.this);
			imageView.setLayoutParams(new LayoutParams(15, 15));
			imageView.setPadding(10, 0, 10, 0);
			imageViews[i] = imageView;

			if (i == 0) {
				// 默认选中第一张图片
				imageViews[i].setBackgroundResource(R.drawable.dot_now);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.dot);
			}

			group.addView(imageViews[i]);
		}

		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	}

	/**
	 * 设置按钮渐显效果
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1 && mAlpha < 255) {
				// 通过设置不透明度设置按钮的渐显效果
				mAlpha += 50;

				if (mAlpha > 255)
					mAlpha = 255;
				if (!isHide && mAlpha < 255)
					mHandler.sendEmptyMessageDelayed(1, 100);
			}
		}
	};

	private void showImageButtonView() {
		isHide = false;
		mHandler.sendEmptyMessage(1);
	}

	private void hideImageButtonView() {
		new Thread() {
			public void run() {
				try {
					isHide = true;
					mHandler.sendEmptyMessage(0);
				} catch (Exception e) {
				}
			}
		}.start();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_MOVE:
		case MotionEvent.ACTION_DOWN:
			showImageButtonView();
			break;
		case MotionEvent.ACTION_UP:
			hideImageButtonView();
			break;
		}

		return super.dispatchTouchEvent(ev);
	}

	// 指引页面数据适配器,实现适配器方法
	class GuidePageAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}

	// 指引页面更改事件监听器,左右滑动图片时候，小圆点变换显示当前图片位置
	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			currentIndex = arg0;
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0].setBackgroundResource(R.drawable.dot_now);

				if (arg0 != i) {
					imageViews[i].setBackgroundResource(R.drawable.dot);
				}
			}
		}
	}
}

package com.zxcloud.tel.controller;

import webwalker.framework.utils.ImageUnity;
import android.text.TextUtils;
import android.widget.ImageView;

/**
 * @author xu.jian
 * 
 */
public class ImageController extends BaseController {

	public static void loadImage(String imagePath, ImageView iv) {
		if (TextUtils.isEmpty(imagePath))
			return;
		if (imagePath.startsWith("http://")) {
			ImageUnity.getLoader().displayImage(imagePath, iv);
		}
	}

}

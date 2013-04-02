package com.foxchan.foxui.utils;

import android.os.AsyncTask;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 该类提供控件的移动效果
 * @author gulangxiangjie@gmail.com
 * @create 2013-4-2
 */
public class FoxAsynMove extends AsyncTask<Integer, Integer, Void> {
	
	/** 移动的最大距离 */
	private int maxWidth = 0;
	/** 需要移动的布局 */
	private LinearLayout layout;

	/**
	 * 构造一个控件的移动效果
	 * @param maxWidth	移动的最大距离
	 */
	public FoxAsynMove(int maxWidth, LinearLayout layout) {
		this.maxWidth = maxWidth;
		this.layout = layout;
	}

	@Override
	protected Void doInBackground(Integer... params) {
		int times = 0;
		if(maxWidth % Math.abs(params[0]) == 0){
			times = maxWidth / Math.abs(params[0]);
		} else {
			times = maxWidth / Math.abs(params[0]) + 1;
		}
		for(int i = 0; i < times; i++){
			publishProgress(params[0]);
			try {
				Thread.sleep(Math.abs(params[0]));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		RelativeLayout.LayoutParams layoutParams = 
				(RelativeLayout.LayoutParams)layout.getLayoutParams();
		//向右移动
		if(values[0] > 0){
			layoutParams.leftMargin = Math.min(layoutParams.leftMargin + values[0], 0);
		} else {//向左移动
			layoutParams.leftMargin = Math.max(layoutParams.leftMargin + values[0], 0);
		}
		layout.setLayoutParams(layoutParams);
	}
	
}

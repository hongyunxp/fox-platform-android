package com.foxchan.foxui.widget.listener;

import android.widget.LinearLayout;

/**
 * 该接口定义了控件被拖动的事件
 * @author gulangxiangjie@gmail.com
 * @create 2013-3-30
 */
public interface OnItemDraggingListener {
	
	/**
	 * 当向左拖动组件的时候触发的事件
	 * @param deltaX	控件在x轴方向上改变的位移值
	 * @param deltaY	控件在Y轴方向上改变的位移值
	 * @param maxDeltaX	控件在X轴方向上能够被拖动的最大距离
	 * @param maxDeltaY	控件在Y轴方向上能够被拖动的最大距离
	 */
	void onDraggingToLeft(int deltaX, int deltaY, int maxDeltaX, int maxDeltaY, LinearLayout layout);
	
	/**
	 * 当向右拖动组件的时候将触发的事件
	 * @param deltaX	控件在x轴方向上改变的位移值
	 * @param deltaY	控件在Y轴方向上改变的位移值
	 * @param maxDeltaX	控件在X轴方向上能够被拖动的最大距离
	 * @param maxDeltaY	控件在Y轴方向上能够被拖动的最大距离
	 */
	void onDraggingToRight(int deltaX, int deltaY, int maxDeltaX, int maxDeltaY, LinearLayout layout);
	
}

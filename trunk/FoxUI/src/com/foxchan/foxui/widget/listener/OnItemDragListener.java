package com.foxchan.foxui.widget.listener;

import android.view.View;

/**
 * 该接口定义了控件被拖动的事件
 * @author gulangxiangjie@gmail.com
 * @create 2013-3-30
 */
public interface OnItemDragListener {
	
	/**
	 * 当向左拖动组件的时候触发的事件
	 * @param view	被拖动的组件
	 */
	void onDraggingToLeft(View view);
	
	/**
	 * 当向右拖动组件的时候将触发的事件
	 * @param view	被拖动的组件
	 */
	void onDraggingToRight(View view);
	
	/**
	 * 当向左拖动组件到目标位置时将触发该事件
	 * @param view	被拖动的组件
	 */
	void onDraggedToLeft(View view);
	
	/**
	 * 当向右拖动组件到目标位置时将触发该事件
	 * @param view	被拖动的组件
	 */
	void onDraggedToRight(View view);

}

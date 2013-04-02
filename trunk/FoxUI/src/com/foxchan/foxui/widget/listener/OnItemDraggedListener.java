package com.foxchan.foxui.widget.listener;

/**
 * 该接口定义了控件被拖动的事件
 * @author gulangxiangjie@gmail.com
 * @create 2013-3-30
 */
public interface OnItemDraggedListener {
	
	/**
	 * 当向左拖动组件到目标位置时将触发该事件
	 * @param deltaX	控件在x轴方向上改变的位移值
	 * @param deltaY	控件在Y轴方向上改变的位移值
	 * @param maxDeltaX	控件在X轴方向上能够被拖动的最大距离
	 * @param maxDeltaY	控件在Y轴方向上能够被拖动的最大距离
	 * @param obj		控件上绑定的数据
	 */
	void onDraggedToLeft(int deltaX, int deltaY, int maxDeltaX, int maxDeltaY, Object obj);
	
	/**
	 * 当向右拖动组件到目标位置时将触发该事件
	 * @param deltaX	控件在x轴方向上改变的位移值
	 * @param deltaY	控件在Y轴方向上改变的位移值
	 * @param maxDeltaX	控件在X轴方向上能够被拖动的最大距离
	 * @param maxDeltaY	控件在Y轴方向上能够被拖动的最大距离
	 * @param obj		控件上绑定的数据
	 */
	void onDraggedToRight(int deltaX, int deltaY, int maxDeltaX, int maxDeltaY, Object obj);

}

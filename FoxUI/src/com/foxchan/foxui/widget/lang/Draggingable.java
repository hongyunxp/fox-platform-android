package com.foxchan.foxui.widget.lang;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.foxchan.foxui.widget.listener.OnItemDragListener;

/**
 * 该类实现了组件被拖动时的各种事件的触发条件
 * @author gulangxiangjie@gmail.com
 * @create 2013-3-30
 */
public class Draggingable {
	
	/** 组件被拖动的事件的监听器 */
	private OnItemDragListener onItemDragListener;
	/** 组件的拖动的坐标值 */
	private int startX, endX, startY, endY;
	/** 用户在纵向上的滑动的有效值的边界值，当超过了这个值之后，该次拖动将被认为是无效的 */
	private final int LIMIT_Y = 20;
	/** 用户在横向上的滑动的有效值的边界值，当小于该值时，将认为该次拖动是无效的 */
	private final int LIMIT_X = 50;
	
	/**
	 * 绑定拖动事件的监听器
	 * @param onItemDragListener
	 */
	public void setOnItemDragListener(OnItemDragListener onItemDragListener){
		this.onItemDragListener = onItemDragListener;
	}
	
	/**
	 * 控件的拖动的实现
	 * @param e
	 * @param view
	 */
	public void onDraging(MotionEvent e, View view){
		if(onItemDragListener == null){
			throw new NullPointerException("导致的原因：" + OnItemDragListener.class + "当前为NULL，请设置该监听器的值。");
		}
		switch(e.getAction()){
		case MotionEvent.ACTION_DOWN:
			startX = (int)e.getX();
			startY = (int)e.getY();
			break;
		case MotionEvent.ACTION_UP:
			endX = (int)e.getX();
			endY = (int)e.getY();
			int moveOnX = startX - endX;
			if(Math.abs(startY - endY) <= LIMIT_Y && Math.abs(moveOnX) >= LIMIT_X){
				Log.d("cqm", "用户向" + (moveOnX > 0 ? "左" : "右") + "拖动的距离是：" + Math.abs(moveOnX));
				if(moveOnX > 0){
					onItemDragListener.onDraggedToLeft(view);
				} else {
					onItemDragListener.onDraggedToRight(view);
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			
			break;
		}
	}

}

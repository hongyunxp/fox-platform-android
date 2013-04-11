package com.foxchan.foxui.widget.lang;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.foxchan.foxui.utils.FoxAsynMove;
import com.foxchan.foxui.utils.FoxAsynMove.OnTaskCompleteListener;
import com.foxchan.foxui.widget.listener.OnItemDraggedListener;
import com.foxchan.foxui.widget.listener.OnItemDraggingListener;

/**
 * 该类实现了组件被拖动时的各种事件的触发条件
 * @author gulangxiangjie@gmail.com
 * @create 2013-3-30
 */
public class Draggingable implements OnTouchListener, OnGestureListener {
	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	
	private Activity activity;
	/** 是否Measured */
	private boolean isMeasured = false;
	/** 每次自动展开/收缩的速度 */
	private final static int SPEED = 50;
	/** 手势 */
	private GestureDetector gestureDetector;
	/** 当前组件是否滚动的标志 */
	private boolean isScrolling = false;
	/** 滑块滑动距离 */
	private float scrollX;
	/** 屏幕的宽度 */
	private int windowWidth;
	/** 需要滑动的布局 */
	private LinearLayout layout;
	/** 组件被拖动的最大距离 */
	private int maxWidth;
	/** 用户松开选项后是否自动回到原来的地方的标志 */
	private boolean isAutoBack = true;
	/** 当前控件需要传递的数据 */
	private Object obj;
	
	/** 组件被拖动中的事件的监听器 */
	private OnItemDraggingListener onItemDraggingListener;
	/** 组件被拖动完毕的事件的监听器 */
	private OnItemDraggedListener onItemDraggedListener;
	/** 列表项的点击事件的监听器 */
	private OnItemClickListener onItemClickListener;
	
	public Draggingable(Activity activity, LinearLayout layout, Object obj){
		this.activity = activity;
		this.layout = layout;
		this.obj = obj;
		layout.setOnTouchListener(this);
		gestureDetector = new GestureDetector(this);
		getMaxWidth();
	}
	
	/**
	 * 获得移动距离
	 */
	public void getMaxWidth(){
		ViewTreeObserver viewTreeObserver = layout.getViewTreeObserver();
		//获取控件宽度
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			
			public boolean onPreDraw() {
				if(!isMeasured){
					windowWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
					RelativeLayout.LayoutParams layoutParams = 
							(RelativeLayout.LayoutParams)layout.getLayoutParams();
					layoutParams.width = windowWidth;
					layout.setLayoutParams(layoutParams);
					maxWidth = layout.getWidth();
					isMeasured = true;
				}
				return true;
			}
		});
	}
	
	/**
	 * 控件被拖动中的事件监听
	 * @param onItemDraggingListener
	 */
	public void setOnItemDraggingListener(
			OnItemDraggingListener onItemDraggingListener) {
		this.onItemDraggingListener = onItemDraggingListener;
	}

	/**
	 * 控件拖动完成的事件监听
	 * @param onItemDraggedListener
	 */
	public void setOnItemDraggedListener(OnItemDraggedListener onItemDraggedListener) {
		this.onItemDraggedListener = onItemDraggedListener;
	}


	/**
	 * 控件的拖动的实现
	 */
	@Override
	public boolean onTouch(View view, MotionEvent e) {
		if(onItemDraggingListener == null){
			throw new NullPointerException("导致的原因：" + OnItemDraggingListener.class + "当前为NULL，请设置该监听器的值。");
		}
		if(onItemDraggedListener == null){
			throw new NullPointerException("导致的原因：" + OnItemDraggedListener.class + "当前为NULL，请设置该监听器的值。");
		}
		if(e.getAction() == MotionEvent.ACTION_UP && isAutoBack){
			final RelativeLayout.LayoutParams layoutParams = 
					(RelativeLayout.LayoutParams)layout.getLayoutParams();
			final int deltaX = Math.abs(layoutParams.leftMargin);
			final int deltaY = Math.abs(layoutParams.topMargin);
			FoxAsynMove asynMove = null;
			//向左移动
			if(layoutParams.leftMargin >= 0){
				asynMove = new FoxAsynMove(maxWidth, layout, false);
				asynMove.setOnTaskCompleteListener(new OnTaskCompleteListener() {
					
					@Override
					public void onTaskComplete() {
						onItemDraggedListener.onDraggedToRight(deltaX, deltaY, maxWidth, 0, obj);
					}
				});
				asynMove.execute(-SPEED);
			} else {//向右移动
					asynMove = new FoxAsynMove(maxWidth, layout, true);
					asynMove.setOnTaskCompleteListener(new OnTaskCompleteListener() {
					
					@Override
					public void onTaskComplete() {
						onItemDraggedListener.onDraggedToLeft(deltaX, deltaY, maxWidth, 0, obj);
					}
				});
				asynMove.execute(SPEED);
			}
		}
		return gestureDetector.onTouchEvent(e);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		scrollX = 0;
		isScrolling = false;
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		isScrolling = true;
		if(Math.abs(distanceY) <= 200){
			scrollX += distanceX;//向左为正，向右为负
			RelativeLayout.LayoutParams layoutParams = 
					(RelativeLayout.LayoutParams)layout.getLayoutParams();
			layoutParams.leftMargin -= scrollX;
			if(layoutParams.leftMargin >= maxWidth){
				isScrolling = false;//拖过了就不需要再执行Move了
				layoutParams.leftMargin = maxWidth;
			} else if(layoutParams.leftMargin <= -maxWidth){
				isScrolling = false;
				layoutParams.leftMargin = -maxWidth;
			}
			layout.setLayoutParams(layoutParams);
			if(layoutParams.leftMargin >= 0){//向右拖动
				onItemDraggingListener.onDraggingToRight(Math.abs(layoutParams.leftMargin), 
						Math.abs(layoutParams.topMargin), maxWidth, 0, layout);
			} else {//向左拖动
				onItemDraggingListener.onDraggingToLeft(Math.abs(layoutParams.leftMargin), 
						Math.abs(layoutParams.topMargin), maxWidth, 0, layout);
			}
		}
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		if(onItemClickListener != null){
			onItemClickListener.onItemClick(obj);
		}
		return true;
	}
	
	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public interface OnItemClickListener{
		void onItemClick(Object obj);
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public boolean isAutoBack() {
		return isAutoBack;
	}

	public void setAutoBack(boolean isAutoBack) {
		this.isAutoBack = isAutoBack;
	}

}

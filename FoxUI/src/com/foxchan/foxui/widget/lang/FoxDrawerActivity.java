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

/**
 * 支持左右滑动的抽屉效果的控件
 * @author gulangxiangjie@gmail.com
 * @create 2013-4-10
 */
public abstract class FoxDrawerActivity extends Activity implements
		OnTouchListener, OnGestureListener {
	
	//打印调试信息的标签
	public String TAG = "FoxUI";
	/** 支持的滑动方向：向左 */
	protected static final int TO_LEFT = 0x00;
	/** 支持的滑动方向：向右 */
	protected static final int TO_RIGHT = 0x01;
	/** 支持的滑动方向：双向 */
	protected static final int TO_BOTH = 0x03;
	
	/** 左边的布局是否Measured */
	protected boolean hasLeftMeasured = false;
	/** 右边的布局是否Measured */
	protected boolean hasRightMeasured = false;
	/** 当主界面向右滑动时显示的界面的布局 */
	protected LinearLayout llLeft;
	/** 当主界面向左滑动时显示的界面的布局 */
	protected LinearLayout llRight;
	/** 被滑动的主界面的布局 */
	protected LinearLayout llMiddle;
	
	/** 每次向右自动展开/收缩的范围 */
	protected int maxWidthLeft = 0;
	/** 每次向左自动展开/收缩的范围 */
	protected int maxWidthRight = 0;
	/** 每次自动展开/收缩的速度 */
	protected static final int SPEED = 30;
	
	private GestureDetector gd;
	private boolean isScrolling = false;
	private float mScrollX;//滑块滑动的距离
	private int windowWidth;//屏幕的宽度
	
	protected void initFoxDrawer(){
		llMiddle = (LinearLayout)findViewById(getMiddleLayoutResource());
		if(getScrollMode() == TO_BOTH || getScrollMode() == TO_RIGHT){
			llLeft = (LinearLayout)findViewById(getLeftLayoutResource());
		}
		if(getScrollMode() == TO_BOTH || getScrollMode() == TO_LEFT){
			llRight = (LinearLayout)findViewById(getRightLayoutResource());
		}
		llMiddle.setOnTouchListener(this);
		gd = new GestureDetector(this);
		gd.setIsLongpressEnabled(false);
		getMaxWidth();
	}

	/**
	 * 获取移动的距离，移动的距离就是llLeft或者llRight的宽度
	 */
	private void getMaxWidth() {
		if(getScrollMode() == TO_BOTH || getScrollMode() == TO_RIGHT){
			ViewTreeObserver leftObserver = llLeft.getViewTreeObserver();
			//获取控件的宽度
			leftObserver.addOnPreDrawListener(new OnPreDrawListener() {
				
				@Override
				public boolean onPreDraw() {
					if(!hasLeftMeasured){
						windowWidth = getWindowManager().getDefaultDisplay().getWidth();
						RelativeLayout.LayoutParams layoutParams = 
								(RelativeLayout.LayoutParams)llMiddle.getLayoutParams();
						layoutParams.width = windowWidth;
						llMiddle.setLayoutParams(layoutParams);
						maxWidthLeft = llLeft.getWidth();
						hasLeftMeasured = true;
					}
					return true;
				}
			});
		}
		if(getScrollMode() == TO_BOTH || getScrollMode() == TO_LEFT){
			ViewTreeObserver rightObserver = llRight.getViewTreeObserver();
			rightObserver.addOnPreDrawListener(new OnPreDrawListener() {
				
				@Override
				public boolean onPreDraw() {
					if(!hasRightMeasured){
						windowWidth = getWindowManager().getDefaultDisplay().getWidth();
						RelativeLayout.LayoutParams layoutParams = 
								(RelativeLayout.LayoutParams)llMiddle.getLayoutParams();
						layoutParams.width = windowWidth;
						llMiddle.setLayoutParams(layoutParams);
						maxWidthRight = llRight.getWidth();
						hasRightMeasured = true;
					}
					return true;
				}
			});
		}
	}
	
	@Override
	public boolean onDown(MotionEvent ev) {
		mScrollX = 0;
		isScrolling = false;
		if(getScrollMode() == TO_BOTH){
			if(ev.getX() <= windowWidth / 2){
				llLeft.setVisibility(View.VISIBLE);
				llRight.setVisibility(View.GONE);
			} else {
				llLeft.setVisibility(View.GONE);
				llRight.setVisibility(View.VISIBLE);
			}
		}
		return true;
	}

	@Override
	public boolean onFling(MotionEvent ev1, MotionEvent ev2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent ev) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * distanceX>0，说明用户在向右滑动
	 */
	@Override
	public boolean onScroll(MotionEvent ev1, MotionEvent ev2, float distanceX,
			float distanceY) {
		isScrolling = true;
		mScrollX += distanceX;
		RelativeLayout.LayoutParams layoutParams = 
				(RelativeLayout.LayoutParams)llMiddle.getLayoutParams();
		layoutParams.leftMargin -= mScrollX;
		if(getScrollMode() == TO_LEFT){
			if(layoutParams.leftMargin >= 0){
				isScrolling = false;
				layoutParams.leftMargin = 0;
			} else if(layoutParams.leftMargin <= -maxWidthRight){
				isScrolling = false;
				layoutParams.leftMargin = -maxWidthRight;
			}
		} else if(getScrollMode() == TO_RIGHT){
			if(layoutParams.leftMargin <= 0){
				isScrolling = false;
				layoutParams.leftMargin = 0;
			} else if(layoutParams.leftMargin >= maxWidthLeft){
				isScrolling = false;
				layoutParams.leftMargin = maxWidthLeft;
			}
		}
		llMiddle.setLayoutParams(layoutParams);
		return false;
	}

	@Override
	public void onShowPress(MotionEvent ev) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent ev) {
		RelativeLayout.LayoutParams layoutParams  = 
				(RelativeLayout.LayoutParams)llMiddle.getLayoutParams();
		if(ev.getX() < windowWidth / 2){
			if(layoutParams.leftMargin == 0){//向右展开
				if(getScrollMode() == TO_LEFT) return false;
				new FoxAsynMove(maxWidthLeft, llMiddle, false).execute(SPEED);
			} else {//向左收拢
				new FoxAsynMove(maxWidthLeft, llMiddle, false).execute(-SPEED);
			}
		} else {
			if(layoutParams.leftMargin == 0){//向左展开
				if(getScrollMode() == TO_RIGHT) return false;
				new FoxAsynMove(maxWidthRight, llMiddle, true).execute(-SPEED);
			} else {//向右收拢
				new FoxAsynMove(maxWidthRight, llMiddle, true).execute(SPEED);
			}
		}
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent e) {
		//松开的时候要判断，如果不到屏幕的一半则要缩回原位
		if(MotionEvent.ACTION_UP == e.getAction() && isScrolling == true){
			RelativeLayout.LayoutParams layoutParams = 
					(RelativeLayout.LayoutParams)llMiddle.getLayoutParams();
			if(getScrollMode() == TO_LEFT){
				if(layoutParams.leftMargin < -windowWidth / 2){
					new FoxAsynMove(maxWidthRight, llMiddle, true).execute(-SPEED);
				} else if(layoutParams.leftMargin < 0){
					new FoxAsynMove(maxWidthRight, llMiddle, true).execute(SPEED);
				}
			} else if(getScrollMode() == TO_RIGHT){
				if(layoutParams.leftMargin > windowWidth / 2){
					new FoxAsynMove(maxWidthLeft, llMiddle, false).execute(SPEED);
				} else if(layoutParams.leftMargin > 0){
					new FoxAsynMove(maxWidthLeft, llMiddle, false).execute(-SPEED);
				}
			} else if(getScrollMode() == TO_BOTH){
				if(llLeft.getVisibility() == View.VISIBLE){
					if(layoutParams.leftMargin > windowWidth / 2){
						new FoxAsynMove(maxWidthLeft, llMiddle, false).execute(SPEED);
					} else if(layoutParams.leftMargin > 0){
						new FoxAsynMove(maxWidthLeft, llMiddle, false).execute(-SPEED);
					}
				}
				if(llRight.getVisibility() == View.VISIBLE){
					if(layoutParams.leftMargin < -windowWidth / 2){
						new FoxAsynMove(maxWidthRight, llMiddle, true).execute(-SPEED);
					} else if(layoutParams.leftMargin < 0){
						new FoxAsynMove(maxWidthRight, llMiddle, true).execute(SPEED);
					}
				}
			}
		}
		return gd.onTouchEvent(e);
	}
	
	/**
	 * 获得界面支持的滑动方向
	 * @return	返回界面支持的滑动的方向
	 */
	protected abstract int getScrollMode();
	
	/**
	 * 获得左边布局的资源id号
	 * @return	返回左边布局的资源id号
	 */
	protected abstract int getLeftLayoutResource();
	
	/**
	 * 获得中间布局的资源id号
	 * @return	返回中间布局的资源id号
	 */
	protected abstract int getMiddleLayoutResource();
	
	/**
	 * 获得右边布局的资源id号
	 * @return	返回右边布局的资源id号
	 */
	protected abstract int getRightLayoutResource();

}

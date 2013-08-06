package com.foxchan.foxui.widget.lang;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

/**
 * 具有卡片切换效果的控件
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013年8月6日
 */
public class CardsSwitcher extends RelativeLayout implements OnTouchListener, OnGestureListener {
	
	public static final String TAG = "CardsSwitcher";
	private Context context;
	/** 前一个界面 */
	private View prevView;
	/** 当前在最前面的界面 */
	private View currView;
	/** 后一个界面（即将显示的界面） */
	private View nextView;
	
	/** 是否是第一次加载的标志 */
	private boolean isFirst = true;
	/** 当前显示界面在横坐标上的位置 */
	private int currViewX;
	
	/** 第一个位移动画 */
	private TranslateAnimation firstAnimation;
	/** 第二个位移动画 */
	private TranslateAnimation secondAnimation;
	
	public CardsSwitcher(Context context) {
		super(context);
		init(context);
	}
	
	public CardsSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public CardsSwitcher(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	/**
	 * 初始化控件
	 * @param context
	 */
	private void init(Context context){
		this.context = context;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(isFirst){
			isFirst = false;
			int count = getChildCount();
			if(count < 2){
				refreshViews(null, getChildAt(0), null);
			} else {
				refreshViews(null, getChildAt(count - 1), getChildAt(count - 2));
			}
			firstAnimation = new TranslateAnimation(0, currView.getMeasuredWidth(), currView.getTop(), currView.getTop());
			firstAnimation.setDuration(2000);
			secondAnimation = new TranslateAnimation(0, -currView.getMeasuredWidth(), currView.getTop(), currView.getTop());
			secondAnimation.setDuration(3000);
			firstAnimation.setAnimationListener(new Animation.AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					secondAnimation.start();
				}
			});
			secondAnimation.setAnimationListener(new Animation.AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					firstAnimation.start();
				}
			});
			currView.startAnimation(firstAnimation);
		}
	}
	
	/**
	 * 更新控件中的三个界面
	 * @param prevView	上一个界面
	 * @param currView	正在显示的界面
	 * @param nextView	下一个要显示的界面
	 */
	private void refreshViews(View prevView, View currView, View nextView){
		this.prevView = prevView;
		this.currView = currView;
		this.nextView = nextView;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 交换界面的线程类
	 * @author foxchan@live.cn
	 * @version 1.0.0
	 * @create 2013年8月6日
	 */
	private class ViewSwaper implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}

}

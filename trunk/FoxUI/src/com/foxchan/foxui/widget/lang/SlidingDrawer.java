package com.foxchan.foxui.widget.lang;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

/**
 * 支持左右滑动的抽屉控件
 * @create 2013年8月21日
 * @author foxchan@live.cn
 * @version 1.0.0
 */
public class SlidingDrawer extends RelativeLayout implements OnTouchListener, OnGestureListener {
	
	public static final String TAG = "FoxUI-SlidingDrawer";
	
	/** 支持的滑动方向：从左边 */
	public static final int FROM_LEFT = 0;
	/** 支持的滑动方向：从右边 */
	public static final int FROM_RIGHT = 1;
	/** 支持的滑动方向：双向 */
	public static final int FROM_BOTH = 2;
	
	/** 滑动的方式：覆盖 */
	public static final int SCROLL_COVER = 0;
	/** 滑动的方式：推动 */
	public static final int SCROLL_PUSH = 1;
	
	/** 中间界面的回弹时间 */
	private static final int DURATION_MIDDLE_BACK = 300;
	/** 中间界面的回弹距离 */
	private static final int DISTANCE_MIDDLE_BACK = 100;
	
	private GestureDetector gd;
	
	/** 左边的界面 */
	private View vLeft;
	/** 左边界面的宽度 */
	private int mWidthLeft;
	/** 左边控件完全滑出的时间，单位：毫秒/mm */
	private int mDurationLeft = 350;
	/** 左边的界面是否经过测量的标志 */
	private boolean isLeftMeasured;
	/** 右边的界面 */
	private View vRight;
	/** 右边界面的宽度 */
	private int mWidthRight;
	/** 右边控件完全滑出的时间，单位：毫秒/mm */
	private int mDurationRight = 350;
	/** 右边的界面是否经过测量的标志 */
	private boolean isRightMeasured;
	/** 中间的界面 */
	private View vMiddle;
	/** 中间的界面的宽度 */
	private int mWidthMiddle;
	/** 中间的界面是否经过测量的标志 */
	private boolean isMiddleMeasured;
	/** 控件支持的滑动方向 */
	private int slidingDirection = FROM_BOTH;
	/** 控件滑动时的方式，有覆盖和推动两种方式 */
	private int scrollType = SCROLL_COVER;
	/** 点击空白处是否进行回弹的标志 */
	private boolean isTapBackEnable = false;
	
	/** 左边控件出现的位移动画 */
	private TranslateAnimation animLeftIn;
	/** 左边控件消失的位移动画 */
	private TranslateAnimation animLeftOut;
	
	/** 中间控件伴随左边出现的位移动画 */
	private TranslateAnimation animMiddleLeftIn;
	/** 中间控件伴随左边消失的位移动画 */
	private TranslateAnimation animMiddleLeftOut;
	/** 中间控件伴随右边出现的位移动画 */
	private TranslateAnimation animMiddleRightIn;
	/** 中间控件伴随右边消失的位移动画 */
	private TranslateAnimation animMiddleRightOut;
	
	/** 右边控件出现的位移动画 */
	private TranslateAnimation animRightIn;
	/** 右边控件消失的位移动画 */
	private TranslateAnimation animRightOut;

	public SlidingDrawer(Context context) {
		super(context);
		init();
	}
	
	public SlidingDrawer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SlidingDrawer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	/**
	 * 初始化控件
	 * @create 2013年8月21日
	 * @modify 2013年8月21日
	 * @author foxchan@live.cn
	 */
	private void init(){
		gd = new GestureDetector(this);
		scrollType = SCROLL_PUSH;
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		initChildrenPosition();
		if(!isMiddleMeasured){
			if(slidingDirection == FROM_LEFT || slidingDirection == FROM_BOTH){
				vMiddle = getChildAt(1);
			} else if(slidingDirection == FROM_RIGHT){
				vMiddle = getChildAt(0);
			}
			mWidthMiddle = vMiddle.getWidth();
			isMiddleMeasured = true;
			vMiddle.setVisibility(View.VISIBLE);
			vMiddle.setOnTouchListener(this);
			vMiddle.setFocusable(true);
			vMiddle.setFocusableInTouchMode(true);
		}
		if((slidingDirection == FROM_BOTH || slidingDirection == FROM_LEFT) && !isLeftMeasured){
			vLeft = getChildAt(0);
			if(vLeft != null){
				vLeft.setFocusable(true);
				vLeft.setFocusableInTouchMode(true);
				mWidthLeft = vLeft.getWidth();
				isLeftMeasured = true;
				vLeft.setVisibility(View.GONE);
				//初始化位移动画
				animLeftIn = createAnimation(0 - mWidthLeft, 0, vLeft.getTop(), vLeft.getTop(), mDurationLeft);
				animLeftIn.setAnimationListener(new Animation.AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						vMiddle.clearFocus();
						vLeft.requestFocus();
					}
				});
				animLeftOut = createAnimation(0, 0 - mWidthLeft, vLeft.getTop(), vLeft.getTop(), mDurationLeft);
				animLeftOut.setAnimationListener(new Animation.AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						vLeft.clearFocus();
						vLeft.setVisibility(View.GONE);
						initChildrenPosition();
						vMiddle.requestFocus();
					}
				});
				//初始化中间的位移动画
				if(scrollType == SCROLL_PUSH){
					animMiddleLeftIn = createAnimation(0, mWidthLeft, vMiddle.getTop(), vMiddle.getTop(), mDurationLeft);
					animMiddleLeftOut = createAnimation(mWidthLeft, 0, vMiddle.getTop(), vMiddle.getTop(), mDurationLeft);
				}
			}
		}
		if((slidingDirection == FROM_BOTH || slidingDirection == FROM_RIGHT) && !isRightMeasured){
			if(slidingDirection == FROM_BOTH){
				vRight = getChildAt(2);
			} else if(slidingDirection == FROM_RIGHT){
				vRight = getChildAt(1);
			}
			if(vRight != null) {
				vRight.setFocusable(true);
				vRight.setFocusableInTouchMode(true);
				mWidthRight = vRight.getWidth();
				vRight.setVisibility(View.GONE);
				isRightMeasured = true;
				RelativeLayout.LayoutParams p = (LayoutParams)vRight.getLayoutParams();
				int toX = mWidthMiddle - mWidthRight - vRight.getLeft();
				//初始化位移动画
				animRightIn = createAnimation(mWidthMiddle, toX, 
						vRight.getTop(), vRight.getTop(), mDurationRight);
				animRightIn.setAnimationListener(new Animation.AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						vMiddle.clearFocus();
						vRight.requestFocus();
					}
				});
				animRightOut = createAnimation(toX, mWidthMiddle, 
						vRight.getTop(), vRight.getTop(), mDurationRight);
				animRightOut.setAnimationListener(new Animation.AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						vRight.clearFocus();
						vRight.setVisibility(View.GONE);
						initChildrenPosition();
						vMiddle.requestFocus();
					}
				});
				//初始化中间的位移动画
				if(scrollType == SCROLL_PUSH){
					animMiddleRightIn = createAnimation(0, 0 - mWidthRight, vMiddle.getTop(), vMiddle.getTop(), mDurationRight);
					animMiddleRightOut = createAnimation(0 - mWidthRight, 0, vMiddle.getTop(), vMiddle.getTop(), mDurationRight);
				}
			}
		}
		//初始化中间界面的回弹动画
		if(slidingDirection == FROM_LEFT){
			animMiddleRightIn = createAnimation(0, 0 - DISTANCE_MIDDLE_BACK, vMiddle.getTop(), vMiddle.getTop(), DURATION_MIDDLE_BACK);
			animMiddleRightOut = createAnimation(0 - DISTANCE_MIDDLE_BACK, 0, vMiddle.getTop(), vMiddle.getTop(), DURATION_MIDDLE_BACK);
			animMiddleRightIn.setAnimationListener(new Animation.AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					vMiddle.startAnimation(animMiddleRightOut);
				}
			});
		} else if(slidingDirection == FROM_RIGHT){
			animMiddleLeftIn = createAnimation(0, DISTANCE_MIDDLE_BACK, vMiddle.getTop(), 
					vMiddle.getTop(), DURATION_MIDDLE_BACK);
			animMiddleLeftOut = createAnimation(DISTANCE_MIDDLE_BACK, 0, vMiddle.getTop(), 
					vMiddle.getTop(), DURATION_MIDDLE_BACK);
			animMiddleLeftIn.setAnimationListener(new Animation.AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					vMiddle.startAnimation(animMiddleLeftOut);
				}
			});
		}
	}
	
	/**
	 * 获得一个位移动画
	 * @param animation	需要被处理的动画对象
	 * @param fromX		动画开始的横坐标
	 * @param toX		动画结束的横坐标
	 * @param fromY		动画开始的纵坐标
	 * @param toY		动画结束的纵坐标
	 * @return			返回动画对象
	 * @create 2013年8月21日
	 * @modify 2013年8月21日
	 * @author foxchan@live.cn
	 */
	private TranslateAnimation createAnimation(int fromX, int toX, int fromY, int toY, int duration){
		TranslateAnimation animation = new TranslateAnimation(fromX, toX, fromY, toY);
		animation.setDuration(duration);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		animation.setFillAfter(true);
		return animation;
	}
	
	/**
	 * 初始化所有子控件的排序
	 * @create 2013年8月21日
	 * @modify 2013年8月21日
	 * @author foxchan@live.cn
	 */
	private void initChildrenPosition(){
		if(vRight != null) vRight.bringToFront();
		if(vLeft != null) vLeft.bringToFront();
		if(vMiddle != null) vMiddle.bringToFront();
	}
	
	/**
	 * 显示左边的界面
	 * @create 2013年8月21日
	 * @modify 2013年8月21日
	 * @author foxchan@live.cn
	 */
	public void showLeftView(){
		vLeft.setVisibility(View.VISIBLE);
		vLeft.bringToFront();
		vLeft.startAnimation(animLeftIn);
		if(scrollType == SCROLL_PUSH){
			vMiddle.startAnimation(animMiddleLeftIn);
		}
	}
	
	/**
	 * 隐藏左边的界面
	 * @create 2013年8月21日
	 * @modify 2013年8月21日
	 * @author foxchan@live.cn
	 */
	public void hideLeftView(){
		vLeft.startAnimation(animLeftOut);
		if(scrollType == SCROLL_PUSH){
			vMiddle.startAnimation(animMiddleLeftOut);
		}
	}
	
	/**
	 * 显示右边的界面
	 * @create 2013年8月21日
	 * @modify 2013年8月21日
	 * @author foxchan@live.cn
	 */
	public void showRightView(){
		vRight.setVisibility(View.VISIBLE);
		vRight.bringToFront();
		vRight.startAnimation(animRightIn);
		if(scrollType == SCROLL_PUSH){
			vMiddle.startAnimation(animMiddleRightIn);
		}
	}
	
	/**
	 * 隐藏右边的界面
	 * @create 2013年8月21日
	 * @modify 2013年8月21日
	 * @author foxchan@live.cn
	 */
	public void hideRightView(){
		vRight.startAnimation(animRightOut);
		if(scrollType == SCROLL_PUSH){
			vMiddle.startAnimation(animMiddleRightOut);
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		if(!isTapBackEnable) return false;
		if(vLeft != null && vLeft.getVisibility() == View.VISIBLE){
			hideLeftView();
		}
		if(vRight != null && vRight.getVisibility() == View.VISIBLE){
			hideRightView();
		}
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		float x1 = e1.getX();
		float x2 = e2.getX();
		float y1 = e1.getY();
		float y2 = e2.getY();
		if(x2 - x1 > 50 && Math.abs(y1 - y2) < 50){
			//向右滑动
			if(slidingDirection == FROM_LEFT){
				if(vLeft != null && vLeft.getVisibility() == View.GONE){
					//滑出左边的界面
					showLeftView();
				}
			} else if(slidingDirection == FROM_RIGHT){
				if(vRight != null && vRight.getVisibility() == View.GONE){
					vMiddle.startAnimation(animMiddleLeftIn);
				} else if(vRight != null && vRight.getVisibility() == View.VISIBLE){
					//将右边的界面滑出
					hideRightView();
				}
			} else if(slidingDirection == FROM_BOTH){
				if(vLeft != null && vLeft.getVisibility() == View.GONE 
						&& vRight != null && vRight.getVisibility() == View.GONE){
					//滑出左边的界面
					showLeftView();
				} else if(vRight.getVisibility() == View.VISIBLE){
					//将右边的界面滑出
					hideRightView();
				}
			}
		} else if(x2 - x1 < -50 && Math.abs(y1 - y2) < 50){
			//向左滑动
			switch(slidingDirection){
			case FROM_LEFT:
				if(vLeft != null && vLeft.getVisibility() == View.VISIBLE){
					//将左边的界面滑出
					hideLeftView();
				} else if(vLeft != null && vLeft.getVisibility() == View.GONE){
					vMiddle.startAnimation(animMiddleRightIn);
				}
				break;
			case FROM_RIGHT:
				if(vRight != null && vRight.getVisibility() == View.GONE){
					Log.d(TAG, "滑出右边的界面");
					//滑出右边的界面
					showRightView();
				}
				break;
			case FROM_BOTH:
				if(vLeft != null && vLeft.getVisibility() == View.GONE 
					&& vRight != null && vRight.getVisibility() == View.GONE){
					//滑出右边的界面
					showRightView();
				} else if(vLeft.getVisibility() == View.VISIBLE){
					//将左边的界面滑出
					hideLeftView();
				}
				break;
			}
		}
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return gd.onTouchEvent(event);
	}

	/**
	 * 设置左边界面滑出的时间，单位：毫秒
	 * @create 2013年8月21日
	 * @modify 2013年8月21日
	 * @author foxchan@live.cn
	 */
	public void setDurationLeft(int duration) {
		this.mDurationLeft = duration;
	}

	/**
	 * 设置右边界面滑出的时间，单位：毫秒
	 * @create 2013年8月21日
	 * @modify 2013年8月21日
	 * @author foxchan@live.cn
	 */
	public void setDurationRight(int duration) {
		this.mDurationRight = duration;
	}

	/**
	 * 设置控件支持的滑动方向，有三个值可取
	 * <ul>
	 * <li>TO_LEFT:支持左边滑出</li>
	 * <li>TO_RIGHT:支持右边滑出</li>
	 * <li>TO_BOTH:支持两侧滑出</li>
	 * </ul>
	 * @create 2013年8月21日
	 * @modify 2013年8月21日
	 * @author foxchan@live.cn
	 */
	public void setSlidingDirection(int slidingDirection) {
		this.slidingDirection = slidingDirection;
	}

	/**
	 * 设置界面的滑出模式，有SCROLL_COVER和SCROLL_PUSH两种模式
	 * @create 2013年8月21日
	 * @modify 2013年8月21日
	 * @author foxchan@live.cn
	 */
	public void setScrollType(int scrollType) {
		this.scrollType = scrollType;
	}

	/**
	 * 设置是否允许点击界面完成界面的回弹
	 * @param isTapBackEnable	如果允许点击界面完成界面的回弹则传入true，否则传入false
	 * @create 2013年8月21日
	 * @modify 2013年8月21日
	 * @author foxchan@live.cn
	 */
	public void setTapBackEnable(boolean isTapBackEnable) {
		this.isTapBackEnable = isTapBackEnable;
	}
	
}

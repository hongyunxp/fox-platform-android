package com.foxchan.foxui.widget.lang;

import java.util.Random;

import com.foxchan.foxui.core.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

/**
 * 具有卡片切换效果的控件，向右为将第一张卡片切换到最后，向左为将最后一张卡片切换到最上面
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013年8月6日
 */
public class CardsSwitcher extends RelativeLayout implements OnTouchListener, OnGestureListener {
	
	public static final String TAG = "CardsSwitcher";
	/** 切换到最前面 */
	private static final int TO_FRONT = 0;
	/** 切换到最后面 */
	private static final int TO_BEHIND = 1;
	
	private Context context;
	/** 前一个界面 */
	private View prevView;
	/** 当前在最前面的界面 */
	private View currView;
	/** 后一个界面（即将显示的界面） */
	private View nextView;
	
	/** 是否是第一次加载的标志 */
	private boolean isFirst = true;
	/** 当前显示在最前面的界面的索引号 */
	private int currentItemPosition = 0;
	/** 当前绑定的数据的总数量 */
	private int itemCount;
	/** 界面组件集合 */
	private SparseArray<View> viewMap = new SparseArray<View>();
	
	/** 第一个位移动画 */
	private TranslateAnimation firstAnimation;
	/** 第二个位移动画 */
	private TranslateAnimation secondAnimation;
	/** 旋转界面的动画 */
	private RotateAnimation rotateAnimation;
	/** 手势侦测器 */
	private GestureDetector gd;
	/** 是否还有下一个元素的标志 */
	private boolean hasNext = true;
	/** 数据适配器 */
	private BaseAdapter baseAdapter;
	
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
		gd = new GestureDetector(context, this);
		setOnTouchListener(this);
		setChildrenDrawingOrderEnabled(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private void measureChildWidth(View child, int l, int t, int r, int b){
		LayoutParams p = (LayoutParams)child.getLayoutParams();
		if(p == null){
			p = (RelativeLayout.LayoutParams)generateDefaultLayoutParams();
			child.setLayoutParams(p);
		}
		
		if(p.width == RelativeLayout.LayoutParams.WRAP_CONTENT){
			p.width = 800;
			p.height = 480;
			p.leftMargin = 50;
			p.topMargin = 50;
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(isFirst){
			isFirst = false;
			int count = getChildCount();
			if(count <= 0) {
				isFirst = true;
				return;
			}
			currentItemPosition = 0;
			if(count < 2){
				refreshViews(null, getChildAt(0), null);
			} else if(count < 3) {
				refreshViews(getChildAt(count - 2), getChildAt(count - 1), getChildAt(count - 2));
			} else {
				refreshViews(getChildAt(0), getChildAt(count - 1), getChildAt(count - 2));
			}
			//重置子控件的大小
			for(int i = 0; i < count; i++){
				final View child = getChildAt(i);
				measureChildWidth(child, l, count, r, b);
			}
			//旋转界面
			if(count <= 2){
				viewRotate(nextView, 10);
			} else if(count <= 5) {
				for(int i = count - 1; i >= 0; i--){
					viewRotate(getChildAt(i), 10);
				}
			} else {
				for(int i = count - 2; i >= count - 6; i--){
					viewRotate(getChildAt(i), 10);
				}
			}
		}
		
	}
	
	/**
	 * 构建界面的第一个平移动画
	 * @param fromXDelta	开始移动的位置
	 * @param toXDelta		最终移动的位置
	 * @param duration		动画持续的时间
	 * @param cardPosition	被移动的卡片的最终位置
	 * @create 2013年8月7日
	 * @modify 2013年8月7日
	 * @author foxchan@live.cn
	 */
	private void buildFirstTranslateAnimation(int fromXDelta, int toXDelta, int duration, final int cardPosition){
		firstAnimation = new TranslateAnimation(fromXDelta, toXDelta, 0, 0);
		firstAnimation.setDuration(duration);
		firstAnimation.setInterpolator(new AccelerateInterpolator(1));
		firstAnimation.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				//将当前的视图放到控件的最后面
				if(hasNext && cardPosition == TO_BEHIND){
					for(int i = 0; i < getChildCount() - 1; i++){
						getChildAt(0).bringToFront();
					}
					refreshViews(getChildAt(0), getChildAt(getChildCount() - 1), getChildAt(getChildCount() - 2));
					viewReverseRotate(currView);
					prevView.startAnimation(secondAnimation);
				} else if(hasNext && cardPosition == TO_FRONT){
					viewReverseRotate(prevView);
					prevView.bringToFront();
					refreshViews(getChildAt(0), getChildAt(getChildCount() - 1), getChildAt(getChildCount() - 2));
					currView.startAnimation(secondAnimation);
				}
			}
		});
	}

	/**
	 * 构建界面的第二个平移动画
	 * @param fromXDelta	平移开始的位置
	 * @param duration		动画持续的时间
	 * @param cardPosition	被移动的卡片的最终位置
	 * @create 2013年8月7日
	 * @modify 2013年8月7日
	 * @author foxchan@live.cn
	 */
	private void buildSecondTranslateAnimation(int fromXDelta, int duration, final int cardPosition){
		secondAnimation = new TranslateAnimation(fromXDelta, 0, 0, 0);
		secondAnimation.setDuration(duration);
		secondAnimation.setInterpolator(new AccelerateInterpolator(1));
		secondAnimation.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				if(getChildCount() >= 5){
					if(hasNext && cardPosition == TO_BEHIND){
						viewRotate(prevView, 10);
					} else if(hasNext && cardPosition == TO_FRONT){
						viewRotate(nextView, 10);
					}
				}
			}
		});
	}
	
	/**
	 * 生成组件的旋转动画
	 * @param target	需要被旋转的视图对象
	 * @param duration	动画持续的时间
	 * @create 2013年8月7日
	 * @modify 2013年8月7日
	 * @author foxchan@live.cn
	 */
	private void viewRotate(final View target, int duration){
		Random random = new Random();
		boolean isPositive = true;
		if(random.nextInt(100) % 2 == 0){
			isPositive = false;
		}
		int angle = random.nextInt(5) + 1;
		if(!isPositive){
			angle = 0 - angle;
		}
		target.setTag(R.id.id_cards_switcher_angle, angle);
		int pivotX = (target.getLeft() + target.getMeasuredWidth()) / 2;
		int pivotY = (target.getTop() + target.getMeasuredHeight()) / 2;
		rotateAnimation = new RotateAnimation(0, angle, pivotX, pivotY);
		rotateAnimation.setDuration(duration);
		rotateAnimation.setFillAfter(true);
		target.startAnimation(rotateAnimation);
	}
	
	/**
	 * 恢复界面的旋转角度
	 * @param target	需要被旋转的视图对象
	 * @create 2013年8月7日
	 * @modify 2013年8月7日
	 * @author foxchan@live.cn
	 */
	private void viewReverseRotate(final View target){
		int angle = target.getTag(R.id.id_cards_switcher_angle) == null ? 
				0 : (Integer)target.getTag(R.id.id_cards_switcher_angle);
		int pivotX = (target.getLeft() + target.getMeasuredWidth()) / 2;
		int pivotY = (target.getTop() + target.getMeasuredHeight()) / 2;
		rotateAnimation = new RotateAnimation(angle, 0, pivotX, pivotY);
		rotateAnimation.setDuration(200);
		rotateAnimation.setFillAfter(true);
		target.startAnimation(rotateAnimation);
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
		/*if(hasNext){
			currView.startAnimation(firstAnimation);
		} else {
			int toXDelta = 200;
			buildFirstTranslateAnimation(0, toXDelta, 500);
			buildSecondTranslateAnimation(toXDelta, 500);
			currView.startAnimation(firstAnimation);
		}*/
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
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
		int x1 = (int)e1.getX();
		int x2 = (int)e2.getX();
		int y1 = (int)e1.getY();
		int y2 = (int)e2.getY();
		int deltaX = x2 - x1;
		int deltaY = y2 - y1;
		int absDeltaX = Math.abs(deltaX);
		int absDeltaY = Math.abs(deltaY);
		if(deltaX > 0 && absDeltaX > 100 && absDeltaY < 50){//向右
			buildFirstTranslateAnimation(0, currView.getMeasuredWidth(), 300, TO_BEHIND);
			buildSecondTranslateAnimation(currView.getMeasuredWidth(), 300, TO_BEHIND);
			currView.startAnimation(firstAnimation);
			
			nextItemIndex();
			nextView = getViewFromMap(currentItemPosition % 5);
			nextView.postInvalidate();
		}
		if(deltaX <= 0 && absDeltaX > 100 && absDeltaY < 50){//向左
			buildFirstTranslateAnimation(0, -prevView.getMeasuredWidth(), 300, TO_FRONT);
			buildSecondTranslateAnimation(-prevView.getMeasuredWidth(), 300, TO_FRONT);
			prevView.startAnimation(firstAnimation);
			
			prevItemIndex();
			prevView = getViewFromMap(currentItemPosition % 5);
			prevView.postInvalidate();
		}
		return false;
	}
	
	/**
	 * 选中下一条数据
	 * @create 2013年8月8日
	 * @modify 2013年8月8日
	 * @author foxchan@live.cn
	 */
	private void nextItemIndex(){
		if(currentItemPosition + 1 < itemCount){
			currentItemPosition++;
		} else {
			currentItemPosition = 0;
		}
	}
	
	/**
	 * 选中上一条数据
	 * @create 2013年8月8日
	 * @modify 2013年8月8日
	 * @author foxchan@live.cn
	 */
	private void prevItemIndex(){
		if(currentItemPosition - 1 >= 0){
			currentItemPosition--;
		} else {
			currentItemPosition = itemCount - 1;
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		return gd.onTouchEvent(ev);
	}
	
	/**
	 * 绑定数据适配器
	 * @param baseAdapter	数据适配器
	 */
	public void setAdapter(BaseAdapter baseAdapter){
		this.baseAdapter = baseAdapter;
		this.itemCount = baseAdapter.getCount();
		//重新布局
		isFirst = true;
		//清除组件中原有的子控件
		removeAllViews();
		if(itemCount <= 5){
			for(int i = itemCount - 1; i >= 0 ; i--){
				addView(getViewFromMap(i));
			}
		} else {
			for(int i = 4; i >= 0; i--){
				addView(getViewFromMap(i));
			}
		}
		currentItemPosition = 0;
		requestLayout();
	}
	
	/**
	 * 从当前的ViewMap中获得视图
	 * @param index	要获取的界面的索引号
	 * @return		从视图缓存中获取控件的布局信息
	 */
	private View getViewFromMap(int index){
		View view = viewMap.get(index);
		if(view == null){
			view = baseAdapter.getView(index, null, this);
			viewMap.put(index, view);
		} else {
			view = baseAdapter.getView(currentItemPosition, view, this);
		}
		return view;
	}

}

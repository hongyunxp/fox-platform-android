package com.foxchan.foxui.widget.lang;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.foxchan.foxui.core.R;
import com.foxchan.foxutils.tool.PhoneUtils;

/**
 * 具有卡片切换效果的控件，向右为将第一张卡片切换到最后，向左为将最后一张卡片切换到最上面
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013年8月6日
 */
public class CardsSwitcher extends RelativeLayout implements OnTouchListener, OnGestureListener {
	
	public static final String TAG = "CardsSwitcher";
	/** 保持原位 */
	private static final int TO_NO_WHERE = -1;
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
	/** 当前选用的缓存中的View的索引号 */
	private int currentViewIndex = 0;
	/** 当前绑定的数据的总数量 */
	private int itemCount;
	/** 界面组件集合 */
	private SparseArray<View> viewMap = new SparseArray<View>();
	/** 子控件的maigin值 */
	private float childMargin = 0.0f;
	private float childMarginLeft = 0.0f;
	private float childMarginTop = 0.0f;
	private float childMarginRight = 0.0f;
	private float childMarginBottom = 0.0f;
	/** 数据观察者对象 */
	private DataSetObserver dataSetObserver;
	
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
	/** 翻动卡片的事件监听器 */
	private OnFlingListener onFlingListener;
	
	public CardsSwitcher(Context context) {
		super(context);
		init(context, null);
	}
	
	public CardsSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}
	
	public CardsSwitcher(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}
	
	/**
	 * 初始化控件
	 * @param context
	 */
	private void init(Context context, AttributeSet attrs){
		this.context = context;
		gd = new GestureDetector(context, this);
		setOnTouchListener(this);
		setChildrenDrawingOrderEnabled(true);
		if(attrs != null){
			TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FoxUI);
			childMargin = typedArray.getDimension(R.styleable.FoxUI_child_margin, 0);
			if(childMargin == -1){
				childMarginLeft = typedArray.getDimension(R.styleable.FoxUI_child_margin_left, 0);
				childMarginRight = typedArray.getDimension(R.styleable.FoxUI_child_margin_right, 0);
				childMarginTop = typedArray.getDimension(R.styleable.FoxUI_child_margin_top, 0);
				childMarginBottom = typedArray.getDimension(R.styleable.FoxUI_child_margin_bottom, 0);
			}
			typedArray.recycle();
		}
		
		//初始化数据观察者对象
		dataSetObserver = new DataSetObserver() {

			@Override
			public void onChanged() {
				//更新卡片的数量
				itemCount = baseAdapter.getCount();
				//更新当前显示的卡片的内容
				currView = getViewFromMap(currentItemPosition % 5);
				super.onChanged();
			}

			@Override
			public void onInvalidated() {
				//重绘界面
				isFirst = true;
				requestLayout();
				super.onInvalidated();
			}
			
		};
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//测量子控件的大小
		if(isFirst){
			for(int i = 0; i < getChildCount(); i++){
				final View child = getChildAt(i);
				RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams)child.getLayoutParams();
				if(childMargin != 0){
					p.leftMargin = (int)childMargin;
					p.rightMargin = (int)childMargin;
					p.topMargin = (int)childMargin;
					p.bottomMargin = (int)childMargin;
				}
				if(childMarginLeft != 0) p.leftMargin = (int)childMarginLeft;
				if(childMarginRight != 0) p.rightMargin = (int)childMarginRight;
				if(childMarginTop != 0) p.topMargin = (int)childMarginTop;
				if(childMarginBottom != 0) p.bottomMargin = (int)childMarginBottom;
				int childWidth = measureChildWidth(child, widthMeasureSpec);
				int childHeight = measureChildHeight(child, heightMeasureSpec);
				p.width = childWidth;
				p.height = childHeight;
			}
		}
	}
	
	/**
	 * 获得子控件测量后的宽度
	 * @param child				需要测量的子控件对象
	 * @param widthMeasureSpec	父控件对于子控件在宽度上的要求
	 * @return					返回子控件测量之后的宽度
	 * @create 2013年8月9日
	 * @modify 2013年8月9日
	 * @author foxchan@live.cn
	 */
	private int measureChildWidth(View child, int widthMeasureSpec){
		int marginLeft = child.getLeft();
		int marginRight = child.getRight();
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec) - marginLeft - marginRight;
		int finalWidth = PhoneUtils.getWindowWidth((Activity)context);
		if(specMode == MeasureSpec.AT_MOST){
			finalWidth = specSize;
		} else if(specMode == MeasureSpec.EXACTLY){
			finalWidth = specSize;
		}
		return finalWidth;
	}
	
	/**
	 * 获得子控件测量后的高度
	 * @param child				需要测量的子控件对象
	 * @param heightMeasureSpec	父控件对于子控件在高度上的要求
	 * @return					返回子控件测量后的高度
	 * @create 2013年8月9日
	 * @modify 2013年8月9日
	 * @author foxchan@live.cn
	 */
	private int measureChildHeight(View child, int heightMeasureSpec){
		int marginTop = child.getTop();
		int marginBottom = child.getBottom();
		int specMode = MeasureSpec.getMode(heightMeasureSpec);
		int specSize = MeasureSpec.getSize(heightMeasureSpec);
		int finalHeight = PhoneUtils.getWindowHeight((Activity)context) - marginTop - marginBottom;
		if(specMode == MeasureSpec.AT_MOST){
			finalHeight = specSize;
		} else if(specMode == MeasureSpec.EXACTLY){
			finalHeight = specSize;
		}
		return finalHeight;
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
			//旋转界面
			if(count == 2){
				viewRotate(nextView, 10);
			} else if(count <= 5) {
				for(int i = count - 2; i >= 0; i--){
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
				if(getChildCount() == 1){
					currView.startAnimation(secondAnimation);
				} else {
					//将当前的视图放到控件的最后面
					if(hasNext && cardPosition == TO_BEHIND){
						for(int i = 0; i < getChildCount() - 1; i++){
							getChildAt(0).bringToFront();
						}
						if(getChildCount() == 2){
							refreshViews(getChildAt(0), getChildAt(getChildCount() - 1), getChildAt(0));
						} else {
							refreshViews(getChildAt(0), getChildAt(getChildCount() - 1), getChildAt(getChildCount() - 2));
						}
						viewReverseRotate(currView);
						prevView.startAnimation(secondAnimation);
					} else if(hasNext && cardPosition == TO_FRONT){
						viewReverseRotate(prevView);
						prevView.bringToFront();
						if(getChildCount() == 2){
							refreshViews(getChildAt(0), getChildAt(getChildCount() - 1), getChildAt(0));
						} else {
							refreshViews(getChildAt(0), getChildAt(getChildCount() - 1), getChildAt(getChildCount() - 2));
						}
						currView.startAnimation(secondAnimation);
					}
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
				if(getChildCount() >= 2){
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
		if(getChildCount() <= 0) return false;
		int x1 = (int)e1.getX();
		int x2 = (int)e2.getX();
		int y1 = (int)e1.getY();
		int y2 = (int)e2.getY();
		int deltaX = x2 - x1;
		int deltaY = y2 - y1;
		int absDeltaX = Math.abs(deltaX);
		int absDeltaY = Math.abs(deltaY);
		if(deltaX > 0 && absDeltaX > 100 && absDeltaY < 100){//向右
			if(getChildCount() == 1){
				buildFirstTranslateAnimation(0, 200, 200, TO_BEHIND);
				buildSecondTranslateAnimation(200, 200, TO_BEHIND);
				currView.startAnimation(firstAnimation);
			} else {
				buildFirstTranslateAnimation(0, currView.getMeasuredWidth(), 300, TO_BEHIND);
				buildSecondTranslateAnimation(currView.getMeasuredWidth(), 300, TO_BEHIND);
				currView.startAnimation(firstAnimation);
				
				nextItemIndex();
				currentViewIndex++;
				currentViewIndex %= getChildCount();
				nextView = getViewFromMap(currentViewIndex);
			}
		}
		if(deltaX <= 0 && absDeltaX > 100 && absDeltaY < 100){//向左
			if(getChildCount() == 1){
				buildFirstTranslateAnimation(0, -200, 200, TO_FRONT);
				buildSecondTranslateAnimation(-200, 200, TO_FRONT);
				currView.startAnimation(firstAnimation);
			} else {
				buildFirstTranslateAnimation(0, -prevView.getMeasuredWidth(), 300, TO_FRONT);
				buildSecondTranslateAnimation(-prevView.getMeasuredWidth(), 300, TO_FRONT);
				prevView.startAnimation(firstAnimation);
				
				prevItemIndex();
				if(currentViewIndex > 0){
					currentViewIndex--;
				} else {
					currentViewIndex = getChildCount() - 1;
				}
				currentViewIndex %= getChildCount();
				prevView = getViewFromMap(currentViewIndex);
			}
		}
		if(onFlingListener != null){
			onFlingListener.onFlingTo(getChildAt(getChildCount() - 1), currentItemPosition);
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
		//绑定数据监听器
		baseAdapter.registerDataSetObserver(dataSetObserver);
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
				View view = getViewFromMap(i);
				addView(view);
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
		Log.d(TAG, "index = " + index);
		View view = viewMap.get(index);
		if(view == null){
			view = baseAdapter.getView(index, null, this);
			viewMap.put(index, view);
		} else {
			view = baseAdapter.getView(currentItemPosition, view, this);
			Log.d(TAG, "currentItemPosition = " + currentItemPosition);
		}
		return view;
	}
	
	public float getChildMarginLeft() {
		return childMarginLeft;
	}

	public void setChildMarginLeft(float childMarginLeft) {
		this.childMarginLeft = childMarginLeft;
		isFirst = true;
	}

	public float getChildMarginTop() {
		return childMarginTop;
	}

	public void setChildMarginTop(float childMarginTop) {
		this.childMarginTop = childMarginTop;
		isFirst = true;
	}

	public float getChildMarginRight() {
		return childMarginRight;
	}

	public void setChildMarginRight(float childMarginRight) {
		this.childMarginRight = childMarginRight;
		isFirst = true;
	}

	public float getChildMarginBottom() {
		return childMarginBottom;
	}

	public void setChildMarginBottom(float childMarginBottom) {
		this.childMarginBottom = childMarginBottom;
		isFirst = true;
	}

	public void setOnFlingListener(OnFlingListener onFlingListener) {
		this.onFlingListener = onFlingListener;
	}

	/**
	 * 翻动卡片的事件监听器
	 * @create 2013年8月9日
	 * @author foxchan@live.cn
	 * @version 1.0.0
	 */
	public interface OnFlingListener{
		
		/**
		 * 当翻动卡片时将调用该方法
		 * @param v			当前显示在最前面的View
		 * @param postion	当前数据的显示位置的索引
		 * @create 2013年8月9日
		 * @modify 2013年8月9日
		 * @author foxchan@live.cn
		 */
		void onFlingTo(View v, int postion);
		
	}

}

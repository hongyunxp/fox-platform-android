package com.foxchan.foxui.widget.lang;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.foxchan.foxui.core.R;

/**
 * 支持下拉事件和上划事件的列表控件
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-6-24
 */
public class RefreshListView extends ListView implements OnScrollListener {
	
	/** 状态：还未到触发事件的位置 */
	private static final int CONTINU_TO_PULL = 0;
	/** 状态：已经到了触发事件的位置 */
	private static final int IN_POSITION = 1;
	/** 状态：正在执行任务 */
	private static final int TASK_DOING = 2;
	/** 状态：任务执行结束 */
	private static final int TASK_DONE = 3;
	/** 滑动反弹的最大距离 */
	private static final int MAX_Y_OVERSCROLL_DISTANCE = 150;
	
	private LayoutInflater inflater;
	private int maxOverScrollDIstance;
	
	/** 当前列表的状态 */
	private int state;
	/** 记录TOUCH事件的标志 */
	private boolean isRecored = false;
	/** 是否应该恢复状态的标志 */
	private boolean isBack = false;
	/** 第一个子项的ID号 */
	private int firstItemIndex;
	/** 当前滚动的状态 */
	private int currentScrollState;
	/** 用户点击的位置的纵坐标值 */
	private int startY;
	/** 执行任务的监听器 */
	private OnTaskDoingListener onTaskDoingListener;
	
	/** 头部的布局 */
	private LinearLayout headerView;
	/** 头部内容的高度 */
	private int headerContentHeight;
	/** 头部原来的高度 */
	private int headerContentOriginalHeight;
	private TextView tvState;
	
	public RefreshListView(Context context) {
		super(context);
		init(context);
	}
	
	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * 初始化控件
	 * @param context
	 */
	private void init(Context context) {
		inflater = LayoutInflater.from(context);
		headerView = (LinearLayout)inflater.inflate(R.layout.refresh_header_view, null);
		initHeaderView();
		
		headerContentOriginalHeight = headerView.getPaddingTop();
		measureHeaderView(headerView);
		headerContentHeight = headerView.getMeasuredHeight();
		headerView.setPadding(0, -1 * headerContentHeight, 0, 0);
		headerView.invalidate();
		
		Log.v("size", "height:" + headerContentHeight);
		
		addHeaderView(headerView, null, false);
		setOnScrollListener(this);
		
		final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		final float density = metrics.density;
		maxOverScrollDIstance = (int)(density * MAX_Y_OVERSCROLL_DISTANCE);
	}

	/**
	 * 初始化头部的控件
	 */
	private void initHeaderView() {
		tvState = (TextView)headerView.findViewById(R.id.refresh_header_view_state);
	}

	/**
	 * 重置头部的布局
	 * @param child	头部的布局
	 */
	private void measureHeaderView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if(p == null){
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec = 0;
		if(lpHeight > 0){
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		firstItemIndex = firstVisibleItem;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		currentScrollState = scrollState;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
			if(firstItemIndex == 0 && !isRecored){
				startY = (int)ev.getY();
				isRecored = true;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if(state != TASK_DOING){
				if(state == TASK_DONE){
					
				} else if(state == CONTINU_TO_PULL){
					state = TASK_DONE;
					changeHeaderViewByState();
				} else if(state == IN_POSITION){
					state = TASK_DOING;
					changeHeaderViewByState();
					doTask();
				}
			}
			isBack = false;
			isRecored = false;
			break;
		case MotionEvent.ACTION_MOVE:
			int tempY = (int)ev.getY();
			if(firstItemIndex == 0 && !isRecored){
				isRecored = true;
				startY = tempY;
			}
			if(isRecored && state != TASK_DOING){
				if(state == IN_POSITION){//可以开始执行任务了
					//往上推，推到头部的距离不足以执行任务的高度
					if((tempY - startY < headerContentHeight + 20) && (tempY - startY > 0)){
						state = CONTINU_TO_PULL;
						changeHeaderViewByState();
					} else if(tempY - startY <= 0){//一推到顶
						state = TASK_DONE;
						changeHeaderViewByState();
					} else {//当下拉的超过了头部的高度时
						changeHeaderViewByState();
						headerView.setPadding(headerView.getPaddingLeft(),
								headerContentHeight - 20,
								headerView.getPaddingRight(),
								headerView.getPaddingBottom());
						headerView.invalidate();
					}
				} else if(state == CONTINU_TO_PULL){
					//下拉到可以进入执行任务的高度
					if ((tempY - startY >= headerContentHeight + 20)
							&& currentScrollState == SCROLL_STATE_TOUCH_SCROLL) {
						state = IN_POSITION;
						isBack = true;
						changeHeaderViewByState();
					} else if(tempY - startY <= 0){//上推到顶部
						state = TASK_DONE;
						changeHeaderViewByState();
					}
				} else if(state == TASK_DONE){
					if(tempY - startY > 0){
						state = CONTINU_TO_PULL;
						changeHeaderViewByState();
					}
				}
				//更新headerView的大小
				if(state == CONTINU_TO_PULL){
					int topPadding = (int)(-1 * headerContentHeight + tempY - startY);
					headerView.setPadding(headerView.getPaddingLeft(),
							topPadding, headerView.getPaddingRight(),
							headerView.getPaddingBottom());
					headerView.invalidate();
				}
				if(state == IN_POSITION){
					int topPadding = (int)(tempY - startY - headerContentHeight);
					headerView.setPadding(headerView.getPaddingLeft(),
							topPadding, headerView.getPaddingRight(),
							headerView.getPaddingBottom());
					headerView.invalidate();
				}
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 根据当前的状态改变头部
	 */
	private void changeHeaderViewByState() {
		switch(state){
		case IN_POSITION:
			tvState.setText("松开刷新");
			break;
		case CONTINU_TO_PULL:
			tvState.setText("下拉刷新");
			break;
		case TASK_DOING:
			headerView.setPadding(headerView.getPaddingLeft(),
					headerContentOriginalHeight, headerView.getPaddingRight(),
					headerView.getPaddingBottom());
			headerView.invalidate();
			tvState.setText("正在加载...");
			break;
		case TASK_DONE:
			headerView.setPadding(headerView.getPaddingLeft(), -1
					* headerContentHeight, headerView.getPaddingRight(),
					headerView.getPaddingBottom());
			headerView.invalidate();
			tvState.setText("下拉刷新");
			break;
		}
	}
	
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		if(firstItemIndex == 0){
			return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
					scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
		}
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
				scrollRangeY, maxOverScrollX, maxOverScrollDIstance, isTouchEvent);
	}

	/**
	 * 触发事件，执行任务
	 */
	private void doTask(){
		if(onTaskDoingListener != null){
			onTaskDoingListener.doTask();
		}
	}

	/**
	 * 控件的刷新状态的监听器
	 * @author foxchan@live.cn
	 * @version 1.0.0
	 * @create 2013-6-24
	 */
	public interface OnTaskDoingListener{
		
		/**
		 * 执行任务
		 */
		void doTask();
		
	}

}

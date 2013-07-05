package com.foxchan.foxdiary.core.widgets;

import java.util.Date;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.foxchan.foxdiary.core.R;
import com.foxchan.foxutils.data.DateUtils;

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
	private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;
	
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
	/** 上一次更新的时间 */
	private Date lastUpdateDate;
	
	/** 头部的布局 */
	private LinearLayout headerView;
	/** 头部内容的高度 */
	private int headerContentHeight;
	/** 头部原来的高度 */
	private int headerContentOriginalHeight;
	/** 拖动时的提示信息 */
	private TextView tvState;
	/** 更新的时间提示信息 */
	private TextView tvLastUpdate;
	/** 文字提示信息的容器 */
	private LinearLayout llText;
	/** 箭头图片 */
	private ImageView ivArraw;
	/** 旋转箭头的动画：正向 */
	private RotateAnimation arrawAnimation;
	/** 旋转箭头的动画：反向 */
	private RotateAnimation arrawReverseAnimation;
	/** 加载中的图片 */
	private ImageView ivLoading;
	/** 加载中的动画 */
	private AnimationDrawable adLoading;
	
	/** 脚部的布局 */
	private View footerView;
	/** 列表当前是否滚动到底部的标志 */
	private boolean isScrollToEnd = false;
	/** 是否还有更多数据的标志 */
	private boolean isExistMoreData = true;
	/** 刷新的动画 */
	private Animation animLoading;
	/** 刷新的图片 */
	private ImageView ivCircleLoading;
	
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
		headerView = (LinearLayout)inflater.inflate(R.layout.pull_to_refresh_header_view, null);
		initHeaderView();
		
		headerContentOriginalHeight = headerView.getPaddingTop();
		measureHeaderView(headerView);
		headerContentHeight = headerView.getMeasuredHeight();
		headerView.setPadding(0, -1 * headerContentHeight, 0, 0);
		headerView.invalidate();
		
		footerView = inflater.inflate(R.layout.pull_to_refresh_footer_view, null);
		initFooterView();
		addFooterView(footerView);
		
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
		tvState = (TextView)headerView.findViewById(R.id.pull_to_refresh_state);
		tvLastUpdate = (TextView)headerView.findViewById(R.id.pull_to_refresh_last_update_date);
		ivArraw = (ImageView)headerView.findViewById(R.id.pull_to_refresh_arraw);
		ivArraw.setMinimumWidth(50);
		ivArraw.setMinimumHeight(50);
		llText = (LinearLayout)headerView.findViewById(R.id.pull_to_refresh_text);
		ivLoading = (ImageView)headerView.findViewById(R.id.pull_to_refresh_animation);
		//初始化旋转动画
		arrawAnimation = new RotateAnimation(0, -180,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		arrawAnimation.setInterpolator(new LinearInterpolator());
		arrawAnimation.setDuration(100);
		arrawAnimation.setFillAfter(true);
		
		arrawReverseAnimation = new RotateAnimation(-180, 0,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		arrawReverseAnimation.setInterpolator(new LinearInterpolator());
		arrawReverseAnimation.setDuration(100);
		arrawReverseAnimation.setFillAfter(true);
		//初始化加载的动画
		adLoading = (AnimationDrawable)ivLoading.getBackground();
	}
	
	/**
	 * 初始化脚部的相关组件
	 */
	private void initFooterView() {
		ivCircleLoading = (ImageView)footerView.findViewById(R.id.pull_to_refresh_footer_loading);
		animLoading = AnimationUtils.loadAnimation(getContext(), R.anim.circle);
		ivCircleLoading.startAnimation(animLoading);
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
		//判断是否滚动到了底部
		try {
			if(view.getPositionForView(footerView) == view.getLastVisiblePosition()){
				isScrollToEnd = true;
			}
		} catch (Exception e) {
			isScrollToEnd = false;
		}
		if(isScrollToEnd){
			loadMoreData();
		}
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
					refreshingData();
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
					int topPadding = (int)(tempY - startY - headerContentHeight);
					headerView.setPadding(0, topPadding, 0, 0);
					headerView.invalidate();
				} else if(state == IN_POSITION){
					int topPadding = (int)(tempY - startY - headerContentOriginalHeight);
					topPadding /= 10;
					headerView.setPadding(0, topPadding, 0, 0);
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
			ivLoading.setVisibility(View.GONE);
			adLoading.stop();
			tvState.setText(getResources().getString(R.string.pull_to_refresh_state_release));
			ivArraw.setVisibility(View.VISIBLE);
			llText.setVisibility(View.VISIBLE);
			ivArraw.clearAnimation();
			ivArraw.startAnimation(arrawAnimation);
			break;
		case CONTINU_TO_PULL:
			ivLoading.setVisibility(View.GONE);
			adLoading.stop();
			tvState.setText(getResources().getString(R.string.pull_to_refresh_state_pull));
			ivArraw.setVisibility(View.VISIBLE);
			llText.setVisibility(View.VISIBLE);
			if(isBack){
				isBack = false;
				ivArraw.clearAnimation();
				ivArraw.startAnimation(arrawReverseAnimation);
			}
			break;
		case TASK_DOING:
			headerView.setPadding(headerView.getPaddingLeft(),
					headerContentOriginalHeight, headerView.getPaddingRight(),
					headerView.getPaddingBottom());
			headerView.invalidate();
			tvState.setText(getResources().getString(R.string.pull_to_refresh_state_loading_more));
			
			ivLoading.setVisibility(View.VISIBLE);
			adLoading.start();
			ivArraw.clearAnimation();
			ivArraw.setVisibility(View.GONE);
			llText.setVisibility(View.GONE);
			break;
		case TASK_DONE:
			headerView.setPadding(headerView.getPaddingLeft(), -1
					* headerContentHeight, headerView.getPaddingRight(),
					headerView.getPaddingBottom());
			headerView.invalidate();
			tvState.setText(getResources().getString(R.string.pull_to_refresh_state_pull));
			
			ivLoading.setVisibility(View.GONE);
			adLoading.stop();
			llText.setVisibility(View.VISIBLE);
			ivArraw.setImageResource(R.drawable.arraw_blue);
			break;
		}
		refreshLastUpdateDate();
	}
	
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		if(firstItemIndex == 0 || isExistMoreData){
			return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
					scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
		}
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
				scrollRangeY, maxOverScrollX, maxOverScrollDIstance, isTouchEvent);
	}

	/**
	 * 刷新列表的数据
	 */
	private void refreshingData(){
		if(onTaskDoingListener != null){
			onTaskDoingListener.refreshingData(this);
		}
	}
	
	/**
	 * 刷新数据完成
	 * @param updateDate	数据更新的日期
	 */
	public void refreshingDataComplete(){
		state = TASK_DONE;
		changeHeaderViewByState();
		//更新刷新的日期
		lastUpdateDate = new Date();
		refreshLastUpdateDate();
	}
	
	/**
	 * 刷新上一次刷新列表的时间
	 */
	private void refreshLastUpdateDate(){
		if(lastUpdateDate == null){
			tvLastUpdate.setVisibility(View.GONE);
		} else {
			String dateStr = DateUtils.datesIntervalDetail(lastUpdateDate, new Date(), true);
			String updateDate = getResources().getString(
					R.string.pull_to_refresh_state_last_update_date, dateStr);
			tvLastUpdate.setText(updateDate);
			tvLastUpdate.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 加载更多的数据
	 */
	private void loadMoreData(){
		if(isExistMoreData && onTaskDoingListener != null){
			onTaskDoingListener.loadMoreData(this);
		}
	}
	
	/**
	 * 加载更多数据完成
	 */
	public void loadMoreDataComplete(){
		isExistMoreData = false;
		removeFooterView(footerView);
		invalidate();
	}

	public void setOnTaskDoingListener(OnTaskDoingListener onTaskDoingListener) {
		this.onTaskDoingListener = onTaskDoingListener;
	}

	public void setExistMoreData(boolean isExistMoreData) {
		this.isExistMoreData = isExistMoreData;
	}

	/**
	 * 控件的刷新状态的监听器
	 * @author foxchan@live.cn
	 * @version 1.0.0
	 * @create 2013-6-24
	 */
	public interface OnTaskDoingListener{
		
		/**
		 * 刷新列表的数据
		 */
		void refreshingData(RefreshListView view);
		
		/**
		 * 加载更多的数据
		 */
		void loadMoreData(RefreshListView view);
		
	}

}

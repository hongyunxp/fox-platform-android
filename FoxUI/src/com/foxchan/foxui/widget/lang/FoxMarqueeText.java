package com.foxchan.foxui.widget.lang;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

import com.foxchan.foxutils.tool.PhoneUtils;

public class FoxMarqueeText extends TextView implements Runnable {
	
	/** 当前字幕滚动的位置 */
	private int currentScrollX = 0;
	/** 是否停止滚动的标志 */
	private boolean isStop;
	/** 字幕的宽度 */
	private int textWidth;
	/** 字幕是否有效的标志 */
	private boolean isValid = false;
	/** 字幕滚动的速度 */
	private int scrollSpeed = 2;
	/** 屏幕的宽度 */
	private int windowWidth;
	/** 字幕滚动的次数 */
	private int scrollCount = -1;
	/** 字幕当前滚动的次数 */
	private int currentScrollNumber = 0;
	/** 字幕滚动完毕的事件监听器 */
	private OnScrollOverListener onScrollOverListener;
	
	private Handler handler = new Handler();
	
	public FoxMarqueeText(Context context) {
		super(context);
		init(context);
	}

	public FoxMarqueeText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public FoxMarqueeText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	/**
	 * 初始化控件
	 * @create 2013年8月5日
	 * @modify 2013年8月5日
	 * @author foxchan@live.cn
	 */
	private void init(Context context){
		windowWidth = PhoneUtils.getWindowWidth((Activity)context);
		currentScrollX = -windowWidth;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(!isValid){
			measureTextWidth();
		}
	}
	
	/**
	 * 测量文字的宽度
	 * @create 2013年8月5日
	 * @modify 2013年8月5日
	 * @author foxchan@live.cn
	 */
	private void measureTextWidth(){
		Paint paint = getPaint();
		String str = getText().toString();
		textWidth = (int)paint.measureText(str);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		super.setText(text, type);
		this.isValid = false;
	}

	@Override
	public void run() {
		if(isStop) return;
		currentScrollX += scrollSpeed;
		scrollTo(currentScrollX, 0);
		if(getScrollX() >= getWidth() + windowWidth){
			currentScrollNumber++;
			if(isScrollOver()){
				stopScroll();
				if(onScrollOverListener != null){
					onScrollOverListener.onScrollOver(this);
				}
				return;
			}
			currentScrollX = -windowWidth;
			scrollTo(currentScrollX, 0);
		}
		handler.postDelayed(this, 5);
	}
	
	/**
	 * 开始滚动
	 * @create 2013年8月5日
	 * @modify 2013年8月5日
	 * @author foxchan@live.cn
	 */
	public void startScroll(){
		isStop = false;
		/*removeCallbacks(this);
		post(this);*/
		handler.post(this);
	}
	
	/**
	 * 停止滚动
	 * @create 2013年8月5日
	 * @modify 2013年8月5日
	 * @author foxchan@live.cn
	 */
	public void stopScroll(){
		isStop = true;
	}
	
	/**
	 * 从头开始滚动
	 * @create 2013年8月5日
	 * @modify 2013年8月5日
	 * @author foxchan@live.cn
	 */
	public void startScrollFromHeader(){
		currentScrollX = 0;
		startScroll();
	}

	public void setScrollSpeed(int scrollSpeed) {
		this.scrollSpeed = scrollSpeed;
	}
	
	/**
	 * 判断当前字幕是否滚动结束
	 * @return	如果字幕滚动的次数已经超过上限，则返回true，否则返回false
	 * @create 2013年8月5日
	 * @modify 2013年8月5日
	 * @author foxchan@live.cn
	 */
	private boolean isScrollOver(){
		if(scrollCount == -1) return false;
		if(currentScrollNumber >= scrollCount){
			return true;
		}
		return false;
	}
	
	/**
	 * 绑定字幕滚动完毕的事件监听器
	 * @param onScrollOverListener
	 * @create 2013年8月5日
	 * @modify 2013年8月5日
	 * @author foxchan@live.cn
	 */
	public void setOnScrollOverListener(OnScrollOverListener onScrollOverListener) {
		this.onScrollOverListener = onScrollOverListener;
	}

	public void setScrollCount(int scrollCount) {
		this.scrollCount = scrollCount;
	}

	/**
	 * 监听字幕滚动完毕的监听器类
	 * @create 2013年8月5日
	 * @author foxchan@live.cn
	 * @version 1.0.0
	 */
	public interface OnScrollOverListener{
		
		/**
		 * 当字幕滚动次数达到上限后将执行该方法
		 * @param foxMarqueeText
		 * @create 2013年8月5日
		 * @modify 2013年8月5日
		 * @author foxchan@live.cn
		 */
		void onScrollOver(FoxMarqueeText foxMarqueeText);
		
	}

}

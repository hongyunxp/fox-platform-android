package com.foxchan.foxui.widget.lang;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 允许自动换行的布局
 * @author foxchan@live.cn
 * @create 2013-5-2
 */
public class AutoChangeLineViewGroup extends ViewGroup {
	
	/** 子界面之间的Margin */
	private int childViewMargin = 2;
	
	public AutoChangeLineViewGroup(Context context){
		super(context);
	}

	public AutoChangeLineViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public AutoChangeLineViewGroup(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		for(int index = 0; index < getChildCount(); index ++){
			final View child = getChildAt(index);
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int count = getChildCount();
		int row = 0;
		int lengthX = l;
		int lengthY = t;
		for(int i = 0; i < count; i++){
			final View child = getChildAt(i);
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();
			lengthX += (width + childViewMargin);
			lengthY = row * (height + childViewMargin) + (height + childViewMargin + t);
			//如果一行里面画不下，跳转到下一行进行绘制
			if(lengthX > r){
				lengthX = width + childViewMargin + l;
				row++;
				lengthY = row * (height + childViewMargin) + (height + childViewMargin + t);
			}
			int pl = lengthX - width;
			int pt = lengthY - height;
			int pr = lengthX;
			int pb = lengthY;
			if(t != 0){
				pt -= t;
				pb -= t;
			}
			if(l != 0){
				pl -= l;
				pr -= l;
			}
			child.layout(pl, pt, pr, pb);
		}
	}

	public int getChildViewMargin() {
		return childViewMargin;
	}

	public void setChildViewMargin(int childViewMargin) {
		this.childViewMargin = childViewMargin;
	}

}

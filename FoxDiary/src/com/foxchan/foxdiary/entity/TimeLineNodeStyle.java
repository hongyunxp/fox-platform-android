package com.foxchan.foxdiary.entity;

import com.foxchan.foxdiary.core.R;

/**
 * 日记的类型
 * @author gulangxiangjie@gmail.com
 * @create 2013-4-16
 */
public class TimeLineNodeStyle {
	
	/** 日记颜色的类型，黄色 */
	public static final int STYLE_YELLOW = 0;
	/** 日记颜色的类型，浅橙色 */
	public static final int STYLE_LITE_ORANGE = 1;
	/** 日记颜色的类型，橙色 */
	public static final int STYLE_ORANGE = 2;
	/** 日记颜色的类型，深橙色 */
	public static final int STYLE_DARK_ORANGE = 3;
	/** 日记颜色的类型，红色 */
	public static final int STYLE_RED = 4;
	/** 日记颜色的类型，紫色 */
	public static final int STYLE_PURPLE = 5;
	/** 日记颜色的类型，深紫色 */
	public static final int STYLE_DARK_PURPLE = 6;
	/** 日记颜色的类型，深蓝色 */
	public static final int STYLE_DARK_BLUE = 7;
	/** 日记颜色的类型，蓝色 */
	public static final int STYLE_BLUE = 8;
	/** 日记颜色的类型，浅蓝色 */
	public static final int STYLE_LITE_BLUE = 9;
	/** 日记颜色的类型，绿色 */
	public static final int STYLE_GREEN = 10;
	/** 日记颜色的类型，浅绿色 */
	public static final int STYLE_LITE_GREEN = 11;
	
	/** 日记标题的圆的资源的ID号 */
	private int circleId;
	/** 中轴（左）的资源ID号 */
	private int lineBarLeftId;
	/** 中轴（右）的资源ID号 */
	private int lineBarRightId;
	/** 字体的颜色的ID号 */
	private int textColor;
	/** 日期的字体的颜色的ID号 */
	private int dateTextColor;
	
	/**
	 * 构造一个日记的类型
	 * @param style	颜色类型
	 */
	public TimeLineNodeStyle(int style){
		switch(style){
		case STYLE_BLUE:
			init(R.drawable.blue_circle, R.drawable.blue_line_bar_left,
					R.drawable.blue_line_bar_right, R.color.blue, -1);
			break;
		case STYLE_DARK_BLUE:
			init(R.drawable.dark_blue_circle, R.drawable.dark_blue_line_bar_left,
					R.drawable.dark_blue_line_bar_right, R.color.dark_blue, -1);
			break;
		case STYLE_DARK_ORANGE:
			init(R.drawable.dark_orange_circle, R.drawable.dark_orange_line_bar_left,
					R.drawable.dark_orange_line_bar_right, R.color.dark_orange, -1);
			break;
		case STYLE_DARK_PURPLE:
			init(R.drawable.dark_purple_circle, R.drawable.dark_purple_line_bar_left,
					R.drawable.dark_purple_line_bar_right, R.color.dark_purple, -1);
			break;
		case STYLE_GREEN:
			init(R.drawable.green_circle, R.drawable.green_line_bar_left,
					R.drawable.green_line_bar_right, R.color.green, -1);
			break;
		case STYLE_LITE_BLUE:
			init(R.drawable.lite_blue_circle, R.drawable.lite_blue_line_bar_left,
					R.drawable.lite_blue_line_bar_right, R.color.lite_blue, -1);
			break;
		case STYLE_LITE_GREEN:
			init(R.drawable.lite_green_circle, R.drawable.lite_green_line_bar_left,
					R.drawable.lite_green_line_bar_right, R.color.lite_green, -1);
			break;
		case STYLE_LITE_ORANGE:
			init(R.drawable.lite_orange_circle, R.drawable.lite_orange_line_bar_left,
					R.drawable.lite_orange_line_bar_right, R.color.lite_orange, -1);
			break;
		case STYLE_ORANGE:
			init(R.drawable.orange_circle, R.drawable.orange_line_bar_left,
					R.drawable.orange_line_bar_right, R.color.orange, -1);
			break;
		case STYLE_PURPLE:
			init(R.drawable.purple_circle, R.drawable.purple_line_bar_left,
					R.drawable.purple_line_bar_right, R.color.purple, -1);
			break;
		case STYLE_RED:
			init(R.drawable.red_circle, R.drawable.red_line_bar_left,
					R.drawable.red_line_bar_right, R.color.red, -1);
			break;
		case STYLE_YELLOW:
			init(R.drawable.yellow_circle, R.drawable.yellow_line_bar_left,
					R.drawable.yellow_line_bar_right, R.color.yellow, R.color.dark_purple);
			break;
		}
	}

	/**
	 * 初始化一个日记类型
	 * @param circleId			日记标题的圆的资源的ID号
	 * @param lineBarLeftId		中轴（左）的资源ID号
	 * @param lineBarRightId	中轴（右）的资源ID号
	 * @param textColor			字体的颜色的ID号
	 * @param dateTextColor		日期的字体的颜色的ID号
	 */
	public void init(int circleId, int lineBarLeftId, int lineBarRightId,
			int dateTextColor, int textColor) {
		this.circleId = circleId;
		this.lineBarLeftId = lineBarLeftId;
		this.lineBarRightId = lineBarRightId;
		if(textColor == -1){
			this.textColor = R.color.white;
		} else {
			this.textColor = textColor;
		}
		this.dateTextColor = dateTextColor;
	}

	public int getCircleId() {
		return circleId;
	}

	public void setCircleId(int circleId) {
		this.circleId = circleId;
	}

	public int getLineBarLeftId() {
		return lineBarLeftId;
	}

	public void setLineBarLeftId(int lineBarLeftId) {
		this.lineBarLeftId = lineBarLeftId;
	}

	public int getLineBarRightId() {
		return lineBarRightId;
	}

	public void setLineBarRightId(int lineBarRightId) {
		this.lineBarRightId = lineBarRightId;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public int getDateTextColor() {
		return dateTextColor;
	}

	public void setDateTextColor(int dateTextColor) {
		this.dateTextColor = dateTextColor;
	}

}

package com.foxchan.foxdiary.entity;

import java.util.Random;

import com.foxchan.foxdiary.core.R;

/**
 * 日记的类型
 * @author gulangxiangjie@gmail.com
 * @create 2013-4-16
 */
public class TimeLineNodeStyle {
	
	/** 日记颜色的类型，青色 */
	public static final int STYLE_CYAN = 0;
	/** 日记颜色的类型，绿色 */
	public static final int STYLE_GREEN = 1;
	/** 日记颜色的类型，红色 */
	public static final int STYLE_RED = 2;
	/** 日记颜色的类型，紫色 */
	public static final int STYLE_PURPLE = 3;
	/** 日记颜色的类型，橙色 */
	public static final int STYLE_ORANGE = 4;
	/** 日记颜色的类型，深蓝色 */
	public static final int STYLE_DARK_BLUE = 5;
	
	/** 日记类型的总数 */
	public static final int STYLE_COUNT = 6;
	
	/** 日记发表的时间的颜色 */
	private int timeColor;
	/** 日记节点的背景图片的资源ID号 */
	private int nodeBg;
	/** 日记节点的正文部分的气泡图片的资源ID号 */
	private int balloonBg;
	/** 日记正文的文字的颜色 */
	private int contentColor;
	
	/**
	 * 构造一个日记的类型
	 * @param style	颜色类型
	 */
	public TimeLineNodeStyle(int style){
		switch(style){
		case STYLE_CYAN:
			init(R.color.cyan, R.drawable.cyan_node, R.drawable.cyan_balloon);
			break;
		case STYLE_DARK_BLUE:
			init(R.color.dark_blue, R.drawable.dark_blue_node, R.drawable.dark_blue_balloon);
			break;
		case STYLE_GREEN:
			init(R.color.green, R.drawable.green_node, R.drawable.green_balloon);
			break;
		case STYLE_ORANGE:
			init(R.color.orange, R.drawable.orange_node, R.drawable.orange_balloon);
			break;
		case STYLE_PURPLE:
			init(R.color.purple, R.drawable.purple_node, R.drawable.purple_balloon);
			break;
		case STYLE_RED:
			init(R.color.red, R.drawable.red_node, R.drawable.red_balloon);
			break;
		}
	}
	
	/**
	 * 初始化日记显示类型，正文部分文字的颜色为白色
	 * @param timeColor		日记发表的时间的颜色
	 * @param nodeBg		日记节点的背景图片的资源ID号
	 * @param balloonBg		日记节点的正文部分的气泡图片的资源ID号
	 */
	private void init(int timeColor, int nodeBg, int balloonBg) {
		init(timeColor, nodeBg, balloonBg, R.color.white);
	}

	/**
	 * 初始化日记显示类型
	 * @param timeColor		日记发表的时间的颜色
	 * @param nodeBg		日记节点的背景图片的资源ID号
	 * @param balloonBg		日记节点的正文部分的气泡图片的资源ID号
	 * @param contentColor	日记正文的文字的颜色
	 */
	private void init(int timeColor, int nodeBg, int balloonBg,
			int contentColor) {
		if(timeColor == -1){
			this.timeColor = R.color.black;
		} else {
			this.timeColor = timeColor;
		}
		this.nodeBg = nodeBg;
		this.balloonBg = balloonBg;
		this.contentColor = contentColor;
	}
	
	/**
	 * 获得一个随机的节点样式
	 * @return	返回一个随机的节点样式
	 */
	public static TimeLineNodeStyle getRandomStyle(){
		int styleId = getRandomStyleId();
		return new TimeLineNodeStyle(styleId);
	}
	
	/**
	 * 获得一个随机的节点样式的ID号
	 * @return	返回一个随机的节点样式的ID号
	 */
	public static int getRandomStyleId(){
		return new Random().nextInt(STYLE_COUNT);
	}

	public int getTimeColor() {
		return timeColor;
	}

	public void setTimeColor(int timeColor) {
		this.timeColor = timeColor;
	}

	public int getNodeBg() {
		return nodeBg;
	}

	public void setNodeBg(int nodeBg) {
		this.nodeBg = nodeBg;
	}

	public int getBalloonBg() {
		return balloonBg;
	}

	public void setBalloonBg(int balloonBg) {
		this.balloonBg = balloonBg;
	}

	public int getContentColor() {
		return contentColor;
	}

	public void setContentColor(int contentColor) {
		this.contentColor = contentColor;
	}

}

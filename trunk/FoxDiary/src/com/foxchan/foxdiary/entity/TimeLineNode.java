package com.foxchan.foxdiary.entity;

import java.util.Date;

import com.wecan.veda.utils.DateUtil;

/**
 * 时间线的节点
 * @author gulangxiangjie@gmail.com
 * @create 2013-4-17
 */
public class TimeLineNode {
	
	/** 节点的类型：日记 */
	public static final int TYPE_DIARY = 0;
	/** 节点的类型：功能节点 */
	public static final int TYPE_TOOL = 1;
	
	/** 时间线节点的样式类型 */
	private TimeLineNodeStyle style;
	/** 该节点的类型 */
	private int type;
	
	public TimeLineNodeStyle getStyle() {
		return style;
	}

	public void setStyle(TimeLineNodeStyle style) {
		this.style = style;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}

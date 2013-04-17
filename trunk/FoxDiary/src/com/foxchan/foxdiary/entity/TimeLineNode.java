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

	/**
	 * 获得日记的类型
	 * @param createDate	该节点的创建时间
	 * @return				返回日记的类型
	 */
	public TimeLineNodeStyle diaryStyle(Date createDate){
		if(this.style == null){
			int styleId = TimeLineNodeStyle.STYLE_BLUE;
			int hour = DateUtil.getHour(createDate);
			switch(hour){
			case 0:
				styleId = TimeLineNodeStyle.STYLE_LITE_BLUE;
				break;
			case 1:
				styleId = TimeLineNodeStyle.STYLE_BLUE;
				break;
			case 2:
				styleId = TimeLineNodeStyle.STYLE_DARK_BLUE;
				break;
			case 3:
				styleId = TimeLineNodeStyle.STYLE_DARK_PURPLE;
				break;
			case 4:
				styleId = TimeLineNodeStyle.STYLE_PURPLE;
				break;
			case 5:
				styleId = TimeLineNodeStyle.STYLE_DARK_PURPLE;
				break;
			case 6:
				styleId = TimeLineNodeStyle.STYLE_DARK_BLUE;
				break;
			case 7:
				styleId = TimeLineNodeStyle.STYLE_GREEN;
				break;
			case 8:
				styleId = TimeLineNodeStyle.STYLE_LITE_GREEN;
				break;
			case 9:
				styleId = TimeLineNodeStyle.STYLE_YELLOW;
				break;
			case 10:
				styleId = TimeLineNodeStyle.STYLE_LITE_ORANGE;
				break;
			case 11:
				styleId = TimeLineNodeStyle.STYLE_ORANGE;
				break;
			case 12:
				styleId = TimeLineNodeStyle.STYLE_DARK_ORANGE;
				break;
			case 13:
				styleId = TimeLineNodeStyle.STYLE_RED;
				break;
			case 14:
				styleId = TimeLineNodeStyle.STYLE_PURPLE;
				break;
			case 15:
				styleId = TimeLineNodeStyle.STYLE_DARK_PURPLE;
				break;
			case 16:
				styleId = TimeLineNodeStyle.STYLE_DARK_BLUE;
				break;
			case 17:
				styleId = TimeLineNodeStyle.STYLE_BLUE;
				break;
			case 18:
				styleId = TimeLineNodeStyle.STYLE_LITE_BLUE;
				break;
			case 19:
				styleId = TimeLineNodeStyle.STYLE_BLUE;
				break;
			case 20:
				styleId = TimeLineNodeStyle.STYLE_DARK_BLUE;
				break;
			case 21:
				styleId = TimeLineNodeStyle.STYLE_DARK_PURPLE;
				break;
			case 22:
				styleId = TimeLineNodeStyle.STYLE_PURPLE;
				break;
			case 23:
				styleId = TimeLineNodeStyle.STYLE_BLUE;
				break;
			}
			this.style = new TimeLineNodeStyle(styleId);
		}
		return this.style;
	}
}

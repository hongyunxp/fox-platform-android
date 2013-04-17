package com.foxchan.foxdiary.entity;

/**
 * 日记时间线上的工具节点
 * @author foxchan@live.cn
 * @create 2013-4-17
 */
public class DiaryTool extends TimeLineNode {
	
	/** 工具的图片资源的ID号 */
	private int resourceId;

	/**
	 * 构造一个工具节点
	 * @param resourceId	工具的图片的资源ID号
	 */
	public DiaryTool(int resourceId) {
		this.resourceId = resourceId;
		setType(TYPE_TOOL);
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

}

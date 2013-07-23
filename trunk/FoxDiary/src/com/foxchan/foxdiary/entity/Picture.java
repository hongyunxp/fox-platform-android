package com.foxchan.foxdiary.entity;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * 图片对象
 * @author foxchan@live.cn
 * @create 2013-7-23
 */
public class Picture {

	/** 索引 */
	private int id;
	/** 标题 */
	private String title;
	/** 描述 */
	private String description;
	/** 图片资源的id号 */
	private int resourceId;
	/** 图片的内容 */
	private Drawable drawable;
	
	/**
	 * 构造一个图片对象
	 * @param title			标题
	 * @param resourceId	图片资源的id号
	 */
	public Picture(String title, int resourceId) {
		this(-1, title, title, resourceId);
	}

	/**
	 * 构造一个图片对象
	 * @param id			索引
	 * @param title			标题
	 * @param description	描述
	 * @param resourceId	图片资源的id号
	 */
	public Picture(int id, String title, String description, int resourceId) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.resourceId = resourceId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public Drawable getDrawable(Context context) {
		if(this.drawable == null){
			drawable = context.getResources().getDrawable(resourceId);
		}
		return drawable;
	}

}

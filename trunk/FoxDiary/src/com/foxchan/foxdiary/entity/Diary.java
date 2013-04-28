package com.foxchan.foxdiary.entity;

import java.util.Date;

import com.foxchan.foxutils.tool.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * 实体：日记
 * @author gulangxiangjie@gmail.com
 * @create 2013-4-16
 */
public class Diary extends TimeLineNode {
	
	/** 日记的唯一标识，采用32位UUID */
	private String id;
	/** 日记的标题，可以为空，最多15个字符 */
	private String title;
	/** 日记的一张图片的存储位置 */
	private String imagePath;
	/** 日记的正文，不可为空，最多140字 */
	private String content;
	/** 日记的心情 */
	private int emotion;
	/** 日记的生成时间 */
	private Date createDate;

	public Diary() {
		setType(TYPE_DIARY);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getEmotion() {
		return emotion;
	}

	public void setEmotion(int emotion) {
		this.emotion = emotion;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	/**
	 * 获得节点的图片
	 * @param context
	 * @return			返回节点的图片
	 */
	public Bitmap photo(Context context){
		Bitmap pic = BitmapUtils.loadBitmapFromSdCard(context, imagePath);
		if(pic != null){
			pic = BitmapUtils.getRoundedCornerBitmap(pic, 57.5f);
		}
		return pic;
	}
	
}

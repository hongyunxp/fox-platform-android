package com.foxchan.foxdiary.entity;

import java.util.Date;
import java.util.Random;

/**
 * 实体：日记
 * @author gulangxiangjie@gmail.com
 * @create 2013-4-16
 */
public class Diary {
	
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
	 * 获得日记的类型
	 * @return	返回日记的类型
	 */
	public DiaryStyle diaryStyle(){
		DiaryStyle diaryStyle = null;
		int style = new Random().nextInt(12);
		diaryStyle = new DiaryStyle(style);
		return diaryStyle;
	}

}

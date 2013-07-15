package com.foxchan.foxdiary.entity;

/**
 * 心情对象
 * @author foxchan@live.cn
 * @create 2013-7-15
 */
public class Emotion {

	/** id号 */
	private int id;
	/** 对应的图片资源的id号 */
	private int drawableId;

	public Emotion(int id, int drawableId) {
		this.id = id;
		this.drawableId = drawableId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDrawableId() {
		return drawableId;
	}

	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
	}

}

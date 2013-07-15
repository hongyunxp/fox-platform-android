package com.foxchan.foxdiary.entity;

/**
 * 天气对象
 * @author foxchan@live.cn
 * @create 2013-7-15
 */
public class Weather {
	
	/** 天气对象的id号 */
	private int id;
	/** 对应的资源id号 */
	private int drawableId;
	
	public Weather(int id, int drawableId) {
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

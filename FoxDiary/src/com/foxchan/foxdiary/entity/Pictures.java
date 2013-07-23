package com.foxchan.foxdiary.entity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.foxchan.foxutils.data.StringUtils;

/**
 * 图片对象集合
 * @author foxchan@live.cn
 * @create 2013-7-23
 */
public class Pictures {
	
	private Context context;
	/** 图片对象列表 */
	private List<Picture> pictures;
	/** 当前选中的图片的索引号 */
	private int currentIndex = 0;
	
	public Pictures(Context context){
		this.context = context;
		pictures = new ArrayList<Picture>();
	}

	public Pictures(Context context, List<Picture> pictures) {
		this.context = context;
		this.pictures = pictures;
	}
	
	/**
	 * 添加图片对象到集合中
	 * @param picture	图片对象
	 */
	public void addPicture(Picture picture){
		getInstance();
		picture.getDrawable(context);
		pictures.add(picture);
	}
	
	/**
	 * 下一张图片，到了最后一张会自动跳转到第一张
	 * @return	返回下一张图片对象
	 */
	public Picture nextPicture(){
		if(currentIndex + 1 >= pictures.size()){
			currentIndex = 0;
		} else {
			currentIndex++;
		}
		return pictures.get(currentIndex);
	}
	
	/**
	 * 上一张图片，到了第一张会自动跳转到最后一张
	 * @return	返回上一张图片对象
	 */
	public Picture prevPicture(){
		if(currentIndex - 1 < 0){
			currentIndex = pictures.size();
		} else {
			currentIndex--;
		}
		return pictures.get(currentIndex);
	}
	
	/**
	 * 获得指定位置的图片对象
	 * @param index	图片对象的索引号
	 * @return		返回指定位置的图片对象
	 */
	public Picture getPictureAt(int index){
		return pictures.get(index);
	}
	
	/**
	 * 初始化列表集合
	 */
	private void getInstance(){
		if(StringUtils.isEmpty(pictures)){
			pictures = new ArrayList<Picture>();
		}
	}

}

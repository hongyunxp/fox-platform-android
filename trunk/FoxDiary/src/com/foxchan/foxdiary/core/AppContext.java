package com.foxchan.foxdiary.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.Application;

import com.baidu.location.LocationClient;
import com.foxchan.foxdiary.entity.Diary;

/**
 * 应用程序的上下文信息
 * @author foxchan@live.cn
 * @create 2013-4-28
 */
public class AppContext extends Application {
	
	/** 应用程序的标签 */
	public static final String TAG = "FoxDiary";
	
	/** 显示在日记时间轴的日记列表 */
	public static LinkedHashMap<String, Diary> diaryMapForShow = new LinkedHashMap<String, Diary>();
	public static Diary tempDiary = new Diary();
	/** 定位的对象 */
	private LocationClient locationClient;
	
	@Override
	public void onCreate() {
		locationClient = new LocationClient(getApplicationContext());
		super.onCreate();
	}

	/**
	 * 添加日记到日记时间轴的日记集合中
	 * @param diary	添加的日记
	 */
	public static void addDiaryToDiaryLineView(Diary diary){
		if(diaryMapForShow == null){
			diaryMapForShow = new LinkedHashMap<String, Diary>();
		}
		diaryMapForShow.put(diary.getId(), diary);
	}
	
	/**
	 * 添加日记到日记时间轴的日记集合中
	 * @param diaries	日记列表
	 */
	public static void addDiaryToDiaryLineView(List<Diary> diaries){
		if(diaryMapForShow == null){
			diaryMapForShow = new LinkedHashMap<String, Diary>();
		}
		for(Diary diary : diaries){
			diaryMapForShow.put(diary.getId(), diary);
		}
	}
	
	/**
	 * 获得所有需要显示的日记
	 * @return	返回需要显示的日记
	 */
	public static List<Diary> getDiariesForShow(){
		List<Diary> diaries = new ArrayList<Diary>();
		for(String id : diaryMapForShow.keySet()){
			diaries.add(diaryMapForShow.get(id));
		}
		return diaries;
	}
	
	/**
	 * 获得需要显示的日记的数量
	 * @return	返回需要显示的日记的数量
	 */
	public static int getDiaryCountForShow(){
		return diaryMapForShow.size();
	}

	public LocationClient getLocationClient() {
		return locationClient;
	}

	public void setLocationClient(LocationClient locationClient) {
		this.locationClient = locationClient;
	}

}

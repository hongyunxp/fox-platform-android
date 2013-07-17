package com.foxchan.foxdiary.core;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;

import com.baidu.location.LocationClient;
import com.foxchan.foxdiary.entity.Diary;
import com.foxchan.foxutils.data.CollectionUtils;

/**
 * 应用程序的上下文信息
 * @author foxchan@live.cn
 * @create 2013-4-28
 */
public class AppContext extends Application {
	
	/** 应用程序的标签 */
	public static final String TAG = "FoxDiary";
	
	/** 显示在日记时间轴的日记列表 */
	public static List<Diary> diariesOnDiaryLineView;
	
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
		if(CollectionUtils.isEmpty(diariesOnDiaryLineView)){
			diariesOnDiaryLineView = new ArrayList<Diary>();
		}
		diariesOnDiaryLineView.add(diary);
	}

	public LocationClient getLocationClient() {
		return locationClient;
	}

	public void setLocationClient(LocationClient locationClient) {
		this.locationClient = locationClient;
	}

}

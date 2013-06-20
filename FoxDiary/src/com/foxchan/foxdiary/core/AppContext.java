package com.foxchan.foxdiary.core;

import java.util.List;

import com.foxchan.foxdiary.entity.Diary;

/**
 * 应用程序的上下文信息
 * @author foxchan@live.cn
 * @create 2013-4-28
 */
public class AppContext {
	
	/** 应用程序的标签 */
	public static final String TAG = "FoxDiary";
	
	/** 显示在日记时间轴的日记列表 */
	public static List<Diary> diariesOnDiaryLineView;

}

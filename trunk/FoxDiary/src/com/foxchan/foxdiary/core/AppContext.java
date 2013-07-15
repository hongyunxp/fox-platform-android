package com.foxchan.foxdiary.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.foxchan.foxdiary.entity.Diary;
import com.foxchan.foxdiary.entity.Emotion;
import com.foxchan.foxdiary.entity.Weather;
import com.foxchan.foxutils.data.CollectionUtils;

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
	/** 天气的图片资源的MAP */
	public static final HashMap<Integer, Weather> weatherMap = new HashMap<Integer, Weather>(){{
		put(R.id.rb_weather_sunny, new Weather(0, R.drawable.weather_sunny_normal));
		put(R.id.rb_weather_sunny_cloudy, new Weather(1, R.drawable.weather_sunny_cloudy_normal));
		put(R.id.rb_weather_cloudy, new Weather(2, R.drawable.weather_cloudy_normal));
		put(R.id.rb_weather_thunder, new Weather(3, R.drawable.weather_thunder_normal));
		put(R.id.rb_weather_rainny, new Weather(4, R.drawable.weather_rainny_normal));
		put(R.id.rb_weather_snowy, new Weather(5, R.drawable.weather_snowy_normal));
	}};
	/** 心情的图片资源的MAP */
	public static final HashMap<Integer, Emotion> emotionMap = new HashMap<Integer, Emotion>(){{
		put(R.id.rb_emotion_xiao, new Emotion(0, R.drawable.emotion_xiao_normal));
		put(R.id.rb_emotion_nu, new Emotion(1, R.drawable.emotion_nu_normal));
		put(R.id.rb_emotion_ku, new Emotion(2, R.drawable.emotion_ku_normal));
		put(R.id.rb_emotion_ganga, new Emotion(3, R.drawable.emotion_ganga_normal));
		put(R.id.rb_emotion_ai, new Emotion(4, R.drawable.emotion_ai_normal));
		put(R.id.rb_emotion_gandong, new Emotion(5, R.drawable.emotion_gandong_normal));
	}};
	
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

}

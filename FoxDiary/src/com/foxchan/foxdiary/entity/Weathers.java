package com.foxchan.foxdiary.entity;

import android.util.SparseArray;

import com.foxchan.foxdiary.core.R;

/**
 * 天气对象的集合对象
 * @author foxchan@live.cn
 * @create 2013-7-16
 */
public class Weathers {
	
	/** 天气对象的集合-键值采用RadioButton的id号 */
	private static SparseArray<Weather> weatherMap;
	/** 天气对象的集合-键值采用表情对象的id号 */
	private static SparseArray<Weather> weatherMapById;
	
	static{
		weatherMap = new SparseArray<Weather>();
		weatherMap.put(R.id.rb_weather_sunny, new Weather(0, R.drawable.weather_sunny_normal));
		weatherMap.put(R.id.rb_weather_sunny_cloudy, new Weather(1, R.drawable.weather_sunny_cloudy_normal));
		weatherMap.put(R.id.rb_weather_cloudy, new Weather(2, R.drawable.weather_cloudy_normal));
		weatherMap.put(R.id.rb_weather_thunder, new Weather(3, R.drawable.weather_thunder_normal));
		weatherMap.put(R.id.rb_weather_rainny, new Weather(4, R.drawable.weather_rainny_normal));
		weatherMap.put(R.id.rb_weather_snowy, new Weather(5, R.drawable.weather_snowy_normal));
		
		weatherMapById = new SparseArray<Weather>();
		weatherMapById.put(0, new Weather(0, R.drawable.weather_sunny_normal));
		weatherMapById.put(1, new Weather(1, R.drawable.weather_sunny_cloudy_normal));
		weatherMapById.put(2, new Weather(2, R.drawable.weather_cloudy_normal));
		weatherMapById.put(3, new Weather(3, R.drawable.weather_thunder_normal));
		weatherMapById.put(4, new Weather(4, R.drawable.weather_rainny_normal));
		weatherMapById.put(5, new Weather(5, R.drawable.weather_snowy_normal));
	}
	
	/**
	 * 根据天气的id号获得天气对象
	 * @param weatherId	天气的id号
	 * @return			返回天气对象
	 */
	public static Weather getWeatherById(int weatherId){
		return weatherMapById.get(weatherId);
	}
	
	/**
	 * 根据单选按钮的id号获得天气对象
	 * @param radioButtonId	单选框的id号
	 * @return				返回天气对象
	 */
	public static Weather getWeatherByRadioButton(int radioButtonId){
		return weatherMap.get(radioButtonId);
	}

}

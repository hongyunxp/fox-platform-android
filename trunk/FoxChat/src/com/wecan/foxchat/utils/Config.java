package com.wecan.foxchat.utils;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 软件的系统配置信息
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-14
 */
public class Config {
	
	private static Config config;
	/** 系统配置文件的名称 */
	public static final String CONFIG_FILE = "settings";
	/** 配置文件对象 */
	private SharedPreferences preferences;
	/** 配置文件的编辑器 */
	private SharedPreferences.Editor editor;
	
	/** 是否启用个性签名 */
	public static final String SIGN_STATE = "signState";
	/** 个性签名 */
	public static final String MY_SIGN = "mySign";
	
	private Config(){}
	
	private Config(Context context){
		if(preferences == null) {
			preferences = context.getSharedPreferences(
				CONFIG_FILE, Activity.MODE_PRIVATE);
		}
		if(editor == null){
			editor = preferences.edit();
		}
	}
	
	/**
	 * 获得系统的配置信息
	 * @return
	 */
	public static Config getConfig(Context context){
		if(config == null){
			return new Config(context);
		}
		return config;
	}
	
	/**
	 * 获得字符串型的配置，默认返回空串
	 * @param key
	 * @return
	 */
	public String getString(String key){
		return preferences.getString(key, "");
	}
	
	/**
	 * 配置设置
	 * @param content
	 */
	public void putString(HashMap<String, Object> content){
		for(String key : content.keySet()){
			Object obj = content.get(key);
			if(obj instanceof String){
				editor.putString(key, (String)obj);
			} else if(obj instanceof Boolean){
				editor.putBoolean(key, (Boolean)obj);
			} else if(obj instanceof Integer){
				editor.putInt(key, (Integer)obj);
			}
		}
		editor.commit();
	}
	
	/**
	 * 获得布尔型的配置，默认返回false
	 * @param key
	 * @return
	 */
	public Boolean getBoolean(String key){
		return preferences.getBoolean(key, false);
	}
	
	/**
	 * 获得整形类型的配置，默认返回0
	 * @param key
	 * @return
	 */
	public Integer getInt(String key){
		return preferences.getInt(key, 0);
	}

}

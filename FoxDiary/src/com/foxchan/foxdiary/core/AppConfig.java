package com.foxchan.foxdiary.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.foxchan.foxdiary.utils.Closer;
import com.foxchan.foxutils.data.StringUtils;

/**
 * 应用程序的配置类
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-5
 */
public class AppConfig {
	
	/** 软件的配置文件的字符串 */
	private final static String APP_CONFIG = "config";
	
	/** cookie的键值 */
	public static final String CONF_COOKIE = "cookie";
	/** 应用程序的唯一标识的键值 */
	public static final String CONF_APP_UNIQUEID = "APP_UNIQUEID";
	
	/** 是否记住用户的标志 */
	public static final String USER_IS_REMEMBER_ME = "userIsRememberMe";
	/** 用户的登陆账号 */
	public static final String USER_ACCOUNT = "userAccount";
	/** 用户的登陆密码 */
	public static final String USER_PASSWORD = "userPassword";
	
	/** 返回给用户的配置对象 */
	private static AppConfig appConfig;
	/** 配置文件对象 */
	private SharedPreferences preferences;
	/** 配置文件的编辑器 */
	private SharedPreferences.Editor editor;
	private Context context;
	
	private AppConfig(Context context){
		this.context = context;
		if(this.preferences == null){
			this.preferences = context.getSharedPreferences(APP_CONFIG,
					Activity.MODE_PRIVATE);
		}
		if(editor == null){
			editor = preferences.edit();
		}
	}
	
	/**
	 * 获得AppConfig的实例
	 * @return	返回AppConfig的实例
	 */
	public static AppConfig getInstance(Context context){
		if(appConfig == null){
			appConfig = new AppConfig(context);
		}
		return appConfig;
	}
	
	/**
	 * 保存配置文件信息
	 * @param configs	配置项的集合
	 */
	public void persistConfig(HashMap<String, Object> configs){
		for(String key : configs.keySet()){
			Object obj = configs.get(key);
			if(obj instanceof String){
				editor.putString(key, (String)obj);
			} else if(obj instanceof Boolean){
				editor.putBoolean(key, (Boolean)obj);
			} else if(obj instanceof Integer){
				editor.putInt(key, (Integer)obj);
			} else if(obj instanceof Float){
				editor.putFloat(key, (Float)obj);
			} else if(obj instanceof Long){
				editor.putLong(key, (Long)obj);
			}
		}
		editor.commit();
	}
	
	/**
	 * 保存配置文件信息
	 * @param key	键值
	 * @param value	取值
	 */
	public void persistConfig(final String key, final Object value){
		HashMap<String, Object> configs = new HashMap<String, Object>();
		configs.put(key, value);
		persistConfig(configs);
	}
	
	/**
	 * 获得字符串型的配置信息，默认返回空串
	 * @param key	键值
	 * @return		返回字符串型的配置信息，默认返回空串
	 */
	public String getPreferencesString(String key){
		return preferences.getString(key, "");
	}
	
	/**
	 * 获得整数型的配置信息，默认返回0
	 * @param key	键值
	 * @return		返回整数型的配置信息，默认返回0
	 */
	public Integer getPreferencesInteger(String key){
		return preferences.getInt(key, 0);
	}
	
	/**
	 * 获得布尔型的配置信息，默认返回false
	 * @param key	键值
	 * @return		返回布尔型的配置信息，默认返回false
	 */
	public Boolean getPreferencesBoolean(String key){
		return preferences.getBoolean(key, false);
	}
	
	/**
	 * 获得单精度类型的浮点型配置信息，默认返回0
	 * @param key	键值
	 * @return		返回单精度类型的浮点型配置信息，默认返回0
	 */
	public Float getPreferencesFloat(String key){
		return preferences.getFloat(key, 0f);
	}
	
	/**
	 * 获得长整形类型的配置信息，默认返回0
	 * @param key	键值
	 * @return		返回长整形类型的配置信息，默认返回0
	 */
	public Long getPreferencesLong(String key){
		return preferences.getLong(key, 0l);
	}
	
	/**
	 * 获得Cookie信息
	 * @return	返回Cookie信息
	 */
	public String getCookie(){
		return getProperty(CONF_COOKIE);
	}

	/**
	 * 获得某项配置信息的取值
	 * @param key	建的名称
	 * @return		返回制定的健对应的取值
	 */
	public String getProperty(String key) {
		Properties properties = loadProperties();
		return properties == null ? null : (String)properties.get(key);
	}
	
	/**
	 * 判断当前的应用程序中是否有该键值的属性信息
	 * @param key	键值
	 * @return		如果存在则返回true，否则返回false
	 */
	public boolean containsProperty(String key){
		Properties properties = loadProperties();
		return properties.containsKey(key);
	}
	
	/**
	 * 加载软件中的Properties文件
	 * @return	返回Properties对象
	 */
	public Properties loadProperties(){
		FileInputStream fis = null;
		Properties properties = new Properties();
		try {
			//加载APP_CONFIG下的配置文件
			File dirConfig = context.getDir(APP_CONFIG, Context.MODE_PRIVATE);
			fis = new FileInputStream(StringUtils.concat(new Object[]{
				dirConfig.getPath(), File.separator, APP_CONFIG
			}));
			properties.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Closer.close(fis);
		}
		return properties;
	}
	
	/**
	 * 保存配置文件信息
	 * @param properties	Properties对象
	 */
	public void persistProperties(Properties properties){
		FileOutputStream fos = null;
		try {
			File dirConfig = context.getDir(APP_CONFIG, Context.MODE_PRIVATE);
			File config = new File(dirConfig, APP_CONFIG);
			fos = new FileOutputStream(config);
			properties.store(fos, null);
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Closer.close(fos);
		}
	}
	
	/**
	 * 设置某一项设置的信息
	 * @param key	健
	 * @param value	取值
	 */
	public void setProperty(String key, String value){
		Properties properties = loadProperties();
		properties.setProperty(key, value);
		persistProperties(properties);
	}
	
	/**
	 * 删除配置信息
	 * @param keys	健的数组
	 */
	public void removeProperties(String... keys){
		Properties properties = loadProperties();
		for(String key : keys){
			properties.remove(key);
		}
		persistProperties(properties);
	}

}

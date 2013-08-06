package com.foxchan.foxutils.tool;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.Log;

/**
 * 该类提供与获取不同项目中的资源文件相关的方法
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013年8月5日
 */
public class ResourceUtils {
	
	/**
	 * 根据资源的name属性获得资源的ID号
	 * @param context	
	 * @param name		资源的名称
	 * @return			返回指定名称的资源的ID号
	 */
	public static int getResIdByName(Context context, String name){
		return context.getResources().getIdentifier(name, "id", context.getPackageName());
	}
	
	/**
	 * 根据资源的name属性获得资源的布局的ID号
	 * @param context
	 * @param name			布局文件的名称
	 * @param packageName	包名
	 * @return				返回指定名称的布局文件的ID号
	 */
	public static int getLayoutIdByName(Context context, String name, String packageName){
		Log.d("cqm", "package name is " + packageName);
		return context.getResources().getIdentifier(name, "layout", packageName);
	}
	
	public static int getId(Context context, String packageName, String resourceType, String resourceName){
		try {
			Class<?> localClass = Class.forName(packageName + ".R$" + resourceType);
			Field localField = localClass.getField(resourceName);
			int i = Integer.parseInt(localField.get(localField.getName()).toString());
			return i;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}

}

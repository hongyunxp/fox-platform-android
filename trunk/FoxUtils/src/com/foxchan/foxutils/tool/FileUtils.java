package com.foxchan.foxutils.tool;

import java.io.File;
import java.util.Locale;

import com.foxchan.foxutils.data.StringUtils;

/**
 * 与文件操作相关的辅助方法
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-5-9
 */
public class FileUtils {
	
	/**
	 * 构造文件的文件路径
	 * @param filePaths	文件保存轮径的层级文件名
	 * @param fileName	文件名
	 * @return			返回文件的层级路径地址
	 */
	public static String buildFileName(String[] filePaths, String fileName){
		String filePath = File.separator;
		if(!StringUtils.isEmpty(filePaths)){
			for(String path : filePaths){
				filePath = StringUtils.concat(new Object[]{
						filePath, path, File.separator
				});
			}
		}
		if(!StringUtils.isEmpty(fileName)){
			filePath = StringUtils.concat(new Object[]{
					filePath, fileName
			});
		}
		return filePath;
	}
	
	/**
	 * 获得文件的拓展名
	 * @param fileName	文件名
	 * @return			返回文件的拓展名
	 */
	public static String getExt(String fileName){
		String ext = "";
		int pointIndex = fileName.lastIndexOf(".");
		if(pointIndex > -1){
			ext = fileName.substring(pointIndex + 1).toLowerCase();
		}
		return ext;
	}

}

package com.foxchan.foxutils.tool;

import java.io.File;

import com.foxchan.foxutils.data.StringUtils;

/**
 * 与文件操作相关的辅助方法
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-5-9
 */
public class FileUtils {
	
	/**
	 * 构造文件保存的路径
	 * @param filePaths	文件保存路径的层级文件名
	 * @return			返回文件的层级路径地址
	 */
	public static String buildFilePath(String[] filePaths){
		return buildFileName(filePaths, null);
	}
	
	/**
	 * 构造文件的文件路径
	 * @param filePaths	文件保存路径的层级文件名
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
		filePath = filePath.replaceAll("//", "/");
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
	
	/**
	 * 删除文件
	 * @param file	文件对象
	 */
	public static void deleteFile(File file){
		if(file != null && file.exists()){
			file.delete();
		}
	}
	
	/**
	 * 删除文件
	 * @param filePath	文件的路径
	 */
	public static void deleteFile(String filePath){
		deleteFile(new File(filePath));
	}
	
	/**
	 * 删除指定文件夹下的文件
	 * @param dir	文件夹
	 */
	public static void deleteDir(File dir){
		if(dir != null && dir.isDirectory() && dir.exists()){
			for(File file : dir.listFiles()){
				if(file.isFile()){
					deleteFile(file);
				} else {
					deleteDir(file);
				}
			}
			dir.delete();
		}
	}
	
	/**
	 * 删除指定文件夹下的文件
	 * @param dirPath	文件夹的路径
	 */
	public static void deleteDir(String dirPath){
		deleteDir(new File(dirPath));
	}

}

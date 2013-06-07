package com.foxchan.foxutils.tool;

import java.io.File;

import com.foxchan.foxutils.data.StringUtils;

import android.os.Environment;

/**
 * 关于操作SD卡的工具类
 * @author foxchan@live.cn
 * @create 2013-4-28
 */
public class SdCardUtils {
	
	/**
	 * 判断当前设备上是否存在SD卡
	 * @return	如果当前设备上存在SD卡，则返回true，否则返回false
	 */
	public static boolean isSdCardExist(){
		boolean isExist = false;
		isExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		return isExist;
	}
	
	/**
	 * 获取SD卡的根路径
	 * @return	返回SD卡的根路径
	 */
	public static String getSdCardPath(){
		if(isSdCardExist()){
			return Environment.getExternalStorageDirectory().toString() + File.separator;
		}
		return null;
	}
	
	/**
	 * 格式化SD卡中的路径
	 * @param filePath	原来的文件路径名称
	 * @return			返回标准的SD卡路径
	 */
	public static String foarmatSdCardPath(String filePath){
		String rootPath = getSdCardPath();
		String targetPath = "";
		if(filePath.startsWith(rootPath)){
			targetPath = filePath;
		} else {
			targetPath = StringUtils.concat(new Object[]{
					rootPath, filePath
			});
		}
		return targetPath;
	}

}

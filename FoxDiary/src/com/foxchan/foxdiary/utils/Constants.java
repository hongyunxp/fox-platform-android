package com.foxchan.foxdiary.utils;

import com.foxchan.foxutils.tool.FileUtils;
import com.foxchan.foxutils.tool.SdCardUtils;

/**
 * 该类维护项目中涉及到的常量
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-4-30
 */
public class Constants {
	
	/** 应用程序的资源文件夹名称 */
	public static final String APP_RESOURCE = "FoxDiary";
	/** 应用程序中存储图片的文件夹名 */
	public static final String IMAGES = "images";
	/** 应用程序中存储音频的文件夹名 */
	public static final String AUDIOS = "audios";
	
	/** 日记模块的TAG */
	public static final String DIARY_TAG = "tag_diary";
	/** 日记模块的数据库的名称 */
	public static final String DIARY_DB_NAME = "db_diary.db";
	/** 日记模块的数据库的版本号 */
	public static final int DIARY_DB_VERSION = 1;
	/** 日记模块的数据库是否打印调试信息的标志 */
	public static final boolean DIARY_DEBUG = true;
	/** 日记模块的文字输入上限 */
	public static final int DIARY_WORDS_MAX = 140;
	/** 日记模块的图片保存文件夹路径 */
	public static final String DIARY_IMAGE_DOCUMENT = "diary";
	/** 日记模块的临时图片的保存名称 */
	public static final String DIARY_TEMP_IMAGE = "temp_photo.png";
	/** 日记模块的临时正文的文件保存名称 */
	public static final String DIARY_TEMP_WORDS = "temp_words.temp";
	/** 3GP格式的录音的高品质数据 */
	public static final int THREE_GPP_AUDIO_QUENTITY_HIGH = 44100;
	/** 3GP格式的录音的低品质数据 */
	public static final int THREE_GPP_AUDIO_QUENTITY_LOW = 22050;
	/** ARM格式的录音的高品质数据 */
	public static final int ARM_AUDIO_QUENTITY_HIGH = 16000;
	/** ARM格式的录音的低品质数据 */
	public static final int ARM_AUDIO_QUENTITY_LOW = 8000;
	
	/**
	 * 构造日记模块的临时图片的文件路径
	 * @return	返回日记模块的临时图片的文件路径
	 */
	public static final String buildDiaryTempImagePath(){
		return FileUtils.buildFileName(new String[]{
				SdCardUtils.getSdCardPath(), APP_RESOURCE, IMAGES
		}, Constants.DIARY_TEMP_IMAGE);
	}
	
	/**
	 * 构造写日记模块中的录音文件的文件夹路径
	 * @return	返回写日记模块中的录音文件的文件夹路径
	 */
	public static final String buildDiaryAudioPath(){
		return FileUtils.buildFilePath(new String[]{
				SdCardUtils.getSdCardPath(), APP_RESOURCE, AUDIOS
		});
	}

}

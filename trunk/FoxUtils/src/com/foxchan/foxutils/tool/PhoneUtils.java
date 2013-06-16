package com.foxchan.foxutils.tool;

import java.io.IOException;

import android.media.MediaPlayer;

/**
 * 该类提供与手机提供的基本信息相关的方法
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-6-16
 */
public class PhoneUtils {
	
	/**
	 * 播放声音媒体
	 * @param audioPath	声音文件的路径
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static void playAudio(String audioPath)
			throws IllegalArgumentException, SecurityException,
			IllegalStateException, IOException {
		MediaPlayer mp = new MediaPlayer();
		mp.setDataSource(audioPath);
		mp.prepare();
		mp.start();
	}

}

package com.foxchan.foxutils.tool;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.view.Display;
import android.view.WindowManager;

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
	
	/**
	 * 获得手机的屏幕宽度
	 * @param activity
	 * @return			返回手机屏幕的宽度
	 */
	public static int getWindowWidth(Activity activity){
		WindowManager manager = activity.getWindowManager();
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

}

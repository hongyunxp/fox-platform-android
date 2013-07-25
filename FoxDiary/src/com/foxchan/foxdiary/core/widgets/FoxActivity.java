package com.foxchan.foxdiary.core.widgets;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;

/**
 * 通用的Activity
 * @author foxchan@live.cn
 * @create 2013年7月25日
 */
public abstract class FoxActivity extends Activity {
	
	/** 音量控制对象 */
	private AudioManager am;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode){
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			am.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
			break;
		case KeyEvent.KEYCODE_VOLUME_UP:
			am.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

}

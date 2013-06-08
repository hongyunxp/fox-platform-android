package com.wecan.foxchat.observer;

import com.wecan.foxchat.observer.listener.OnSmsListChangedListener;

import android.database.ContentObserver;
import android.os.Handler;

/**
 * 监听短信列表变化的观察者
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-9
 */
public class SmsObserver extends ContentObserver {
	
	private OnSmsListChangedListener listener;

	public SmsObserver(Handler handler) {
		super(handler);
	}
	
	public void setOnSmsListChangedListener(OnSmsListChangedListener listener){
		this.listener = listener;
	}

	/**
	 * 当短信表发生变化时，调用该方法
	 */
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		listener.doAfterSmsListChanged();
	}

}

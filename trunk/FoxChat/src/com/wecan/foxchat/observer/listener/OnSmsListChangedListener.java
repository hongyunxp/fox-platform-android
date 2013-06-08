package com.wecan.foxchat.observer.listener;

/**
 * 监听短信收件箱变化的监听器
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-9
 */
public interface OnSmsListChangedListener {
	
	/**
	 * 当短信列表发生变化后用户执行的方法
	 */
	void doAfterSmsListChanged();

}

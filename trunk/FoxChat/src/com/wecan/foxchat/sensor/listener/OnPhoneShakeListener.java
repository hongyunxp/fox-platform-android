package com.wecan.foxchat.sensor.listener;

/**
 * 手机摇动的监听器
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-8
 */
public interface OnPhoneShakeListener {
	
	/**
	 * 监听到用户摇手机后执行的方法
	 */
	void doAfterShake();

}

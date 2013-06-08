package com.wecan.foxchat.sensor.listener;

/**
 * 该类监听用户把手机拿到耳边的行为
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-8
 */
public interface OnPhoneToFaceListener {
	
	/**
	 * 当用户把手机拿到耳边后将执行该方法
	 */
	void doAfterPhoneToFace();

}

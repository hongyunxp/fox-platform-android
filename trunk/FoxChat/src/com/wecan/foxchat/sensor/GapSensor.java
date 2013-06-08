package com.wecan.foxchat.sensor;

import com.wecan.foxchat.sensor.listener.OnPhoneToFaceListener;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 距离传感器
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-5
 */
public class GapSensor implements SensorEventListener {

	// sensor管理器
	private SensorManager sensorManager;
	//监听用户把手机拿到耳边的监听器
	private OnPhoneToFaceListener listener;

	public GapSensor(Context context) {
		// 获取传感器管理服务
		sensorManager = (SensorManager) context
				.getSystemService(Activity.SENSOR_SERVICE);
	}
	
	public void setOnPhoneToFaceListener(OnPhoneToFaceListener listener){
		this.listener = listener;
	}

	/**
	 * 注册加速度传感器
	 */
	public void register() {
		// 加速度传感器
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	/**
	 * 释放加速度传感器
	 */
	public void stop() {
		sensorManager.unregisterListener(this);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	public void onSensorChanged(SensorEvent event) {
		int sensorType = event.sensor.getType();
		float[] values = event.values;
		if(sensorType == Sensor.TYPE_PROXIMITY){
			if(values[0] <= 0){
				listener.doAfterPhoneToFace();
			}
		}
	}

}

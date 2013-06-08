package com.wecan.foxchat.sensor;

import com.wecan.foxchat.sensor.listener.OnPhoneShakeListener;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 触发手机摇动的感应器
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-5
 */
public class ShakeSensor implements SensorEventListener {
	
	//sensor管理器
	private SensorManager sensorManager;
	//手机摇动的监听器
	private OnPhoneShakeListener listener;
	
	public ShakeSensor(Context context){
		//获取传感器管理服务
		sensorManager = (SensorManager)context.getSystemService(Activity.SENSOR_SERVICE);
	}
	
	public void setOnPhoneShakeListener(OnPhoneShakeListener listener){
		this.listener = listener;
	}
	
	/**
	 * 注册加速度传感器
	 */
	public void register(){
		//加速度传感器
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	/**
	 * 释放加速度传感器
	 */
	public void stop(){
		sensorManager.unregisterListener(this);
	}
	
	public void onAccuracyChanged(Sensor arg0, int arg1) {}

	public void onSensorChanged(SensorEvent event) {
		int sensorType = event.sensor.getType();
		float[] values = event.values;
		if(sensorType == Sensor.TYPE_ACCELEROMETER){
			if(Math.abs(values[0]) > 14 || Math.abs(values[1]) > 14 || Math.abs(values[2]) > 14){
				listener.doAfterShake();
			}
		}
	}

}

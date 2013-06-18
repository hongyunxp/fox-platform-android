package com.foxchan.foxdiary.core.widgets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 模拟android中Activity实现的类，该类提供和Activity相似的接口和生命周期声明
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-4-30
 */
public abstract class FakeActivity {
	
	/**
	 * 获得该界面的布局对象
	 * @return	返回该界面的布局对象
	 */
	public abstract View layoutView();
	
	/**
	 * 该界面的初始化方法
	 */
	public abstract void onCreate(Bundle savedInstanceState);
	
	/**
	 * 界面重绘的方法
	 */
	public void onResume(){}
	
	/**
	 * 该界面的暂停方法
	 */
	public void onPause(){}
	
	/**
	 * 该界面的停止方法
	 */
	public void onStop(){}
	
	/**
	 * 该界面的重新启动方法
	 */
	public void onRestart(){}
	
	/**
	 * 该界面的销毁方法
	 */
	public void onDestroy(){}
	
	/**
	 * 启动一个返回结果的Activity的回调方法
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data){}

}

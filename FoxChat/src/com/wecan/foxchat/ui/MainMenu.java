package com.wecan.foxchat.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.wecan.foxchat.AppManager;
import com.wecan.foxchat.R;
import com.wecan.foxchat.utils.UIUtils;

/**
 * 主菜单
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-10
 */
public class MainMenu extends Activity {
	
	/** 退出程序的按钮 */
	private Button btnExitApp;
	/** 设置的按钮 */
	private Button btnSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_menu);
		AppManager.getAppManager().addActivity(this);
		init();
	}

	/**
	 * 初始化主菜单
	 */
	private void init() {
		//初始化相关组件
		btnExitApp = (Button)findViewById(R.id.app_menu_exit);
		btnSettings = (Button)findViewById(R.id.app_menu_setting);
		
		//绑定退出程序的事件
		btnExitApp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AppManager.getAppManager().AppExit(v.getContext());
			}
		});
		//绑定跳转到软件设置的界面的事件
		btnSettings.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UIUtils.toConfigView(v.getContext());
				finish();
			}
		});
	}

}

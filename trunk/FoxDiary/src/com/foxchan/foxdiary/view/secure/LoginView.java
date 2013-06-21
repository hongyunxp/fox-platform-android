package com.foxchan.foxdiary.view.secure;

import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.core.widgets.FoxToast;
import com.foxchan.foxdiary.core.widgets.locuspassword.LocusPasswordPanel;
import com.foxchan.foxdiary.view.DiaryLineView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * 登录界面，该界面作为用户进入软件核心的入口
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-6-21
 */
public class LoginView extends Activity {
	
	/** 图形密码盘 */
	private LocusPasswordPanel passwordPanel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.secure_password_manage);
		
		//初始化相关的组件
		passwordPanel = (LocusPasswordPanel)findViewById(R.id.password_manage_panel);
		
		//绑定密码盘输入完成的事件
		passwordPanel.setOnCompleteListener(new LocusPasswordPanel.OnCompleteListener() {
			@Override
			public void onComplete(String p) {
				FoxToast.showToast(LoginView.this, "您输入的密码是：" + p, Toast.LENGTH_SHORT);
				toDiaryLineView();
			}
		});
	}
	
	/**
	 * 跳转到日记时间轴的界面
	 */
	private void toDiaryLineView() {
		Intent intent = new Intent(this, DiaryLineView.class);
		startActivity(intent);
		finish();
	}

}

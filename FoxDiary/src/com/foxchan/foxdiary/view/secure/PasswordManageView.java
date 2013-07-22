package com.foxchan.foxdiary.view.secure;

import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.core.widgets.FoxToast;
import com.foxchan.foxdiary.core.widgets.locuspassword.LocusPasswordPanel;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * 管理用户的密码的界面（包括创建新密码、修改密码）
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-6-21
 */
public class PasswordManageView extends Activity {
	
	/** 图形密码盘 */
	private LocusPasswordPanel passwordPanel;
	
	/** 密码 */
	private String password;

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
				password = p;
				FoxToast.showToast(PasswordManageView.this, "您设置的密码是：" + password, Toast.LENGTH_SHORT);
//				passwordPanel.clearPassword(2000);
			}
		});
	}

}

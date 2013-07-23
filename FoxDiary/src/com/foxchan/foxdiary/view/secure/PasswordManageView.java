package com.foxchan.foxdiary.view.secure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.foxchan.foxdiary.core.AppConfig;
import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.core.widgets.FoxToast;
import com.foxchan.foxdiary.core.widgets.locuspassword.LocusPasswordPanel;
import com.foxchan.foxdiary.utils.Constants;
import com.foxchan.foxdiary.view.DiaryLineView;
import com.foxchan.foxutils.data.StringUtils;

/**
 * 管理用户的密码的界面（包括创建新密码、修改密码）
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-6-21
 */
public class PasswordManageView extends Activity {
	
	private AppConfig config;
	
	/** 标题栏 */
	private RelativeLayout rlHeader;
	/** 图形密码盘 */
	private LocusPasswordPanel passwordPanel;
	/** 创建密码的按钮 */
	private Button btnCreatePassword;
	/** 重置密码的按钮 */
	private Button btnResetPassword;
	/** 确认密码的按钮 */
	private Button btnSavePassword;
	
	/** 用户创建的新密码 */
	private String newPassword;
	/** 用户使用中的密码 */
	private String oldPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.secure_password_manage);
		
		//初始化数据
		config = AppConfig.getInstance(this);
		oldPassword = config.getPreferencesString(Constants.USER_PASSWORD_KEY);
		
		//初始化相关的组件
		rlHeader = (RelativeLayout)findViewById(R.id.diary_line_header);
		passwordPanel = (LocusPasswordPanel)findViewById(R.id.password_manage_panel);
		btnCreatePassword = (Button)findViewById(R.id.password_manage_create_password);
		btnResetPassword = (Button)findViewById(R.id.password_manage_reset_password);
		btnSavePassword = (Button)findViewById(R.id.password_manage_save_password);
		
		if(StringUtils.isEmpty(oldPassword)){
			FoxToast.showToast(PasswordManageView.this, String.format(
					getString(R.string.secure_password_tip),
					passwordPanel.getPasswordMinLength()), Toast.LENGTH_LONG);
			//绑定密码盘输入完成的事件
			passwordPanel.setOnCompleteListener(new LocusPasswordPanel.OnCompleteListener() {
				@Override
				public void onComplete(String p) {
					if(StringUtils.isEmpty(newPassword)){
						newPassword = p;
						passwordPanel.clearPassword(2000);
						FoxToast.showToast(PasswordManageView.this,
								R.string.secure_reinput_password, Toast.LENGTH_SHORT);
					} else {
						if(newPassword.equals(p)){
							config.persistConfig(Constants.USER_PASSWORD_KEY, newPassword);
							FoxToast.showToast(PasswordManageView.this,
									R.string.secure_password_complete, Toast.LENGTH_SHORT);
							toDiaryLineView();
						} else {
							passwordPanel.clearPassword();
							FoxToast.showWarning(PasswordManageView.this, 
									R.string.secure_password_not_same, Toast.LENGTH_SHORT);
						}
					}
				}
			});
			//绑定创建密码的点击事件
			btnCreatePassword.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					passwordPanel.clearPassword(2000);
					FoxToast.showToast(PasswordManageView.this,
							R.string.secure_reinput_password, Toast.LENGTH_SHORT);
				}
			});
			//绑定重置密码的点击事件
			btnResetPassword.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					passwordPanel.clearPassword();
					newPassword = "";
					FoxToast.showToast(PasswordManageView.this, String.format(
							getString(R.string.secure_password_tip),
							passwordPanel.getPasswordMinLength()), Toast.LENGTH_SHORT);
				}
			});
			//绑定保存密码的点击事件
			btnSavePassword.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(StringUtils.isEmpty(oldPassword)){
						config.persistConfig(Constants.USER_PASSWORD_KEY, newPassword);
					} else {
						FoxToast.showWarning(PasswordManageView.this,
								R.string.secure_already_have_password,
								Toast.LENGTH_SHORT);
					}
				}
			});
		} else {
			rlHeader.setVisibility(View.GONE);
			btnResetPassword.setVisibility(View.GONE);
			passwordPanel.setOnCompleteListener(new LocusPasswordPanel.OnCompleteListener(){

				@Override
				public void onComplete(String p) {
					if(p.equals(oldPassword)){
						toDiaryLineView();
					} else {
						passwordPanel.clearPassword();
						FoxToast.showException(PasswordManageView.this,
								R.string.secure_password_error,
								Toast.LENGTH_SHORT);
					}
				}
			});
		}
	}
	
	/**
	 * 显示创建密码的按钮
	 */
	private void showCreatePasswordButton(){
		btnCreatePassword.setVisibility(View.VISIBLE);
		btnSavePassword.setVisibility(View.GONE);
	}
	
	/**
	 * 显示保存密码的按钮
	 */
	private void showSavePasswordButton(){
		btnCreatePassword.setVisibility(View.GONE);
		btnSavePassword.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 跳转到日记时间轴界面
	 */
	private void toDiaryLineView(){
		Intent intent = new Intent(this, DiaryLineView.class);
		startActivity(intent);
		finish();
	}

}

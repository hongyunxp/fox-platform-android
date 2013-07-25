package com.foxchan.foxdiary.view.secure;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.foxchan.foxdb.core.FoxDB;
import com.foxchan.foxdb.core.Session;
import com.foxchan.foxdb.engine.TableInfo;
import com.foxchan.foxdiary.core.AppConfig;
import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.core.widgets.FoxActivity;
import com.foxchan.foxdiary.core.widgets.FoxToast;
import com.foxchan.foxdiary.core.widgets.locuspassword.LocusPasswordPanel;
import com.foxchan.foxdiary.entity.Diary;
import com.foxchan.foxdiary.entity.Picture;
import com.foxchan.foxdiary.entity.Pictures;
import com.foxchan.foxdiary.entity.Record;
import com.foxchan.foxdiary.utils.Constants;
import com.foxchan.foxdiary.view.DiaryLineView;
import com.foxchan.foxutils.data.StringUtils;
import com.foxchan.foxutils.tool.FileUtils;
import com.foxchan.foxutils.tool.SdCardUtils;

/**
 * 管理用户的密码的界面（包括创建新密码、修改密码）
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-6-21
 */
public class PasswordManageView extends FoxActivity {
	
	private AppConfig config;
	private FoxDB db;
	
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
	/** 应用返回图标 */
	private LinearLayout llAppBack;
	/** 背景图片 */
	private ImageView ivBg;
	/** 渐入动画 */
	private Animation aniFadeIn;
	/** 放大动画 */
	private Animation aniScale;
	/** 渐出动画 */
	private Animation aniFadeOut;
	/** 展示的图片 */
	private Pictures pictures;
	
	/** 用户创建的新密码 */
	private String newPassword;
	/** 用户使用中的密码 */
	private String oldPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.secure_password_manage);
		db = FoxDB.create(this, Constants.DIARY_DB_NAME, Constants.DIARY_DB_VERSION);
		
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
			//删除原来的所有日记信息
			Session session = db.getCurrentSession();
			String diaryTableName = TableInfo.getInstance(Diary.class).getTableName();
			session.executeUpdate("DROP TABLE IF EXISTS " + diaryTableName);
			String recordTableName = TableInfo.getInstance(Record.class).getTableName();
			session.executeUpdate("DROP TABLE IF EXISTS " + recordTableName);
			FileUtils.deleteDir(FileUtils.buildFilePath(new String[]{
					SdCardUtils.getSdCardPath(), Constants.APP_RESOURCE
			}));
			
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
					passwordPanel.clearPassword(100);
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
			ivBg = (ImageView)findViewById(R.id.secure_password_manage_bg);
			//初始化动画
			aniFadeIn = AnimationUtils.loadAnimation(PasswordManageView.this, R.anim.guide_welcome_fade_in);
			aniFadeIn.setDuration(1000);
			aniScale = AnimationUtils.loadAnimation(PasswordManageView.this, R.anim.guide_welcome_scale);
			aniScale.setDuration(6000);
			aniFadeOut = AnimationUtils.loadAnimation(PasswordManageView.this, R.anim.guide_welcome_fade_out);
			aniFadeOut.setDuration(1000);
			//初始化背景的图片资源
			pictures = new Pictures(PasswordManageView.this);
			pictures.addPicture(new Picture("测试一", R.drawable.guide_pic1));
			pictures.addPicture(new Picture("测试二", R.drawable.guide_pic2));
			pictures.addPicture(new Picture("测试三", R.drawable.guide_pic3));
			pictures.addPicture(new Picture("测试三", R.drawable.guide_pic4));
			
			//开始播放第一个动画
			ivBg.setBackgroundDrawable(pictures.getPictureAt(0).getDrawable(PasswordManageView.this));
			ivBg.startAnimation(aniFadeIn);
			//绑定渐入的动画效果
			aniFadeIn.setAnimationListener(new Animation.AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					ivBg.startAnimation(aniScale);
				}
			});
			//绑定放大的动画效果
			aniScale.setAnimationListener(new Animation.AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					ivBg.startAnimation(aniFadeOut);
				}
			});
			//绑定渐出的动画
			aniFadeOut.setAnimationListener(new Animation.AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					ivBg.setBackgroundDrawable(pictures.nextPicture().getDrawable(PasswordManageView.this));
					ivBg.startAnimation(aniFadeIn);
				}
			});
		}
		//初始化应用的返回图标按钮
		llAppBack = (LinearLayout)findViewById(R.id.diary_write_back);
		llAppBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
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

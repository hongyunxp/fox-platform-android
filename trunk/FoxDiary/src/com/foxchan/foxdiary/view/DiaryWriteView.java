package com.foxchan.foxdiary.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import cn.com.lezhixing.foxdb.core.FoxDB;
import cn.com.lezhixing.foxdb.core.Session;

import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.entity.Diary;
import com.foxchan.foxdiary.entity.TimeLineNodeStyle;
import com.foxchan.foxdiary.utils.Constants;

/**
 * 写日记的界面
 * @author foxchan@live.cn
 * @create 2013-4-22
 */
public class DiaryWriteView extends Activity {
	
	/** Activity Code：从相册中选择图片 */
	public static final int ACTIVITY_CODE_IMAGE_FROM_ALBUM = 1;
	/** Activity Code：从相机中拍摄照片 */
	public static final int ACTIVITY_CODE_IMAGE_FROM_CAMARA = 2;
	/** Activity Code：裁减图片 */
	public static final int ACTIVITY_CODE_IMAGE_CUT = 3;
	
	/** 数据库操作对象 */
	private FoxDB db;
	/** 软键盘管理对象 */
	private InputMethodManager imm;
	
	/** 显示的界面列表集合 */
	private List<View> pageViews;
	/** 翻页容器对象 */
	private ViewPager viewPager;
	/** 文字内容界面 */
	private View vWords;
	/** 从相册中选择图片的界面 */
	private View vPicFromAlbum;
	/** 从照相机中拍摄照片的界面 */
	private View vPicFromCamara;
	/** 录音的界面 */
	private View vVoice;
	/** 用文字记录日记的封装对象 */
	private DiaryWriteWordsView diaryWriteWordsView;
	/** 用图片（从相册）记录日记的封装对象 */
	private DiaryWritePicFromAlbumView diaryWritePicFromAlbumView;
	/** 用图片（从相机）记录日记的封装对象 */
	private DiaryWritePicFromCamaraView diaryWritePicFromCamaraView;
	/** 用声音记录日记的封装对象 */
	private DiaryWriteVoiceView diaryWriteVoiceView;
	/** 底部的单选框组 */
	private RadioGroup rgMenus;
	/** 返回按钮 */
	private LinearLayout llBack;
	/** 保存按钮 */
	private ImageView ivSave;
	/** 刷新图标 */
	private ImageView ivRefresh;
	/** 保存&刷新状态转换器 */
	private ViewSwitcher vsSaveAndRefresh;
	/** 旋转动画播放器 */
	private Animation animCircle;
	
	/** 图片保存的路径 */
	private String imagePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary_write);
		init();
	}

	/**
	 * 初始化该界面
	 */
	private void init() {
		//初始化数据库连接对象
		FoxDB.DEBUG = Constants.DIARY_DEBUG;
		db = FoxDB.create(this, Constants.DIARY_DB_NAME, Constants.DIARY_DB_VERSION);
		
		//初始化键盘管理对象
		imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		
		//初始化界面元素
		diaryWriteWordsView = new DiaryWriteWordsView(this);
		diaryWritePicFromAlbumView = new DiaryWritePicFromAlbumView(this);
		diaryWritePicFromCamaraView = new DiaryWritePicFromCamaraView(this);
		diaryWriteVoiceView = new DiaryWriteVoiceView(this);
		
		pageViews = new ArrayList<View>();
		vWords = diaryWriteWordsView.layoutView();
		vPicFromAlbum = diaryWritePicFromAlbumView.layoutView();
		vPicFromCamara = diaryWritePicFromCamaraView.layoutView();
		vVoice = diaryWriteVoiceView.layoutView();
		pageViews.add(0, vWords);
		pageViews.add(1, vPicFromAlbum);
		pageViews.add(2, vPicFromCamara);
		pageViews.add(3, vVoice);
		
		rgMenus = (RadioGroup)findViewById(R.id.diary_write_down_menu);
		viewPager = (ViewPager)findViewById(R.id.diary_write_viewpager);
		viewPager.setAdapter(new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view == object;
			}
			
			@Override
			public int getCount() {
				return pageViews.size();
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				((ViewPager)container).addView(pageViews.get(position));
				return pageViews.get(position);
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				((ViewPager)container).removeView(pageViews.get(position));
			}

			@Override
			public int getItemPosition(Object object) {
				return POSITION_NONE;
			}
			
		});
		//设置默认的会话界面
		viewPager.setCurrentItem(0);
		//绑定底部菜单的点击事件
		rgMenus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
				case R.id.diary_write_text:
					viewPager.setCurrentItem(0);
					break;
				case R.id.diary_write_photo:
					viewPager.setCurrentItem(1);
					showOrHideImm(1);
					break;
				case R.id.diary_write_camera:
					viewPager.setCurrentItem(2);
					showOrHideImm(2);
					break;
				case R.id.diary_write_voice:
					viewPager.setCurrentItem(3);
					showOrHideImm(3);
					break;
				}
			}
		});
		//绑定界面切换时的事件
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				((RadioButton)rgMenus.getChildAt(position)).setChecked(true);
				showOrHideImm(position);
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {}
			
			@Override
			public void onPageScrollStateChanged(int state) {}
		});
		//绑定头部的按钮事件
		llBack = (LinearLayout)findViewById(R.id.diary_write_back);
		ivSave = (ImageView)findViewById(R.id.diary_write_save);
		ivRefresh = (ImageView)findViewById(R.id.diary_write_refresh);
		vsSaveAndRefresh = (ViewSwitcher)findViewById(R.id.diary_write_switcher);
		animCircle = AnimationUtils.loadAnimation(this, R.anim.circle);
		llBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ivSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//切换状态
				ivRefresh.startAnimation(animCircle);
				vsSaveAndRefresh.showNext();
				//隐藏输入键盘
				imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				
				//保存日记
				if(saveDiary()){
					Toast.makeText(
							DiaryWriteView.this,
							v.getResources().getString(
									R.string.diary_write_save_success),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(
							DiaryWriteView.this,
							v.getResources().getString(
									R.string.diary_write_save_fail),
							Toast.LENGTH_SHORT).show();
				}
				
				//切换状态
				ivRefresh.clearAnimation();
				vsSaveAndRefresh.showPrevious();
			}
		});
		//初始化文字输入界面
		diaryWriteWordsView.onCreate();
		diaryWritePicFromAlbumView.onCreate();
		diaryWritePicFromCamaraView.onCreate();
		diaryWriteVoiceView.onCreate();
	}
	
	/**
	 * 判断日记是否可以进行保存
	 * @return	如果日记的内容验证无误，则返回true，否则返回false
	 */
	private boolean isDiaryReady(){
		boolean validation = true;
		validation = diaryWriteWordsView.isDiaryWordsReady();
		return validation;
	}
	
	/**
	 * 构建一篇日记
	 * @return	返回构建好的日记
	 */
	private Diary buildDiary(){
		Diary diary = new Diary();
		diary.setContent(diaryWriteWordsView.getContent());
		diary.setCreateDate(new Date());
		diary.setImagePath(imagePath);
		diary.setTimeLineNodeStyleId(TimeLineNodeStyle.getRandomStyleId());
		return diary;
	}
	
	private boolean saveDiary(){
		if(isDiaryReady()){
			Diary diary = buildDiary();
			Session session = db.openSession();
			session.save(diary);
			return true;
		}
		return false;
	}
	
	/**
	 * 显示或者隐藏软键盘
	 * @param viewIndex	当前界面的索引号
	 */
	private void showOrHideImm(int viewIndex){
		if(viewIndex > 0){
			imm.hideSoftInputFromWindow(diaryWriteWordsView.getEtContent()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case ACTIVITY_CODE_IMAGE_FROM_ALBUM:
			Uri uri = data.getData();
			if(uri != null){
				diaryWritePicFromAlbumView.startPicCut(data.getData());
			}
			break;
		case ACTIVITY_CODE_IMAGE_FROM_CAMARA:
//			diaryWritePicView
			break;
		case ACTIVITY_CODE_IMAGE_CUT:
			diaryWritePicFromAlbumView.setPicToView(data);
			break;
		}
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}

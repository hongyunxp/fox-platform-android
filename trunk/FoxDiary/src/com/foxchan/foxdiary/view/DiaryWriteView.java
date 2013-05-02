package com.foxchan.foxdiary.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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
	
	/** 数据库操作对象 */
	private FoxDB db;
	
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
	/** 用图片记录日记的封装对象 */
	private DiaryWritePicView diaryWritePicView;
	/** 用声音记录日记的封装对象 */
	private DiaryWriteVoiceView diaryWriteVoiceView;
	/** 底部的单选框组 */
	private RadioGroup rgMenus;
	/** 返回按钮 */
	private LinearLayout llBack;
	/** 保存按钮 */
	private ImageView ivSave;

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
		
		//初始化界面元素
		diaryWriteWordsView = new DiaryWriteWordsView(this);
		diaryWritePicView = new DiaryWritePicView(this);
		diaryWriteVoiceView = new DiaryWriteVoiceView(this);
		
		pageViews = new ArrayList<View>();
		vWords = diaryWriteWordsView.layoutView();
		vPicFromAlbum = diaryWritePicView.layoutView();
		vPicFromCamara = diaryWritePicView.layoutView();
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
					break;
				case R.id.diary_write_camera:
					viewPager.setCurrentItem(2);
					break;
				case R.id.diary_write_voice:
					viewPager.setCurrentItem(3);
					break;
				}
			}
		});
		//绑定界面切换时的事件
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				((RadioButton)rgMenus.getChildAt(position)).setChecked(true);
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
		llBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ivSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(Constants.DIARY_TAG, "saving the diary...");
				Diary diary = buildDiary();
				Session session = db.openSession();
				session.save(diary);
				Log.d(Constants.DIARY_TAG, "diary has been saved.");
			}
		});
		//初始化文字输入界面
		diaryWriteWordsView.onCreate();
	}
	
	/**
	 * 构建一篇日记
	 * @return	返回构建好的日记
	 */
	private Diary buildDiary(){
		Diary diary = new Diary();
		diary.setContent(diaryWriteWordsView.getContent());
		diary.setCreateDate(new Date());
		diary.setTimeLineNodeStyleId(TimeLineNodeStyle.getRandomStyleId());
		return diary;
	}

}

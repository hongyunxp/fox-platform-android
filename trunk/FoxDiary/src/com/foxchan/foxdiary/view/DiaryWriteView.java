package com.foxchan.foxdiary.view;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
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

import com.foxchan.foxdb.core.FoxDB;
import com.foxchan.foxdb.core.Session;
import com.foxchan.foxdiary.core.AppContext;
import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.core.widgets.FoxConfirmDialog;
import com.foxchan.foxdiary.core.widgets.FoxToast;
import com.foxchan.foxdiary.entity.Diary;
import com.foxchan.foxdiary.entity.Record;
import com.foxchan.foxdiary.entity.TimeLineNodeStyle;
import com.foxchan.foxdiary.exception.DiaryEmptyException;
import com.foxchan.foxdiary.utils.Constants;
import com.foxchan.foxutils.data.StringUtils;
import com.foxchan.foxutils.tool.BitmapUtils;

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
	
	/** 状态：正在保存日记 */
	private static final int STATE_DIARY_SAVING = 0;
	/** 状态：日记保存成功 */
	private static final int STATE_DIARY_SAVED = 1;
	
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
	/** 选择图片的界面 */
	private View vPic;
	/** 录音的界面 */
	private View vVoice;
	/** 用文字记录日记的封装对象 */
	private DiaryWriteWordsView diaryWriteWordsView;
	/** 用图片（从相册）记录日记的封装对象 */
	private DiaryWritePicView diaryWritePicView;
	/** 用声音记录日记的封装对象 */
	private DiaryWriteVoiceView diaryWriteVoiceView;
	/** 底部的单选框组 */
	private RadioGroup rgMenus;
	/** 返回按钮 */
	private LinearLayout llBack;
	/** 返回的确认框 */
	private FoxConfirmDialog cdBack;
	/** 保存按钮 */
	private ImageView ivSave;
	/** 刷新图标 */
	private ImageView ivRefresh;
	/** 保存&刷新状态转换器 */
	private ViewSwitcher vsSaveAndRefresh;
	/** 旋转动画播放器 */
	private Animation animCircle;
	
	/** 图片保存的文件夹路径 */
	private String imagePath;
	/** 图片的文件名 */
	private String imageName;
	/** 图片对象 */
	private Bitmap image;
	/** 录音保存的文件夹路径 */
	private String voicePath;
	/** 录音文件的时长 */
	private long voiceLength;
	/** 日记保存的结果，成功或者失败 */
	private boolean isDiarySaveSuccess = false;
	
	private MyHandler handler = new MyHandler(this);
	static class MyHandler extends Handler{
		WeakReference<DiaryWriteView> reference;
		
		public MyHandler(DiaryWriteView activity){
			this.reference = new WeakReference<DiaryWriteView>(activity);
		}
		
		@Override
		public void handleMessage(android.os.Message msg) {
			DiaryWriteView activity = reference.get();
			switch (msg.what) {
			case STATE_DIARY_SAVING:
				activity.diarySaving();
				break;
			case STATE_DIARY_SAVED:
				activity.diarySaved();
				break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary_write);
		init(savedInstanceState);
	}

	/**
	 * 初始化该界面
	 */
	private void init(Bundle savedInstanceState) {
		//初始化数据库连接对象
		FoxDB.DEBUG = Constants.DIARY_DEBUG;
		db = FoxDB.create(this, Constants.DIARY_DB_NAME, Constants.DIARY_DB_VERSION);
		
		//初始化键盘管理对象
		imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		
		//初始化界面元素
		diaryWriteWordsView = new DiaryWriteWordsView(this);
		diaryWritePicView = new DiaryWritePicView(this);
		diaryWriteVoiceView = new DiaryWriteVoiceView(this);
		
		pageViews = new ArrayList<View>();
		vWords = diaryWriteWordsView.layoutView();
		vPic = diaryWritePicView.layoutView();
		vVoice = diaryWriteVoiceView.layoutView();
		pageViews.add(0, vWords);
		pageViews.add(1, vPic);
		pageViews.add(2, vVoice);
		
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
				case R.id.diary_write_voice:
					viewPager.setCurrentItem(2);
					showOrHideImm(2);
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
				leaveView();
			}
		});
		ivSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.sendEmptyMessage(STATE_DIARY_SAVING);
			}
		});
		//初始化文字输入界面
		diaryWriteWordsView.onCreate(savedInstanceState);
		diaryWritePicView.onCreate(savedInstanceState);
		diaryWriteVoiceView.onCreate(savedInstanceState);
		
		//初始化返回的确认框
		cdBack = new FoxConfirmDialog(this, getString(R.string.diary_write_back_confirm));
		cdBack.setOnNegativeButtonClickListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		cdBack.setOnPositiveButtonClickListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//删除录得声音临时文件
				File voiceFile = new File(voicePath);
				if(voiceFile != null && voiceFile.exists()){
					voiceFile.delete();
				}
				finish();
			}
		});
	}
	
	/**
	 * 正在保存日记
	 */
	public void diarySaving(){
		//切换状态
		ivRefresh.startAnimation(animCircle);
		vsSaveAndRefresh.showNext();
		//隐藏输入键盘
		imm.hideSoftInputFromWindow(ivSave.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		
		//保存日记
		try {
			isDiarySaveSuccess = saveDiary();
			handler.sendEmptyMessage(STATE_DIARY_SAVED);
		} catch (DiaryEmptyException e) {
			String errMsg = String.format(getString(R.string.diary_write_save_fail), e.getMessage());
			FoxToast.showToast(this, errMsg, Toast.LENGTH_SHORT);
			//将状态恢复
			ivRefresh.clearAnimation();
			vsSaveAndRefresh.showPrevious();
			e.printStackTrace();
		}
	}
	
	/**
	 * 日记保存完毕
	 */
	public void diarySaved(){
		if(isDiarySaveSuccess){
			FoxToast.showToast(
					DiaryWriteView.this,
					getResources().getString(
							R.string.diary_write_save_success),
					Toast.LENGTH_SHORT);
			finish();
		}
		
		//切换状态
		ivRefresh.clearAnimation();
		vsSaveAndRefresh.showPrevious();
	}
	
	/**
	 * 判断日记是否可以进行保存
	 * @return	如果日记的内容验证无误，则返回true，否则返回false
	 */
	private boolean isDiaryReady() throws DiaryEmptyException{
		boolean validation = true;
		if(diaryWriteWordsView.isWordsExist() || 
				diaryWritePicView.isPicExist() ||
				diaryWriteVoiceView.isAudioFileExist()){
			validation = true;
		} else {
			validation = false;
			throw new DiaryEmptyException(this, R.string.ex_diary_empty);
		}
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
		String targetPath = StringUtils.concat(new Object[]{imagePath, imageName});
		targetPath = targetPath.replaceAll("//", "/");
		diary.setImagePath(targetPath);
		if(diaryWriteVoiceView.isAudioFileExist()){
			Record record = new Record(voicePath, voiceLength);
			Session session = db.getCurrentSession();
			session.save(record);
			diary.setRecord(record);
		} else {
			diary.setRecord(null);
		}
		diary.setTimeLineNodeStyleId(TimeLineNodeStyle.getRandomStyleId());
		return diary;
	}
	
	private boolean saveDiary() throws DiaryEmptyException{
		if(isDiaryReady()){
			Diary diary = buildDiary();
			Session session = db.getCurrentSession();
			session.save(diary);
			//保存图片
			BitmapUtils.persistImageToSdCard(imagePath, imageName, image);
			//删除临时的照片
			File tempImage = new File(Constants.buildDiaryTempImagePath());
			if(tempImage != null && tempImage.exists()){
				tempImage.delete();
			}
			//将添加的日记添加到公共容器中
			AppContext.addDiaryToDiaryLineView(diary);
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
		//super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			Uri uri = null;
			switch(requestCode){
			case ACTIVITY_CODE_IMAGE_FROM_ALBUM:
				Log.d(Constants.DIARY_TAG, "从相册返回照片了。");
				diaryWritePicView.dealWithImage(data.getData());
				break;
			case ACTIVITY_CODE_IMAGE_FROM_CAMARA:
				Log.d(Constants.DIARY_TAG, "从相机返回照片了。");
				File tempImage = new File(Constants.buildDiaryTempImagePath());
				uri = Uri.fromFile(tempImage);
				if(uri != null){
					diaryWritePicView.dealWithImage(uri);
				}
				break;
			}
		}
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public String getVoicePath() {
		return voicePath;
	}

	public void setVoicePath(String voicePath) {
		this.voicePath = voicePath;
	}

	public void setVoiceLength(long voiceLength) {
		this.voiceLength = voiceLength;
	}

	@Override
	protected void onDestroy() {
		diaryWriteWordsView.onDestroy();
		diaryWritePicView.onDestroy();
		diaryWriteVoiceView.onDestroy();
		if(image != null){
			image.recycle();
			image = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		diaryWriteWordsView.onResume();
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			leaveView();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 离开当前的界面
	 */
	private void leaveView() {
		if(!StringUtils.isEmpty(diaryWriteWordsView.getContent()) ||
				!StringUtils.isEmpty(imagePath) ||
				diaryWriteVoiceView.isAudioFileExist()){
			cdBack.show();
		} else {
			finish();
		}
	}

}

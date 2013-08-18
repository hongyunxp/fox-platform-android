package com.foxchan.foxdiary.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.foxchan.foxdb.core.FoxDB;
import com.foxchan.foxdb.core.Session;
import com.foxchan.foxdb.engine.Pager;
import com.foxchan.foxdiary.core.widgets.FoxActivity;
import com.foxchan.foxdiary.entity.Diary;
import com.foxchan.foxdiary.utils.Constants;
import com.foxchan.foxutils.data.CollectionUtils;

/**
 * 日记时间线界面
 * @author gulangxiangjie@gmail.com
 * @create 2013-4-16
 */
public class DiaryLineView extends FoxActivity {
	
	public static final String TAG = "DiaryLineView";
	
	/** 状态：正在删除日记 */
	public static final int STATE_DIARY_DELETING = 0;
	/** 状态：删除日记结束 */
	public static final int STATE_DIARY_DELETED = 1;
	
	/** 竖版日记的界面 */
	private DiaryPortraitView diaryPortraitView;
	/** 横版日记的界面 */
	private DiaryLandscapeView diaryLandscapeView;
	
	private FoxDB db;
	private Session session;
	/** 日记的数据集 */
	private List<Diary> diaries = new ArrayList<Diary>();
	/** 用户所有的日记的日期列表 */
	private static List<Date> diaryDates;
	/** 日记的分页对象 */
	private Pager<Diary> pager;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		diaryPortraitView = new DiaryPortraitView(this);
		diaryLandscapeView = new DiaryLandscapeView(this);
		flushContentView(getResources().getConfiguration());
		//初始化组件和数据
		db = FoxDB.create(this, Constants.DIARY_DB_NAME, Constants.DIARY_DB_VERSION);
		session = db.getCurrentSession();
		initDatas();
		diaryPortraitView.onCreate(savedInstanceState);
		diaryLandscapeView.onCreate(savedInstanceState);
	}

	/**
	 * 更新当前的界面布局
	 * @param newConfig	配置信息对象
	 * @create 2013年8月14日
	 * @modify 2013年8月14日
	 * @author foxchan@live.cn
	 */
	private void flushContentView(Configuration configuration){
		if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
			//切换到竖屏
			setContentView(diaryPortraitView.layoutView());
			Log.d(TAG, "当前切换到竖屏模式。");
		} else if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
			//切换到横屏
			setContentView(diaryLandscapeView.layoutView());
			Log.d(TAG, "当前切换到横屏模式。");
		}
	}
	
	/**
	 * 跳转到写日记的界面
	 * @param diaryId	日记的id号
	 */
	public void toDiaryWriteView(String diaryId) {
		Intent intent = new Intent(this, DiaryWriteView.class);
		intent.putExtra(Constants.TAG_DIARY_ID, diaryId);
		startActivity(intent);
	}

	/**
	 * 初始化数据
	 */
	private void initDatas() {
		//初始化日记的日期列表
		String sql = "SELECT * FROM tb_core_diary WHERE 1=1 GROUP BY substr(createDate,1,11) ORDER BY createDate asc";
		List<Diary> tempDiaries = session.executeQuery(sql, Diary.class);
		if(!CollectionUtils.isEmpty(tempDiaries)){
			diaryDates = new ArrayList<Date>();
			for(Diary d : tempDiaries){
				diaryDates.add(d.getCreateDate());
			}
		}
		//初始化默认显示的日记
		pager = new Pager<Diary>(Constants.DIARY_RECORD_NUMBER, 1);
		diaries = (List<Diary>)getLastNonConfigurationInstance();
		if(diaries == null){
			diaries = new ArrayList<Diary>();
			loadDiaries();
		}
	}
	
	/**
	 * 加载日记
	 */
	public void loadDiaries(){
		session.query(pager, null, null, null, Diary.class);
		diaries.addAll(pager.getContent());
		Log.d(TAG, "加载日记");
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return this.diaries;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		flushContentView(newConfig);
	}

	@Override
	protected void onResume() {
		int orientation = getResources().getConfiguration().orientation;
		if(orientation == Configuration.ORIENTATION_PORTRAIT){
			diaryPortraitView.onResume();
		} else if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
			diaryLandscapeView.onResume();
		}
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		int orientation = getResources().getConfiguration().orientation;
		if(orientation == Configuration.ORIENTATION_PORTRAIT){
			diaryPortraitView.onDestroy();
		} else if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
			diaryLandscapeView.onDestroy();
		}
		super.onDestroy();
	}
	
	/**
	 * 获得分页对象
	 * @return	返回日记的分页对象
	 * @create 2013年8月13日
	 * @modify 2013年8月13日
	 * @author foxchan@live.cn
	 */
	public Pager<Diary> getPager(){
		return this.pager;
	}
	
	/**
	 * 获得日记列表
	 * @return	返回日记列表
	 * @create 2013年8月13日
	 * @modify 2013年8月13日
	 * @author foxchan@live.cn
	 */
	public List<Diary> getDiaries(){
		return this.diaries;
	}
	
}

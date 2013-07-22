package com.foxchan.foxdiary.view;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foxchan.foxdb.core.FoxDB;
import com.foxchan.foxdb.core.Session;
import com.foxchan.foxdb.engine.Pager;
import com.foxchan.foxdiary.adapter.DiaryLineAdapter;
import com.foxchan.foxdiary.adapter.DiaryLineAdapter.NodeListener;
import com.foxchan.foxdiary.core.AppContext;
import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.core.widgets.FoxConfirmDialog;
import com.foxchan.foxdiary.core.widgets.FoxToast;
import com.foxchan.foxdiary.core.widgets.RefreshListView;
import com.foxchan.foxdiary.entity.Diary;
import com.foxchan.foxdiary.entity.Record;
import com.foxchan.foxdiary.utils.Constants;
import com.foxchan.foxutils.data.CollectionUtils;
import com.foxchan.foxutils.data.DateUtils;
import com.foxchan.foxutils.data.StringUtils;
import com.foxchan.foxutils.tool.FileUtils;

/**
 * 日记时间线界面
 * @author gulangxiangjie@gmail.com
 * @create 2013-4-16
 */
public class DiaryLineView extends Activity {
	
	/** 状态：正在删除日记 */
	private static final int STATE_DIARY_DELETING = 0;
	/** 状态：删除日记结束 */
	private static final int STATE_DIARY_DELETED = 1;
	
	/** 日记的列表 */
	private RefreshListView lvDiarys;
	/** 日记列表的数据适配器 */
	private DiaryLineAdapter diaryLineAdapter;
	/** 日期控件 */
	private TextView tvShowDate;
	/** 添加日记的按钮 */
	private ImageButton ibAddDiary;
	/** 加载中的图片 */
	private ImageView ivLoading;
	/** 加载中的动画 */
	private Animation aniLoading;
	/** 确认删除的对话框控件 */
	private FoxConfirmDialog fcdDeleteDiary;
	
	private FoxDB db;
	private Session session;
	/** 日记的数据集 */
	private List<Diary> diaries = new ArrayList<Diary>();
	/** 当前显示的日期 */
	private Date showDate;
	/** 用户所有的日记的日期列表 */
	private static List<Date> diaryDates;
	/** 日记的分页对象 */
	private Pager<Diary> pager;
	/** 删除日记的线程 */
	private Thread threadDeleteDiary;
	/** 正在被操作的日记的索引号 */
	private int activeDiaryIndex = -1;
	/** 对日记的操作的索引号 */
	private int actionIndex = Constants.ACTION_NONE;
	
	private MyHandler handler = new MyHandler(this);
	static class MyHandler extends Handler{
		WeakReference<DiaryLineView> reference;
		
		public MyHandler(DiaryLineView activity){
			this.reference = new WeakReference<DiaryLineView>(activity);
		}
		
		@Override
		public void handleMessage(android.os.Message msg) {
			DiaryLineView activity = reference.get();
			switch (msg.what) {
			case STATE_DIARY_DELETING:
				activity.showLoadingView();
				activity.onDiaryDeleting();
				break;
			case STATE_DIARY_DELETED:
				activity.showAddDiaryButton();
				activity.afterDiaryDeleted();
				break;
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary_line);
		//初始化组件和数据
		db = FoxDB.create(this, Constants.DIARY_DB_NAME, Constants.DIARY_DB_VERSION);
		session = db.getCurrentSession();
		initDatas();
		initWidgets();
	}

	/**
	 * 初始化组件
	 */
	private void initWidgets() {
		lvDiarys = (RefreshListView)findViewById(R.id.diary_line_listview);
		//绑定下拉刷新和上划加载的事件
		lvDiarys.setOnTaskDoingListener(new RefreshListView.OnTaskDoingListener() {
			
			@Override
			public void refreshingData(RefreshListView view) {
//				pager = new Pager<Diary>(Constants.DIARY_RECORD_NUMBER, 1);
//				List<Diary> nDiaries = pager.getContent();
//				int newDiaryCount = 0;
//				if(!CollectionUtils.isEmpty(nDiaries)){
//					for(Diary d : nDiaries){
//						boolean b = false;
//						for(Diary diary : diaries){
//							if(d.getId().equals(diary.getId())){
//								b = true;
//								break;
//							}
//						}
//						if(b == false){
//							diaries.add(0, d);
//							newDiaryCount++;
//						}
//					}
//				}
//				FoxToast.showToast(view.getContext(),
//						String.format(
//								getResources().getString(
//										R.string.pull_to_refresh_result),
//								newDiaryCount), Toast.LENGTH_SHORT);
				view.refreshingDataComplete();
			}
			
			@Override
			public void loadMoreData(RefreshListView view) {
				if(pager.hasNextPage()){
					pager.nextPage();
					loadDiaries();
				} else {
					view.loadMoreDataComplete();
				}
			}
		});
		
		diaryLineAdapter = new DiaryLineAdapter(this, diaries, session);
		//绑定操作日记的事件
		diaryLineAdapter.setNodeListener(new NodeListener() {
			
			@Override
			public void onShare(int position) {
				if(DateUtils.isBeforeToday(diaries.get(position).getCreateDate())){
//					Diary diary = diaries.get(position);
				} else {
					FoxToast.showException(getApplicationContext(),
							R.string.ex_diary_cant_be_modify,
							Toast.LENGTH_SHORT);
				}
			}
			
			@Override
			public void onEdit(int position) {
				if(DateUtils.isBeforeToday(diaries.get(position).getCreateDate())){
					FoxToast.showException(getApplicationContext(),
							R.string.ex_diary_cant_be_modify,
							Toast.LENGTH_SHORT);
				} else {
					Diary diary = diaries.get(position);
					actionIndex = Constants.ACTION_UPDATE;
					activeDiaryIndex = position;
					toDiaryWriteView(diary.getId());
				}
			}
			
			@Override
			public void onDelete(int position) {
				activeDiaryIndex = position;
				fcdDeleteDiary.show();
			}
		});
		lvDiarys.setAdapter(diaryLineAdapter);
		
		tvShowDate = (TextView)findViewById(R.id.diary_line_header_title);
		
		//设置显示的日期为当前的日期
		if(CollectionUtils.isEmpty(diaries)){
			showDate = new Date();
		} else {
			showDate = diaries.get(0).getCreateDate();
		}
		String showDateStr = DateUtils.formatDate(showDate, "yyyy年MM月dd日");
		tvShowDate.setText(showDateStr);
		
		ibAddDiary = (ImageButton)findViewById(R.id.diary_line_add);
		ibAddDiary.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				actionIndex = Constants.ACTION_INSERT;
				toDiaryWriteView("");
			}
		});
		//初始化加载中的相关资源
		ivLoading = (ImageView)findViewById(R.id.loading);
		aniLoading = AnimationUtils.loadAnimation(this, R.anim.circle);
		
		if(pager.getTotalPage() <= 1){
			lvDiarys.loadMoreDataComplete();
		}
		//初始化删除日记的线程
		threadDeleteDiary = new Thread(){
			public void run(){
				if(activeDiaryIndex >= 0){
					Diary deleteDiary = diaries.get(activeDiaryIndex);
					//删除日记的图片资源
					FileUtils.deleteFile(deleteDiary.getImagePath());
					//删除日记的录音资源
					session = db.getCurrentSession();
					if(deleteDiary.hasVoice(session)){
						Record deleteRecord = session.findObjectFrom(deleteDiary, "record", Record.class);
						FileUtils.deleteFile(deleteRecord.getPath());
						session.delete(deleteRecord);
					}
					//删除日记在数据库中的记录
					session.delete(deleteDiary);
					handler.sendEmptyMessage(STATE_DIARY_DELETED);
				}
			}
		};
		//初始化删除日记的警告框控件
		fcdDeleteDiary = new FoxConfirmDialog(DiaryLineView.this, getString(R.string.diary_line_delete_comfirm));
		fcdDeleteDiary.setOnPositiveButtonClickListener(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler.sendEmptyMessage(STATE_DIARY_DELETING);
			}
		}).setOnNegativeButtonClickListener(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
	}
	
	/**
	 * 跳转到写日记的界面
	 * @param diaryId	日记的id号
	 */
	private void toDiaryWriteView(String diaryId) {
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
		loadDiaries();
	}
	
	/**
	 * 加载日记
	 */
	private void loadDiaries(){
		session.query(pager, null, null, null, Diary.class);
		diaries.addAll(pager.getContent());
		AppContext.addDiaryToDiaryLineView(diaries);
	}

	@Override
	protected void onResume() {
		if(AppContext.tempDiary != null && !StringUtils.isEmpty(AppContext.tempDiary.getId())){
			if(actionIndex == Constants.ACTION_INSERT){
				diaries.add(AppContext.tempDiary);
			} else if(actionIndex == Constants.ACTION_UPDATE){
				diaries.get(activeDiaryIndex).flush(AppContext.tempDiary);
			}
		}
		AppContext.tempDiary = null;
		diaryLineAdapter.notifyDataSetChanged();
		super.onResume();
	}
	
	/**
	 * 显示加载动画
	 */
	private void showLoadingView(){
		ibAddDiary.setVisibility(View.GONE);
		ivLoading.setVisibility(View.VISIBLE);
		ivLoading.clearAnimation();
		ivLoading.startAnimation(aniLoading);
	}
	
	/**
	 * 显示添加日记的按钮7
	 */
	private void showAddDiaryButton(){
		ivLoading.clearAnimation();
		ivLoading.setVisibility(View.GONE);
		ibAddDiary.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 删除日记
	 */
	private void onDiaryDeleting(){
		if(threadDeleteDiary.getState() == Thread.State.NEW){
			threadDeleteDiary.start();
		} else {
			threadDeleteDiary.run();
		}
	}
	
	/**
	 * 日记被删除之后进行的操作
	 */
	private void afterDiaryDeleted(){
		Diary diary = diaries.get(activeDiaryIndex);
		AppContext.diaryMapForShow.remove(diary.getId());
		diaries.remove(activeDiaryIndex);
		diaryLineAdapter.notifyDataSetChanged();
		FoxToast.showToast(this, R.string.diary_line_delete_success, Toast.LENGTH_SHORT);
		activeDiaryIndex = -1;
	}
	
}

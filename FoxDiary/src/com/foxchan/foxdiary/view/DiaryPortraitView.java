package com.foxchan.foxdiary.view;

import java.lang.ref.WeakReference;
import java.util.Date;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foxchan.foxdb.core.FoxDB;
import com.foxchan.foxdb.core.Session;
import com.foxchan.foxdiary.adapter.DiaryLineAdapter;
import com.foxchan.foxdiary.adapter.DiaryLineAdapter.NodeListener;
import com.foxchan.foxdiary.core.AppContext;
import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.core.widgets.FakeActivity;
import com.foxchan.foxdiary.core.widgets.FoxConfirmDialog;
import com.foxchan.foxdiary.core.widgets.FoxToast;
import com.foxchan.foxdiary.core.widgets.RefreshListView;
import com.foxchan.foxdiary.entity.Diary;
import com.foxchan.foxdiary.entity.Record;
import com.foxchan.foxdiary.utils.Constants;
import com.foxchan.foxutils.data.DateUtils;
import com.foxchan.foxutils.data.StringUtils;
import com.foxchan.foxutils.tool.FileUtils;

/**
 * 竖版的日记展示界面
 * @create 2013年8月13日
 * @author foxchan@live.cn
 * @version 1.0.0
 */
public class DiaryPortraitView extends FakeActivity {
	
	private DiaryLineView diaryLineView;
	private View layoutView;
	private FoxDB db;
	private Session session;
	
	/** 添加日记的按钮 */
	private ImageButton ibAddDiary;
	/** 加载中的图片 */
	private ImageView ivLoading;
	/** 加载中的动画 */
	private Animation aniLoading;
	/** 确认删除的对话框控件 */
	private FoxConfirmDialog fcdDeleteDiary;
	/** 日记的列表 */
	private RefreshListView lvDiarys;
	/** 日记列表的数据适配器 */
	private DiaryLineAdapter diaryLineAdapter;
	/** 日期控件 */
	private TextView tvShowDate;
	/** 删除日记的线程 */
	private Thread threadDeleteDiary;
	
	/** 正在被操作的日记的索引号 */
	private int activeDiaryIndex = -1;
	/** 对日记的操作的索引号 */
	private int actionIndex = Constants.ACTION_NONE;
	/** 当前显示的日期 */
	private Date showDate;
	
	private MyHandler handler = new MyHandler(this);
	static class MyHandler extends Handler{
		WeakReference<DiaryPortraitView> reference;
		
		public MyHandler(DiaryPortraitView activity){
			this.reference = new WeakReference<DiaryPortraitView>(activity);
		}
		
		@Override
		public void handleMessage(android.os.Message msg) {
			DiaryPortraitView activity = reference.get();
			switch (msg.what) {
			case DiaryLineView.STATE_DIARY_DELETING:
				activity.showLoadingView();
				activity.onDiaryDeleting();
				break;
			case DiaryLineView.STATE_DIARY_DELETED:
				activity.showAddDiaryButton();
				activity.afterDiaryDeleted();
				break;
			}
		}
	}
	
	/**
	 * 构造一个竖版的日记界面
	 * @param diaries	日记列表
	 */
	public DiaryPortraitView(DiaryLineView diaryLineView) {
		this.diaryLineView = diaryLineView;
		db = FoxDB.create(diaryLineView, Constants.DIARY_DB_NAME, Constants.DIARY_DB_VERSION);
		session = db.getCurrentSession();
	}

	@Override
	public View layoutView() {
		if(this.layoutView == null){
			layoutView = LayoutInflater.from(diaryLineView).inflate(R.layout.diary_line, null);
		}
		return this.layoutView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		initWidgets();
	}
	
	/**
	 * 初始化组件
	 */
	private void initWidgets() {
		lvDiarys = (RefreshListView)layoutView().findViewById(R.id.diary_line_listview);
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
				if(diaryLineView.getPager().hasNextPage()){
					diaryLineView.getPager().nextPage();
					diaryLineView.loadDiaries();
				} else {
					view.loadMoreDataComplete();
				}
			}
		});
		
		diaryLineAdapter = new DiaryLineAdapter(diaryLineView, diaryLineView.getDiaries(), session);
		//绑定操作日记的事件
		diaryLineAdapter.setNodeListener(new NodeListener() {
			
			@Override
			public void onShare(int position) {
				if(DateUtils.isBeforeToday(diaryLineView.getDiaries().get(position).getCreateDate())){
//					Diary diary = diaries.get(position);
				} else {
					FoxToast.showException(diaryLineView.getApplicationContext(),
							R.string.ex_diary_cant_be_modify,
							Toast.LENGTH_SHORT);
				}
			}
			
			@Override
			public void onEdit(int position) {
				if(DateUtils.isBeforeToday(diaryLineView.getDiaries().get(position).getCreateDate())){
					FoxToast.showException(diaryLineView.getApplicationContext(),
							R.string.ex_diary_cant_be_modify,
							Toast.LENGTH_SHORT);
				} else {
					Diary diary = diaryLineView.getDiaries().get(position);
					actionIndex = Constants.ACTION_UPDATE;
					activeDiaryIndex = position;
					diaryLineView.toDiaryWriteView(diary.getId());
				}
			}
			
			@Override
			public void onDelete(int position) {
				activeDiaryIndex = position;
				fcdDeleteDiary.show();
			}
		});
		lvDiarys.setAdapter(diaryLineAdapter);
		
		tvShowDate = (TextView)layoutView().findViewById(R.id.diary_line_header_title);
		
		//设置显示的日期为当前的日期
//		if(CollectionUtils.isEmpty(diaries)){
//			showDate = new Date();
//		} else {
//			showDate = diaries.get(0).getCreateDate();
//		}
		showDate = new Date();
		String showDateStr = DateUtils.formatDate(showDate, "yyyy年MM月dd日");
		tvShowDate.setText(showDateStr);
		
		ibAddDiary = (ImageButton)layoutView().findViewById(R.id.diary_line_add);
		ibAddDiary.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				actionIndex = Constants.ACTION_INSERT;
				diaryLineView.toDiaryWriteView("");
			}
		});
		//初始化加载中的相关资源
		ivLoading = (ImageView)layoutView().findViewById(R.id.loading);
		aniLoading = AnimationUtils.loadAnimation(diaryLineView, R.anim.circle);
		
		if(diaryLineView.getPager().getTotalPage() <= 1){
			lvDiarys.loadMoreDataComplete();
		}
		//初始化删除日记的线程
		threadDeleteDiary = new Thread(){
			public void run(){
				if(activeDiaryIndex >= 0){
					Diary deleteDiary = diaryLineView.getDiaries().get(activeDiaryIndex);
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
					handler.sendEmptyMessage(DiaryLineView.STATE_DIARY_DELETED);
				}
			}
		};
		//初始化删除日记的警告框控件
		fcdDeleteDiary = new FoxConfirmDialog(diaryLineView, diaryLineView.getString(R.string.diary_line_delete_comfirm));
		fcdDeleteDiary.setOnPositiveButtonClickListener(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler.sendEmptyMessage(DiaryLineView.STATE_DIARY_DELETING);
			}
		}).setOnNegativeButtonClickListener(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
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
		Diary diary = diaryLineView.getDiaries().get(activeDiaryIndex);
		AppContext.diaryMapForShow.remove(diary.getId());
		diaryLineView.getDiaries().remove(activeDiaryIndex);
		diaryLineAdapter.notifyDataSetChanged();
		FoxToast.showToast(diaryLineView, R.string.diary_line_delete_success, Toast.LENGTH_SHORT);
		activeDiaryIndex = -1;
	}

	@Override
	public void onResume() {
		if(AppContext.tempDiary != null && !StringUtils.isEmpty(AppContext.tempDiary.getId())){
			if(actionIndex == Constants.ACTION_INSERT){
				diaryLineView.getDiaries().add(AppContext.tempDiary);
			} else if(actionIndex == Constants.ACTION_UPDATE){
				diaryLineView.getDiaries().get(activeDiaryIndex).flush(AppContext.tempDiary);
			}
		}
		AppContext.tempDiary = null;
		diaryLineAdapter.notifyDataSetChanged();
	}

	@Override
	public void onDestroy() {
		if(fcdDeleteDiary != null){
			fcdDeleteDiary.dismiss();
		}
	}

}

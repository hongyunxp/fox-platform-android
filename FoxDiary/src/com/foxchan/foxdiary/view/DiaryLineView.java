package com.foxchan.foxdiary.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.lezhixing.foxdb.core.FoxDB;
import cn.com.lezhixing.foxdb.core.Session;
import cn.com.lezhixing.foxdb.engine.Pager;

import com.foxchan.foxdiary.adapter.DiaryLineAdapter;
import com.foxchan.foxdiary.adapter.DiaryLineAdapter.NodeListener;
import com.foxchan.foxdiary.core.AppContext;
import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.core.widgets.FoxToast;
import com.foxchan.foxdiary.core.widgets.RefreshListView;
import com.foxchan.foxdiary.entity.Diary;
import com.foxchan.foxdiary.utils.Constants;
import com.foxchan.foxutils.data.CollectionUtils;
import com.foxchan.foxutils.data.DateUtils;

/**
 * 日记时间线界面
 * @author gulangxiangjie@gmail.com
 * @create 2013-4-16
 */
public class DiaryLineView extends Activity {
	
	/** 日记的数据集 */
	private List<Diary> diaries = new ArrayList<Diary>();
	/** 日记的列表 */
	private RefreshListView lvDiarys;
	/** 日记列表的数据适配器 */
	private DiaryLineAdapter diaryLineAdapter;
	/** 日期控件 */
	private TextView tvShowDate;
	/** 当前显示的日期 */
	private Date showDate;
	/** 用户所有的日记的日期列表 */
	private static List<Date> diaryDates;
	/** 日记的分页对象 */
	private Pager<Diary> pager;
	
	private FoxDB db;
	private Session session;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary_line);
//		FoxDB.DEBUG = true;
		//初始化组件和数据
		db = FoxDB.create(this, Constants.DIARY_DB_NAME, Constants.DIARY_DB_VERSION);
		session = db.openSession();
		initDatas();
		initWidgets();
	}

	/**
	 * 初始化组件
	 */
	private void initWidgets() {
		lvDiarys = (RefreshListView)findViewById(R.id.diary_line_listview);
		lvDiarys.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				
			}
		});
		lvDiarys.setOnTaskDoingListener(new RefreshListView.OnTaskDoingListener() {
			
			@Override
			public void refreshingData(RefreshListView view) {
				view.setExistMoreData(true);
				pager = new Pager<Diary>(4, 1);
				diaries.clear();
				loadDiaries();
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
		
		diaryLineAdapter = new DiaryLineAdapter(this, diaries);
		diaryLineAdapter.setNodeListener(new NodeListener() {
			
			@Override
			public void share(int position) {
				Diary diary = diaries.get(position);
				FoxToast.showToast(DiaryLineView.this, "分享的日记的标题是：" + diary.getTitle(), Toast.LENGTH_SHORT);
				Log.d(Constants.DIARY_TAG, "分享日记");
				diaryLineAdapter.notifyAll();
			}
			
			@Override
			public void edit(int position) {
				Diary diary = diaries.get(position);
				FoxToast.showToast(DiaryLineView.this, "编辑的日记的标题是：" + diary.getTitle(), Toast.LENGTH_SHORT);
			}
			
			@Override
			public void delete(int position) {
				Diary diary = diaries.get(position);
				FoxToast.showToast(DiaryLineView.this, "删除的日记的标题是：" + diary.getTitle(), Toast.LENGTH_SHORT);
			}
		});
		lvDiarys.setAdapter(diaryLineAdapter);
		
		tvShowDate = (TextView)findViewById(R.id.diary_line_header_title);
		
		//设置显示的日期为当前的日期
		String showDateStr = DateUtils.formatDate(showDate, "yyyy年MM月dd日");
		tvShowDate.setText(showDateStr);
	}
	
	/**
	 * 跳转到写日记的界面
	 */
	private void toDiaryWriteView() {
		Intent intent = new Intent(this, DiaryWriteView.class);
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
		pager = new Pager<Diary>(10, 1);
		loadDiaries();
	}
	
	/**
	 * 加载日记
	 */
	private void loadDiaries(){
		session.query(pager, null, null, null, Diary.class);
		diaries.addAll(pager.getContent());
		AppContext.diariesOnDiaryLineView = diaries;
		Log.d(Constants.DIARY_TAG, "总共找到" + pager.getTotalRecordsNumber() + "篇日记，当前是第" + pager.getCurrentPage() + "页，总共"  + pager.getTotalPage() + "页");
		Log.d(Constants.DIARY_TAG, "当前页有" + diaries.size() + "篇日记。");
	}

	@Override
	protected void onResume() {
		if(AppContext.diariesOnDiaryLineView.size() != diaries.size()){
			diaryLineAdapter.notifyDataSetChanged();
			diaries = AppContext.diariesOnDiaryLineView;
		}
		super.onResume();
	}
	
}

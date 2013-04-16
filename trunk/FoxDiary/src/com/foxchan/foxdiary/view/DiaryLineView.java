package com.foxchan.foxdiary.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.foxchan.foxdiary.adapter.DiaryLineAdapter;
import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.entity.Diary;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * 日记时间线界面
 * @author gulangxiangjie@gmail.com
 * @create 2013-4-16
 */
public class DiaryLineView extends Activity {
	
	/** 日记的数据集 */
	private List<Diary> diaries;
	/** 日记的列表 */
	private ListView lvDiarys;
	/** 日记列表的数据适配器 */
	private DiaryLineAdapter diaryLineAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary_line);
		//初始化组件和数据
		initDatas();
		initWidgets();
	}

	/**
	 * 初始化组件
	 */
	private void initWidgets() {
		lvDiarys = (ListView)findViewById(R.id.diary_line_listview);
		diaryLineAdapter = new DiaryLineAdapter(this, diaries);
		lvDiarys.setAdapter(diaryLineAdapter);
	}

	/**
	 * 初始化数据
	 */
	private void initDatas() {
		diaries = new ArrayList<Diary>();
		for(int i = 0; i < 10; i++){
			Diary diary = new Diary();
			diary.setContent("正文内容" + i);
			diary.setCreateDate(new Date());
			diary.setEmotion(0);
			diary.setId(i + "");
			diary.setImagePath("/path/...");
			diary.setTitle("标题" + i);
			diaries.add(diary);
		}
	}

}

package com.foxchan.foxdiary.view;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.foxchan.foxdiary.adapter.DiaryLineAdapter;
import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.entity.Diary;
import com.foxchan.foxutils.data.DateUtils;

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
		lvDiarys.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				if(position < diaries.size()){
					//点击的日记
					Toast.makeText(
							v.getContext(),
							"你你现在点击的是第" + position + "篇日记，日记的标题是："
									+ diaries.get(position).getTitle(),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(
							v.getContext(),
							"添加一篇日记", Toast.LENGTH_SHORT).show();
				}
			}
		});
		lvDiarys.setAdapter(diaryLineAdapter);
	}

	/**
	 * 初始化数据
	 */
	private void initDatas() {
		diaries = new ArrayList<Diary>();
		for(int i = 0; i < 10; i++){
			Date createDate = new Date();
			try {
				createDate = DateUtils.generateDateFrom("2013-4-17 " + (i + 8) + ":35:00");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Diary diary = new Diary();
			diary.setContent("正文内容" + i);
			diary.setCreateDate(createDate);
			diary.setEmotion(0);
			diary.setId(i + "");
			diary.setImagePath("/path/...");
			diary.setTitle("标题" + i);
			diaries.add(diary);
		}
	}

}

package com.foxchan.foxdiary.core;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.foxchan.foxdiary.entity.Diary;
import com.foxchan.foxdiary.view.DiaryLandscapeView;
import com.foxchan.foxdiary.view.DiaryLineView;

public class AppStart extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary_landscape);
		toDiaryLineView();
		/*List<Diary> diaries = new ArrayList<Diary>();
		for(int i = 0; i < 4; i++){
			Diary diary = new Diary();
			diary.setContent("这是日记" + i + "的内容。");
			diary.setLocation("这是第" + i + "个地址");
			diaries.add(diary);
		}
		DiaryLandscapeView diaryLandscapeView = new DiaryLandscapeView(this, diaries);
		diaryLandscapeView.onCreate(savedInstanceState);
		setContentView(diaryLandscapeView.layoutView());*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.app_start, menu);
		return true;
	}
	
	/**
	 * 跳转到日记轴截面
	 */
	private void toDiaryLineView(){
		Intent intent = new Intent(this, DiaryLineView.class);
		startActivity(intent);
		finish();
	}
	
}

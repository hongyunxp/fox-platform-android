package com.foxchan.foxdiary.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.foxchan.foxdiary.view.DiaryLineView;

public class AppStart extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary_landscape);
//		toDiaryLineView();
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

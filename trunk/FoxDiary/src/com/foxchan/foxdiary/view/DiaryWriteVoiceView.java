package com.foxchan.foxdiary.view;

import android.view.View;

import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.core.widgets.FakeActivity;

/**
 * 用声音记录日记的界面
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-4-30
 */
public class DiaryWriteVoiceView extends FakeActivity {
	
	private DiaryWriteView diaryWriteView;

	public DiaryWriteVoiceView(DiaryWriteView diaryWriteView) {
		this.diaryWriteView = diaryWriteView;
	}

	@Override
	public View layoutView() {
		return diaryWriteView.getLayoutInflater().inflate(R.layout.diary_write_voice, null);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRestart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

}

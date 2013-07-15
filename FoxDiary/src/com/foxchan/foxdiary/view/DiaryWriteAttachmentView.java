package com.foxchan.foxdiary.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.foxchan.foxdiary.core.AppContext;
import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.core.widgets.FakeActivity;
import com.foxchan.foxdiary.utils.Constants;

/**
 * 写日记添加附件的界面（天气、心情、地点）
 * @author foxchan@live.cn
 * @create 2013-7-15
 */
public class DiaryWriteAttachmentView extends FakeActivity {
	
	private DiaryWriteView diaryWriteView;
	private View layoutView;
	
	/** 天气单选框集合 */
	private RadioGroup rgWeather;
	/** 心情单选框集合 */
	private RadioGroup rgEmotion;
	/** 自动检测的地点的列表 */
	private ListView lvLocations;
	/** 地点的输入框 */
	private AutoCompleteTextView actvLocation;
	/** 地点的显示区域 */
	private TextView tvLocation;
	
	private ImageView ivTest;
	
	public DiaryWriteAttachmentView(DiaryWriteView diaryWriteView){
		this.diaryWriteView = diaryWriteView;
	}

	@Override
	public View layoutView() {
		if(layoutView == null){
			layoutView = diaryWriteView.getLayoutInflater().inflate(
					R.layout.diary_write_attachment, null);
		}
		return layoutView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//初始化相关的组件
		rgEmotion = (RadioGroup)layoutView.findViewById(R.id.diary_write_attachment_emotion_group);
		rgWeather = (RadioGroup)layoutView.findViewById(R.id.diary_write_attachment_weather_group);
		lvLocations = (ListView)layoutView.findViewById(R.id.diary_write_attachment_location_listview);
		actvLocation = (AutoCompleteTextView)layoutView.findViewById(R.id.diary_write_location_input);
		tvLocation = (TextView)layoutView.findViewById(R.id.diary_write_location_address);
		ivTest = (ImageView)layoutView.findViewById(R.id.test_icon);
		//隐藏自己输入地点的显示区域
		tvLocation.setVisibility(View.GONE);
		actvLocation.setVisibility(View.VISIBLE);
		//绑定选择表情的选择事件
		rgEmotion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				ivTest.setImageResource(AppContext.emotionMap.get(checkedId).getDrawableId());
			}
		});
		//绑定选择天气的选择事件
		rgWeather.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				ivTest.setImageResource(AppContext.weatherMap.get(checkedId).getDrawableId());
			}
		});
	}

}

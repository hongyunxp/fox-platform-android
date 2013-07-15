package com.foxchan.foxdiary.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.foxchan.foxdiary.adapter.DiaryWriteLocationAdapter;
import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.core.widgets.FakeActivity;
import com.foxchan.foxdiary.core.widgets.FoxToast;

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
	/** 地点的数据适配器 */
	private DiaryWriteLocationAdapter locationAdapter;
	/** 地点的显示区域 */
	private TextView tvLocation;
	
	/** 地点集合 */
	private List<String> locations;
	
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
		//初始化数据
		locations = new ArrayList<String>();
		locations.add("北京市 海淀区");
		locations.add("北京市 门头沟区");
		locations.add("北京市 朝阳区");
		locationAdapter = new DiaryWriteLocationAdapter(diaryWriteView, locations);
		
		//初始化相关的组件
		rgEmotion = (RadioGroup)layoutView.findViewById(R.id.diary_write_attachment_emotion_group);
		rgWeather = (RadioGroup)layoutView.findViewById(R.id.diary_write_attachment_weather_group);
		lvLocations = (ListView)layoutView.findViewById(R.id.diary_write_attachment_location_listview);
		tvLocation = (TextView)layoutView.findViewById(R.id.diary_write_location_address);
		//隐藏自己输入地点的显示区域
		tvLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FoxToast.showToast(diaryWriteView, "自己写地点", Toast.LENGTH_SHORT);
			}
		});
		//绑定选择表情的选择事件
		rgEmotion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
			}
		});
		//绑定选择天气的选择事件
		rgWeather.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
			}
		});
		//绑定地点的数据适配器
		lvLocations.setAdapter(locationAdapter);
	}

}
package com.foxchan.foxdiary.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.foxchan.foxdiary.adapter.DiaryWriteLocationAdapter;
import com.foxchan.foxdiary.core.AppContext;
import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.core.widgets.FakeActivity;
import com.foxchan.foxdiary.core.widgets.FoxInputDialog;
import com.foxchan.foxdiary.entity.Emotions;
import com.foxchan.foxdiary.entity.Weathers;
import com.foxchan.foxdiary.utils.Constants;
import com.foxchan.foxutils.data.StringUtils;

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
	/** 地点的项的内容 */
	private LinearLayout llLocation;
	/** 地点的输入对话框 */
	private FoxInputDialog foxInputDialog;
	
	/** 地点集合 */
	private List<String> locations;
	/** 获取地点的客户端 */
	private LocationClient locationClient;
	/** 地点信息的监听器 */
	private FoxLocationListener foxLocationListener;
	/** 查询地点的次数 */
	private int searchCount = 0;
	
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
		locationAdapter = new DiaryWriteLocationAdapter(diaryWriteView, locations);
		
		//初始化相关的组件
		rgEmotion = (RadioGroup)layoutView.findViewById(R.id.diary_write_attachment_emotion_group);
		rgWeather = (RadioGroup)layoutView.findViewById(R.id.diary_write_attachment_weather_group);
		lvLocations = (ListView)layoutView.findViewById(R.id.diary_write_attachment_location_listview);
		tvLocation = (TextView)layoutView.findViewById(R.id.diary_write_location_address);
		llLocation = (LinearLayout)layoutView.findViewById(R.id.diary_write_location_item);
		//隐藏自己输入地点的显示区域
		tvLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				llLocation.setBackgroundColor(diaryWriteView.getResources()
						.getColor(R.color.blue_lite));
				locationAdapter.setSelectedPosition(-1);
				locationAdapter.notifyDataSetChanged();
				foxInputDialog.show();
			}
		});
		//绑定选择表情的选择事件
		rgEmotion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int emotionId = Emotions.getEmotionByRadioButton(checkedId).getId();
				diaryWriteView.setEmotionId(emotionId);
			}
		});
		//绑定选择天气的选择事件
		rgWeather.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int weatherId = Weathers.getWeatherByRadioButton(checkedId).getId();
				diaryWriteView.setWeatherId(weatherId);
			}
		});
		//绑定地点的数据适配器
		lvLocations.setAdapter(locationAdapter);
		lvLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				locationAdapter.setSelectedPosition(position);
				locationAdapter.notifyDataSetChanged();
				llLocation.setBackgroundColor(diaryWriteView
					.getResources().getColor(android.R.color.transparent));
				diaryWriteView.setLocation(locations.get(position));
			}
		});
		//初始化地点输入框对象以及绑定事件
		foxInputDialog = new FoxInputDialog(
				diaryWriteView,
				diaryWriteView.getString(R.string.diary_write_attachment_location_input_title));
		foxInputDialog.setOnPositiveButtonClickListener(new FoxInputDialog.OnClickListener() {
			
			@Override
			public void onClick(String content, FoxInputDialog dialog) {
				if(!StringUtils.isEmpty(content)){
					tvLocation.setText(content);
					diaryWriteView.setLocation(content);
				}
			}
		}).setOnNegativeButtonClickListener(new FoxInputDialog.OnClickListener() {
			
			@Override
			public void onClick(String content, FoxInputDialog dialog) {
			}
		});
		//初始化与地理信息相关的组件
		foxLocationListener = new FoxLocationListener();
		locationClient = ((AppContext)diaryWriteView.getApplication()).getLocationClient();
		locationClient.registerLocationListener(foxLocationListener);
		locationClient.start();
		setLocationOption();
		locationClient.requestLocation();
	}
	
	/**
	 * 配置地点客户端的配置信息
	 */
	private void setLocationOption(){
		LocationClientOption option = new LocationClientOption();
		option.setAddrType("all");//返回的定位结果包含地址信息
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);//禁止启用缓存定位
		option.setPoiNumber(5);	//最多返回POI个数	
		option.setPoiDistance(1000); //poi查询距离		
		option.setPoiExtraInfo(false); //是否需要POI的电话和地址等详细信息
		option.setOpenGps(false);//关闭GPS定位
		option.setProdName(Constants.APP_RESOURCE);//设置产品线名称
		option.setPriority(LocationClientOption.NetWorkFirst);//设置网络定位优先
		option.setServiceName("com.baidu.location.service_v2.9");
		locationClient.setLocOption(option);
	}

	@Override
	public void onDestroy() {
		foxInputDialog.dismiss();
		locationClient.stop();
	}
	
	/**
	 * 获取用户地理位置信息的监听器
	 * @author foxchan@live.cn
	 * @version 1.0.0
	 * @create 2013年7月17日
	 */
	public class FoxLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			if(bdLocation == null) return;
			if(locations.size() >= Constants.MAX_LOCATION_COUNT_FOR_SHOW) {
				locationClient.stop();
				return;
			}
			searchCount++;
			if(searchCount > Constants.MAX_LOCATION_COUNT_FOR_SEARCH) {
				locationClient.stop();
				return;
			}
			String locationStr = "";
			if(bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
				locationStr = bdLocation.getAddrStr();
			}
			if (!StringUtils.isEmpty(locationStr)
					&& !locations.contains(locationStr)) {
				locations.add(locationStr);
			}
			locationAdapter.notifyDataSetChanged();
		}

		@Override
		public void onReceivePoi(BDLocation bdLocation) {
			if(bdLocation == null) return;
			String locationStr = "";
			if(bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
				locationStr = bdLocation.getAddrStr();
			}
			locations.add(locationStr);
		}
		
	}
	
}

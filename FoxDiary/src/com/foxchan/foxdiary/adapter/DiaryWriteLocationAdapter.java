package com.foxchan.foxdiary.adapter;

import java.util.List;

import com.foxchan.foxdiary.core.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 写日记界面的用户地点的数据适配器
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013年7月15日
 */
public class DiaryWriteLocationAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater inflater;
	/** 地点集合 */
	private List<String> locations;
	/** 选中的地点的位置索引 */
	private int selectedPosition = -1;
	
	/**
	 * 地点信息项
	 * @author foxchan@live.cn
	 * @create 2013-7-16
	 */
	private static class LocationItem{
		/** 地点项目的容器 */
		public LinearLayout llContainer;
		/** 地点项目的显示内容 */
		public TextView tvLocation;
	}

	/**
	 * 构造一个日记界面的地点的数据适配器
	 * @param context	
	 * @param locations	地点的名称集合
	 */
	public DiaryWriteLocationAdapter(Context context, List<String> locations) {
		this.context = context;
		this.locations = locations;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return locations.size();
	}

	@Override
	public Object getItem(int position) {
		return locations.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LocationItem locationItem;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.diary_write_location_item, null);
			locationItem = new LocationItem();
			locationItem.llContainer = (LinearLayout)convertView.findViewById(R.id.diary_write_location_item);
			locationItem.tvLocation = (TextView)convertView.findViewById(R.id.diary_write_location_address);
			convertView.setTag(locationItem);
		} else {
			locationItem = (LocationItem)convertView.getTag();
		}
		//绑定数据
		locationItem.tvLocation.setText(locations.get(position));
		if(selectedPosition == position){
			locationItem.llContainer.setBackgroundColor(context.getResources()
					.getColor(R.color.blue_lite));
		} else {
			locationItem.llContainer.setBackgroundColor(context.getResources()
					.getColor(android.R.color.transparent));
		}
		return convertView;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

}

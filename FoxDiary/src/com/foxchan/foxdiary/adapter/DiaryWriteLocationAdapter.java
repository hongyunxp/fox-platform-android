package com.foxchan.foxdiary.adapter;

import java.util.List;

import com.foxchan.foxdiary.core.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
		TextView tvLocation;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.diary_write_location_item, null);
			tvLocation = (TextView)convertView.findViewById(R.id.diary_write_location_address);
			convertView.setTag(tvLocation);
		} else {
			tvLocation = (TextView)convertView.getTag();
		}
		//绑定数据
		tvLocation.setText(locations.get(position));
		return convertView;
	}

}

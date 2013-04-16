package com.foxchan.foxdiary.adapter;

import java.util.List;

import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.entity.Diary;
import com.foxchan.foxdiary.entity.DiaryStyle;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 日记时间线的数据适配器
 * @author gulangxiangjie@gmail.com
 * @create 2013-4-16
 */
public class DiaryLineAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater inflater;
	/** 在左边的日记的布局的文件的资源ID号 */
	private int resourceDiaryLeft;
	/** 在右边的日记的布局的文件的资源ID号 */
	private int resourceDiaryRight;
	private List<Diary> diaries;
	
	/**
	 * 日记项的数据容器
	 * @author gulangxiangjie@gmail.com
	 * @create 2013-4-16
	 */
	static class ContentHolder{
		/** 日记的标题 */
		public TextView tvTitle;
		/** 时间线的中间轴 */
		public ImageView ivLine;
		/** 日记的发布时间 */
		public TextView tvDate;
	}

	/**
	 * 构造一个日记的时间线的数据适配器
	 * @param context
	 * @param diaries	日记列表
	 */
	public DiaryLineAdapter(Context context, List<Diary> diaries) {
		this.context = context;
		this.diaries = diaries;
		this.inflater = LayoutInflater.from(this.context);
		this.resourceDiaryLeft = R.layout.diary_line_left;
		this.resourceDiaryRight = R.layout.diary_line_right;
	}

	@Override
	public int getCount() {
		return diaries.size();
	}

	@Override
	public Object getItem(int position) {
		return (Object)diaries.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		return position % 2;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContentHolder holder = null;
		if(convertView == null){
			if(getItemViewType(position) == 0){
				convertView = inflater.inflate(resourceDiaryLeft, null);
			} else {
				convertView = inflater.inflate(resourceDiaryRight, null);
			}
			//初始化相关的组件
			holder = new ContentHolder();
			holder.ivLine = (ImageView)convertView.findViewById(R.id.diary_line);
			holder.tvDate = (TextView)convertView.findViewById(R.id.diray_create_date);
			holder.tvTitle = (TextView)convertView.findViewById(R.id.diary_title);
			convertView.setTag(holder);
		} else {
			holder = (ContentHolder)convertView.getTag();
		}
		//绑定数据
		Diary diary = diaries.get(position);
		DiaryStyle style = diary.diaryStyle();
		holder.tvDate.setTextColor(context.getResources().getColor(style.getTextColor()));
		holder.tvDate.setText(diary.getCreateDate().toLocaleString());
		if(getItemViewType(position) == 0){
			holder.ivLine.setBackgroundResource(style.getLineBarLeftId());
		} else {
			holder.ivLine.setBackgroundResource(style.getLineBarRightId());
		}
		holder.tvTitle.setBackgroundResource(style.getCircleId());
		holder.tvTitle.setText(diary.getTitle());
		return convertView;
	}

}

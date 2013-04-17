package com.foxchan.foxdiary.adapter;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.entity.Diary;
import com.foxchan.foxdiary.entity.TimeLineNodeStyle;
import com.foxchan.foxdiary.entity.DiaryTool;
import com.foxchan.foxdiary.entity.TimeLineNode;
import com.wecan.veda.utils.DateUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

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
	/** 在左边的工具的布局文件的资源ID号 */
	private int resourceToolLeft;
	/** 在右边的工具的布局文件的资源ID号 */
	private int resourceToolRight;
	private LinkedList<TimeLineNode> diaries;
	
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
		/** 功能按钮 */
		public ImageView ivToolBtn;
		/** 界面切换工具 */
		public ViewSwitcher switcher;
	}

	/**
	 * 构造一个日记的时间线的数据适配器
	 * @param context
	 * @param diaries	日记列表
	 */
	public DiaryLineAdapter(Context context, List<Diary> diaries) {
		this.context = context;
		this.diaries = new LinkedList<TimeLineNode>();
		for(Diary diary : diaries){
			this.diaries.add(diary);
		}
		this.inflater = LayoutInflater.from(this.context);
		this.resourceDiaryLeft = R.layout.diary_line_left;
		this.resourceDiaryRight = R.layout.diary_line_right;
		this.resourceToolLeft = R.layout.tool_line_left;
		this.resourceToolRight = R.layout.tool_line_right;
		DiaryTool addNewDiary = new DiaryTool(R.drawable.icon_plus_normal);
		this.diaries.addLast(addNewDiary);
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
		int side = position % 2;
		if(side == 0){//左边
			if(diaries.get(position).getType() == TimeLineNode.TYPE_DIARY){
				return 0;
			} else if(diaries.get(position).getType() == TimeLineNode.TYPE_TOOL){
				return 1;
			}
		} else {//右边
			if(diaries.get(position).getType() == TimeLineNode.TYPE_DIARY){
				return 2;
			} else if(diaries.get(position).getType() == TimeLineNode.TYPE_TOOL){
				return 3;
			}
		}
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 4;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContentHolder holder = null;
		TimeLineNode node = diaries.get(position);
		if(convertView == null){
			if(getItemViewType(position) == 0){
				convertView = inflater.inflate(resourceDiaryLeft, null);
			} else if(getItemViewType(position) == 1){
				convertView = inflater.inflate(resourceToolLeft, null);
			} else if(getItemViewType(position) == 2){
				convertView = inflater.inflate(resourceDiaryRight, null);
			} else if(getItemViewType(position) == 3){
				convertView = inflater.inflate(resourceToolRight, null);
			}
			//初始化相关的组件
			holder = new ContentHolder();
			holder.ivLine = (ImageView)convertView.findViewById(R.id.diary_line);
			holder.tvDate = (TextView)convertView.findViewById(R.id.diray_create_date);
			holder.tvTitle = (TextView)convertView.findViewById(R.id.diary_title);
			holder.ivToolBtn = (ImageView)convertView.findViewById(R.id.diary_tool);
			holder.switcher = (ViewSwitcher)convertView.findViewById(R.id.diary_switcher);
			convertView.setTag(holder);
		} else {
			holder = (ContentHolder)convertView.getTag();
		}
		//绑定数据
		if(getItemViewType(position) == 1 || getItemViewType(position) == 3){
			DiaryTool addNewDiary = (DiaryTool)node;
			TimeLineNodeStyle style = addNewDiary.diaryStyle(new Date());
			holder.tvDate.setTextColor(context.getResources().getColor(style.getDateTextColor()));
			if(getItemViewType(position) == 0){
				holder.ivLine.setBackgroundResource(style.getLineBarLeftId());
			} else {
				holder.ivLine.setBackgroundResource(style.getLineBarRightId());
			}
			holder.tvDate.setText(R.string.diary_line_add);
			holder.ivToolBtn.setImageResource(addNewDiary.getResourceId());
			holder.ivToolBtn.setBackgroundResource(style.getCircleId());
		} else if(getItemViewType(position) == 0 || getItemViewType(position) == 2) {
			Diary diary = (Diary)node;
			TimeLineNodeStyle style = diary.diaryStyle(diary.getCreateDate());
			holder.tvDate.setTextColor(context.getResources().getColor(style.getDateTextColor()));
			holder.tvDate.setText(DateUtil.formatDate(diary.getCreateDate(), "HH:mm"));
			if(getItemViewType(position) == 0){
				holder.ivLine.setBackgroundResource(style.getLineBarLeftId());
			} else {
				holder.ivLine.setBackgroundResource(style.getLineBarRightId());
			}
			holder.tvTitle.setBackgroundResource(style.getCircleId());
			holder.tvTitle.setTextColor(context.getResources().getColor(style.getTextColor()));
			holder.tvTitle.setText(diary.getTitle());
		}
		return convertView;
	}

}

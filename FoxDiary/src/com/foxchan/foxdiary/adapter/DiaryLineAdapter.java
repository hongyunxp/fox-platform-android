package com.foxchan.foxdiary.adapter;

import java.util.List;

import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.entity.Diary;
import com.foxchan.foxdiary.entity.TimeLineNodeStyle;
import com.foxchan.foxutils.data.DateUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	private int diaryItemResource;
	private NodeListener nodeListener;
	
	/** 日记列表 */
	private List<Diary> diaries;
	
	/**
	 * 节点
	 * @author foxchan@live.cn
	 * @create 2013-4-28
	 */
	public static class NodeItem{
		/** 日记的记录时间 */
		public TextView tvCreateDate;
		/** 节点图片 */
		public ImageView ivNode;
		/** 内容的气泡框 */
		public LinearLayout llBalloon;
		/** 日记的正文 */
		public TextView tvContent;
		/** 日记的图片 */
		public ImageView ivPhoto;
		/** 界面转换器 */
		public ViewSwitcher switcher;
		/** 删除按钮 */
		public ImageView ivDelete;
		/** 编辑按钮 */
		public ImageView ivEdit;
		/** 分享按钮 */
		public ImageView ivShare;
		/** 返回按钮 */
		public ImageView ivBack;
	}
	
	/**
	 * 构造一个日记时间线的数据适配器
	 * @param context
	 * @param diaries	日记列表
	 */
	public DiaryLineAdapter(Context context, List<Diary> diaries) {
		this.context = context;
		this.diaries = diaries;
		this.inflater = LayoutInflater.from(this.context);
		this.diaryItemResource = R.layout.diary_line_item;
	}

	public NodeListener getNodeListener() {
		return nodeListener;
	}

	public void setNodeListener(NodeListener nodeListener) {
		this.nodeListener = nodeListener;
	}

	@Override
	public int getCount() {
		return diaries.size();
	}

	@Override
	public Object getItem(int position) {
		return diaries.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		NodeItem nodeItem = null;
		if(convertView == null){
			convertView = inflater.inflate(diaryItemResource, null);
			nodeItem = new NodeItem();
			nodeItem.ivNode = (ImageView)convertView.findViewById(R.id.diary_line_node);
			nodeItem.ivPhoto = (ImageView)convertView.findViewById(R.id.diary_line_photo);
			nodeItem.llBalloon = (LinearLayout)convertView.findViewById(R.id.diary_line_balloon);
			nodeItem.tvContent = (TextView)convertView.findViewById(R.id.diary_line_content);
			nodeItem.tvCreateDate = (TextView)convertView.findViewById(R.id.diary_line_item_time);
			nodeItem.ivBack = (ImageView)convertView.findViewById(R.id.diary_line_tool_back);
			nodeItem.ivDelete = (ImageView)convertView.findViewById(R.id.diary_line_tool_delete);
			nodeItem.ivEdit = (ImageView)convertView.findViewById(R.id.diary_line_tool_edit);
			nodeItem.ivShare = (ImageView)convertView.findViewById(R.id.diary_line_tool_share);
			nodeItem.switcher = (ViewSwitcher)convertView.findViewById(R.id.diary_line_view_switcher);
			//绑定事件
			final ViewSwitcher switcher = nodeItem.switcher;
			nodeItem.llBalloon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switcher.showNext();
				}
			});
			nodeItem.ivBack.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switcher.showPrevious();
				}
			});
			nodeItem.ivDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					nodeListener.delete(position);
				}
			});
			nodeItem.ivEdit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					nodeListener.edit(position);
				}
			});
			nodeItem.ivShare.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					nodeListener.share(position);
				}
			});
			convertView.setTag(nodeItem);
		} else {
			nodeItem = (NodeItem)convertView.getTag();
		}
		Diary diary = diaries.get(position);
		TimeLineNodeStyle style = diary.getStyle();
		//设置节点的样式
		nodeItem.tvCreateDate.setTextColor(context.getResources().getColor(style.getTimeColor()));
		nodeItem.ivNode.setBackgroundResource(style.getNodeBg());
		nodeItem.llBalloon.setBackgroundResource(style.getBalloonBg());
		//绑定数据
		nodeItem.tvCreateDate.setText(DateUtils.formatDate(diary.getCreateDate(), "HH:mm"));
		nodeItem.tvContent.setText(diary.getContent());
		Bitmap pic = diary.photo(context);
		if(pic != null){
			nodeItem.ivPhoto.setImageBitmap(pic);
		}
		return convertView;
	}
	
	/**
	 * 对节点的操作接口
	 * @author foxchan@live.cn
	 * @create 2013-4-28
	 */
	public interface NodeListener{
		
		/**
		 * 删除该节点
		 * @param position	节点的位置
		 */
		void delete(int position);
		
		/**
		 * 编辑节点的内容
		 * @param position	节点的位置
		 */
		void edit(int position);
		
		/**
		 * 分享节点的内容
		 * @param position	节点的位置
		 */
		void share(int position);
		
	}
	
}

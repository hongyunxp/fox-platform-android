package com.foxchan.foxdiary.adapter;

import java.util.List;

import com.foxchan.foxdb.core.Session;
import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.core.widgets.FoxToast;
import com.foxchan.foxdiary.entity.Diary;
import com.foxchan.foxdiary.entity.Emotions;
import com.foxchan.foxdiary.entity.Record;
import com.foxchan.foxdiary.entity.Weathers;
import com.foxchan.foxutils.data.DateUtils;
import com.foxchan.foxutils.data.StringUtils;
import com.foxchan.foxutils.tool.PhoneUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
	private Session session;
	
	/** 日记列表 */
	private List<Diary> diaries;
	
	/**
	 * 日记信息
	 * @author foxchan@live.cn
	 * @create 2013-4-28
	 */
	public static class NodeItem{
		/** 表情图标 */
		public ImageView ivEmotion;
		/** 文字内容 */
		public TextView tvWords;
		/** 声音内容 */
		public TextView tvVoice;
		/** 照片内容 */
		public ImageView ivPhoto;
		/** 天气图标 */
		public ImageView ivWeather;
		/** 地点 */
		public TextView tvLocation;
		/** 发布时间 */
		public TextView tvDatetime;
		/** 删除按钮 */
		public ImageButton ibDelete;
		/** 编辑按钮 */
		public ImageButton ibEdit;
		/** 分享按钮 */
		public ImageButton ibShare;
		/** 播放声音的按钮 */
		public ImageButton ibVoice;
	}
	
	/**
	 * 构造一个日记时间线的数据适配器
	 * @param context
	 * @param diaries	日记列表
	 */
	public DiaryLineAdapter(Context context, List<Diary> diaries, Session session) {
		this.context = context;
		this.diaries = diaries;
		this.inflater = LayoutInflater.from(this.context);
		this.diaryItemResource = R.layout.diary_line_item;
		this.session = session;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final int index = position;
		NodeItem nodeItem = null;
		final Diary diary = diaries.get(index);
		if(convertView == null){
			convertView = inflater.inflate(diaryItemResource, null);
			nodeItem = new NodeItem();
			nodeItem.ibDelete = (ImageButton)convertView.findViewById(R.id.diary_line_item_delete);
			nodeItem.ibEdit = (ImageButton)convertView.findViewById(R.id.diary_line_item_edit);
			nodeItem.ibShare = (ImageButton)convertView.findViewById(R.id.diary_line_item_share);
			nodeItem.ibVoice = (ImageButton)convertView.findViewById(R.id.diary_line_item_voice);
			nodeItem.ivEmotion = (ImageView)convertView.findViewById(R.id.diary_line_item_emotion);
			nodeItem.ivPhoto = (ImageView)convertView.findViewById(R.id.diary_line_item_photo);
			nodeItem.ivWeather = (ImageView)convertView.findViewById(R.id.diary_line_item_weather);
			nodeItem.tvDatetime = (TextView)convertView.findViewById(R.id.diary_line_item_datetime);
			nodeItem.tvLocation = (TextView)convertView.findViewById(R.id.diary_line_item_location);
			nodeItem.tvVoice = (TextView)convertView.findViewById(R.id.diary_line_item_voice_content);
			nodeItem.tvWords = (TextView)convertView.findViewById(R.id.diary_line_item_words);
			convertView.setTag(nodeItem);
		} else {
			nodeItem = (NodeItem)convertView.getTag();
		}
		
		//绑定数据
		final Record record = session.findObjectFrom(diary, "record", Record.class);
		if(diary.hasWords()){
			nodeItem.tvWords.setText(diary.getContent());
			nodeItem.tvWords.setVisibility(View.VISIBLE);
			nodeItem.tvVoice.setVisibility(View.GONE);
		} else if(diary.hasVoice(session)){
			nodeItem.tvVoice.setText(DateUtils.formatTimeLong(record.getLength()));
			nodeItem.tvVoice.setVisibility(View.VISIBLE);
			nodeItem.tvWords.setVisibility(View.GONE);
		} else {
			nodeItem.tvVoice.setVisibility(View.GONE);
			nodeItem.tvWords.setVisibility(View.GONE);
		}
		if(diary.hasVoice(session)){
			nodeItem.ibVoice.setVisibility(View.VISIBLE);
		} else {
			nodeItem.ibVoice.setVisibility(View.GONE);
		}
		if(diary.hasPhoto()){
			nodeItem.ivPhoto.setVisibility(View.VISIBLE);
			nodeItem.ivPhoto.setImageBitmap(diary.photo(context));
		} else {
			nodeItem.ivPhoto.setVisibility(View.GONE);
		}
		//设置日记的发布时间和地点信息
		nodeItem.tvDatetime.setText(diary.getCreateDatetimeStr());
		if(StringUtils.isEmpty(diary.getLocation())){
			nodeItem.tvLocation.setText(context.getString(R.string.diary_write_attachment_location_default));
		} else {
			nodeItem.tvLocation.setText(diary.getLocation());
		}
		//绑定日记的发表心情和天气
		nodeItem.ivEmotion.setImageResource(Emotions.getEmotionById(diary.getEmotionId()).getDrawableId());
		nodeItem.ivWeather.setImageResource(Weathers.getWeatherById(diary.getWeatherId()).getDrawableId());
		//绑定对于日记的操作事件
		nodeItem.ibDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nodeListener.onDelete(position);
			}
		});
		nodeItem.ibEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nodeListener.onEdit(position);
			}
		});
		nodeItem.ibShare.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nodeListener.onShare(position);
			}
		});
		nodeItem.ibVoice.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					PhoneUtils.playAudio(record.getPath());
				} catch (Exception e) {
					FoxToast.showException(context, R.string.ex_voice_not_found, Toast.LENGTH_SHORT);
				}
			}
		});
		//如果不是当天的日记，隐藏删除和修改的按钮
		if(DateUtils.isBeforeToday(diary.getCreateDate())){
			nodeItem.ibEdit.setVisibility(View.INVISIBLE);
		} else {
			nodeItem.ibEdit.setVisibility(View.VISIBLE);
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
		void onDelete(int position);
		
		/**
		 * 编辑节点的内容
		 * @param position	节点的位置
		 */
		void onEdit(int position);
		
		/**
		 * 分享节点的内容
		 * @param position	节点的位置
		 */
		void onShare(int position);
		
	}

}

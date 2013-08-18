package com.foxchan.foxdiary.view;

import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.core.widgets.FakeActivity;
import com.foxchan.foxdiary.core.widgets.FoxToast;
import com.foxchan.foxdiary.entity.Diary;
import com.foxchan.foxui.widget.lang.CardsSwitcher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 日记的横向界面
 * @create 2013年8月12日
 * @author foxchan@live.cn
 * @version 1.0.0
 */
public class DiaryLandscapeView extends FakeActivity {
	
	private DiaryLineView diaryLineView;
	/** 布局文件 */
	private View layoutView;
	/** 卡片切换控件 */
	private CardsSwitcher cardsSwitcher;
	/** 卡片控件的数据适配器 */
	private DiaryAdapter adapter;
	
	public DiaryLandscapeView(DiaryLineView diaryLineView){
		this.diaryLineView = diaryLineView;
	}

	@Override
	public View layoutView() {
		if(layoutView == null){
			layoutView = LayoutInflater.from(diaryLineView).inflate(R.layout.diary_landscape, null);
		}
		return this.layoutView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		init();
	}
	
	/**
	 * 初始化界面
	 * @create 2013年8月12日
	 * @modify 2013年8月12日
	 * @author foxchan@live.cn
	 */
	private void init(){
		cardsSwitcher = (CardsSwitcher)layoutView().findViewById(R.id.diary_landscape_cards);
		adapter = new DiaryAdapter();
		cardsSwitcher.setAdapter(adapter);
	}
	
	/**
	 * 日记卡片的数据适配器
	 * @create 2013年8月12日
	 * @author foxchan@live.cn
	 * @version 1.0.0
	 */
	class DiaryAdapter extends BaseAdapter{
		
		/**
		 * 数据集合
		 * @create 2013年8月12日
		 * @author foxchan@live.cn
		 * @version 1.0.0
		 */
		class ContentHolder{
			/** 天气图标 */
			public ImageView ivWeather;
			/** 声音按钮 */
			public Button btnVoiceContent;
			/** 照片 */
			public ImageView ivPhoto;
			/** 日记的内容 */
			public TextView tvContent;
			/** 日记的地点 */
			public TextView tvLocation;
			/** 日记的时间 */
			public TextView tvDatetime;
			/** 删除日记的按钮 */
			public ImageButton ibDelete;
			/** 编辑日记的按钮 */
			public ImageButton ibEdit;
			/** 分享日记的按钮 */
			public ImageButton ibShare;
			/** 播放声音的按钮 */
			public ImageButton ibPlayVoice;
		}

		@Override
		public int getCount() {
			return diaryLineView.getDiaries().size();
		}

		@Override
		public Object getItem(int position) {
			return diaryLineView.getDiaries().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ContentHolder contentHolder = null;
			if(convertView == null){
				convertView = LayoutInflater.from(diaryLineView).inflate(R.layout.diary_landscape_item, null);
				contentHolder = new ContentHolder();
				contentHolder.ivWeather = (ImageView)convertView.findViewById(R.id.diary_landscape_weather);
				contentHolder.btnVoiceContent = (Button)convertView.findViewById(R.id.diary_landscape_voice_content);
				contentHolder.ivPhoto = (ImageView)convertView.findViewById(R.id.diary_landscape_photo);
				contentHolder.tvContent = (TextView)convertView.findViewById(R.id.diary_landscape_words);
				contentHolder.tvLocation = (TextView)convertView.findViewById(R.id.diary_landscape_location);
				contentHolder.tvDatetime = (TextView)convertView.findViewById(R.id.diary_landscape_datetime);
				contentHolder.ibDelete = (ImageButton)convertView.findViewById(R.id.diary_landscape_delete);
				contentHolder.ibEdit = (ImageButton)convertView.findViewById(R.id.diary_landscape_edit);
				contentHolder.ibShare = (ImageButton)convertView.findViewById(R.id.diary_landscape_share);
				contentHolder.ibPlayVoice = (ImageButton)convertView.findViewById(R.id.diary_landscape_voice);
				convertView.setTag(contentHolder);
			} else {
				contentHolder = (ContentHolder)convertView.getTag();
			}
			//绑定数据
			final Diary diary = diaryLineView.getDiaries().get(position);
			contentHolder.tvContent.setText(diary.getContent());
			contentHolder.tvLocation.setText(diary.getLocation());
			
			//绑定事件
			contentHolder.ibDelete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FoxToast.showToast(diaryLineView, "要删除的日记是：" + diary.getContent(), Toast.LENGTH_SHORT);
				}
			});
			contentHolder.ibEdit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FoxToast.showToast(diaryLineView, "要编辑的日记是：" + diary.getContent(), Toast.LENGTH_SHORT);
				}
			});
			contentHolder.ibPlayVoice.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FoxToast.showToast(diaryLineView, "正在播放音乐的日记是：" + diary.getContent(), Toast.LENGTH_SHORT);
				}
			});
			
			return convertView;
		}
		
	}

}

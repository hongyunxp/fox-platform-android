package com.wecan.foxchat.adapter;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wecan.foxchat.R;
import com.wecan.foxchat.entity.Contacter;
import com.wecan.foxchat.entity.Sms;
import com.wecan.foxchat.utils.PhoneUtil;
import com.wecan.veda.utils.DateUtil;
import com.wecan.veda.utils.StringUtil;

public class SmsContacterListAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	/** 短信列表 */
	private List<Contacter> contacters;
	/** 每一条列表项的显示资源的id号 */
	private int resourceId;
	
	/**
	 * 每一条短信类
	 * @author jiladeyouxiang@qq.com
	 * @version 1.0.0
	 * @create 2012-12-7
	 */
	static class SmsContacerItem{
		/** 用户的头像 */
		public ImageView photo;
		/** 聊天的对象 */
		public TextView contacterName;
		/** 未读短信的数量 */
		public TextView newSmsCount;
		/** 短信的发送/接收日期 */
		public TextView smsDate;
		/** 短信的正文的简介 */
		public TextView smsBrief;
	}
	
	public SmsContacterListAdapter(Context context, List<Contacter> contacters, int resourceId){
		this.inflater = LayoutInflater.from(context);
		this.contacters = contacters;
		this.resourceId = resourceId;
	}

	public int getCount() {
		return this.contacters.size();
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		//自定义视图
		SmsContacerItem smsContacerItem = null;
		if(convertView == null){
			convertView = inflater.inflate(resourceId, null);
			smsContacerItem = new SmsContacerItem();
			//获得控件对象
			smsContacerItem.photo = (ImageView)convertView.findViewById(R.id.sms_contactor_photo);
			smsContacerItem.contacterName = (TextView)convertView.findViewById(R.id.sms_contactor_name);
			smsContacerItem.newSmsCount = (TextView)convertView.findViewById(R.id.sms_contactor_sms_amount);
			smsContacerItem.smsDate = (TextView)convertView.findViewById(R.id.sms_contactor_sms_date);
			smsContacerItem.smsBrief = (TextView)convertView.findViewById(R.id.sms_contactor_sms_brief);
			//设置控件集到convertView
			convertView.setTag(smsContacerItem);
		} else {
			smsContacerItem = (SmsContacerItem)convertView.getTag();
		}
		//设置文字和图片
		Contacter contacter = contacters.get(position);
		Sms sms = contacter.getSmsList().get(0);//获得该联系人最近的一封短信
		Bitmap photo = contacter.getPhoto();
		if(photo == null){
			smsContacerItem.photo.setImageResource(R.drawable.default_photo);
		} else {
			smsContacerItem.photo.setImageBitmap(sms.getSender().getPhoto());
		}
		String nameToShow = "";//用于显示的用户的姓名
		if(PhoneUtil.isFetion(sms.getMobilePhone())){
			nameToShow = StringUtil.concat(new Object[]{
					"[飞信]", contacter.displayName()
			});
		} else {
			nameToShow = contacter.displayName();
		}
		smsContacerItem.contacterName.setText(nameToShow);
		int newSmsCount = contacter.newSmsCount();
		if(newSmsCount > 0){
			smsContacerItem.newSmsCount.setText(contacter.newSmsCount()+"");
			smsContacerItem.newSmsCount.setVisibility(View.VISIBLE);
		} else {
			smsContacerItem.newSmsCount.setText("");
			smsContacerItem.newSmsCount.setVisibility(View.GONE);
		}
		
		smsContacerItem.smsBrief.setText(sms.getContent());
		Date from = sms.getCreateDate();
		Date to = new Date();
		smsContacerItem.smsDate.setText(DateUtil.datesIntervalDetail(from, to, true));
		return convertView;
	}

}

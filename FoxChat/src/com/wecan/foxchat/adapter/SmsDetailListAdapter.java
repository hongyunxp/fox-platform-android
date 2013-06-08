package com.wecan.foxchat.adapter;

import java.util.Date;
import java.util.List;

import com.wecan.foxchat.R;
import com.wecan.foxchat.entity.Sms;
import com.wecan.veda.utils.DateUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 用户短信详情信息的列表适配器
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-8
 */
public class SmsDetailListAdapter extends BaseAdapter {
	
	private final static int RECEIVE = 0;
	private final static int SEND = 1;
	
	private LayoutInflater inflater;
	/** 接收到的信息的格式xml的资源ID号 */
	private int fromResourceId;
	/** 发送出去的信息的格式xml的资源ID号 */
	private int toResourceId;
	/** 短信的列表 */
	private List<Sms> smsList;
	
	/**
	 * 每一条短信的具体信息
	 * @author jiladeyouxiang@qq.com
	 * @version 1.0.0
	 * @create 2012-12-8
	 */
	static class SmsDetailItem{
		/** 短信的具体内容 */
		public TextView content;
		/** 短信的发送/接收时间 */
		public TextView date;
	}

	/**
	 * 构造一个适配器
	 * @param context
	 * @param smsList			需要呈现的短信列表
	 * @param fromResourceId	呈现接收到的信息的格式xml的资源ID号
	 * @param toResourceId		呈现发送出的信息的格式xml的资源ID号
	 */
	public SmsDetailListAdapter(Context context, List<Sms> smsList,
			int fromResourceId, int toResourceId) {
		inflater = LayoutInflater.from(context);
		this.fromResourceId = fromResourceId;
		this.toResourceId = toResourceId;
		this.smsList = smsList;
	}

	public int getCount() {
		return smsList.size();
	}

	public Object getItem(int posotion) {
		return (Object)smsList.get(posotion);
	}

	public long getItemId(int posotion) {
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		Sms sms = smsList.get(position);
		if(sms.getSmsType() == Sms.RECEIVE){
			return RECEIVE;
		} else if(sms.getSmsType() == Sms.SEND){
			return SEND;
		}
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		//自定义视图
		SmsDetailItem smsDetailItem = null;
		Sms sms = smsList.get(position);
		int smsType = sms.getSmsType();//获得短信的类型，发送的或者接收的
		if(convertView == null){
			if(smsType == Sms.RECEIVE){
				convertView = inflater.inflate(fromResourceId, null);
			} else if(smsType == Sms.SEND){
				convertView = inflater.inflate(toResourceId, null);
			}
			//得到相关的组件
			smsDetailItem = new SmsDetailItem();
			smsDetailItem.content = (TextView)convertView.findViewById(R.id.sms_detail_content);
			smsDetailItem.date = (TextView)convertView.findViewById(R.id.sms_detail_date);
			convertView.setTag(smsDetailItem);
		} else {
			smsDetailItem = (SmsDetailItem)convertView.getTag();
		}
		//设置相关的数据
		smsDetailItem.content.setText(sms.getContent());
		Date from = sms.getCreateDate();
		Date to = new Date();
		String dateStr = DateUtil.datesIntervalDetail(from, to, true);
		smsDetailItem.date.setText(dateStr);
		return convertView;
	}

}

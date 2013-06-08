package com.wecan.foxchat.adapter;

import java.util.Date;
import java.util.List;

import com.wecan.foxchat.R;
import com.wecan.foxchat.entity.Call;
import com.wecan.veda.utils.DateUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 用户选择收信人界面的通话记录的数据适配器
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-11
 */
public class CallLogListAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	/** 资源的id号 */
	private int resourceId;
	/** 通话记录的数据 */
	private List<Call> callLogs;

	public CallLogListAdapter(Context context, int resourceId,
			List<Call> callLogs) {
		inflater = LayoutInflater.from(context);
		this.resourceId = resourceId;
		this.callLogs = callLogs;
	}
	
	static class CallLogHolder{
		public ImageView ivCallType;
		public TextView tvContacterName;
		public TextView tvCallDate;
		public TextView tvCallCount;
		public TextView tvPhoneNumber;
	}

	public int getCount() {
		return callLogs.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		CallLogHolder callLogHolder = null;
		if(convertView == null){
			convertView = inflater.inflate(resourceId, null);
			callLogHolder = new CallLogHolder();
			callLogHolder.ivCallType = (ImageView)convertView.findViewById(R.id.call_record_item_call_type);
			callLogHolder.tvCallCount = (TextView)convertView.findViewById(R.id.call_record_item_call_count);
			callLogHolder.tvCallDate = (TextView)convertView.findViewById(R.id.call_record_item_call_date);
			callLogHolder.tvContacterName = (TextView)convertView.findViewById(R.id.call_record_item_name);
			callLogHolder.tvPhoneNumber = (TextView)convertView.findViewById(R.id.call_record_item_phone_number);
			convertView.setTag(callLogHolder);
		} else {
			callLogHolder = (CallLogHolder)convertView.getTag();
		}
		//给组件赋值
		Call call = callLogs.get(position);
		if(call.getCallType() == Call.CALL_IN){
			callLogHolder.ivCallType.setImageResource(R.drawable.icon_call_in);
		} else if(call.getCallType() == Call.CALL_OUT){
			callLogHolder.ivCallType.setImageResource(R.drawable.icon_call_out);
		} else if(call.getCallType() == Call.CALL_MISSED){
			callLogHolder.ivCallType.setImageResource(R.drawable.icon_call_missed);
		}
		callLogHolder.tvCallCount.setText(call.getCallCount()+"");
		Date from = call.getCallDate();
		Date to = new Date();
		callLogHolder.tvCallDate.setText(DateUtil.datesIntervalDetail(from, to, true));
		callLogHolder.tvContacterName.setText(call.displayName());
		callLogHolder.tvPhoneNumber.setText(call.getPhone());
		return convertView;
	}

}

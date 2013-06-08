package com.wecan.foxchat.adapter;

import java.util.List;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.wecan.foxchat.R;
import com.wecan.foxchat.entity.Contacter;

/**
 * 发送短信中的选择收件人界面中的联系人列表数据适配器
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-11
 */
public class SmsWriteContacterListAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private int resourceId;
	private List<Contacter> contacters;
	/** 每一条记录当前的选中状态 */
	public static SparseBooleanArray selectMap;
	
	public final static class ContacterHolder{
		public ImageView ivPhoto;
		public TextView tvContacterName;
		public TextView tvPhone;
		public CheckBox cbIsSelect;
	}

	public SmsWriteContacterListAdapter(Context context, int resourceId,
			List<Contacter> contacters) {
		inflater = LayoutInflater.from(context);
		this.resourceId = resourceId;
		this.contacters = contacters;
		selectMap = new SparseBooleanArray();
		for(int i = 0; i < contacters.size(); i++){
			selectMap.put(i, false);
		}
	}

	public int getCount() {
		return contacters.size();
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ContacterHolder contacterHolder = null;
		if(convertView == null){
			contacterHolder = new ContacterHolder();
			convertView = inflater.inflate(resourceId, null);
			//初始化各个组件
			contacterHolder.ivPhoto = (ImageView)convertView.findViewById(R.id.select_contacter_item_photo);
			contacterHolder.tvContacterName = (TextView)convertView.findViewById(R.id.select_contacter_item_name);
			contacterHolder.tvPhone = (TextView)convertView.findViewById(R.id.select_contacter_item_phone);
			contacterHolder.cbIsSelect = (CheckBox)convertView.findViewById(R.id.select_contacter_item_check_box);
			convertView.setTag(contacterHolder);
		} else {
			contacterHolder = (ContacterHolder)convertView.getTag();
		}
		//给容器赋值
		Contacter contacter = contacters.get(position);
		if(contacter.getPhoto() == null){
			contacterHolder.ivPhoto.setImageResource(R.drawable.default_photo);
		} else {
			contacterHolder.ivPhoto.setImageBitmap(contacter.getPhoto());
		}
		contacterHolder.tvContacterName.setText(contacter.displayName());
		contacterHolder.tvPhone.setText(contacter.getMobilePhone());
		contacterHolder.cbIsSelect.setChecked(selectMap.get(position));
		return convertView;
	}

}

package com.wecan.foxchat.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.wecan.foxchat.R;
import com.wecan.foxchat.entity.Contacter;
import com.wecan.foxchat.filter.ContentFilter;

public class ContacterPromptingListAdapter extends BaseAdapter implements
		Filterable {
	
	/** 自定义的内容过滤器 */
	private ContentFilter<Contacter> contentFilter;
	private Object mLock;
	private LayoutInflater inflater;
	private int resourceId;
	/** 需要进行过滤的内容 */
	private ArrayList<Contacter> contacters;
	/** 过滤后的内容 */
	private List<Integer> mObjects;
	
	public ContacterPromptingListAdapter(Context context, int resourceId,
			ArrayList<Contacter> contacters) {
		this.inflater = LayoutInflater.from(context);
		this.resourceId = resourceId;
		this.contacters = contacters;
		if(contentFilter == null){
			contentFilter = new ContentFilter<Contacter>(this, mLock, contacters, mObjects, -1);
		}
	}

	public int getCount() {
		mObjects = contentFilter.getObjectsBeFound();
		return mObjects == null ? 0 : mObjects.size();
	}

	public Object getItem(int position) {
		mObjects = contentFilter.getObjectsBeFound();
		return mObjects == null ? null : mObjects.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		SmsWriteContacterListAdapter.ContacterHolder contacterHolder = null;
		if(convertView == null){
			convertView = inflater.inflate(resourceId, null);
			//初始化各个组件
			contacterHolder = new SmsWriteContacterListAdapter.ContacterHolder();
			contacterHolder.cbIsSelect = (CheckBox)convertView.findViewById(R.id.select_contacter_item_check_box);
			contacterHolder.ivPhoto = (ImageView)convertView.findViewById(R.id.select_contacter_item_photo);
			contacterHolder.tvContacterName = (TextView)convertView.findViewById(R.id.select_contacter_item_name);
			contacterHolder.tvPhone = (TextView)convertView.findViewById(R.id.select_contacter_item_phone);
			convertView.setTag(contacterHolder);
		} else {
			contacterHolder = (SmsWriteContacterListAdapter.ContacterHolder) convertView
					.getTag();
		}
		//绑定数据
		int i = mObjects.get(position);
		Contacter contacter = contacters.get(i);
		if(contacter.getPhoto() == null){
			contacterHolder.ivPhoto.setImageResource(R.drawable.default_photo);
		} else {
			contacterHolder.ivPhoto.setImageBitmap(contacter.getPhoto());
		}
		contacterHolder.tvContacterName.setText(contacter.displayName());
		contacterHolder.tvPhone.setText(contacter.getMobilePhone());
		contacterHolder.cbIsSelect.setVisibility(View.GONE);
		return convertView;
	}

	public Filter getFilter() {
		return contentFilter;
	}

}

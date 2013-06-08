package com.wecan.foxchat.filter;

import java.util.ArrayList;
import java.util.List;

import android.widget.BaseAdapter;
import android.widget.Filter;

/**
 * 内容过滤器
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-13
 */
public class ContentFilter<E extends FilterEntity> extends Filter {
	
	private BaseAdapter adapter;
	private Object mLock;
	/** 需要过滤的内容 */
	private ArrayList<E> mOriginalValues;
	/** 过滤后的数据在原数组的索引号 */
	private List<Integer> mObjects;
	/** 最多显示多少个选项，-1表示全部显示 */
	private int maxMatch = -1;

	/**
	 * 构造一个内容过滤器
	 * @param adapter			适配器
	 * @param mLock
	 * @param mOriginalValues	需要过滤的内容
	 * @param mObjects			过滤后的内容
	 * @param maxMatch			最多显示多少个选项，-1表示全部显示
	 */
	public ContentFilter(BaseAdapter adapter, Object mLock,
			ArrayList<E> mOriginalValues, List<Integer> mObjects,
			int maxMatch) {
		this.adapter = adapter;
		this.mLock = mLock;
		this.mOriginalValues = mOriginalValues;
		this.mObjects = mObjects;
		if(maxMatch > 0) this.maxMatch = maxMatch;
	}

	@Override
	protected FilterResults performFiltering(CharSequence prefix) {
		FilterResults results = new FilterResults();
		// 没有输入信息
		if (prefix == null || prefix.length() == 0) {
			synchronized (mLock) {
				ArrayList<FilterEntity> list = new ArrayList<FilterEntity>(mOriginalValues);
				results.values = list;
				results.count = list.size();
				return results;
			}
		} else {
			String prefixString = prefix.toString().toLowerCase();
			final int count = mOriginalValues.size();
			final ArrayList<Integer> newValues = new ArrayList<Integer>(count);
			for (int i = 0; i < count; i++) {
				final FilterEntity value = mOriginalValues.get(i);
				if (value.getStringToSearch().startsWith(prefixString) || 
						value.getPinYinHeaderChar().startsWith(prefixString) || 
						value.getQuanPin().startsWith(prefixString)) { // 源码 ,匹配开头
					newValues.add(i);// 存放的是他在原数组的id值
				}
				if (maxMatch > 0) {// 有数量限制
					if (newValues.size() > maxMatch - 1) {// 不要太多
						break;
					}
				}
			}
			results.values = newValues;
			results.count = newValues.size();
		}
		return results;
	}

	@Override
	protected void publishResults(CharSequence constraint, FilterResults results) {
		// 刷新信息
		mObjects = (List<Integer>)results.values;
		if (results.count > 0) {
			adapter.notifyDataSetChanged();
		} else {
			adapter.notifyDataSetInvalidated();
		}
	}
	
	/**
	 * 获得找到的数据列表
	 * @return	返回找到的数据列表
	 */
	public List<Integer> getObjectsBeFound(){
		return this.mObjects;
	}
	
}

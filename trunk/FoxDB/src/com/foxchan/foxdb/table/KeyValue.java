package com.foxchan.foxdb.table;

import java.util.Date;

import com.foxchan.foxutils.data.DateUtils;

/**
 * 键值对对象
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-13
 */
public class KeyValue {
	
	/** 键值 */
	private String key;
	/** 取值 */
	private Object value;
	
	/**
	 * 构造一个键值对信息
	 * @param key	键值
	 * @param value	取值
	 */
	public KeyValue(String key, Object value){
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		if(value instanceof java.util.Date || value instanceof java.sql.Date){
			return DateUtils.formatDate((Date)value, "yyyy-MM-dd HH:mm:ss");
		}
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}

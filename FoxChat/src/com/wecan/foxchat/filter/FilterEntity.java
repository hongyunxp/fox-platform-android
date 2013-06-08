package com.wecan.foxchat.filter;

/**
 * 用于查询的实体
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-13
 */
public abstract class FilterEntity{
	
	/**
	 * 获得需要进行查询的内容
	 * @return	返回需要进行查询的内容
	 */
	public abstract String getStringToSearch();
	
	/**
	 * 获得需要进行查询的内容的拼音的全拼
	 * @return	返回需要进行查询的内容的拼音的全拼
	 */
	public abstract String getQuanPin();
	
	/**
	 * 获得需要进行查询的内容的拼音的首字母
	 * @return	返回需要进行查询的内容的拼音的首字母
	 */
	public abstract String getPinYinHeaderChar();
	
}

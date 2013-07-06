package com.foxchan.foxdb.annotation;

/**
 * 时间保存的类型
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-12
 */
public enum TemporalType {
	
	/** 只保存日期：2013/3/12 */
	DATE,
	/** 只保存时间：10：29：36 */
	TIME,
	/** 保存完整的时间：2013/3/12 10:29:36 */
	TIMESTAMP

}

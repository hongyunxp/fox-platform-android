package com.wecan.foxchat.entity;

import java.util.Date;

import com.wecan.veda.utils.StringUtil;

/**
 * 用户的通讯记录
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-11
 */
public class Call {
	
	/** 用户收到的电话 */
	public static final int CALL_IN = 1;
	/** 用户播出的电话 */
	public static final int CALL_OUT = 2;
	/** 未接电话 */
	public static final int CALL_MISSED = 3;
	
	/** 编号 */
	private String id;
	/** 联系人的姓名 */
	private String contacterName;
	/** 电话号码 */
	private String phone;
	/** 通话的类型，拨出或者拨入 */
	private int callType = CALL_IN;
	/** 通话发生的时间 */
	private Date callDate;
	/** 通话持续的时间 */
	private long duration;
	/** 和该号码通信的次数 */
	private int callCount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContacterName() {
		return contacterName;
	}

	public void setContacterName(String contacterName) {
		this.contacterName = contacterName;
	}

	public int getCallType() {
		return callType;
	}

	public void setCallType(int callType) {
		this.callType = callType;
	}

	public Date getCallDate() {
		return callDate;
	}

	public void setCallDate(Date callDate) {
		this.callDate = callDate;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public int getCallCount() {
		return callCount;
	}

	public void setCallCount(int callCount) {
		this.callCount = callCount;
	}

	/**
	 * 获得用户显示的名称
	 * @return
	 */
	public String displayName(){
		if(!StringUtil.isEmpty(this.contacterName)){
			return this.contacterName;
		}
		return this.phone;
	}

}

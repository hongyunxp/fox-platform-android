package com.wecan.foxchat.entity;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 短信对象
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-7
 */
public class Sms {

	/** 短信的类型：接收的信息 */
	public static final int RECEIVE = 0x1;
	/** 短信的类型：发送的信息 */
	public static final int SEND = 0x2;

	/** 短信的id编号 */
	private int id;
	/** 短信的发送者对象 */
	private Contacter sender;
	/** 短信的发送人的手机号码 */
	private String mobilePhone;
	/** 短信的正文 */
	private String content;
	/** 短信的发送/接收时间 */
	private Date createDate;
	/** 短信的类型：发送的或者是接收的，默认是接收的短信 */
	private int smsType = RECEIVE;
	/** 短信的阅读状态，已读或者是未读 */
	private boolean isNew;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Contacter getSender() {
		return sender;
	}

	public void setSender(Contacter sender) {
		this.sender = sender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getSmsType() {
		return smsType;
	}

	public void setSmsType(int smsType) {
		this.smsType = smsType;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	/**
	 * 从JSON数据中解析出短信列表
	 * @param jsonData	存储短信内容的JSON字符串
	 * @return			返回解析出的短信列表
	 */
	public static List<Sms> parseFromListString(String jsonData){
		Type listType = new TypeToken<List<Sms>>(){}.getType();
		Gson gson = new Gson();
		List<Sms> smsList = gson.fromJson(jsonData, listType);
		return smsList;
	}

}

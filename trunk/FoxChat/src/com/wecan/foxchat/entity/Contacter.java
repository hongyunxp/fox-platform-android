package com.wecan.foxchat.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.wecan.foxchat.filter.FilterEntity;
import com.wecan.foxchat.utils.PhoneUtil;
import com.wecan.foxchat.utils.PinYinUtil;
import com.wecan.veda.utils.StringUtil;

import android.graphics.Bitmap;

/**
 * 联系人对象
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-7
 */
public class Contacter extends FilterEntity implements Serializable {
	
	private static final long serialVersionUID = 5100108905427170734L;
	/** 联系人的id编号 */
	private Long id;
	/** 联系人的姓名 */
	private String name;
	/** 联系人的手机号码 */
	private String mobilePhone;
	/** 联系人分组 */
	private String group;
	/** 用户的头像的图片 */
	private Bitmap photo;
	/** 与该联系人有关的短信列表 */
	private List<Sms> smsList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	public List<Sms> getSmsList() {
		return smsList;
	}

	public void setSmsList(List<Sms> smsList) {
		this.smsList = smsList;
	}

	/**
	 * 获得联系人的显示名称
	 * @return	如果联系人填写了名字则返回联系人的姓名，否则返回联系人的电话
	 */
	public String displayName(){
		if(!StringUtil.isEmpty(this.name)){
			return this.name;
		}
		return PhoneUtil.formatPhoneNumber(this.mobilePhone);
	}
	
	/**
	 * 获得联系人未读的短信的条数
	 * @return	返回联系人未读的短信的条数
	 */
	public int newSmsCount(){
		int newCount = 0;
		for(Sms sms : smsList){
			if(sms.getSmsType() == Sms.RECEIVE && sms.isNew()){
				newCount++;
			}
		}
		return newCount;
	}
	
	/**
	 * 添加一条该联系人的短信
	 * @param sms	短信对象
	 */
	public void addSms(Sms sms){
		if(this.smsList == null){
			this.smsList = new ArrayList<Sms>();
		}
		sms.setSender(this);
		this.smsList.add(sms);
	}

	@Override
	public String getStringToSearch() {
		return StringUtil.concat(new Object[]{
				this.mobilePhone
		});
	}

	@Override
	public String getQuanPin() {
		return PinYinUtil.getPingYin(this.name);
	}

	@Override
	public String getPinYinHeaderChar() {
		return PinYinUtil.getPinYinHeadChar(this.name);
	}

	@Override
	public boolean equals(Object o) {
		if(this == o){
			return true;
		}
		if(o == null){
			return false;
		}
		if(getClass() != o.getClass()){
			return false;
		}
		final Contacter other = (Contacter)o;
		if(mobilePhone == null){
			if(other.mobilePhone != null){
				return false;
			} else if(!mobilePhone.equals(other.mobilePhone)){
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

}

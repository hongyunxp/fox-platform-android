package com.foxchan.foxdiary.entity;

import com.foxchan.foxdb.annotation.Column;
import com.foxchan.foxdb.annotation.GeneratedType;
import com.foxchan.foxdb.annotation.GeneratedValue;
import com.foxchan.foxdb.annotation.Id;
import com.foxchan.foxdb.annotation.Table;

/**
 * 录音对象
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013年7月10日
 */
@Table(name = "tb_core_record")
public class Record {

	/** 录音的主键，采用32位UUID进行编码 */
	@Id
	@GeneratedValue(strategy = GeneratedType.UUID)
	private String id;
	/** 录音的存储地址 */
	@Column(nullable = false)
	private String path;
	/** 录音的时长 */
	@Column(nullable = false)
	private long length;
	
	/**
	 * 构造一个录音对象
	 */
	public Record(){}

	/**
	 * 构造一个录音对象
	 * @param path		录音的存储地址
	 * @param length	录音的时长
	 */
	public Record(String path, long length) {
		this.path = path;
		this.length = length;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

}

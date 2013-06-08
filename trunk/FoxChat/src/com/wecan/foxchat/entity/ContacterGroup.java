package com.wecan.foxchat.entity;

import java.util.List;

/**
 * 通讯录中的分组
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-12
 */
public class ContacterGroup {
	
	/** 分组的id编号 */
	private Long id;
	/** 分组的名称 */
	private String title;
	/** 分组中的成员列表 */
	private List<Contacter> contacters;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Contacter> getContacters() {
		return contacters;
	}

	public void setContacters(List<Contacter> contacters) {
		this.contacters = contacters;
	}

}

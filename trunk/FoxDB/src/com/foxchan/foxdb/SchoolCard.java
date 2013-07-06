package com.foxchan.foxdb;

import com.foxchan.foxdb.annotation.Column;
import com.foxchan.foxdb.annotation.GeneratedType;
import com.foxchan.foxdb.annotation.GeneratedValue;
import com.foxchan.foxdb.annotation.Id;
import com.foxchan.foxdb.annotation.Table;

@Table(name = "tb_school_card")
public class SchoolCard {
	
	@Id @GeneratedValue(strategy = GeneratedType.UUID)
	private String id;
	@Column
	private String number;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}

package cn.com.lezhixing.foxdb;

import cn.com.lezhixing.foxdb.annotation.Column;
import cn.com.lezhixing.foxdb.annotation.GeneratedType;
import cn.com.lezhixing.foxdb.annotation.GeneratedValue;
import cn.com.lezhixing.foxdb.annotation.Id;
import cn.com.lezhixing.foxdb.annotation.Table;

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

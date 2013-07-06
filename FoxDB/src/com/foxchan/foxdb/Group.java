package com.foxchan.foxdb;

import java.util.List;

import com.foxchan.foxdb.annotation.CascadeType;
import com.foxchan.foxdb.annotation.Column;
import com.foxchan.foxdb.annotation.GeneratedType;
import com.foxchan.foxdb.annotation.GeneratedValue;
import com.foxchan.foxdb.annotation.Id;
import com.foxchan.foxdb.annotation.OneToMany;
import com.foxchan.foxdb.annotation.Table;

@Table
public class Group {

	@Id @GeneratedValue(strategy=GeneratedType.UUID)
	private String id;
	@Column
	private String name;
	@OneToMany(cascade=CascadeType.REFRESH, mappedBy="group")
	private List<User> users;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}

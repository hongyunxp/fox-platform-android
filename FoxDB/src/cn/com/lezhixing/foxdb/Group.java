package cn.com.lezhixing.foxdb;

import java.util.List;

import cn.com.lezhixing.foxdb.annotation.CascadeType;
import cn.com.lezhixing.foxdb.annotation.Column;
import cn.com.lezhixing.foxdb.annotation.GeneratedType;
import cn.com.lezhixing.foxdb.annotation.GeneratedValue;
import cn.com.lezhixing.foxdb.annotation.Id;
import cn.com.lezhixing.foxdb.annotation.OneToMany;
import cn.com.lezhixing.foxdb.annotation.Table;

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

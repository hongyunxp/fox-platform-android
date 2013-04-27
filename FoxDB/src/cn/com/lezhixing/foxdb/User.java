package cn.com.lezhixing.foxdb;

import java.util.Date;

import com.wecan.veda.utils.StringUtil;

import cn.com.lezhixing.foxdb.annotation.Column;
import cn.com.lezhixing.foxdb.annotation.Id;
import cn.com.lezhixing.foxdb.annotation.ManyToOne;
import cn.com.lezhixing.foxdb.annotation.OneToOne;
import cn.com.lezhixing.foxdb.annotation.Table;
import cn.com.lezhixing.foxdb.annotation.Transient;

@Table
public class User {

	@Id
	private Integer id;
	@Column(nullable = false)
	private String name;
	private Boolean isGood;
	private Date createDate;
	@Column(defaultValue="15")
	private Double weight;
	@Transient
	private Float height;
	@ManyToOne
	private Group group;
	@OneToOne
	private SchoolCard card;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsGood() {
		return isGood;
	}

	public void setIsGood(Boolean isGood) {
		this.isGood = isGood;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public SchoolCard getCard() {
		return card;
	}

	public void setCard(SchoolCard card) {
		this.card = card;
	}
	
	@Override
	public String toString(){
		return StringUtil.concat(new Object[]{
				"name : ", name, "--createDate : ", createDate.toLocaleString()
		});
	}

}

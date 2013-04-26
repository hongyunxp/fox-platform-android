package cn.com.lezhixing.foxdb.table;

import cn.com.lezhixing.foxdb.annotation.CascadeType;
import cn.com.lezhixing.foxdb.annotation.FetchType;

/**
 * 一对多
 * @author foxchan@live.cn
 * @create 2013-4-24
 */
public class OneToMany extends Column {
	
	/** 一的一方的类 */
	private Class<?> oneClass;
	/** 级联的策略数组 */
	private CascadeType[] cascadeTypes;
	/** 获取数据的策略 */
	private FetchType fetchType;
	/** 控制反转所映射的类，即多那一方的类 */
	private String mappedBy;

	public Class<?> getOneClass() {
		return oneClass;
	}

	public void setOneClass(Class<?> oneClass) {
		this.oneClass = oneClass;
	}

	public CascadeType[] getCascadeTypes() {
		return cascadeTypes;
	}

	public void setCascadeTypes(CascadeType[] cascadeTypes) {
		this.cascadeTypes = cascadeTypes;
	}

	public FetchType getFetchType() {
		return fetchType;
	}

	public void setFetchType(FetchType fetchType) {
		this.fetchType = fetchType;
	}

	public String getMappedBy() {
		return mappedBy;
	}

	public void setMappedBy(String mappedBy) {
		this.mappedBy = mappedBy;
	}

}

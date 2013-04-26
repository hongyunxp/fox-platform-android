package cn.com.lezhixing.foxdb.annotation;

/**
 * 实体的级联方式
 * @author foxchan@live.cn
 * @create 2013-4-24
 */
public enum CascadeType {
	
	/** 级联所有的操作 */
	ALL,
	/** 级联更新 */
	MERGE,
	/** 级联保存 */
	PERSIST,
	/** 级联刷新 */
	REFRESH,
	/** 级联删除 */
	REMOVE;

}

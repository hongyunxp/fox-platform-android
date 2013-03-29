package cn.com.lezhixing.foxdb.core;

import java.util.LinkedHashMap;

import cn.com.lezhixing.foxdb.engine.TableInfo;
import cn.com.lezhixing.foxdb.table.SQLObject;

/**
 * 构建SQL语句的引擎
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-13
 */
public interface SQLEngine {
	
	/**
	 * 生成表结构的方法
	 * @param clazz	实体
	 * @return		返回见表语句
	 */
	String getCreateTableSQL(Class<?> clazz);
	
	/**
	 * 检查数据库中是否存在该数据表的信息
	 * @param table	数据表信息对象
	 * @return		如果存在数据表的信息，则返回true，否则返回false
	 */
	boolean isTableExist(TableInfo table);
	
	/**
	 * 检查数据表是否存在，不存在则在数据库中进行创建
	 * @param clazz	实体
	 */
	void checkTableExist(Class<?> clazz);
	
	/**
	 * 获得插入对象的SQL语句的方法
	 * @param entity	实体
	 * @return			返回插入数据的SQL语句SQL对象
	 */
	SQLObject getInsertSQL(Object entity);
	
	/**
	 * 获得删除对象的SQL语句的方法
	 * @param entity	实体
	 * @return			返回删除对象的SQL语句的SQL对象
	 */
	SQLObject getDeleteSQL(Object entity);
	
	/**
	 * 获得更新对象的SQL语句的方法
	 * @param entity	实体
	 * @return			返回更新对象的SQL语句的SQL对象
	 */
	SQLObject getUpdateSQL(Object entity);
	
	/**
	 * 构造查询的SQL语句
	 * @param startIndex	分页的起始索引号，不进行分页请传入-1
	 * @param maxResult		每一页显示的记录数，不进行分页请传入-1
	 * @param where			查询的条件语句，不需要输入where关键字，形如：name=? and id=?，不需要设置条件请传入null
	 * @param params		查询的条件条件中对应的参数，和where语句中的文号一一对应，不需要设置条件请传入null
	 * @param orderBy		查询的排序条件，不需要写order by关键字，形如：age asc, id desc，不需要进行排序请传入null
	 * @param clazz			数据的封装类型
	 * @return				返回查询的SQL对象
	 */
	SQLObject getQuerySQL(int startIndex, int maxResult, String where,
			Object[] params, LinkedHashMap<String, String> orderBy, Class<?> clazz);
	
	/**
	 * 构造查询数据数量的SQL语句
	 * @param where		查询的条件语句，不需要输入where关键字，形如：name=? and id=?，不需要设置条件请传入null
	 * @param params	查询的条件条件中对应的参数，和where语句中的文号一一对应，不需要设置条件请传入null
	 * @param clazz		数据的封装类型
	 * @return			返回查询数据数量的SQL对象
	 */
	SQLObject getQueryCountSQL(String where, Object[] params, Class<?> clazz);

}

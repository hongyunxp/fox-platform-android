package cn.com.lezhixing.foxdb.core;

import java.util.LinkedHashMap;
import java.util.List;

import cn.com.lezhixing.foxdb.engine.Pager;
import cn.com.lezhixing.foxdb.engine.PagerTemplate;
import cn.com.lezhixing.foxdb.exception.FoxDbException;

/**
 * 数据库操作的session
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-13
 */
public interface Session {
	
	/**
	 * 保存对象
	 * @param object
	 * @return			返回保存的对象
	 * @throws FoxDbException
	 */
	Object save(Object object) throws FoxDbException;
	
	/**
	 * 删除对象
	 * @param object
	 * @throws FoxDbException
	 */
	void delete(Object object) throws FoxDbException;
	
	/**
	 * 更新对象
	 * @param object
	 * @return			返回更新后的对象
	 * @throws FoxDbException
	 */
	Object update(Object object) throws FoxDbException;
	
	/**
	 * 查找对象
	 * @param startIndex	分页的起始索引号，不进行分页请传入-1
	 * @param maxResult		每一页显示的记录数，不进行分页请传入-1
	 * @param where			查询的条件语句，不需要输入where关键字，形如：name=? and id=?，不需要设置条件请传入null
	 * @param params		查询的条件条件中对应的参数，和where语句中的文号一一对应，不需要设置条件请传入null
	 * @param orderBy		查询的排序条件，不需要写order by关键字，形如：orderBy.put("id", "asc")，不需要进行排序请传入null
	 * @param clazz			查询的结果的目标封装类
	 * @return				返回查询的分页临时对象
	 */
	<T> PagerTemplate<T> query(int startIndex, int maxResult, String where,
			Object[] params, LinkedHashMap<String, String> orderBy, Class<?> clazz);
	
	/**
	 * 查找对象
	 * @param pager		分页对象
	 * @param where		查询的条件语句，不需要输入where关键字，形如：name=? and id=?，不需要设置条件请传入null
	 * @param params	查询的条件条件中对应的参数，和where语句中的文号一一对应，不需要设置条件请传入null
	 * @param orderBy	查询的排序条件，不需要写order by关键字，形如：orderBy.put("id", "asc")，不需要进行排序请传入null
	 * @param clazz		查询的结果的目标封装类
	 * @return			返回查询的分页临时对象
	 */
	<T> PagerTemplate<T> query(Pager<T> pager, String where,
			Object[] params, LinkedHashMap<String, String> orderBy, Class<?> clazz);
	
	/**
	 * 查找对象
	 * @param startIndex	分页的起始索引号，不进行分页请传入-1
	 * @param maxResult		每一页显示的记录数，不进行分页请传入-1
	 * @param where			查询的条件语句，不需要输入where关键字，形如：name=? and id=?，不需要设置条件请传入null
	 * @param params		查询的条件条件中对应的参数，和where语句中的文号一一对应，不需要设置条件请传入null
	 * @param orderBy		查询的排序条件，不需要写order by关键字，形如：orderBy.put("id", "asc")，不需要进行排序请传入null
	 * @param clazz			查询的结果的目标封装类
	 * @return				返回查询的分页列表
	 */
	<T> List<T> list(int startIndex, int maxResult,
			String where, Object[] params, LinkedHashMap<String, String> orderBy, Class<?> clazz);
	
	/**
	 * 查找对象
	 * @param startIndex	分页的起始索引号，不进行分页请传入-1
	 * @param maxResult		每一页显示的记录数，不进行分页请传入-1
	 * @param where			查询的条件语句，不需要输入where关键字，形如：name=? and id=?，不需要设置条件请传入null
	 * @param params		查询的条件条件中对应的参数，和where语句中的文号一一对应，不需要设置条件请传入null
	 * @param clazz			查询的结果的目标封装类
	 * @return				返回查询的分页列表
	 */
	<T> List<T> list(int startIndex, int maxResult, String where, Object[] params, Class<?> clazz);
	
	/**
	 * 查找对象
	 * @param where		查询的条件语句，不需要输入where关键字，形如：name=? and id=?，不需要设置条件请传入null
	 * @param params	查询的条件条件中对应的参数，和where语句中的文号一一对应，不需要设置条件请传入null
	 * @param orderBy	查询的排序条件，不需要写order by关键字，形如：orderBy.put("id", "asc")，不需要进行排序请传入null
	 * @param clazz		查询的结果的目标封装类
	 * @return			返回查询的分页列表
	 */
	<T> List<T> list(String where, Object[] params, LinkedHashMap<String, String> orderBy, Class<?> clazz);
	
	/**
	 * 查找对象
	 * @param where		查询的条件语句，不需要输入where关键字，形如：name=? and id=?，不需要设置条件请传入null
	 * @param params	查询的条件条件中对应的参数，和where语句中的文号一一对应，不需要设置条件请传入null
	 * @param clazz		查询的结果的目标封装类
	 * @return			返回查询的分页列表
	 */
	<T> List<T> list(String where, Object[] params, Class<?> clazz);
	
	/**
	 * 查找对象
	 * @param clazz	查询的结果的目标封装类
	 * @return		返回该类型的所有数据的列表
	 */
	<T> List<T> list(Class<?> clazz);
	
	/**
	 * 查找对象
	 * @param id	实体的主键
	 * @param clazz	查询的结果的目标封装类
	 * @return		返回找到的实体对象
	 */
	<T> T get(Integer id, Class<?> clazz);
	
	/**
	 * 查找对象
	 * @param id	实体的主键
	 * @param clazz	查询的结果的目标封装类
	 * @return		返回找到的实体对象
	 */
	<T> T get(String id, Class<?> clazz);
	
	/**
	 * 查找对象的记录数
	 * @param where		查询的条件语句，不需要输入where关键字，形如：name=? and id=?，不需要设置条件请传入null
	 * @param params	查询的条件条件中对应的参数，和where语句中的文号一一对应，不需要设置条件请传入null
	 * @param clazz		查询的实体的类
	 * @return			返回系统中的该对象的数量
	 */
	int findCount(String where, Object[] params, Class<?> clazz);
	
	/**
	 * 查找符合条件的对象
	 * @param where		查询的条件语句，不需要输入where关键字，形如：name=? and id=?，不需要设置条件请传入null
	 * @param params	查询的条件条件中对应的参数，和where语句中的文号一一对应，不需要设置条件请传入null
	 * @param clazz		查询的结果的目标封装类
	 * @return			返回找到的对象
	 */
	<T> T findObject(String where, Object[] params, Class<T> clazz);
	
	/**
	 * 查找对象
	 * @param parent		需要查询的对象列表的所属对象
	 * @param attributeName	需要查询的对象列表在所属对象中的属性名称
	 * @param sonClass		查询的结果的目标封装类
	 * @return				返回查询的分页列表
	 */
	<T> List<T> listFrom(Object parent, String attributeName, Class<?> sonClass);
	
	/**
	 * 查找对象
	 * @param startIndex	分页的起始索引号，不进行分页请传入-1
	 * @param maxResult		每一页显示的记录数，不进行分页请传入-1
	 * @param where			查询的条件语句，不需要输入where关键字，形如：name=? and id=?，不需要设置条件请传入null
	 * @param params		查询的条件条件中对应的参数，和where语句中的文号一一对应，不需要设置条件请传入null
	 * @param orderBy		查询的排序条件，不需要写order by关键字，形如：orderBy.put("id", "asc")，不需要进行排序请传入null
	 * @param parent		需要查询的对象列表的所属对象
	 * @param attributeName	需要查询的对象列表在所属对象中的属性名称
	 * @param sonClass		查询的结果的目标封装类
	 * @return				返回查询的分页列表
	 */
	<T> List<T> listFrom(int startIndex, int maxResult, String where,
			Object[] params, LinkedHashMap<String, String> orderBy,
			Object parent, String attributeName, Class<?> sonClass);
	
	/**
	 * 查找对象
	 * @param parent		宿主对象
	 * @param attributeName	目标对象在宿主对象中的属性名称
	 * @param targetClass	目标对象的类
	 * @return				返回找到的目标对象
	 */
	<T> T findObjectFrom(Object parent, String attributeName, Class<?> targetClass);
	
	/**
	 * 通过执行自定义的SQL语句查询数据
	 * @param sql			自定义的sql语句
	 * @param targetClass	目标对象的类
	 * @return				返回找到的目标列表
	 */
	<T> List<T> querySQL(String sql, Class<?> targetClass);
	
}

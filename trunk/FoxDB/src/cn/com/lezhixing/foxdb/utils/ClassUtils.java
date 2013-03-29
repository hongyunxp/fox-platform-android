package cn.com.lezhixing.foxdb.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.R.color;
import cn.com.lezhixing.foxdb.annotation.Id;
import cn.com.lezhixing.foxdb.annotation.Table;
import cn.com.lezhixing.foxdb.exception.FoxDbException;
import cn.com.lezhixing.foxdb.table.Column;

import com.wecan.veda.utils.StringUtil;

/**
 * 处理类的工具类
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-13
 */
public class ClassUtils {
	
	/**
	 * 获得表的名称，如果用户配置了@Table(name="fox")那么将返回用户配置的数据表名称，
	 * 否则，将采用类名作为数据库的名称
	 * @param clazz
	 * @return
	 */
	public static String getTableName(Class<?> clazz){
		Table table = clazz.getAnnotation(Table.class);
		if(table == null || StringUtil.isEmpty(table.name())){
			return clazz.getName().replaceAll("[.]", "_").toLowerCase();
		}
		return table.name();
	}
	
	/**
	 * 获得实体的主键的属性
	 * @param clazz
	 * @return
	 */
	public static Field getPrimaryKeyField(Class<?> clazz){
		Field primaryKeyField = null;
		Field[] fields = clazz.getDeclaredFields();
		if(!StringUtil.isEmpty(fields)){
			for(Field f : fields){
				//获取ID注解
				if(f.getAnnotation(Id.class) != null){
					primaryKeyField = f;
					break;
				}
			}
			//没有Id注解
			if(primaryKeyField == null){
				for(Field f : fields){
					if("_id".equals(f.getName())){
						primaryKeyField = f;
						break;
					}
				}
			}
		} else {
			throw new FoxDbException("当前的实体中没有可供存储的属性");
		}
		return primaryKeyField;
	}
	
	public static String getPrimaryKeyFieldName(Class<?> clazz){
		Field field = getPrimaryKeyField(clazz);
		return field == null ? null : field.getName();
	}
	
	public static List<Column> getColumnList(Class<?> clazz){
		List<Column> columns = new ArrayList<Column>();
		try {
			Field[] fields = clazz.getDeclaredFields();
			String primaryKeyFieldName = getPrimaryKeyFieldName(clazz);
			for(Field f : fields){
				if(FieldUtils.isBaseDataType(f) && !FieldUtils.isTransient(f)){
					//过滤主键
					if(f.getName().equals(primaryKeyFieldName)){
						continue;
					}
					Column column = new Column();
					column.setName(FieldUtils.getColumnByField(f));
					column.setFieldName(f.getName());
					column.setDataType(f.getType());
					column.setDefaultValue(FieldUtils.getColumnDefaultValue(f));
					column.setSet(FieldUtils.getFieldSetMethod(clazz, f));
					column.setGet(FieldUtils.getFieldGetMethod(clazz, f));
					column.setField(f);
					column.setNullable(FieldUtils.getColumnNullable(f));
					columns.add(column);
				}
			}
		} catch (SecurityException e) {
			throw new FoxDbException(e.getMessage(), e);
		}
		return columns;
	}

}

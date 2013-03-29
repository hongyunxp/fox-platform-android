package cn.com.lezhixing.foxdb.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import com.wecan.veda.utils.StringUtil;

import cn.com.lezhixing.foxdb.annotation.Column;
import cn.com.lezhixing.foxdb.annotation.Transient;

/**
 * 该类提供跟方法相关的方法
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-13
 */
public class FieldUtils {
	
	/**
	 * 获得属性对应的数据表中的列名
	 * @param field
	 * @return
	 */
	public static String getColumnByField(Field field){
		Column column = field.getAnnotation(Column.class);
		if(column != null && !StringUtil.isEmpty(column.name())){
			return column.name();
		}
		return field.getName();
	}
	
	public static Method getFieldGetMethod(Class<?> clazz, Field field){
		String fn = field.getName();
		Method method = null;
		if(field.getType() == boolean.class){
			method = getBooleanGetMethod(clazz, fn);
		}
		return getFieldGetMethod(clazz, fn);
	}
	
	
	public static Method getBooleanGetMethod(Class<?> clazz, Field field){
		String fn = field.getName();
		return getBooleanGetMethod(clazz, fn);
	}
	
	public static Method getBooleanGetMethod(Class<?> clazz, String fieldName) {
		String mn = StringUtil.concat(new Object[]{
				"is" + fieldName.substring(0, 1).toUpperCase(), fieldName.substring(1)
		});
		if(isStartWithIs(fieldName)){
			mn = fieldName;
		}
		try {
			return clazz.getDeclaredMethod(mn);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断属性的名称是否是以is开头，即isGood这样的属性名
	 * @param fieldName
	 * @return
	 */
	private static boolean isStartWithIs(String fieldName) {
		if(!StringUtil.isEmpty(fieldName) && fieldName.startsWith("is") && !Character.isLowerCase(2)){
			return true;
		}
		return false;
	}

	/**
	 * 获得属性对应的get方法
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Method getFieldGetMethod(Class<?> clazz, String fieldName){
		String mn = StringUtil.concat(new Object[]{
				"get", fieldName.substring(0, 1).toUpperCase(), fieldName.substring(1)
		});
		try {
			return clazz.getDeclaredMethod(mn);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Method getFieldSetMethod(Class<?> clazz, Field field){
		String fn = field.getName();//属性名称
		String mn = StringUtil.concat(new Object[]{
				"set", fn.substring(0, 1).toUpperCase(), fn.substring(1)
		});
		try {
			return clazz.getDeclaredMethod(mn, field.getType());
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			if(field.getType() == boolean.class){
				return getBooleanFieldSetMethod(clazz, field);
			}
		}
		return null;
	}
	
	public static Method getFieldSetMethod(Class<?> clazz, String fieldName){
		try {
			return getFieldSetMethod(clazz, clazz.getDeclaredField(fieldName));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获得boolean类型的属性的set方法
	 * @param clazz
	 * @param field
	 * @return
	 */
	public static Method getBooleanFieldSetMethod(Class<?> clazz, Field field){
		String fn = field.getName();
		String mn = StringUtil.concat(new Object[]{
				"set", fn.substring(2, 3).toUpperCase(), fn.substring(3)
		});
		try {
			return clazz.getDeclaredMethod(mn, field.getType());
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获得属性的默认取值
	 * @param field
	 * @return
	 */
	public static String getColumnDefaultValue(Field field){
		Column column = field.getAnnotation(Column.class);
		if(column != null && !StringUtil.isEmpty(column.defaultValue())){
			return column.defaultValue();
		}
		return null;
	}
	
	/**
	 * 获得该属性能否为空
	 * @param field
	 * @return
	 */
	public static boolean getColumnNullable(Field field){
		Column column = field.getAnnotation(Column.class);
		if(column != null){
			return column.nullable();
		}
		return true;
	}
	
	public static Object getFieldValue(Object entity, Field field){
		Method method = getFieldGetMethod(entity.getClass(), field);
		return invoke(entity, method);
	}
	
	public static Object invoke(Object target, Method method){
		if(target == null || method == null) return null;
		try {
			return method.invoke(target);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 判断属性是否是java的基本数据类型
	 * @param field
	 * @return
	 */
	public static boolean isBaseDataType(Field field){
		Class<?> clazz = field.getType();
		return clazz.equals(String.class) ||
				clazz.equals(Integer.class) ||
				clazz.equals(Byte.class) ||
				clazz.equals(Long.class) ||
				clazz.equals(Double.class) ||
				clazz.equals(Float.class) ||
				clazz.equals(Character.class) ||
				clazz.equals(Short.class) ||
				clazz.equals(Boolean.class) ||
				clazz.equals(Date.class) ||
				clazz.equals(java.util.Date.class) ||
				clazz.equals(java.sql.Date.class) ||
				clazz.isPrimitive();
	}
	
	/**
	 * 判断数据类型是否是数字类型
	 * @param clazz
	 * @return
	 */
	public static boolean isNumber(Class<?> clazz){
		return clazz.equals(Integer.class) ||
				clazz.equals(Long.class) ||
				clazz.equals(Double.class) ||
				clazz.equals(Float.class) ||
				clazz.equals(Short.class) ||
				clazz.equals(int.class) ||
				clazz.equals(float.class) ||
				clazz.equals(double.class) ||
				clazz.equals(long.class);
	}
	
	/**
	 * 判断数据类型是不是整形
	 * @param clazz
	 * @return
	 */
	public static boolean isInteger(Class<?> clazz){
		return clazz.equals(Integer.class) ||
				clazz.equals(Long.class) ||
				clazz.equals(Short.class) ||
				clazz.equals(int.class) ||
				clazz.equals(long.class) ||
				clazz.equals(short.class);
	}
	
	/**
	 * 判断数据类型是不是浮点数
	 * @param clazz
	 * @return
	 */
	public static boolean isRealNumber(Class<?> clazz){
		return clazz.equals(Double.class) ||
				clazz.equals(Float.class) ||
				clazz.equals(double.class) ||
				clazz.equals(float.class);
	}
	
	/**
	 * 判断属性是否是瞬时的
	 * @param field
	 * @return
	 */
	public static boolean isTransient(Field field){
		Transient transient1 = field.getAnnotation(Transient.class);
		return  transient1 != null ? true : false;
	}

}

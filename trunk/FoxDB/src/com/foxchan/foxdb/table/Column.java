package com.foxchan.foxdb.table;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Date;

import com.foxchan.foxdb.exception.FoxDbException;
import com.foxchan.foxutils.data.DateUtils;
import com.foxchan.foxutils.data.StringUtils;

/**
 * 属性
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-13
 */
public class Column {
	
	/** 标记：升序 */
	public static final String ASC = "asc";
	/** 标记：降序 */
	public static final String DESC = "desc";
	
	/** 属性的名称 */
	private String fieldName;
	/** 列对应的属性的数据类型 */
	private Class<?> dataType;
	/** 列对应的属性 */
	private Field field;
	/** 该属性属于的类 */
	private Class<?> parent;
	
	/** 用户给该列重新取的别名，将被用作数据库中的表的列名 */
	private String name;
	/** 该列能否为空 */
	private boolean nullable;
	/** 该列的的默认值 */
	private String defaultValue;
	
	private Method get;
	private Method set;
	
	/**
	 * 构造一个属性
	 */
	public Column(){}
	
	/**
	 * 构造一个属性
	 * @param fieldName		该属性的名称
	 * @param dataType		该属性的数据类型
	 * @param parent		该属性对应的类
	 * @param defaultValue	该属性的默认值
	 * @param nullable		该属性能否为空的标志
	 */
	public Column(String fieldName, Class<?> dataType, Class<?> parent,
			String defaultValue, boolean nullable) {
		this.name = fieldName;
		this.dataType = dataType;
		this.parent = parent;
		this.defaultValue = defaultValue;
		this.nullable = nullable;
	}
	
	public void setValue(Object receiver, Object value){
		if(set != null && value != null){
			try {
				if(dataType == String.class){
					set.invoke(receiver, value.toString());
				} else if(dataType == int.class || dataType == Integer.class){
					set.invoke(receiver, value == null ? (Integer)null : Integer.parseInt(value.toString()));
				} else if(dataType == float.class || dataType == Float.class){
					set.invoke(receiver, value == null ? (Float)null : Float.parseFloat(value.toString()));
				} else if(dataType == double.class || dataType == Double.class){
					set.invoke(receiver, value == null ? (Double)null : Double.parseDouble(value.toString()));
				} else if(dataType == long.class || dataType == Long.class){
					set.invoke(receiver, value == null ? (Long)null : Long.parseLong(value.toString()));
				} else if(dataType == java.util.Date.class || dataType == java.sql.Date.class){
					set.invoke(receiver, value == null ? (Date)null : DateUtils.generateDateFrom(value.toString()));
				} else if(dataType == boolean.class || dataType == Boolean.class){
					set.invoke(receiver, value == null ? (Boolean)null : Boolean.parseBoolean(value.toString()));
				} else {
					set.invoke(receiver, value);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			try {
				field.setAccessible(true);
				field.set(receiver, value);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取实体的该属性的值
	 * @param object
	 * @return
	 */
	public <T> T getValue(Object object){
		if(object != null && get != null){
			try {
				return (T)get.invoke(object);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Class<?> getDataType() {
		return dataType;
	}

	public void setDataType(Class<?> dataType) {
		this.dataType = dataType;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Method getGet() {
		return get;
	}

	public void setGet(Method get) {
		this.get = get;
	}

	public Method getSet() {
		return set;
	}

	public void setSet(Method set) {
		this.set = set;
	}
	
	public Class<?> getParent() {
		return parent;
	}

	public void setParent(Class<?> parent) {
		this.parent = parent;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	/**
	 * 将属性列对象转换为键值对对象
	 * @param target	调用该方法的实体
	 * @return			返回键值对对象
	 */
	public KeyValue toKeyValue(Object target){
		KeyValue kv = null;
		String columnName = this.name;
		Object value = this.getValue(target);
		if(value != null){
			kv = new KeyValue(columnName, value);
		} else {
			if(!StringUtils.isEmpty(this.defaultValue)){
				kv = new KeyValue(columnName, defaultValue);
			} else if(nullable == false){
				throw new FoxDbException("[" + target.getClass() + "]的["
						+ fieldName + "]属性设置了非空属性，当前值为空，请检查实体的配置信息或者为属性["
						+ fieldName + "]设置一个值。");
			}
		}
		return kv;
	}

}

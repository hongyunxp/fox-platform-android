package cn.com.lezhixing.foxdb.engine;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import cn.com.lezhixing.foxdb.annotation.GeneratedType;
import cn.com.lezhixing.foxdb.exception.FoxDbException;
import cn.com.lezhixing.foxdb.table.Column;
import cn.com.lezhixing.foxdb.table.Enumerated;
import cn.com.lezhixing.foxdb.table.Id;
import cn.com.lezhixing.foxdb.table.ManyToOne;
import cn.com.lezhixing.foxdb.table.OneToMany;
import cn.com.lezhixing.foxdb.table.Temporal;
import cn.com.lezhixing.foxdb.utils.ClassUtils;
import cn.com.lezhixing.foxdb.utils.FieldUtils;

import com.wecan.veda.utils.StringUtil;

/**
 * 该类存储数据表的信息
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-13
 */
public class TableInfo {
	
	private String className;
	private String tableName;
	
	private Id id;
	
	//获得主键的生成策略
	public GeneratedType strategy = GeneratedType.IDENTITY;
	public final HashMap<String, Column> columnMap = new HashMap<String, Column>();
	public final HashMap<String, Enumerated> enumMap = new HashMap<String, Enumerated>();
	public final HashMap<String, Temporal> temporalMap = new HashMap<String, Temporal>();
	public final HashMap<String, OneToMany> oneToManyMap = new HashMap<String, OneToMany>();
	public final HashMap<String, ManyToOne> manyToOneMap = new HashMap<String, ManyToOne>();
	
	/** 是否已经检查过数据库 */
	private boolean isDatabaseChecked;
	
	private static final HashMap<String, TableInfo> tableInfoMap = new HashMap<String, TableInfo>();
	
	private TableInfo(){}
	
	/**
	 * 实例化TableInfo
	 * @param clazz
	 * @return
	 */
	public static TableInfo getInstance(Class<?> clazz){
		if(clazz == null){
			throw new FoxDbException("数据表获取失败，实体对象为空");
		}
		TableInfo tableInfo = tableInfoMap.get(clazz.getName());
		if(tableInfo == null){
			tableInfo = new TableInfo();
			tableInfo.tableName = ClassUtils.getTableName(clazz);
			tableInfo.className = clazz.getName();
			
			Field idField = ClassUtils.getPrimaryKeyField(clazz);
			if(idField != null){
				Id id = new Id();
				id.setName(FieldUtils.getColumnByField(idField));
				id.setDataType(idField.getType());
				id.setFieldName(idField.getName());
				id.setGet(FieldUtils.getFieldGetMethod(clazz, idField));
				id.setSet(FieldUtils.getFieldSetMethod(clazz, idField));
				tableInfo.id = id;
				tableInfo.strategy = FieldUtils.getStrategy(idField);
			} else {
				throw new FoxDbException("该实体没有设置主键，请使用@Id注解为实体加上主键。");
			}
			
			List<Column> columns = ClassUtils.getColumnList(clazz);
			if(!StringUtil.isEmpty(columns)){
				for(Column column : columns){
					if(column != null){
						tableInfo.columnMap.put(column.getName(), column);
					}
				}
			}
			List<ManyToOne> manyToOneList = ClassUtils.getManyToOneList(clazz);
			if(!StringUtil.isEmpty(manyToOneList)){
				for(ManyToOne m2o : manyToOneList){
					if(m2o != null){
						tableInfo.manyToOneMap.put(m2o.getName(), m2o);
					}
				}
			}
			List<OneToMany> oneToManieList = ClassUtils.getOneToManyList(clazz);
			if(!StringUtil.isEmpty(oneToManieList)){
				for(OneToMany o2m : oneToManieList){
					if(o2m != null){
						tableInfo.oneToManyMap.put(o2m.getName(), o2m);
					}
				}
			}
			tableInfoMap.put(clazz.getName(), tableInfo);
		}
		if(tableInfo == null){
			throw new FoxDbException("没有找到类：" + clazz.getName() + "对应的数据表信息。");
		}
		return tableInfo;
	}
	
	public static TableInfo getInstance(String className){
		try {
			return getInstance(Class.forName(className));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public boolean isDatabaseChecked() {
		return isDatabaseChecked;
	}

	public void setDatabaseChecked(boolean isDatabaseChecked) {
		this.isDatabaseChecked = isDatabaseChecked;
	}
	
	/**
	 * 构造外键的数据字段的名称
	 * @param columnName	作为外键的属性的名称
	 * @return				返回外键在数据库中的数据字段的名称
	 */
	public static String buildForeignKeyName(String columnName){
		return StringUtil.concat(new Object[]{
				columnName, "_id"
		});
	}

}

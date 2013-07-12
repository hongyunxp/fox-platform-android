package com.foxchan.foxdb.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.foxchan.foxdb.core.FoxDB;
import com.foxchan.foxdb.core.SQLEngine;
import com.foxchan.foxdb.core.FoxDB.DbConfiguration;
import com.foxchan.foxdb.exception.FoxDbException;
import com.foxchan.foxdb.table.Column;
import com.foxchan.foxdb.table.Id;
import com.foxchan.foxdb.table.KeyValue;
import com.foxchan.foxdb.table.ManyToOne;
import com.foxchan.foxdb.table.OneToOne;
import com.foxchan.foxdb.table.SQLObject;
import com.foxchan.foxdb.utils.FieldUtils;
import com.foxchan.foxutils.data.StringUtils;

/**
 * SQLite数据库的语言构造器
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-13
 */
public class SQLiteEngine implements SQLEngine {
	
	private DbConfiguration configuration;
	private SQLiteDatabase db;
	
	public SQLiteEngine(SQLiteDatabase db){
		this(null, db);
	}
	
	public SQLiteEngine(DbConfiguration configuration, SQLiteDatabase db){
		this.configuration = configuration;
		this.db = db;
	}

	@Override
	public String getCreateTableSQL(Class<?> clazz) {
		TableInfo tableInfo = TableInfo.getInstance(clazz);
		Id id = tableInfo.getId();
		StringBuffer sql = new StringBuffer();
		sql.append("CREATE TABLE IF NOT EXISTS ")
		.append(tableInfo.getTableName())
		.append("(");
		
		Class<?> primaryClass = id.getDataType();
		if(primaryClass == int.class || primaryClass == Integer.class){
			sql.append("\"").append(id.getName()).append("\" ").append("INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,");
		} else {
			sql.append("\"").append(id.getName()).append("\" ").append("TEXT PRIMARY KEY,");
		}
		
		Collection<Column> columns = tableInfo.columnMap.values();
		for(Column c : columns){
			sql.append("\"").append(c.getName()).append("\"");
			if(c.getDataType() == String.class){
				sql.append(" TEXT");
			} else if(FieldUtils.isInteger(c.getDataType())){
				sql.append(" INTEGER");
			} else if(FieldUtils.isRealNumber(c.getDataType())){
				sql.append(" REAL");
			}
			if(!c.isNullable()){
				sql.append(" NOT NULL");
			}
			sql.append(",");
		}
		
		Collection<ManyToOne> manyToOneList = tableInfo.manyToOneMap.values();
		for(ManyToOne m2o : manyToOneList){
			sql.append("\"").append(TableInfo.buildForeignKeyName(m2o.getName())).append("\",");
		}
		
		Collection<OneToOne> oneToOneList = tableInfo.oneToOneMap.values();
		for(OneToOne o2o : oneToOneList){
			sql.append("\"").append(TableInfo.buildForeignKeyName(o2o.getName())).append("\",");
		}
		
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		return sql.toString();
	}

	@Override
	public boolean isTableExist(TableInfo table) {
		if(table.isDatabaseChecked()){
			return true;
		}
		Cursor c = null;
		String sql = StringUtils.concat(new Object[]{
				"SELECT COUNT(*) FROM sqlite_master WHERE type = 'table' AND name = '",
				table.getTableName(), "'"
		});
		debug(sql);
		c = db.rawQuery(sql, null);
		if(c != null && c.moveToNext()){
			int count = c.getInt(0);
			if(count > 0){
				table.setDatabaseChecked(true);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 显示执行的sql语句
	 * @param sql	执行的sql语句
	 */
	private void debug(String sql){
		if(FoxDB.DEBUG){
			android.util.Log.d("FoxDB SQL:", sql);
		}
	}
	
	/**
	 * 显示执行的sql语句
	 * @param sql	执行的sql语句
	 */
	private void debug(StringBuilder sql){
		if(FoxDB.DEBUG){
			android.util.Log.d("FoxDB SQL:", sql.toString());
		}
	}

	@Override
	public void checkTableExist(Class<?> clazz) {
		if(!isTableExist(TableInfo.getInstance(clazz))){
			String sql = getCreateTableSQL(clazz);
			debug(sql);
			db.execSQL(sql);
		}
	}

	@Override
	public SQLObject getInsertSQL(Object entity) {
		List<KeyValue> keyValues = getKeyValuesFromEntity(entity);
		TableInfo table = TableInfo.getInstance(entity.getClass());
		
		LinkedList<Object> params = new LinkedList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ")
		.append(table.getTableName())
		.append("(");
		for(KeyValue kv : keyValues){
			sql.append(kv.getKey()).append(",");
			params.add(kv.getValue());
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(") VALUES (");
		
		for(int i = 0; i < keyValues.size(); i++){
			sql.append("?,");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		debug(sql.toString());
		
		SQLObject sqlObject = new SQLObject(sql.toString(), params);
		return sqlObject;
	}
	
	public List<KeyValue> getKeyValuesFromEntity(Object entity){
		List<KeyValue> keyValues = new ArrayList<KeyValue>();
		TableInfo table = TableInfo.getInstance(entity.getClass());
		Object idValue = table.getId().getValue(entity);
		if(!StringUtils.isEmpty(idValue)){
			KeyValue keyValue = new KeyValue(table.getId().getName(), idValue);
			keyValues.add(keyValue);
		}
		
		//添加属性
		Collection<Column> columns = table.columnMap.values();
		for(Column c : columns){
			KeyValue kv = c.toKeyValue(entity);
			if(kv != null){
				keyValues.add(kv);
			}
		}
		
		//添加外键
		Collection<ManyToOne> manyToOneList = table.manyToOneMap.values();
		for(ManyToOne m2o : manyToOneList){
			KeyValue kv = m2o.toKeyValue(entity);
			if(m2o.isOptional() == false && kv == null){
				throw new FoxDbException("caused by: 您的映射策略标志了[" + kv.getKey() + "]该值不能为空，请为该值赋值。");
			} else if(kv != null){
				keyValues.add(kv);
			}
		}
		Collection<OneToOne> oneToOneList = table.oneToOneMap.values();
		for(OneToOne o2o : oneToOneList){
			KeyValue kv = o2o.toKeyValue(entity);
			if(kv != null){
				keyValues.add(kv);
			}
		}
		return keyValues;
	}

	@Override
	public String getPKValueSQL(Object entity) {
		TableInfo table = TableInfo.getInstance(entity.getClass());
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT last_insert_rowid() FROM ");
		sql.append(table.getTableName());
		debug(sql.toString());
		return sql.toString();
	}

	@Override
	public SQLObject getDeleteSQL(Object entity) {
		TableInfo table = TableInfo.getInstance(entity.getClass());
		Id id = table.getId();
		Object idValue = id.getValue(entity);
		if(idValue == null){
			throw new FoxDbException("没有找到您要删除的对象[" + entity.getClass() + "]的id");
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ")
		.append(table.getTableName())
		.append(" WHERE ").append(id.getName()).append(" = ?");
		debug(sql.toString());
		
		SQLObject sqlObject = new SQLObject(sql, idValue);
		return sqlObject;
	}

	@Override
	public SQLObject getDeleteSQL(Class<?> clazz, HashMap<String, Object> where) {
		TableInfo table = TableInfo.getInstance(clazz);
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ").append(table.getTableName());
		LinkedList<Object> params = new LinkedList<Object>();
		if(!StringUtils.isEmpty(where)){
			sql.append(" WHERE ");
			for(String key : where.keySet()){
				sql.append(key).append(" = ?").append(" and ");
				params.add(where.get(key));
			}
			sql = sql.delete(sql.length()-5, sql.length());
		}
		SQLObject sqlObject = new SQLObject(sql, params);
		debug(sql.toString());
		return sqlObject;
	}

	@Override
	public SQLObject getUpdateSQL(Object entity) {
		TableInfo table = TableInfo.getInstance(entity.getClass());
		Id id = table.getId();
		Object idValue = id.getValue(entity);
		if(StringUtils.isEmpty(idValue)){
			throw new FoxDbException("没有找到您要删除的对象[" + entity.getClass() + "]的id");
		}
		
		List<KeyValue> keyValues = getKeyValuesFromEntity(entity);
		if(StringUtils.isEmpty(keyValues)){
			throw new FoxDbException("没有在类[" + entity.getClass() + "]中找到任何属性。");
		}
		
		StringBuilder sql = new StringBuilder();
		LinkedList<Object> params = new LinkedList<Object>();
		sql.append("UPDATE ").append(table.getTableName())
		.append(" SET ");
		for(KeyValue kv : keyValues){
			sql.append(kv.getKey()).append("=?,");
			params.add(kv.getValue());
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(" WHERE ").append(id.getName()).append("=?");
		debug(sql.toString());
		params.add(idValue);
		SQLObject sqlObject = new SQLObject(sql, params);
		return sqlObject;
	}
	
	public SQLObject getQuerySQL(int startIndex, int maxResult, String where,
			Object[] params, LinkedHashMap<String, String> orderBy, Class<?> clazz) {
		TableInfo table = TableInfo.getInstance(clazz);
		String tableName = table.getTableName();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ")
		.append(tableName).append(buildWhere(where, params))
		.append(buildOrderBy(orderBy)).append(buildLimit(startIndex, maxResult));
		debug(sql.toString());
		SQLObject sqlObject = new SQLObject(sql, params);
		return sqlObject;
	}
	
	/**
	 * 构造查询条件语句
	 * @param where
	 * @param params
	 * @return
	 */
	private String buildWhere(String where, Object[] params){
		if(!StringUtils.isEmpty(where) && !StringUtils.isEmpty(params)){
			StringBuilder whereSQL = new StringBuilder();
			whereSQL.append(" WHERE ").append(where);
			return whereSQL.toString();
		}
		return "";
	}
	
	/**
	 * 构造排序的SQL语句
	 * @param orderBy	排序的条件组
	 * @return			返回排序的条件组
	 */
	private String buildOrderBy(LinkedHashMap<String, String> orderBy){
		if(!StringUtils.isEmpty(orderBy)){
			StringBuilder orderBySQL = new StringBuilder();
			orderBySQL.append(" ORDER BY ");
			for(String key : orderBy.keySet()){
				orderBySQL.append(key).append(" ").append(orderBy.get(key)).append(",");
			}
			orderBySQL.deleteCharAt(orderBySQL.length()-1);
			return orderBySQL.toString();
		}
		return "";
	}
	
	/**
	 * 构造分页的SQL语句
	 * @param startIndex	分页的起始索引
	 * @param maxResult		每一页显示的最大记录数
	 * @return				返回分页的SQL语句
	 */
	private String buildLimit(int startIndex, int maxResult){
		if(startIndex >= 0 && maxResult > 0){
			StringBuilder limitSQL = new StringBuilder();
			limitSQL.append(" LIMIT ").append(startIndex).append(",").append(maxResult);
			return limitSQL.toString();
		}
		return "";
	}

	@Override
	public SQLObject getQueryCountSQL(String where, Object[] params, Class<?> clazz) {
		TableInfo table = TableInfo.getInstance(clazz);
		String tableName = table.getTableName();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) FROM ")
		.append(tableName).append(buildWhere(where, params));
		debug(sql.toString());
		SQLObject sqlObject = new SQLObject(sql, params);
		return sqlObject;
	}

	@Override
	public SQLObject getQueryObjectSQL(Object parentId, Object parent,
			String attributeName, Class<?> sonClass) {
		TableInfo parentTable = TableInfo.getInstance(parent.getClass());
		Id parentIdObject = parentTable.getId();
		String foreignKeyName = "id";
		OneToOne o2o = parentTable.oneToOneMap.get(attributeName);
		if(o2o != null){
			foreignKeyName = TableInfo.buildForeignKeyName(o2o.getName());
		}
		ManyToOne m2o = parentTable.manyToOneMap.get(attributeName);
		if(m2o != null){
			foreignKeyName = TableInfo.buildForeignKeyName(m2o.getName());
		}
		TableInfo sonTable = TableInfo.getInstance(sonClass);
		Id sonId = sonTable.getId();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ").append(sonTable.getTableName())
		.append(" WHERE ").append(sonId.getName()).append(" = (SELECT ")
		.append(foreignKeyName).append(" FROM ").append(parentTable.getTableName())
		.append(" WHERE ").append(parentIdObject.getName()).append(" = ?)");
		debug(sql.toString());
		SQLObject sqlObject = new SQLObject(sql.toString(), parentId);
		return sqlObject;
	}

	@Override
	public SQLObject getAddNewColumnSQL(Column column) {
		StringBuilder sql = new StringBuilder();
		TableInfo tableInfo = TableInfo.getInstance(column.getParent());
		sql.append("ALTER TABLE [");
		sql.append(tableInfo.getTableName());
		sql.append("] add [");
		sql.append(TableInfo.buildForeignKeyName(column.getName())).append("]");
		if (column.getDataType() == String.class) {
			sql.append(" TEXT");
		} else if (FieldUtils.isInteger(column.getDataType())) {
			sql.append(" INTEGER");
		} else if (FieldUtils.isRealNumber(column.getDataType())) {
			sql.append(" REAL");
		}
		if (!column.isNullable()) {
			sql.append(" NOT NULL");
		}
		SQLObject sqlObject = new SQLObject();
		sqlObject.setSql(sql.toString());
		debug(sql);
		return sqlObject;
	}

}

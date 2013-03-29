package cn.com.lezhixing.foxdb.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import com.wecan.veda.utils.StringUtil;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.com.lezhixing.foxdb.core.FoxDB;
import cn.com.lezhixing.foxdb.core.SQLEngine;
import cn.com.lezhixing.foxdb.core.FoxDB.DbConfiguration;
import cn.com.lezhixing.foxdb.exception.FoxDbException;
import cn.com.lezhixing.foxdb.table.Column;
import cn.com.lezhixing.foxdb.table.Id;
import cn.com.lezhixing.foxdb.table.KeyValue;
import cn.com.lezhixing.foxdb.table.SQLObject;
import cn.com.lezhixing.foxdb.utils.FieldUtils;

/**
 * SQLite数据库的语言构造器
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-13
 */
public class SQLiteEngine implements SQLEngine {
	
	private DbConfiguration configuration;
	private SQLiteDatabase db;
	
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
		String sql = StringUtil.concat(new Object[]{
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
		
		if(!(idValue instanceof Integer)){//没有使用自增长类型
			if(idValue instanceof String && !StringUtil.isEmpty(idValue)){
				KeyValue keyValue = new KeyValue(table.getId().getName(), idValue);
				keyValues.add(keyValue);
			}
		}
		
		//添加属性
		Collection<Column> columns = table.columnMap.values();
		for(Column c : columns){
			KeyValue kv = c.toKeyValue(entity);
			if(kv != null){
				keyValues.add(kv);
			}
		}
		return keyValues;
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
	public SQLObject getUpdateSQL(Object entity) {
		TableInfo table = TableInfo.getInstance(entity.getClass());
		Id id = table.getId();
		Object idValue = id.getValue(entity);
		if(StringUtil.isEmpty(idValue)){
			throw new FoxDbException("没有找到您要删除的对象[" + entity.getClass() + "]的id");
		}
		
		List<KeyValue> keyValues = getKeyValuesFromEntity(entity);
		if(StringUtil.isEmpty(keyValues)){
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
		if(!StringUtil.isEmpty(where) && !StringUtil.isEmpty(params)){
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
		if(!StringUtil.isEmpty(orderBy)){
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

}

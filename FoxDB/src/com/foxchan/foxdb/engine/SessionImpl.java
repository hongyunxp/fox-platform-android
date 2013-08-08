package com.foxchan.foxdb.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.foxchan.foxdb.annotation.CascadeType;
import com.foxchan.foxdb.annotation.GeneratedType;
import com.foxchan.foxdb.core.SQLEngine;
import com.foxchan.foxdb.core.Session;
import com.foxchan.foxdb.core.Transaction;
import com.foxchan.foxdb.exception.FoxDbException;
import com.foxchan.foxdb.table.Column;
import com.foxchan.foxdb.table.Id;
import com.foxchan.foxdb.table.OneToMany;
import com.foxchan.foxdb.table.SQLObject;
import com.foxchan.foxdb.utils.Closer;
import com.foxchan.foxdb.utils.CursorUtils;
import com.foxchan.foxdb.utils.FieldUtils;
import com.foxchan.foxutils.data.CollectionUtils;
import com.foxchan.foxutils.data.StringUtils;

/**
 * Session的实现
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-13
 */
public class SessionImpl implements Session {
	
	private SQLiteDatabase db;
	private SQLEngine sqlEngine;
	private boolean isBeginTransaction;
	
	public SessionImpl(SQLiteDatabase db, SQLEngine sqlEngine){
		this.db = db;
		this.sqlEngine = sqlEngine;
	}

	@Override
	public Object save(Object object) throws FoxDbException {
		TableInfo table = TableInfo.getInstance(object.getClass());
		if (table.strategy == GeneratedType.UUID) {
			String str_id = StringUtils.getUUID();
			TableInfo.getInstance(object.getClass()).getId().setValue(object, str_id);
		}
		sqlEngine.checkTableExist(object.getClass());
		SQLObject sqlObject = sqlEngine.getInsertSQL(object);
		db.execSQL(sqlObject.getSql(), sqlObject.getParams().toArray());
		//获得插入的对象的主键并且封装为对象
		Cursor c = db.rawQuery(sqlEngine.getPKValueSQL(object), null);
		if (table.strategy == GeneratedType.IDENTITY &&
				FieldUtils.isInteger(table.getId().getDataType())) {
			c.moveToFirst();
			int int_id = c.getInt(0);
			table.getId().setValue(object, int_id);
		}
		return object;
	}
	
	@Override
	public Object saveOrUpdate(Object object) throws FoxDbException {
		Object id = TableInfo.getInstance(object.getClass()).getId().getValue(object);
		if(id == null){
			return save(object);
		} else {
			return update(object);
		}
	}
	
	@Override
	public void delete(Object object) throws FoxDbException {
		sqlEngine.checkTableExist(object.getClass());
		//当删除少的一方时同时删除多的一方
		TableInfo table = TableInfo.getInstance(object.getClass());
		Collection<OneToMany> oneToManies = table.oneToManyMap.values();
		for(OneToMany o2m : oneToManies){
			String mappedId = TableInfo.buildForeignKeyName(o2m.getMappedBy());
			List<CascadeType> cascadeTypes = Arrays.asList(o2m.getCascadeTypes());
			if ((cascadeTypes.contains(CascadeType.ALL) || 
					cascadeTypes.contains(CascadeType.REMOVE)) && 
					!StringUtils.isEmpty(o2m.getMappedBy())) {
				HashMap<String, Object> where = new HashMap<String, Object>();
				Object idValue = table.getId().getValue(object);
				where.put(mappedId, idValue);
				SQLObject deleteSqlObject = sqlEngine.getDeleteSQL(o2m.getOneClass(), where);
				db.execSQL(deleteSqlObject.getSql(), deleteSqlObject.getParams().toArray());
			}
		}
		SQLObject sqlObject = sqlEngine.getDeleteSQL(object);
		db.execSQL(sqlObject.getSql(), sqlObject.getParams().toArray());
	}

	@Override
	public Object update(Object object) throws FoxDbException {
		Class<?> clazz = object.getClass();
		sqlEngine.checkTableExist(clazz);
		SQLObject sqlObject = sqlEngine.getUpdateSQL(object);
		db.execSQL(sqlObject.getSql(), sqlObject.getParams().toArray());
		return object;
	}

	@Override
	public <T> PagerTemplate<T> query(int startIndex, int maxResult,
			String where, Object[] params, LinkedHashMap<String, String> orderBy, Class<?> clazz) {
		PagerTemplate<T> pagerTemplate = new PagerTemplate<T>();
		Cursor c = null;
		try {
			sqlEngine.checkTableExist(clazz);
			SQLObject sqlObject = sqlEngine.getQuerySQL(startIndex, maxResult, where, params, orderBy, clazz);
			c = db.rawQuery(sqlObject.getSql(), sqlObject.getBindArgsAsStringArray());
			List<T> list = new ArrayList<T>();
			if(c != null){
				while(c.moveToNext()){
					T entity = (T) CursorUtils.getEntity(c, clazz);
					list.add(entity);
				}
			}
			pagerTemplate = new PagerTemplate<T>();
			pagerTemplate.setPageData(list);
			//查询总数量
			long totalRecordsCount = findCount(where, params, clazz);
			pagerTemplate.setTotalRecordsCount(totalRecordsCount);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Closer.close(c);
		}
		return pagerTemplate;
	}
	
	@Override
	public <T> PagerTemplate<T> query(Pager<T> pager, String where,
			Object[] params, LinkedHashMap<String, String> orderBy,
			Class<?> clazz) {
		PagerTemplate<T> pt = query(pager.getStartIndex(), pager.getRecordsNumber(), where,
				params, orderBy, clazz);
		pager.setContent(pt.getPageData());
		pager.setTotalRecordsNumber((int)pt.getTotalRecordsCount());
		return pt;
	}

	@Override
	public <T> List<T> list(int startIndex, int maxResult,
			String where, Object[] params, LinkedHashMap<String, String> orderBy, Class<?> clazz){
		PagerTemplate<T> pagerTemplate = query(startIndex, maxResult, where, params, orderBy, clazz);
		return pagerTemplate.getPageData();
	}
	
	@Override
	public <T> List<T> list(int startIndex, int maxResult, String where, Object[] params, Class<?> clazz){
		return list(startIndex, maxResult, where, params, null, clazz);
	}
	
	@Override
	public <T> List<T> list(String where, Object[] params, Class<?> clazz){
		return list(-1, -1, where, params, null, clazz);
	}

	@Override
	public <T> List<T> list(Class<?> clazz) {
		return list(null, null, clazz);
	}

	@Override
	public <T> T get(Integer id, Class<?> clazz) {
		List<T> list = list("id=?", new Integer[]{id}, clazz);
		return StringUtils.isEmpty(list) ? null : list.get(0);
	}

	@Override
	public <T> T get(String id, Class<?> clazz) {
		List<T> list = list("id=?", new String[]{id}, clazz);
		return StringUtils.isEmpty(list) ? null : list.get(0);
	}

	@Override
	public <T> List<T> list(String where, Object[] params,
			LinkedHashMap<String, String> orderBy, Class<?> clazz) {
		return list(-1, -1, where, params, orderBy, clazz);
	}

	@Override
	public int findCount(String where, Object[] params, Class<?> clazz) {
		//查询总数量
		sqlEngine.checkTableExist(clazz);
		SQLObject sqlObject = sqlEngine.getQueryCountSQL(where, params, clazz);
		Cursor c = db.rawQuery(sqlObject.getSql(), sqlObject.getBindArgsAsStringArray());
		if(c != null && c.moveToNext()){
			return c.getInt(0);
		}
		return 0;
	}

	@Override
	public <T> T findObject(String where, Object[] params, Class<T> clazz) {
		List<T> list = list(where, params, clazz);
		return StringUtils.isEmpty(list) ? null : list.get(0);
	}

	@Override
	public <T> List<T> listFrom(Object parent, String attributeName,
			Class<?> sonClass) {
		return listFrom(-1, -1, null, null, null, parent, attributeName, sonClass);
	}

	@Override
	public <T> List<T> listFrom(int startIndex, int maxResult, String where,
			Object[] params, LinkedHashMap<String, String> orderBy,
			Object parent, String attributeName, Class<?> sonClass) {
		TableInfo table = TableInfo.getInstance(parent.getClass());
		Id parentIdObject = table.getId();
		OneToMany o2m = table.oneToManyMap.get(attributeName);
		String parentIdKey = "";
		if(o2m != null){
			parentIdKey = TableInfo.buildForeignKeyName(o2m.getMappedBy());
		}
		Object parentIdValue = parentIdObject.getValue(parent);
		String whereOrinal = where;
		if(!StringUtils.isEmpty(whereOrinal)){
			where = StringUtils.concat(new Object[]{
					where.trim(), " and ", parentIdKey, " = ?"
			});
		} else {
			where = StringUtils.concat(new Object[]{
					parentIdKey, " = ?"
			});
		}
		LinkedList<Object> newParams = new LinkedList<Object>();
		if(!StringUtils.isEmpty(params)){
			for(Object param : params){
				newParams.add(param);
			}
		}
		newParams.add(parentIdValue);
		return list(startIndex, maxResult, where, newParams.toArray(), orderBy, sonClass);
	}

	@Override
	public <T> T findObjectFrom(Object parent, String attributeName, Class<?> targetClass) {
		TableInfo parentTable = TableInfo.getInstance(parent.getClass());
		Object parentIdValue = parentTable.getId().getValue(parent);
		SQLObject sqlObject = sqlEngine.getQueryObjectSQL(parentIdValue, parent, attributeName, targetClass);
		sqlEngine.checkTableExist(targetClass);
		Cursor c = db.rawQuery(sqlObject.getSql(), sqlObject.getBindArgsAsStringArray());
		T result = null;
		if(c.moveToFirst()){
			result = (T) CursorUtils.getEntity(c, targetClass);
		}
		return result;
	}

	@Override
	public <T> List<T> executeQuery(String sql, Class<?> targetClass) {
		List<T> list = new ArrayList<T>();
		Cursor c;
		try {
			sqlEngine.checkTableExist(targetClass);
			c = db.rawQuery(sql, null);
			if(c != null){
				while(c.moveToNext()){
					T entity = (T) CursorUtils.getEntity(c, targetClass);
					list.add(entity);
				}
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			return list;
		}
		return list;
	}

	@Override
	public void executeUpdate(String sql) {
		db.execSQL(sql);
	}

	@Override
	public Transaction beginTransaction() {
		isBeginTransaction = true;
		db.beginTransaction();
		return new TransactionImpl(this);
	}

	@Override
	public void endTransaction() {
		isBeginTransaction = false;
	}

	@Override
	public SQLiteDatabase getDB() {
		return this.db;
	}

	public boolean isBeginTransaction() {
		return isBeginTransaction;
	}

	public void setBeginTransaction(boolean isBeginTransaction) {
		this.isBeginTransaction = isBeginTransaction;
	}

	@Override
	public void addNewColumn(List<Column> columns) {
		if(CollectionUtils.isEmpty(columns)) return;
		SQLObject sqlObject = null;
		for(Column c : columns){
			sqlObject = sqlEngine.getAddNewColumnSQL(c);
			db.execSQL(sqlObject.getSql());
		}
	}

}
package cn.com.lezhixing.foxdb.engine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.wecan.veda.utils.StringUtil;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.com.lezhixing.foxdb.core.SQLEngine;
import cn.com.lezhixing.foxdb.core.Session;
import cn.com.lezhixing.foxdb.exception.FoxDbException;
import cn.com.lezhixing.foxdb.table.SQLObject;
import cn.com.lezhixing.foxdb.utils.Closer;
import cn.com.lezhixing.foxdb.utils.CursorUtils;

/**
 * Session的实现
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-13
 */
public class SessionImpl implements Session {
	
	private SQLiteDatabase db;
	private SQLEngine sqlEngine;
	
	public SessionImpl(SQLiteDatabase db, SQLEngine sqlEngine){
		this.db = db;
		this.sqlEngine = sqlEngine;
	}

	@Override
	public Object save(Object object) throws FoxDbException {
		sqlEngine.checkTableExist(object.getClass());
		SQLObject sqlObject = sqlEngine.getInsertSQL(object);
		db.execSQL(sqlObject.getSql(), sqlObject.getParams().toArray());
		//获得该对象
		return null;
	}

	@Override
	public void delete(Object object) throws FoxDbException {
		sqlEngine.checkTableExist(object.getClass());
		SQLObject sqlObject = sqlEngine.getDeleteSQL(object);
		db.execSQL(sqlObject.getSql(), sqlObject.getParams().toArray());
	}

	@Override
	public Object update(Object object) throws FoxDbException {
		Class<?> clazz = object.getClass();
		sqlEngine.checkTableExist(clazz);
		SQLObject sqlObject = sqlEngine.getUpdateSQL(object);
		db.execSQL(sqlObject.getSql(), sqlObject.getParams().toArray());
		return null;
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
		return query(pager.getStartIndex(), pager.getRecordsNumber(), where,
				params, orderBy, clazz);
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
		return StringUtil.isEmpty(list) ? null : list.get(0);
	}

	@Override
	public <T> T get(String id, Class<?> clazz) {
		List<T> list = list("id=?", new String[]{id}, clazz);
		return StringUtil.isEmpty(list) ? null : list.get(0);
	}

	@Override
	public <T> List<T> list(String where, Object[] params,
			LinkedHashMap<String, String> orderBy, Class<?> clazz) {
		return list(-1, -1, where, params, orderBy, clazz);
	}

	@Override
	public int findCount(String where, Object[] params, Class<?> clazz) {
		//查询总数量
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
		return StringUtil.isEmpty(list) ? null : list.get(0);
	}

}

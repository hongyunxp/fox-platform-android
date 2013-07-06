package com.foxchan.foxdb.utils;

import com.foxchan.foxdb.engine.TableInfo;
import com.foxchan.foxdb.table.Column;

import android.database.Cursor;

/**
 * 数据集工具
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-13
 */
public class CursorUtils {
	
	/**
	 * 将查询结果封装成对象
	 * @param c
	 * @param clazz	
	 * @return
	 */
	public static <T> T getEntity(Cursor c, Class<T> clazz){
		try {
			if(c != null){
				TableInfo table = TableInfo.getInstance(clazz);
				int columnCount = c.getColumnCount();
				if(columnCount > 0){
					T entity = (T)clazz.newInstance();
					for(int i = 0; i < columnCount; i++){
						String columnName = c.getColumnName(i);
						Column column = table.columnMap.get(columnName);
						if(column != null){
							column.setValue(entity, c.getString(i));
						} else {
							if(table.getId().getName().equals(columnName)){
								table.getId().setValue(entity, c.getString(i));
							}
						}
					}
					return entity;
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}

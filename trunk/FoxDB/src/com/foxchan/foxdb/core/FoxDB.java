package com.foxchan.foxdb.core;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.foxchan.foxdb.engine.SQLiteEngine;
import com.foxchan.foxdb.engine.SessionImpl;
import com.foxchan.foxdb.exception.FoxDbException;
import com.foxchan.foxdb.table.Column;
import com.foxchan.foxdb.utils.Closer;
import com.foxchan.foxutils.data.CollectionUtils;

/**
 * FoxDB的操作类
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-13
 */
public class FoxDB {
	
	public static final String TAG = "FoxDB SQL";
	/** 是否是调试模式，如果是调试模式，将在后台打印SQL文件信息 */
	public static boolean DEBUG = false;
	/** 新添加的属性集合 */
	public static List<Column> newColumns;
	
	/** 数据库链接集合 */
	private static HashMap<String, FoxDB> dbMap = new HashMap<String, FoxDB>();
	
	private SQLiteDatabase db;
	private DbConfiguration configuration;
	
	private Session session;
	private SQLEngine sqlEngine;
	
	private FoxDB(DbConfiguration configuration){
		if(configuration == null){
			throw new FoxDbException("没有找到配置文件信息，请检查您的配置文件信息。");
		}
		if(configuration.context == null){
			throw new FoxDbException("Android context对象为空。");
		}
		this.configuration = configuration;
		this.db = new SqliteDbHelper(this.configuration.context,
				this.configuration.getDbName(), this.configuration.dbVersion)
				.getWritableDatabase();
		this.sqlEngine = new SQLiteEngine(configuration, db);
	}
	
	private synchronized static FoxDB getInstance(DbConfiguration configuration){
		FoxDB foxDB = dbMap.get(configuration.dbName);
		if(foxDB == null){
			foxDB = new FoxDB(configuration);
			dbMap.put(configuration.dbName, foxDB);
		}
		return foxDB;
	}
	
	/**
	 * 创建一个FoxDB对象，数据库的名称为fox.db
	 * @param context
	 * @return			返回一个可用的FoxDB对象
	 */
	public static FoxDB create(Context context){
		DbConfiguration configuration = new DbConfiguration();
		configuration.setContext(context);
		return getInstance(configuration);
	}
	
	/**
	 * 创建一个FoxDB对象
	 * @param context
	 * @param dbName	链接的数据库的名称
	 * @return			返回一个可用的FoxDB对象
	 */
	public static FoxDB create(Context context, String dbName){
		return create(context, dbName, true);
	}
	
	/**
	 * 创建一个FoxDB对象
	 * @param context
	 * @param dbName			数据库的名称
	 * @param isDebug			是否开启调试模式，开启调试模式后将在后台打印SQL语句
	 * @return					返回一个可用的FoxDB对象
	 */
	public static FoxDB create(Context context, String dbName, boolean isDebug){
		return create(context, dbName, 1, isDebug);
	}
	
	/**
	 * 创建一个FoxDB对象
	 * @param context
	 * @param dbName		数据库的名称
	 * @param dbVersion		数据库的版本号
	 * @return				返回一个可用的FoxDB对象
	 */
	public static FoxDB create(Context context, String dbName, int dbVersion){
		return create(context, dbName, dbVersion, true);
	}
	
	/**
	 * 创建一个FoxDB对象
	 * @param context
	 * @param dbName			数据库的名称
	 * @param dbVersion			数据库的版本号
	 * @param dbUpdateListener	数据库版本的监听器
	 * @param isDebug			是否开启调试模式，开启调试模式后将在后台打印SQL语句
	 * @return					返回一个可用的FoxDB对象
	 */
	public static FoxDB create(Context context, String dbName, int dbVersion,
			boolean isDebug) {
		DbConfiguration configuration = new DbConfiguration();
		configuration.setContext(context);
		configuration.setDbName(dbName);
		configuration.setDbVersion(dbVersion);
		return getInstance(configuration);
	}
	
	/**
	 * 创建一个FoxDB对象
	 * @param configuration	数据库配置文件信息
	 * @return				返回一个可用的FoxDB对象
	 */
	public static FoxDB create(DbConfiguration configuration){
		return getInstance(configuration);
	}

	/**
	 * 创建一个数据操作的session
	 * @return	返回一个新的数据库操作的session
	 */
	public Session openSession(){
		return new SessionImpl(db, sqlEngine);
	}
	
	/**
	 * 获得当前可以使用的数据库会话
	 * @return	返回当前可以使用的数据库会话
	 */
	public Session getCurrentSession(){
		if(this.session == null){
			this.session = openSession();
		}
		return this.session;
	}
	
	/**
	 * 添加新的属性
	 * @param columns	属性集合
	 */
	public static void addNewColumns(final List<Column> columns){
		newColumns = columns;
	}
	
	/**
	 * 数据库的配置文件
	 * @author chengqingmin@www.lezhixing.com.cn
	 * @create 2013-3-13
	 */
	public static class DbConfiguration{
		private Context context;
		/** 数据库的名称，默认的将采用fox.db */
		private String dbName = "fox.db";
		/** 数据库的版本 */
		private int dbVersion = 1;

		public Context getContext() {
			return context;
		}

		public void setContext(Context context) {
			this.context = context;
		}

		public String getDbName() {
			return dbName;
		}

		public void setDbName(String dbName) {
			this.dbName = dbName;
		}

		public int getDbVersion() {
			return dbVersion;
		}

		public void setDbVersion(int dbVersion) {
			this.dbVersion = dbVersion;
		}

	}
	
	/**
	 * 操作SQLite数据库的辅助类
	 * @author chengqingmin@www.lezhixing.com.cn
	 * @create 2013-3-13
	 */
	private class SqliteDbHelper extends SQLiteOpenHelper{
		
		public SqliteDbHelper(Context context, String name, int version) {
			super(context, name, null, version);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if(!CollectionUtils.isEmpty(newColumns)){
				FoxDB.this.db = db;
				sqlEngine = new SQLiteEngine(db);
				getCurrentSession().addNewColumn(newColumns);
			} else {
				Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table'", null);
				if(c != null){
					while(c.moveToNext()){
						String tableName = c.getString(0);
						if(!"sqlite_sequence".equals(tableName)){
							db.execSQL("DROP TABLE " + c.getString(0));
						}
					}
				}
				Closer.close(c);
			}
		}
	}
	
}

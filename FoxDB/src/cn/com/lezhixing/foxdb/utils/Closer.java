package cn.com.lezhixing.foxdb.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 该类负责关闭输入流等
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-13
 */
public class Closer {
	
	public static void close(SQLiteDatabase db){
		if(db != null){
			db.close();
			db = null;
		}
	}
	
	public static void close(Cursor cursor){
		if(cursor != null){
			cursor.close();
		}
	}

}

package cn.com.lezhixing.foxdb.engine;

import android.database.sqlite.SQLiteDatabase;
import cn.com.lezhixing.foxdb.core.Session;
import cn.com.lezhixing.foxdb.core.Transaction;

public class TransactionImpl implements Transaction {
	
	private Session session;
	private SQLiteDatabase db;

	public TransactionImpl(Session session){
		this.session = session;
		this.db = session.getDB();
	}
	
	@Override
	public void commit() {
		db.setTransactionSuccessful();
		db.endTransaction();
		session.endTransaction();
	}

}

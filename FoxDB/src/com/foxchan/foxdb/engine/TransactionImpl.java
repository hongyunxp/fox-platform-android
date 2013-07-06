package com.foxchan.foxdb.engine;

import com.foxchan.foxdb.core.Session;
import com.foxchan.foxdb.core.Transaction;

import android.database.sqlite.SQLiteDatabase;

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

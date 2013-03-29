package cn.com.lezhixing.foxdb;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import cn.com.lezhixing.foxdb.core.FoxDB;
import cn.com.lezhixing.foxdb.core.Session;
import cn.com.lezhixing.foxdb.engine.Pager;
import cn.com.lezhixing.foxdb.engine.PagerTemplate;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class FoxDBTest extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		
		FoxDB db = FoxDB.create(this, "fox.db", 2);
		Session session = db.openSession();
		
//		for(int i = 0; i < 43; i++){
//			User user = new User();
//			user.setCreateDate(new Date());
//			user.setIsGood(true);
//			user.setName("FoxDB" + i);
//			session.save(user);
//		}
		
		User user = session.get(2, User.class);
		System.out.println("2号用户的信息：" + user.getName());
		user.setName("我是2号");
		user.setWeight(22.0);
		user.setIsGood(true);
		session.update(user);
		user = session.get(user.getId(), User.class);
		System.out.println("2号用户新的信息：" + user.getName());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fox_dbtest, menu);
		return true;
	}

}

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
		
		FoxDB.DEBUG = true;
		FoxDB db = FoxDB.create(this, "fox.db", 1);
		Session session = db.openSession();
		
//		Group group = session.findObject("id=?", new Object[]{"0fe52f22128646009a6588b94dd2c21e"}, Group.class);
//		session.delete(group);
		
		Group group = session.findObject("id=?", new Object[]{"a5874cedcab9407392075994c89b9a1d"}, Group.class);
		List<User> users = session.listFrom(-1, -1, "weight > ?", new Object[]{16}, null, group, "users", User.class);
		Log.d("FoxDB", users.size() + "");
		
//		Group group = new Group();
//		group.setName("小组一");
//		session.save(group);
//		
//		for(int i = 0; i < 43; i++){
//			User user = new User();
//			user.setCreateDate(new Date());
//			user.setIsGood(true);
//			user.setName("FoxDB" + i);
//			user.setGroup(group);
//			session.save(user);
//		}
//		
//		User user = session.get(2, User.class);
//		System.out.println("2号用户的信息：" + user.getName());
//		user.setName("我是2号");
//		user.setWeight(22.0);
//		user.setIsGood(true);
//		session.update(user);
//		user = session.get(user.getId(), User.class);
//		System.out.println("2号用户新的信息：" + user.getName());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fox_dbtest, menu);
		return true;
	}

}

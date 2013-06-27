package cn.com.lezhixing.foxdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import cn.com.lezhixing.foxdb.core.FoxDB;
import cn.com.lezhixing.foxdb.core.Session;
import cn.com.lezhixing.foxdb.core.Transaction;
import cn.com.lezhixing.foxdb.engine.Pager;
import cn.com.lezhixing.foxdb.engine.PagerTemplate;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class FoxDBTest extends Activity {
	
	private Session session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		
		FoxDB.DEBUG = true;
		FoxDB db = FoxDB.create(this, "fox.db", 2);
		session = db.getCurrentSession();
		
		new Thread(){
			public void run(){
				testTransaction(200);
				testSave(200);
			}
		}.start();
		
//		User user = session.findObject("id = ?", new Object[]{2}, User.class);
//		Log.d("FoxDB", "User's name is " + user.getName());
//		SchoolCard card = session.findObjectFrom(user, "card", SchoolCard.class);
//		Log.d("FoxDB", "SchoolCard's number is " + card.getNumber());
//		Group group = session.findObjectFrom(user, "group", Group.class);
//		Log.d("FoxDB", "Group's name is " + group.getName());
//		List<User> users = session.listFrom(group, "users", User.class);
//		Log.d("FoxDB", group.getName() + "中一共有" + users.size() + "名成员。");
//		
//		Group group = session.findObject("id=?", new Object[]{"0fe52f22128646009a6588b94dd2c21e"}, Group.class);
//		session.delete(group);
//		
//		Group group = session.findObject("id=?", new Object[]{"a5874cedcab9407392075994c89b9a1d"}, Group.class);
//		List<User> users = session.listFrom(-1, -1, "weight > ?", new Object[]{16}, null, group, "users", User.class);
//		Log.d("FoxDB", users.size() + "");
//		
//		Pager<User> pager = new Pager<User>(15, 2);
//		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
//		orderBy.put("createDate", "desc");
//		orderBy.put("id", "desc");
//		PagerTemplate<User> pt = session.query(pager, null, null, orderBy, User.class);
//		pager.setContent(pt.getPageData());
//		pager.setTotalRecordsNumber((int)pt.getTotalRecordsCount());
//		Log.d("FoxDB",
//				"总共有" + pager.getTotalPage() + "页数据，总共"
//						+ pager.getTotalRecordsNumber() + "条数据，每页显示"
//						+ pager.getRecordsNumber() + "条数据，当前是第"
//						+ pager.getCurrentPage() + "页。");
//		for(User u : pager.getContent()){
//			Log.d("FoxDB", u.toString());
//		}
//		
//		Group group = new Group();
//		group.setName("小组一");
//		session.save(group);
//		
//		for(int i = 0; i < 43; i++){
//			SchoolCard card = new SchoolCard();
//			card.setNumber("2220093210620" + i);
//			session.save(card);
//			
//			User user = new User();
//			user.setCreateDate(new Date());
//			user.setIsGood(true);
//			user.setName("FoxDB" + i);
//			user.setGroup(group);
//			user.setCard(card);
//			session.save(user);
//		}
		
//		User user = session.get(2, User.class);
//		System.out.println("2号用户的信息：" + user.getName());
//		user.setName("我是2号");
//		user.setWeight(22.0);
//		user.setIsGood(true);
//		session.update(user);
//		user = session.get(user.getId(), User.class);
//		System.out.println("2号用户新的信息：" + user.getName());
	}

	/**
	 * 数据库的事务测试
	 */
	private void testTransaction(int count) {
		Date start = new Date();
		List<User> users = new ArrayList<User>();
		Transaction tx = session.beginTransaction();
		for(int i = 0; i < count; i++){
			User user = new User();
			user.setCard(null);
			user.setCreateDate(new Date());
			user.setGroup(null);
			user.setHeight(173.0f);
			user.setIsGood(true);
			user.setName("用户" + i);
			user.setWeight(20.00);
			users.add(user);
			session.save(user);
		}
		tx.commit();
		Date end = new Date();
		long time = end.getTime() - start.getTime();
		System.out.println("测试结束，总共耗时：" + time + "ms");
	}
	
	private void testSave(int count){
		Date start = new Date();
		for(int i = 0; i < count; i++){
			User user = new User();
			user.setCard(null);
			user.setCreateDate(new Date());
			user.setGroup(null);
			user.setHeight(173.0f);
			user.setIsGood(true);
			user.setName("用户" + i);
			user.setWeight(20.00);
			session.save(user);
		}
		Date end = new Date();
		long time = end.getTime() - start.getTime();
		System.out.println("测试结束，总共耗时：" + time + "ms");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fox_dbtest, menu);
		return true;
	}

}

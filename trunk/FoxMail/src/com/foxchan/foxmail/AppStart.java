package com.foxchan.foxmail;

import javax.mail.Message;
import javax.mail.Session;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.foxchan.foxmail.entity.FoxMail;
import com.foxchan.foxmail.utils.MailReceiver;
import com.foxchan.foxmail.utils.MailReceiver.MailBoxType;
import com.wecan.veda.utils.StringUtil;

public class AppStart extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_start);
		
		MailReceiver mailReceiver = new MailReceiver();
		Session session = mailReceiver.getSession();
		Message[] messages = mailReceiver.loadMessages("pop3.163.com",
				"gulangxiangjie@163.com", "922922cqm", session,
				MailBoxType.INBOX);
		if(!StringUtil.isEmpty(messages)){
			Log.d("cqm", "您总共有" + messages.length + "封邮件在收件箱中。");
			for(Message msg : messages){
				FoxMail foxMail = new FoxMail(msg);
				Log.d("cqm", foxMail.toString());
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.app_start, menu);
		return true;
	}

}

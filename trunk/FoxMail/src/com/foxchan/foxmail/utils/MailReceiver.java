package com.foxchan.foxmail.utils;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

/**
 * 接收邮件的辅助类
 * @author gulangxiangjie@gmail.com
 * @create 2013-4-5
 */
public class MailReceiver {
	
	/**
	 * 邮件的分类
	 * @author gulangxiangjie@gmail.com
	 * @create 2013-4-5
	 */
	public enum MailBoxType {
		/** 收件箱 */
		INBOX {
			@Override
			public String getName() {
				return "INBOX";
			}
		};
		
		/**
		 * 获得枚举项的名称
		 * @return	返回枚举项的名称
		 */
		public abstract String getName();
	}
	
	private static final String KEY_MAIL_HOST = "mail.smtp.host";
	private static final String KEY_MAIL_AUTH = "mail.smtp.auth";
	private static final String KEY_MAIL_POP3 = "pop3";
	private static final int VALUE_MAIL_POP3_PORT = 110;
	
	private String VALUE_MAIL_HOST = "smtp.163.com";
	private String VALUE_MAIL_AUTH = "true";
	
	/**
	 * 获得邮件处理的会话
	 * @return	返回邮件处理的会话
	 */
	public Session getSession(){
		Properties properties = new Properties();
		properties.put(KEY_MAIL_HOST, VALUE_MAIL_HOST);
		properties.put(KEY_MAIL_AUTH, VALUE_MAIL_AUTH);
		Session session = Session.getDefaultInstance(properties, null);
		return session;
	}
	
	/**
	 * 获得指定的邮箱中的邮件
	 * @param pop3			邮件服务器的POP3地址
	 * @param username		用户的用户名
	 * @param password		用户邮箱的密码
	 * @param session		邮件服务器的会话
	 * @param mailBoxType	邮箱的类型
	 * @return				返回指定的收件箱中的邮件
	 */
	public Message[] loadMessages(String pop3, String username, String password, 
			Session session, MailBoxType mailBoxType){
		Message[] messages = null;
		URLName urlName = new URLName(KEY_MAIL_POP3, pop3, VALUE_MAIL_POP3_PORT, 
				null, username, password);
		try {
			Store store = session.getStore(urlName);
			store.connect();
			Folder folder = store.getFolder(mailBoxType.getName());
			folder.open(Folder.READ_ONLY);
			messages = folder.getMessages();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return messages;
	}

}

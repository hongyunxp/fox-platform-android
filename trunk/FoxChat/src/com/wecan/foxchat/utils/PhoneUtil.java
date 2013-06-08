package com.wecan.foxchat.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.util.Log;

import com.wecan.foxchat.R;
import com.wecan.foxchat.entity.Call;
import com.wecan.foxchat.entity.Contacter;
import com.wecan.foxchat.entity.ContacterGroup;
import com.wecan.foxchat.entity.Sms;
import com.wecan.foxchat.exception.NotFoundPhoneNumberException;
import com.wecan.foxchat.exception.NotFoundSmsException;
import com.wecan.foxchat.exception.SmsEmptyException;
import com.wecan.veda.utils.StringUtil;

/**
 * 该类是与手机相关的辅助类
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-7
 */
public class PhoneUtil {
	
	/** 所有短信 */
	public static final String SMS_URI_ALL = "content://sms/";
	/** 收件箱中的短信 */
	public static final String SMS_URI_INBOX = "content://sms/inbox";
	/** 发件箱中的短信 */
	public static final String SMS_URI_SEND = "content://sms/sent";
	/** 草稿箱中的短信 */
	public static final String SMS_URI_DRAFT = "content://sms/draft";
	
	/** 分隔符 */
	public static final String COMMA = ",";

	/**
	 * 获得手机中的所有短信
	 * @param context
	 * @return			返回找到的所有短信的列表
	 */
	public static List<Sms> getSmsInPhone(Context context) {
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		orderBy.put("date", "desc");
		return findSmsInPhone(context, null, null, orderBy);
	}
	
	/**
	 * 获取与指定手机号码的联系人相关的短息并按降序排列
	 * @param context
	 * @param phoneNumber	手机号码
	 * @return				返回找到的短信列表
	 */
	public static List<Sms> findSmsInPhoneDescBy(Context context, String phoneNumber){
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		orderBy.put("date", "desc");
		phoneNumber = formatPhoneNumber(phoneNumber);
		phoneNumber = StringUtil.concat(new Object[]{
				"%", phoneNumber, "%"
		});
		return findSmsInPhone(context, "address like ?", new String[]{phoneNumber} , orderBy);
	}
	
	/**
	 * 获取与指定手机号码的联系人相关的短息
	 * @param context
	 * @param phoneNumber	手机号码
	 * @return				返回找到的短信列表
	 */
	public static List<Sms> findSmsInPhoneBy(Context context, String phoneNumber){
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		orderBy.put("date", "asc");
		phoneNumber = formatPhoneNumber(phoneNumber);
		phoneNumber = StringUtil.concat(new Object[]{
				"%", phoneNumber, "%"
		});
		return findSmsInPhone(context, "address like ?", new String[]{phoneNumber} , orderBy);
	}
	
	/**
	 * 根据短信的id号查找短信
	 * @param context
	 * @param smsId		短信的id号
	 * @return			返回找到的短信对象
	 */
	public static Sms findSmsById(Context context, int smsId){
		List<Sms> smsList = findSmsInPhone(context, "_id = ?", new String[]{smsId+""}, null);
		return smsList.size() == 0 ? null : smsList.get(0);
	}
	
	/**
	 * 查找出指定范围内的短信
	 * @param context
	 * @param phoneNumbers	需要进行查询的手机号码数组
	 * @return				返回找到的短信列表
	 * @throws NotFoundPhoneNumberException	没有找到手机号码时将抛出该异常
	 */
	public static List<Sms> findSmsIn(Context context, String... phoneNumbers)
			throws NotFoundPhoneNumberException {
		if(StringUtil.isEmpty(phoneNumbers)){
			throw new NotFoundPhoneNumberException(R.string.exception_not_found_phone_number);
		}
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		orderBy.put("date", "asc");
		StringBuilder where = new StringBuilder();
		where.append("address in (");
		for(int i = 0; i < phoneNumbers.length; i++){
			where.append("?,");
			phoneNumbers[i] = formatPhoneNumber(phoneNumbers[i]);
		}
		where.deleteCharAt(where.length()-1);
		where.append(")");
		return findSmsInPhone(context, where.toString(), phoneNumbers, orderBy);
	}
	
	/**
	 * 根据条件查找手机中的短信
	 * @param context
	 * @param where			形如："o._id = ?"的查询条件
	 * @param whereParams	对应条件中的问号的实际参数
	 * @param orderBy		排序的条件
	 * @return				返回找到的短信列表
	 */
	public static List<Sms> findSmsInPhone(Context context, String where,
			String[] whereParams, LinkedHashMap<String, String> orderBy) {
		List<Sms> smsList = new ArrayList<Sms>();
		Sms sms = null;
		try {
			ContentResolver cr = context.getContentResolver();
			String[] projection = new String[] { "_id", "address",
					"body", "date", "type", "read" };
			Uri uri = Uri.parse(SMS_URI_ALL);
			Cursor cur = cr.query(uri, projection, where, whereParams, buildOrderByStr(orderBy));
			if(cur == null) return smsList;
			if (cur.moveToFirst()) {
				int id;
				String phoneNumber;
				String smsbody;
				Date date;
				int type;
				int isNew;

				int idColumn = cur.getColumnIndex("_id");
				int phoneNumberColumn = cur.getColumnIndex("address");
				int smsbodyColumn = cur.getColumnIndex("body");
				int dateColumn = cur.getColumnIndex("date");
				int typeColumn = cur.getColumnIndex("type");
				int isNewColumn = cur.getColumnIndex("read");

				do {
					id = cur.getInt(idColumn);
					phoneNumber = cur.getString(phoneNumberColumn);
					smsbody = cur.getString(smsbodyColumn);
					date = new Date(Long.parseLong(cur.getString(dateColumn)));
					type = cur.getInt(typeColumn);
					isNew = cur.getInt(isNewColumn);
					
					sms = new Sms();
					sms.setContent(smsbody);
					sms.setCreateDate(date);
					sms.setId(id);
					sms.setMobilePhone(phoneNumber);
					if(isNew == 0){
						sms.setNew(true);
					} else {
						sms.setNew(false);
					}
					sms.setSmsType(type);
					smsList.add(sms);
				} while (cur.moveToNext());
			}
		} catch (SQLiteException ex) {
			Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
		}
		return smsList;
	}
	
	/**
	 * 将列表中的短信设置为已读状态
	 * @param smsList	需要处理的短信列表
	 */
	public static void readSmsInList(Activity activity, Sms[] smsList){
		if(StringUtil.isEmpty(smsList)) return;
		Uri uri = Uri.parse(SMS_URI_INBOX);
		StringBuilder sql = new StringBuilder();
		sql.append("_id in (");
		for(Sms sms : smsList){
			sql.append(sms.getId()).append(",");
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(") and read = 0");
		
		ContentValues values = new ContentValues();
		values.put("read", "1");
		activity.getContentResolver().update(uri, values, sql.toString(), null);
	}
	
	/**
	 * 阅读某一些手机号下的所有短信
	 * @param activity
	 * @param phoneNumbers	手机号码的数组
	 */
	public static void readSmsBySender(Activity activity, String[] phoneNumbers){
		if(StringUtil.isEmpty(phoneNumbers)) return;
		Uri uri = Uri.parse(SMS_URI_INBOX);
		StringBuilder sql = new StringBuilder();
		sql.append("address in (");
		for(String phoneNumber : phoneNumbers){
			sql.append(phoneNumber).append(",");
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(") and read = 0");
		
		ContentValues values = new ContentValues();
		values.put("read", "1");
		activity.getContentResolver().update(uri, values, sql.toString(), null);
	}
	
	/**
	 * 根据联系人的手机号码查询联系人的信息
	 * @param phoneNumber	手机号码
	 * @return				返回联系人对象
	 * @throws NotFoundPhoneNumberException  
	 */
	public static Contacter findContacterByPhone(Context context 
			,String phoneNumber) throws NotFoundPhoneNumberException {
	    List<Contacter> contacters = findContactersIn(context, phoneNumber);
	    return contacters.size() == 0 ? null : contacters.get(0);
	}
	
	/**
	 * 查找指定电话号码的联系人的信息
	 * @param context
	 * @param phoneNumbers	电话号码数组
	 * @return				返回找到的联系人的列表
	 * @throws NotFoundPhoneNumberException	
	 */
	public static List<Contacter> findContactersIn(Context context,
			String... phoneNumbers) throws NotFoundPhoneNumberException {
		if(StringUtil.isEmpty(phoneNumbers)){
			throw new NotFoundPhoneNumberException(R.string.exception_not_found_phone_number);
		}
		//组拼条件语句,格式化电话号码
		StringBuilder where = new StringBuilder();
		where.append(Phone.NUMBER).append(" in (");
		for(int i = 0; i < phoneNumbers.length; i++){
			where.append("?,");
			phoneNumbers[i] = formatPhoneNumber(phoneNumbers[i]);
		}
		where.deleteCharAt(where.length()-1);
		where.append(")");
		return findContacters(context, where.toString(), phoneNumbers, null);
	}
	
	/**
	 * 查询出所有的
	 * @param context
	 * @return
	 * @throws NotFoundPhoneNumberException 
	 */
	public static List<Contacter> findContactersWithSmsInGroup(Context context)
			throws NotFoundPhoneNumberException {
		List<Contacter> contacters = new ArrayList<Contacter>();
		String[] projection = new String[] { "DISTINCT(address)" };
		Uri uri = Uri.parse(SMS_URI_ALL);
		/*String selection = StringUtil.concat(new Object[]{
				"0==0) GROUP BY (address),(person"
		});*/
		Cursor contacterIdCur = context.getContentResolver()
				.query(uri, projection, null, null, "date desc");
		List<String> phones = new ArrayList<String>();
		while(contacterIdCur.moveToNext()){
			String phoneNumber = contacterIdCur.getString(0);
			phones.add(phoneNumber);
		}
		List<String> phoneNumbers = new ArrayList<String>();
		for(String phoneNumber : phones){
			phoneNumber = formatPhoneNumber(phoneNumber);
			if(!phoneNumbers.contains(phoneNumber) && !StringUtil.isEmpty(phoneNumber)){
				phoneNumbers.add(phoneNumber);
			}
		}
		for(String phoneNumber : phoneNumbers){
			List<Sms> smsList = findSmsInPhoneDescBy(context, phoneNumber);
			Contacter contacter = findContacterByPhone(context, phoneNumber);
			if(contacter == null){
				contacter = new Contacter();
				contacter.setMobilePhone(phoneNumber);
			}
			contacter.setSmsList(smsList);
			for(Sms s : smsList){
				s.setSender(contacter);
			}
			contacters.add(contacter);
		}
		contacterIdCur.close();
		return contacters;
	}
	
	/**
	 * 查询手机中所有的联系人信息
	 * @param context
	 * @return
	 */
	public static List<Contacter> findAllContacters(Context context){
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		orderBy.put(Phone.CONTACT_ID, "asc");
		return findContacters(context, null, null, orderBy);
	}
	
	/**
	 * 查询一个分组下的联系人信息
	 * @param context	
	 * @param groupId	分组的id号
	 * @return			返回找到的联系人列表
	 */
	public static List<Contacter> findContactersInGroup(Context context,
			int groupId) {
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		orderBy.put(Phone.CONTACT_ID, "asc");
		/*Cursor rawcontactOfGroup = context.getContentResolver().query(
				ContactsContract.Data.CONTENT_URI,
				new String[] { ContactsContract.Data.RAW_CONTACT_ID },
				ContactsContract.Data.MIMETYPE + " = '"
						+ GroupMembership.CONTENT_ITEM_TYPE + "' AND "
						+ ContactsContract.Data.DATA1 + "=" + groupId, null,
				null);*/
		return findContactersList(context, null, null, orderBy);
	}
	
	/**
	 * 查询手机中的联系人的信息
	 * @param context
	 * @param where		查询的条件，形如：_id=?
	 * @param params	条件中对应的问号的值
	 * @param orderBy	排序的规则
	 * @return			返回找到的联系人信息的列表
	 */
	public static List<Contacter> findContactersList(Context context, String where,
			String[] params, LinkedHashMap<String, String> orderBy) {
		final int PHONES_DISPLAY_NAME_INDEX = 0;//联系人显示名称
		final int PHONES_PHONE_NUMBER_INDEX = 1;//联系人的电话
	    final int PHONES_PHOTO_ID_INDEX = 2;//头像ID
	    final int PHONES_CONTACT_ID_INDEX = 3;//联系人的ID
	    
	    List<Contacter> contacters = new ArrayList<Contacter>();
		Contacter contacter = null;
		ContentResolver contentResolver = context.getContentResolver();
		String[] projection = new String[] { 
				PhoneLookup.DISPLAY_NAME, PhoneLookup.NUMBER, 
				PhoneLookup.PHOTO_ID, PhoneLookup._ID
		};
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, "*");
		Cursor cur = contentResolver.query(
				uri,
				projection,
				StringUtil.isEmpty(where) ? null : where,
				StringUtil.isEmpty(params) ? null : params, 
				buildOrderByStr(orderBy));
		if(cur.moveToFirst()){
			do {
				//得到联系人名称
				String contactName = cur.getString(PHONES_DISPLAY_NAME_INDEX);
				//得到联系人的电话
				String phoneNumber = cur.getString(PHONES_PHONE_NUMBER_INDEX);
				if(StringUtil.isEmpty(phoneNumber)) continue;
				phoneNumber = formatPhoneNumber(phoneNumber);
				//得到联系人ID
				Long contactid = cur.getLong(PHONES_CONTACT_ID_INDEX);
				//获得联系人头像id，该值大于0则说明联系人有头像，否则没有
				Long photoID = cur.getLong(PHONES_PHOTO_ID_INDEX);
				Bitmap photo = null;
				if(photoID > 0){
					Uri u =ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactid);
				    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, u);
				    photo = BitmapFactory.decodeStream(input);
				    try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				contacter = new Contacter();
				contacter.setId(contactid);
				contacter.setMobilePhone(phoneNumber);
				contacter.setName(contactName);
				if(contacter.getPhoto() == null) contacter.setPhoto(photo);
				contacters.add(contacter);
			} while (cur.moveToNext());
			cur.close();
		}
		return contacters;
	}
	
	/**
	 * 查询手机中的联系人的信息
	 * @param context
	 * @param where		查询的条件，形如：_id=?
	 * @param params	条件中对应的问号的值
	 * @param orderBy	排序的规则
	 * @return			返回找到的联系人信息的列表
	 */
	public static List<Contacter> findContacters(Context context, String where,
			String[] params, LinkedHashMap<String, String> orderBy) {
		final int PHONES_DISPLAY_NAME_INDEX = 0;//联系人显示名称
		final int PHONES_PHONE_NUMBER_INDEX = 1;//联系人的电话
	    final int PHONES_PHOTO_ID_INDEX = 2;//头像ID
	    final int PHONES_CONTACT_ID_INDEX = 3;//联系人的ID
	    
	    List<Contacter> contacters = new ArrayList<Contacter>();
		Contacter contacter = null;
		ContentResolver contentResolver = context.getContentResolver();
		String[] projection = new String[] { 
				Phone.DISPLAY_NAME, Phone.NUMBER,
				Photo.PHOTO_ID, Phone.CONTACT_ID 
		};
		Cursor cur = contentResolver.query(
				Phone.CONTENT_URI,
				projection,
				StringUtil.isEmpty(where) ? null : where,
				StringUtil.isEmpty(params) ? null : params, 
				buildOrderByStr(orderBy));
		if(cur.moveToFirst()){
			do {
				//得到联系人名称
				String contactName = cur.getString(PHONES_DISPLAY_NAME_INDEX);
				//得到联系人的电话
				String phoneNumber = cur.getString(PHONES_PHONE_NUMBER_INDEX);
				if(StringUtil.isEmpty(phoneNumber)) continue;
				phoneNumber = formatPhoneNumber(phoneNumber);
				//得到联系人ID
				Long contactid = cur.getLong(PHONES_CONTACT_ID_INDEX);
				//获得联系人头像id，该值大于0则说明联系人有头像，否则没有
				Long photoID = cur.getLong(PHONES_PHOTO_ID_INDEX);
				Bitmap photo = null;
				if(photoID > 0){
					Uri uri =ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactid);
				    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, uri);
				    photo = BitmapFactory.decodeStream(input);
				    try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				contacter = new Contacter();
				contacter.setId(contactid);
				contacter.setMobilePhone(phoneNumber);
				contacter.setName(contactName);
				if(contacter.getPhoto() == null) contacter.setPhoto(photo);
				contacters.add(contacter);
			} while (cur.moveToNext());
			cur.close();
		}
		return contacters;
	}
	
	/**
	 * 构造排序的sql语句
	 * @param orderBy	排序的要求
	 * @return			返回构造好的排序语句
	 */
	private static String buildOrderByStr(LinkedHashMap<String, String> orderBy){
		StringBuilder sb = null;
		if(orderBy != null){
			sb = new StringBuilder();
			for(String key : orderBy.keySet()){
				sb.append(key).append(" ").append(orderBy.get(key)).append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			return sb.toString();
		}
		return null;
	}
	
	/**
	 * 删除用户选中的短信
	 * @param context
	 * @param smsIds	短信的id号数组
	 * @throws NotFoundSmsException
	 */
	public static void deleteSmsIn(Context context, String... smsIds)
			throws NotFoundSmsException {
		if(StringUtil.isEmpty(smsIds)){
			throw new NotFoundSmsException(R.string.exception_not_found_sms);
		}
		//组拼条件语句,格式化电话号码
		StringBuilder where = new StringBuilder();
		where.append("_id").append(" in (");
		for(int i = 0; i < smsIds.length; i++){
			where.append("?,");
		}
		where.deleteCharAt(where.length()-1);
		where.append(")");
		
		Uri uri = Uri.parse(SMS_URI_ALL);
		context.getContentResolver().delete(uri, where.toString(), smsIds);
	}
	
	/**
	 * 格式化手机号码，主要是去掉手机号码前面的+86和飞信的12520前缀
	 * @param phoneNumber	需要处理的手机号码
	 * @return				返回处理好的手机号码
	 */
	public static String formatPhoneNumber(String phoneNumber){
		String phone = phoneNumber;
		phone = phone.replaceAll("^(\\+86)", "");
		phone = phone.replaceAll("^(86)", "");
		phone = phone.replaceAll("-", "");
		phone = phone.replaceAll("^(12520)", "");
		phone = phone.trim();
		phone = PhoneNumberUtils.formatNumber(phone);
		return phone;
	}
	
	/**
	 * 判断手机号码是不是飞信号码
	 * @param phoneNumber	需要判断的手机号码
	 * @return				如果手机号码是飞信号码则返回true，否则返回false
	 */
	public static boolean isFetion(String phoneNumber){
		phoneNumber = phoneNumber.trim();
		if(phoneNumber.startsWith("12520")){
			return true;
		}
		return false;
	}
	
	/**
	 * 发送短信
	 * @param context
	 * @param phoneNumber	手机号码
	 * @param content		短信的内容
	 * @param appendWords	短信的添加内容，判断短信是否为空时将排除这些文字
	 */
	public static void send(Context context, String phoneNumber, String content, String appendWords)
			throws SmsEmptyException, NotFoundPhoneNumberException {
		if(StringUtil.isEmpty(phoneNumber)){
			throw new NotFoundPhoneNumberException(R.string.exception_not_found_phone_number);
		}
		if(StringUtil.isEmpty(content) || content.equals(appendWords)){
			throw new SmsEmptyException(R.string.exception_sms_content_empty);
		}
		content = StringUtil.concat(new String[]{
				content, appendWords
		});
		SmsManager smsManager = SmsManager.getDefault();
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
		smsManager.sendTextMessage(phoneNumber, null, content, pi, null);
		Sms sms = new Sms();
		sms.setContent(content);
		sms.setMobilePhone(phoneNumber);
		sms.setNew(false);
		sms.setSmsType(Sms.SEND);
		insertSmsToDb(context, new Sms[]{sms});
	}
	
	/**
	 * 将短信插入短信数据库中
	 * @param context
	 * @param smsArray
	 */
	public static void insertSmsToDb(Context context, Sms[] smsArray){
		//将短信保存到发件箱
		if(!StringUtil.isEmpty(smsArray)){
			for(Sms sms : smsArray){
				ContentValues values = new ContentValues();
				values.put("date", System.currentTimeMillis());//发送时间
				values.put("read", sms.isNew() ? 0 : 1);//阅读状态
				values.put("type", sms.getSmsType());//收发状态
				values.put("address", sms.getMobilePhone());//送达的号码
				values.put("body", sms.getContent());//短信的正文内容
				context.getContentResolver().insert(Uri.parse("content://sms"),values);//插入短信库
			}
		}
	}
	
	/**
	 * 构造用户短信的签名
	 * @param context
	 * @return			返回用户的短信签名
	 */
	public static String buildSmsAppendWords(Context context){
		Boolean signState = Config.getConfig(context).getBoolean(Config.SIGN_STATE);
		if(!signState){
			return "";
		}
		String words = Config.getConfig(context).getString(Config.MY_SIGN);
		if(StringUtil.isEmpty(words)){
			return context.getString(R.string.sms_default_append_words);
		}
		return words;
	}
	
	/**
	 * 发送短信
	 * @param context
	 * @param phoneNumber	手机号码
	 * @param content		短信的内容
	 * @param appendWords	短信的添加内容，判断短信是否为空时将排除这些文字
	 */
	public static void send(Context context, String[] phoneNumbers,
			String content, String appendWords) throws SmsEmptyException,
			NotFoundPhoneNumberException {
		if(StringUtil.isEmpty(phoneNumbers)){
			throw new NotFoundPhoneNumberException(R.string.exception_not_found_phone_number);
		}
		for(int i = 0; i < phoneNumbers.length; i++){
			send(context, phoneNumbers[i], content, appendWords);
		}
	}
	
	/**
	 * 判断手机号码是否符合要求
	 * @param phoneNumber	手机号码
	 * @return				如果手机号码符合要求则返回true，否则返回false
	 */
	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;
		String[] specialPhoneArray = new String[]{
				"10086", "10010", "10000"
		};
		List<String> specialPhone = StringUtil.transferArrayToList(specialPhoneArray);
		if(specialPhone.contains(phoneNumber)) return true;
		String expression = "^(1(([35][0-9])|(47)|[8][01236789]))\\d{8}$";
		String inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}
	
	/**
	 * 打电话
	 * @param phoneNumber	手机号码
	 */
	public static void makeTelephoneCall(Context context, String phoneNumber){
		Uri uri = Uri.parse("tel:" + phoneNumber);
		Intent intent = new Intent(Intent.ACTION_CALL, uri);
		context.startActivity(intent);
	}
	
	/**
	 * 查找手机中的所有通讯记录
	 * @param context
	 * @return			返回找到的通话记录列表
	 */
	public static List<Call> findAllCallLogs(Context context){
		Uri uri = CallLog.Calls.CONTENT_URI;
		String[] projection = new String[] { 
				CallLog.Calls.NUMBER,
				CallLog.Calls.CACHED_NAME, CallLog.Calls.TYPE,
				CallLog.Calls.DATE, CallLog.Calls.DURATION,
				"COUNT(*) AS call_count"
		};
		String selection = StringUtil.concat(new Object[]{
				"0==0) GROUP BY (", Calls.NUMBER, "),(", 
				Calls.TYPE, "),(", Calls.DATE, "/86400000"
		});
		String orderByStr = CallLog.Calls.DEFAULT_SORT_ORDER;
		Cursor cur = context.getContentResolver().query(uri, projection, selection,
				null, orderByStr);
		List<Call> calls = new ArrayList<Call>();
		Call call = null;
		while(cur.moveToNext()){
			int numberColumn = cur.getColumnIndex(CallLog.Calls.NUMBER);
			int cacheNameColumn = cur.getColumnIndex(CallLog.Calls.CACHED_NAME);
			int typeColumn = cur.getColumnIndex(CallLog.Calls.TYPE);
			int dateColumn = cur.getColumnIndex(CallLog.Calls.DATE);
			int durationColumn = cur.getColumnIndex(CallLog.Calls.DURATION);
			int callCountColumn = cur.getColumnIndex("call_count");
			
			String phone = cur.getString(numberColumn);
			String name = cur.getString(cacheNameColumn);
			int type = cur.getInt(typeColumn);
			Date date = new Date(Long.parseLong(cur.getString(dateColumn)));
			long duration = cur.getLong(durationColumn);
			int callCount = cur.getInt(callCountColumn);
			
			call = new Call();
			call.setCallDate(date);
			call.setCallType(type);
			call.setContacterName(name);
			call.setDuration(duration);
			call.setId(StringUtil.getUUID());
			call.setPhone(phone);
			call.setCallCount(callCount);
			calls.add(call);
		}
		cur.close();
		return calls;
	}
	
	/**
	 * 根据要求查询通讯录的分组信息
	 * @param context
	 * @param where				条件子语句
	 * @param params			条件的参数
	 * @param orderBy			排序的语句
	 * @param isLoadContacters	是否需要加载联系人信息
	 * @return					返回找到的分组信息
	 */
	public static List<ContacterGroup> findContacterGroups(Context context,
			String where, String[] params,
			LinkedHashMap<String, String> orderBy, boolean isLoadContacters) {
		List<ContacterGroup> contacterGroups = null;
		String[] projections = new String[] { 
				ContactsContract.Groups.TITLE,
				ContactsContract.Groups._ID 
		};
		Cursor cur = context.getContentResolver().query(
				ContactsContract.Groups.CONTENT_URI,
				projections,
				StringUtil.isEmpty(where) ? null : where, 
				StringUtil.isEmpty(params) ? null : params, 
				buildOrderByStr(orderBy));
		
		int idColumn = cur.getColumnIndex(ContactsContract.Groups._ID);
		int titleColumn = cur.getColumnIndex(ContactsContract.Groups.TITLE);
		long id;
		String title = "";
		ContacterGroup group = null;
		while(cur.moveToNext()){
			id = cur.getLong(idColumn);
			title = cur.getString(titleColumn);
			group = new ContacterGroup();
			group.setId(id);
			group.setTitle(title);
			group.setContacters(null);
			if(isLoadContacters){
				
			}
		}
		return contacterGroups;
	}
	
}

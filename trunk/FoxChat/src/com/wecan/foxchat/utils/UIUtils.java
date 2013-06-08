package com.wecan.foxchat.utils;

import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.Gson;
import com.wecan.foxchat.AppManager;
import com.wecan.foxchat.R;
import com.wecan.foxchat.entity.Sms;
import com.wecan.foxchat.ui.ConfigView;
import com.wecan.foxchat.ui.MainMenu;
import com.wecan.foxchat.ui.SmsDetailView;
import com.wecan.foxchat.ui.SmsMenu;
import com.wecan.foxchat.ui.SmsReceiveView;
import com.wecan.foxchat.ui.SmsSelectSenderView;
import com.wecan.foxchat.ui.SmsView;
import com.wecan.foxchat.ui.SmsWriteView;
import com.wecan.veda.utils.StringUtil;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * 该类提供关于UI界面和界面跳转之间的功能
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-8
 */
public class UIUtils {
	
	/** 程序的Notification的ID号 */
	public static final int NOTIFICATION_ID = 922;
	
	/**
	 * 跳转到显示所有短信列表的界面
	 * @param context
	 */
	public static void toSmsContacterView(Context context){
		Intent intent = new Intent(context, SmsView.class);
		context.startActivity(intent);
	}
	
	/**
	 * 跳转到显示短信详情的界面
	 * @param context		
	 * @param phoneNumber	需要传递的手机号码
	 * @param contacterName	需要传递的联系人的姓名
	 */
	public static void toSmsDetailActivity(Context context, String phoneNumber,
			String contacterName) {
		Intent intent = new Intent(context, SmsDetailView.class);
		intent.putExtra("phoneNumber", phoneNumber);
		intent.putExtra("contacterName", contacterName);
		context.startActivity(intent);
	}
	
	/**
	 * 跳转到写信息的界面
	 * @param context
	 */
	public static void toWriteSmsView(Context context){
		Intent intent = new Intent(context, SmsWriteView.class);
		context.startActivity(intent);
	}
	
	/**
	 * 跳转到写信息的界面
	 * @param context
	 * @param content	短信的正文
	 */
	public static void toWriteSmsView(Context context, String content){
		Intent intent = new Intent(context, SmsWriteView.class);
		intent.putExtra("smsContent", content);
		context.startActivity(intent);
	}
	
	/**
	 * 显示操作短信的菜单界面
	 * @param context
	 * @param smsId		需要操作的短信的id号
	 * @param currentSmsCount	当前列表中短信的条数
	 */
	public static void showSmsMenu(Context context, int smsId, int currentSmsCount){
		Intent intent = new Intent(context, SmsMenu.class);
		intent.putExtra("smsId", smsId);
		intent.putExtra("currentSmsCount", currentSmsCount);
		context.startActivity(intent);
	}
	
	/**
	 * 跳转到选择收件人的界面
	 * @param context
	 * @param contacterMap	当前用户选择的短信接收人
	 */
	public static void toSelectSenderView(Context context,
			LinkedHashMap<String, String> contacterMap) {
		Intent intent = new Intent(context, SmsSelectSenderView.class);
		if(contacterMap != null && contacterMap.size() > 0){
			for(String key : contacterMap.keySet()){
				intent.putExtra(key, contacterMap.get(key));
			}
		}
		context.startActivity(intent);
	}
	
	/**
	 * 用户选择完短信的联系人后跳转到写短信的界面
	 * @param context
	 * @param contactersMap	用户选择的电话号码
	 */
	public static void smsSendersSelected(Context context, 
			LinkedHashMap<String, String> contactersMap) {
		Intent intent = new Intent(context, SmsWriteView.class);
		if(contactersMap != null && contactersMap.size() > 0){
			for(String key : contactersMap.keySet()){
				intent.putExtra(key, contactersMap.get(key));
			}
		}
		context.startActivity(intent);
	}
	
	/**
	 * 显示主菜单
	 * @param context
	 */
	public static void showMainMenu(Context context){
		Intent intent = new Intent(context, MainMenu.class);
		context.startActivity(intent);
	}
	
	/**
	 * 跳转到软件的设置界面
	 * @param context
	 */
	public static void toConfigView(Context context){
		Intent intent = new Intent(context, ConfigView.class);
		context.startActivity(intent);
	}
	
	/**
	 * 跳转到显示短信内容的界面
	 * @param context
	 * @param sms
	 */
	public static void showSmsReceiveView(Context context, List<Sms> smsList){
		Intent intent = new Intent(context, SmsReceiveView.class);
		//设置Activity以一个新的任务来执行task
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Gson gson = new Gson();
		String smsListString = gson.toJson(smsList);
		intent.putExtra("smsListString", smsListString);
		context.startActivity(intent);
	}
	
	/**
	 * 显示一个时间短暂的提示信息
	 * @param context
	 * @param content	需要显示的信息内容
	 */
	public static void toast(Context context, String content){
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 显示一个时间短暂的提示信息
	 * @param context
	 * @param content	需要显示的信息内容
	 */
	public static void toast(Context context, int content){
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 隐藏软键盘
	 * @param context
	 * @param imm
	 */
	public static void hideKeyBoard(Context context, InputMethodManager imm) {
		imm.hideSoftInputFromWindow(AppManager.getAppManager()
				.currentActivity().getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**
	 * 隐藏软键盘
	 * @param context
	 * @param imm
	 */
	public static void hideKeyBoard(Activity activity, InputMethodManager imm) {
		if(activity == null || imm == null || activity.getCurrentFocus() == null) return;
		imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**
	 * 发出软件的Notification的method
	 * @param context
	 * @param nm
	 * @param sms
	 */
	public static void setNotificationType(Context context,
			NotificationManager nm, Sms sms) {
		setNotificationType(context, nm, R.drawable.icon_app_notification, StringUtil.concat(new Object[]{
				sms.getSender().displayName(), ":", sms.getContent()
		}), sms);
	}
	
	/**
	 * 发出Notification的method
	 * @param context
	 * @param nm
	 * @param iconId	图标的id号
	 * @param text		提示信息的文字
	 * @param sms
	 */
	public static void setNotificationType(Context context,
			NotificationManager nm, int iconId, String text, Sms sms) {
		Intent intent = new Intent(context, SmsDetailView.class);
		intent.putExtra("phoneNumber", sms.getMobilePhone());
		intent.putExtra("contacterName", sms.getSender().displayName());
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
		
		Notification notification = new Notification();
		notification.icon = iconId;
		notification.tickerText = text;
		notification.defaults = Notification.DEFAULT_ALL;
		notification.setLatestEventInfo(context, sms.getSender().displayName(),
				sms.getContent(), pi);
		notification.flags = Notification.FLAG_SHOW_LIGHTS;
		//发出Notification
		nm.notify(NOTIFICATION_ID, notification);
	}
	
}

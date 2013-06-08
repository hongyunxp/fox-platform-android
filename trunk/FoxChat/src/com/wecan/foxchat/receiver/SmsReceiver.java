package com.wecan.foxchat.receiver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wecan.foxchat.entity.Contacter;
import com.wecan.foxchat.entity.Sms;
import com.wecan.foxchat.exception.NotFoundPhoneNumberException;
import com.wecan.foxchat.utils.PhoneUtil;
import com.wecan.foxchat.utils.UIUtils;
import com.wecan.veda.utils.StringUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * 接收系统收到短信的广播，并显示短信详情的界面
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-17
 */
public class SmsReceiver extends BroadcastReceiver {
	
	/** 系统收到短信的标识符 */
	private static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(SMS_ACTION)){
			List<Sms> smsList = new ArrayList<Sms>();
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				Object[] objPdus = (Object[])bundle.get("pdus");
				SmsMessage[] messages = new SmsMessage[objPdus.length];
				if(!StringUtil.isEmpty(objPdus)){
					for(int i = 0; i < objPdus.length; i++){
						messages[i] = SmsMessage.createFromPdu((byte[])objPdus[i]);
					}
					//将系统短信封装成短信对象
					for(SmsMessage message : messages){
						Sms sms = new Sms();
						sms.setId(message.getIndexOnIcc());
						sms.setContent(message.getDisplayMessageBody());
						sms.setCreateDate(new Date(message.getTimestampMillis()));
						sms.setMobilePhone(message.getDisplayOriginatingAddress());
						sms.setNew(true);
						sms.setSmsType(Sms.RECEIVE);
						Contacter contacter = null;
						try {
							contacter = PhoneUtil
									.findContacterByPhone(context,
											sms.getMobilePhone());
							if(contacter == null){
								contacter = new Contacter();
								contacter.setMobilePhone(sms.getMobilePhone());
								contacter.setName(sms.getMobilePhone());
							}
						} catch (NotFoundPhoneNumberException e) {
							e.printStackTrace();
						}
						sms.setSender(contacter);
						smsList.add(sms);
					}
				}
				abortBroadcast();
				Sms[] smsArray = new Sms[smsList.size()];
				for(int i = 0; i < smsList.size(); i++){
					smsArray[i] = smsList.get(i);
				}
				//将信息存储到数据库中
				PhoneUtil.insertSmsToDb(context, smsArray);
				//跳转到显示信息内容的界面
				UIUtils.showSmsReceiveView(context, smsList);
			}
		}
	}

}

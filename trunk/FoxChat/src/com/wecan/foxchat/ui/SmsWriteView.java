package com.wecan.foxchat.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.wecan.foxchat.AppManager;
import com.wecan.foxchat.R;
import com.wecan.foxchat.exception.AndroidException;
import com.wecan.foxchat.sensor.ShakeSensor;
import com.wecan.foxchat.sensor.listener.OnPhoneShakeListener;
import com.wecan.foxchat.utils.PhoneUtil;
import com.wecan.foxchat.utils.UIUtils;
import com.wecan.veda.utils.StringUtil;

/**
 * 用户发送短信的界面
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-9
 */
public class SmsWriteView extends Activity {
	
	/** 头部的标题 */
	private TextView tvTitle;
	/** 头部右侧返回的图标 */
	private ImageView ivBack;
	/** 收件人的输入框 */
	private EditText etSmsReceiver;
	/** 添加收件人的按钮 */
	private ImageView ivAddSmsReceiver;
	/** 信息的正文 */
	private EditText etSmsContent;
	/** 发送按钮 */
	private Button btnSendSms;
	/** 手机摇动的传感器 */
	private ShakeSensor shakeSensor;
	/** 管理输入法的工具 */
	private InputMethodManager imm;
	/** 加载中的图片 */
	private ImageView ivLoading;
	/** 动画对象 */
	private Animation loadingAnimation;
	/** 界面切换的对象 */
	private ViewSwitcher viewSwitcher;
	/** 用户选择的短信的接收人 */
	private LinkedHashMap<String, String> contacterMap = new LinkedHashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_write);
		AppManager.getAppManager().addActivity(this);
		init();
	}

	/**
	 * 初始化写信息的界面
	 */
	private void init() {
		//初始化相关的组件
		tvTitle = (TextView)findViewById(R.id.sms_header_title);
		ivBack = (ImageView)findViewById(R.id.sms_header_icon);
		etSmsReceiver = (EditText)findViewById(R.id.sms_write_receiver);
		ivAddSmsReceiver = (ImageView)findViewById(R.id.sms_write_add_receiver);
		etSmsContent = (EditText)findViewById(R.id.sms_write_content);
		btnSendSms = (Button)findViewById(R.id.sms_write_send);
		shakeSensor = new ShakeSensor(this);
		imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		ivLoading = (ImageView)findViewById(R.id.sms_loading);
		loadingAnimation = AnimationUtils.loadAnimation(this, R.anim.loading);
		viewSwitcher = (ViewSwitcher)findViewById(R.id.sms_write_view_switcher);
		
		//设置界面的标题
		tvTitle.setText(getString(R.string.sms_write_title));
		//设置返回按钮
		ivBack.setImageResource(R.drawable.sms_back_btn);
		//让输入电话的文本框获得焦点
		etSmsReceiver.requestFocus();
		imm.showSoftInput(etSmsReceiver, InputMethodManager.SHOW_FORCED);
		
		//设置返回按钮的点击事件
		ivBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		//绑定添加联系人按钮的事件
		ivAddSmsReceiver.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UIUtils.toSelectSenderView(v.getContext(), contacterMap);
			}
		});
		//绑定发信息按钮的事件
		btnSendSms.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sendSms(PhoneUtil.buildSmsAppendWords(v.getContext()));
			}
		});
		//绑定用户摇手机发短信的事件
		shakeSensor.setOnPhoneShakeListener(new OnPhoneShakeListener() {
			public void doAfterShake() {
				sendSms(PhoneUtil.buildSmsAppendWords(SmsWriteView.this));
			}
		});
		//绑定搜索框的内容监听事件
		etSmsReceiver.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_UP) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_DEL:
						if (contacterMap != null && contacterMap.size() > 0) {
							String key = "";
							for (String k : contacterMap.keySet()) {
								key = k;
							}
							contacterMap.remove(key);
							showPhoneNumbers();
						}
						break;
					}
				}
				return false;
			}
		});
	}
	
	/**
	 * 块状显示用户选中的联系人信息
	 */
	private void showPhoneNumbers(){
		etSmsReceiver.getText().clear();
		for(String key : contacterMap.keySet()){
			String str = contacterMap.get(key);
			str = StringUtil.concat(new Object[]{
					" ", str, " ", PhoneUtil.COMMA
			});
			SpannableString ss = new SpannableString(str);
			String colorStr = getString(R.color.app_background);
			int color = Color.parseColor(colorStr);
			//设置背景色
			ss.setSpan(new BackgroundColorSpan(color), 0, str.length()-1,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置前景色
			ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, str.length()-1,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			etSmsReceiver.getText().insert(etSmsReceiver.getSelectionStart(), ss);
		}
	}
	
	/**
	 * 发送短信
	 * @param appendWords	需要在正文后添加的文字，比如说个性签名
	 */
	private void sendSms(String appendWords){
		//隐藏输入框
		UIUtils.hideKeyBoard(AppManager.getAppManager().currentActivity(),
				imm);
		viewSwitcher.showNext();
		tvTitle.setText(getString(R.string.tip_sms_sending));
		ivLoading.startAnimation(loadingAnimation);
		String smsContent = etSmsContent.getText().toString();
		if(!StringUtil.isEmpty(appendWords)){
			smsContent = StringUtil.concat(new Object[]{
				smsContent, appendWords
			});
		}
		//处理用户的短信接收人的号码
		try {
			String[] phoneNumbers = getValidPhoneNumbers();
			PhoneUtil.send(this, phoneNumbers, smsContent, appendWords);
			UIUtils.toast(this, R.string.tip_sms_send_success);
			//获得所有的联系人信息
			String contacterName = getValidContacterName(phoneNumbers);
			//组拼所有号码的字符串
			StringBuilder phoneNumberString = new StringBuilder();
			for(String phone : phoneNumbers){
				phoneNumberString.append(phone).append(PhoneUtil.COMMA);
			}
			phoneNumberString = phoneNumberString.deleteCharAt(phoneNumberString.length()-1);
			UIUtils.toSmsDetailActivity(this, phoneNumberString.toString(), contacterName);
			//清空短信内容的输入框
			etSmsReceiver.setText("");
			etSmsContent.setText("");
			finish();
		} catch (AndroidException e) {
			viewSwitcher.showPrevious();
			tvTitle.setText(getString(R.string.sms_write_title));
			UIUtils.toast(this, e.getMessageInt());
		}
	}
	
	/**
	 * 获得用户当前所有有效的电话号码
	 * @return
	 */
	private String[] getValidPhoneNumbers(){
		List<String> phoneNumberList = new ArrayList<String>();
		if(!StringUtil.isEmpty(contacterMap)){
			for(String key : contacterMap.keySet()){
				phoneNumberList.add(key);
			}
		}
		String[] attachePhone = etSmsReceiver.getText().toString().split(PhoneUtil.COMMA);
		for(String phone : attachePhone){
			if(PhoneUtil.isPhoneNumberValid(phone) && !phoneNumberList.contains(phone)){
				phoneNumberList.add(phone);
			}
		}
		return StringUtil.tranferListToArray(phoneNumberList);
	}
	
	/**
	 * 获得当前用户聊天的人的姓名
	 * @param phoneNumbers
	 * @return
	 */
	private String getValidContacterName(String[] phoneNumbers){
		String str = "";
		for(String phone : phoneNumbers){
			String name = contacterMap.get(phone);
			if(StringUtil.isEmpty(name)){
				name = phone;
			}
			str = StringUtil.concat(new Object[]{
					str, PhoneUtil.COMMA, name
			});
		}
		if(str.startsWith(PhoneUtil.COMMA)){
			str = str.substring(1);
		}
		return str;
	}

	@Override
	protected void onPause() {
		shakeSensor.stop();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		shakeSensor.register();
		//如果是转发的消息，则显示消息正文
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			for(String key : bundle.keySet()){
				if("smsContent".equals(key)){
					String str = bundle.getString(key);
					if(!StringUtil.isEmpty(str)){
						etSmsContent.setText(str);
					}
				} else {
					contacterMap.put(key, bundle.getString(key));
				}
			}
		}
		//显示用户要发送的手机号码
		showPhoneNumbers();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	@Override
	protected void onStop() {
		shakeSensor.stop();
		super.onStop();
	}

}

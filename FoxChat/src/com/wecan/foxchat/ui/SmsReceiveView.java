package com.wecan.foxchat.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.wecan.foxchat.R;
import com.wecan.foxchat.entity.Sms;
import com.wecan.foxchat.exception.AndroidException;
import com.wecan.foxchat.sensor.ShakeSensor;
import com.wecan.foxchat.sensor.listener.OnPhoneShakeListener;
import com.wecan.foxchat.utils.PhoneUtil;
import com.wecan.foxchat.utils.UIUtils;

/**
 * 用户收到短信后弹出的短信窗口
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-17
 */
public class SmsReceiveView extends Activity {
	
	/** 用户收到的短信列表 */
	private List<Sms> smsList = new ArrayList<Sms>();
	/** 用户当前在看的短信 */
	private Sms sms;
	/** 信息的发送者 */
	private TextView tvContacterName;
	/** 关闭窗口的按钮 */
	private ImageView ivClose;
	/** 信息的正文 */
	private TextView tvSmsContent;
	/** 界面切换的对象 */
	private ViewSwitcher viewSwitcher;
	/** 已读的按钮 */
	private Button btnRead;
	/** 回复的按钮 */
	private Button btnResponse;
	/** 用户回复信息的输入框 */
	private EditText etSmsResponseContent;
	/** 发送信息的按钮 */
	private Button btnSend;
	/** 软键盘的管理对象 */
	private InputMethodManager imm;
	/** 管理标题栏图标的组件 */
	private NotificationManager nm;
	/** 检测手机摇一摇的传感器 */
	private ShakeSensor shakeSensor;
	/** 用户选择回复或是已读的界面 */
	private LinearLayout llReadResponse;
	/** 用户回复短信的界面 */
	private LinearLayout llResponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_receive);
		init();
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
	}

	@Override
	protected void onStop() {
		shakeSensor.stop();
		super.onStop();
	}

	/**
	 * 初始化系统的组件
	 */
	private void init() {
		//初始化相关的组件
		tvContacterName = (TextView)findViewById(R.id.sms_receive_contacter_name);
		ivClose = (ImageView)findViewById(R.id.sms_receive_close);
		tvSmsContent = (TextView)findViewById(R.id.sms_receive_content);
		viewSwitcher = (ViewSwitcher)findViewById(R.id.sms_receive_view_switcher);
		btnRead = (Button)findViewById(R.id.sms_receive_read);
		btnResponse = (Button)findViewById(R.id.sms_receive_response);
		etSmsResponseContent = (EditText)findViewById(R.id.sms_receive_repeat_content);
		btnSend = (Button)findViewById(R.id.sms_receive_send);
		imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		shakeSensor = new ShakeSensor(this);
		llReadResponse = (LinearLayout)findViewById(R.id.sms_receive_read_response_view);
		llResponse = (LinearLayout)findViewById(R.id.sms_receive_response_view);
		
		//初始化数据
		initDatas();
		//改变标题栏软件的状态
		for(Sms sms : smsList){
			UIUtils.setNotificationType(this, nm, sms);
		}
		
		//设置组件的数据
		sms = smsList.get(0);
		tvContacterName.setText(sms.getSender().displayName());
		tvSmsContent.setText(sms.getContent());
		//绑定用户点击已读按钮的事件
		btnRead.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PhoneUtil.readSmsBySender(SmsReceiveView.this, new String[]{sms.getMobilePhone()});
				nm.cancel(UIUtils.NOTIFICATION_ID);
				finish();
			}
		});
		//绑定回复按钮的事件
		btnResponse.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				viewSwitcher.showNext();
			}
		});
		//绑定关闭按钮的事件
		ivClose.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		//绑定发送短信的按钮的事件
		btnSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sendSms();
			}
		});
		//绑定摇一摇的事件
		shakeSensor.setOnPhoneShakeListener(new OnPhoneShakeListener() {
			public void doAfterShake() {
				if(viewSwitcher.getCurrentView() == llResponse){
					sendSms();
				} else if(viewSwitcher.getCurrentView() == llReadResponse) {
					viewSwitcher.showNext();
					etSmsResponseContent.requestFocus();
					etSmsResponseContent.requestFocusFromTouch();
					imm.showSoftInput(llReadResponse, InputMethodManager.SHOW_FORCED);
				}
			}
		});
	}
	
	/**
	 * 初始化界面上的数据
	 */
	private void initDatas() {
		//初始化短信的列表
		Bundle bundle = getIntent().getExtras();
		String smslistString = bundle.getString("smsListString");
		smsList = Sms.parseFromListString(smslistString);
	}
	
	/**
	 * 发送短信
	 * @param appendWords	需要在正文后添加的文字，比如说个性签名
	 */
	private void sendSms(){
		//隐藏输入框
		UIUtils.hideKeyBoard(SmsReceiveView.this, imm);
		String appendWords = PhoneUtil.buildSmsAppendWords(this);
		String smsContent = etSmsResponseContent.getText().toString();
		//处理用户的短信接收人的号码
		try {
			PhoneUtil.send(this, sms.getMobilePhone(), smsContent, appendWords);
			PhoneUtil.readSmsBySender(SmsReceiveView.this, new String[]{sms.getMobilePhone()});
			nm.cancel(UIUtils.NOTIFICATION_ID);
			UIUtils.toast(this, R.string.tip_sms_send_success);
			finish();
		} catch (AndroidException e) {
			UIUtils.toast(this, e.getMessageInt());
		}
	}
	
}

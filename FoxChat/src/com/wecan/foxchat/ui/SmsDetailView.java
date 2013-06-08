package com.wecan.foxchat.ui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.NotificationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.wecan.foxchat.AppManager;
import com.wecan.foxchat.R;
import com.wecan.foxchat.adapter.SmsDetailListAdapter;
import com.wecan.foxchat.entity.Sms;
import com.wecan.foxchat.exception.AndroidException;
import com.wecan.foxchat.observer.SmsObserver;
import com.wecan.foxchat.observer.listener.OnSmsListChangedListener;
import com.wecan.foxchat.sensor.GapSensor;
import com.wecan.foxchat.sensor.ShakeSensor;
import com.wecan.foxchat.sensor.listener.OnPhoneShakeListener;
import com.wecan.foxchat.sensor.listener.OnPhoneToFaceListener;
import com.wecan.foxchat.utils.PhoneUtil;
import com.wecan.foxchat.utils.UIUtils;
import com.wecan.veda.utils.StringUtil;

/**
 * 用户对话的短信详情列表
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-8
 */
public class SmsDetailView extends Activity {
	
	/** 资源加载中 */
	private static final int LOADING = 0;
	/** 短信列表加载完毕 */
	private static final int SMS_LIST_LOADED = 1;
	
	/** 展示短信内容的列表 */
	private ListView lvSmsContent;
	/** 需要展示的短信的列表 */
	private List<Sms> smsList;
	/** 绑定短信信息的适配器 */
	private SmsDetailListAdapter smsDetailListAdapter;
	/** 发送的按钮 */
	private Button btnSmsSend;
	/** 用户输入的信息的正文 */
	private EditText etSmsContent;
	/** 内容切换的容器 */
	private ViewSwitcher viewSwitcher;
	/** 加载中的动画 */
	private Animation loadingAnimation;
	/** 加载中的图片 */
	private ImageView ivLoading;
	/** 头部的标题栏 */
	private TextView tvTitle;
	/** 头部的返回按钮 */
	private ImageView ivBack;
	/** 当前联系人（们）的手机号码 */
	private String currentPhoneNumber;
	/** 当前联系人（们）的手机号码数组 */
	private String[] currentPhoneNumbers;
	/** 控制系统输入法的对象 */
	private InputMethodManager imm;
	/** 手机摇一摇的传感器 */
	private ShakeSensor shakeSensor;
	/** 距离传感器 */
	private GapSensor gapSensor;
	/** 监听是否收到或者删除短信的监听器 */
	private SmsObserver smsObserver;
	/** 输入框原来的高度 */
	private int originalHeight = -1;
	/** 输入框在输入状态下的高度 */
	private final int newHeight = 150;
	/** 标题栏的图标管理工具 */
	private NotificationManager nm;
	
	private MyHandler handler = new MyHandler(this);
	
	static class MyHandler extends Handler{
		WeakReference<SmsDetailView> reference;
		
		public MyHandler(SmsDetailView activity){
			this.reference = new WeakReference<SmsDetailView>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			SmsDetailView activity = reference.get();
			switch(msg.what){
			case LOADING:
				activity.ivLoading.startAnimation(activity.loadingAnimation);
				activity.viewSwitcher.showPrevious();
				activity.loadSmsDetailData();
				break;
			case SMS_LIST_LOADED:
				activity.ivLoading.clearAnimation();
				activity.viewSwitcher.showNext();
				activity.showSmsDetailList();
				break;
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_detail);
		AppManager.getAppManager().addActivity(this);
		init();
	}

	/**
	 * 初始化系统
	 */
	private void init(){
		//初始化个组件
		lvSmsContent = (ListView)findViewById(R.id.sms_detail_listview);
		btnSmsSend = (Button)findViewById(R.id.sms_detail_send);
		etSmsContent = (EditText)findViewById(R.id.sms_detail_msg_content);
		ivLoading = (ImageView)findViewById(R.id.sms_detail_loading);
		loadingAnimation = AnimationUtils.loadAnimation(this, R.anim.loading);
		viewSwitcher = (ViewSwitcher)findViewById(R.id.sms_detail_view_switcher);
		tvTitle = (TextView)findViewById(R.id.sms_header_title);
		ivBack = (ImageView)findViewById(R.id.sms_header_icon);
		imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		shakeSensor = new ShakeSensor(this);
		gapSensor = new GapSensor(this);
		smsObserver = new SmsObserver(handler);
		nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		//改变头部的标题
		Bundle bundle = getIntent().getExtras();
		String contacterName = (String)bundle.getString("contacterName");
		String titleTemp = getString(R.string.sms_detail_title);
		String title = String.format(titleTemp, contacterName);
		tvTitle.setText(title);
		tvTitle.requestFocus();
		//改变头部的按钮
		ivBack.setImageResource(R.drawable.sms_back_btn);
		//设置返回按钮的点击事件
		ivBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(etSmsContent.getHeight() == newHeight){
					reduceSmsContentBox();
				} else {
					finish();
				}
			}
		});
		//设置短信光标的位置
		etSmsContent.requestFocus();
		etSmsContent.setSelection(0);
		//清除提示栏的信息
		nm.cancel(UIUtils.NOTIFICATION_ID);
		//绑定用户点击信息正文输入框的事件
		etSmsContent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				appendSmsContentBox();
			}
		});
		//设置发短信按钮的事件
		btnSmsSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sendSms(PhoneUtil.buildSmsAppendWords(v.getContext()));
			}
		});
		//绑定用户摇动手机就发送短信的事件
		shakeSensor.setOnPhoneShakeListener(new OnPhoneShakeListener() {
			public void doAfterShake() {
				sendSms(PhoneUtil.buildSmsAppendWords(SmsDetailView.this));
			}
		});
		//绑定用户打电话的事件
		gapSensor.setOnPhoneToFaceListener(new OnPhoneToFaceListener() {
			public void doAfterPhoneToFace() {
				makeTelephoneCall();
			}
		});
		//绑定收到短信后的事件
		smsObserver.setOnSmsListChangedListener(new OnSmsListChangedListener() {
			public void doAfterSmsListChanged() {
				handler.sendEmptyMessage(LOADING);
			}
		});
		//注册短信监听
		getContentResolver().registerContentObserver(
				Uri.parse(PhoneUtil.SMS_URI_ALL), true, smsObserver);
		
		loadSmsDetailData();
	}
	
	/**
	 * 打电话
	 */
	private void makeTelephoneCall(){
		if(!StringUtil.isEmpty(currentPhoneNumbers) && currentPhoneNumbers.length == 1){
			PhoneUtil.makeTelephoneCall(this, currentPhoneNumbers[0]);
		} else {
			UIUtils.toast(this, "请选择您要拨打的是哪一个电话");
		}
	}
	
	/**
	 * 发送短信
	 * @param appendWords	需要在正文后添加的文字，比如说个性签名
	 */
	private void sendSms(String appendWords){
		String smsContent = etSmsContent.getText().toString();
		if(!StringUtil.isEmpty(appendWords)){
			smsContent = StringUtil.concat(new Object[]{
				smsContent, appendWords
			});
		}
		try {
			PhoneUtil.send(this, currentPhoneNumbers, smsContent, appendWords);
			//隐藏输入框
			UIUtils.hideKeyBoard(AppManager.getAppManager().currentActivity(),
					imm);
			//清空短信内容的输入框
			etSmsContent.setText("");
			//恢复输入框的原始大小
			reduceSmsContentBox();
		} catch (AndroidException e) {
			UIUtils.toast(this, e.getMessageInt());
		}
	}
	
	@Override
	protected void onPause() {
		shakeSensor.stop();
		gapSensor.stop();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		shakeSensor.register();
		gapSensor.register();
	}

	@Override
	protected void onStop() {
		shakeSensor.stop();
		gapSensor.stop();
		super.onStop();
	}

	/**
	 * 加载短信列表的
	 */
	private void loadSmsDetailData(){
		//加载短信列表
		new Thread(){
			public void run(){
				if(StringUtil.isEmpty(currentPhoneNumber)){
					//初始化短信的数据
					Bundle bundle = getIntent().getExtras();
					String phoneNumber = bundle.getString("phoneNumber");
					currentPhoneNumber = phoneNumber;
					currentPhoneNumbers = currentPhoneNumber.split(PhoneUtil.COMMA);
				}
				smsList = new ArrayList<Sms>();
				for(String phone : currentPhoneNumbers){
					List<Sms> tempList = PhoneUtil.findSmsInPhoneBy(SmsDetailView.this, phone);
					smsList.addAll(tempList);
				}
				Sms[] smsArray = new Sms[smsList.size()];
				for(int i = 0; i < smsList.size(); i++){
					smsArray[i] = smsList.get(i);
				}
				//将当前对话的联系人的短信设置为已读
				PhoneUtil.readSmsInList(SmsDetailView.this, smsArray);
				handler.sendEmptyMessage(SMS_LIST_LOADED);
			}
		}.start();
	}
	
	/**
	 * 显示短信详情的列表
	 */
	private void showSmsDetailList(){
		ivLoading.clearAnimation();
		if(!StringUtil.isEmpty(smsList)){
			smsDetailListAdapter = new SmsDetailListAdapter(this, smsList,
					R.layout.sms_detail_from, R.layout.sms_detail_to);
			lvSmsContent.setAdapter(smsDetailListAdapter);
			//设置列表默认滚到底部
			lvSmsContent.setSelection(smsList.size()-1);
			//绑定短信的点击事件
			lvSmsContent.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					//恢复输入框的原始大小
					reduceSmsContentBox();
					Sms sms = smsList.get(position);
					UIUtils.showSmsMenu(v.getContext(), sms.getId(), smsList.size());
				}
			});
		}
	}
	
	/**
	 * 拓展输入框的高度
	 */
	private void appendSmsContentBox(){
		if(originalHeight < 0) originalHeight = etSmsContent.getHeight();
		etSmsContent.setHeight(newHeight);
		etSmsContent.setGravity(Gravity.TOP);
		btnSmsSend.setHeight(newHeight);
	}
	
	/**
	 * 还原输入框的高度
	 */
	private void reduceSmsContentBox(){
		etSmsContent.setHeight(originalHeight);
		etSmsContent.setGravity(Gravity.CENTER_VERTICAL);
		btnSmsSend.setHeight(originalHeight);
		UIUtils.hideKeyBoard(this, imm);
	}

}

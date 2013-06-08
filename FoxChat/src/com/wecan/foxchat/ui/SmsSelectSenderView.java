package com.wecan.foxchat.ui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.wecan.foxchat.AppManager;
import com.wecan.foxchat.R;
import com.wecan.foxchat.adapter.CallLogListAdapter;
import com.wecan.foxchat.adapter.ContacterPromptingListAdapter;
import com.wecan.foxchat.adapter.SmsWriteContacterListAdapter;
import com.wecan.foxchat.adapter.SmsWriteContacterListAdapter.ContacterHolder;
import com.wecan.foxchat.entity.Call;
import com.wecan.foxchat.entity.Contacter;
import com.wecan.foxchat.sensor.ShakeSensor;
import com.wecan.foxchat.sensor.listener.OnPhoneShakeListener;
import com.wecan.foxchat.utils.PhoneUtil;
import com.wecan.foxchat.utils.UIUtils;
import com.wecan.veda.utils.StringUtil;

/**
 * 选择信息的收件人的界面
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-11
 */
public class SmsSelectSenderView extends Activity {
	
	/** 数据加载中 */
	private static final int LOADING = 0;
	/** 用户的通讯录信息加载完成 */
	private static final int CONTACTERS_LOADED = 1;
	/** 用户的通信记录加载完成 */
	private static final int CALL_LOGS_LOADED = 2;
	
	/** 头部的标题 */
	private TextView tvTitle;
	/** 头部的返回按钮 */
	private ImageView ivBack;
	/** 搜索框 */
	private MultiAutoCompleteTextView mactSearchBox;
	/** 通话记录的按钮 */
	private RadioButton rbCallHistory;
	/** 联系人的按钮 */
	private RadioButton rbContacter;
	/** 群发的按钮 */
	private RadioButton rbGroupSend;
	/** 用户点击前选中的按钮 */
	private RadioButton rbCurrent;
	/** 用于显示数据的列表 */
	private ListView listView;
	/** 加载中图片 */
	private ImageView ivLoading;
	/** 加载中的动画 */
	private Animation loadingAnimation;
	/** 界面切换对象 */
	private ViewSwitcher viewSwitcher;
	/** 用于确定显示哪一个界面的索引 */
	private int viewIndex = 0;
	/** 用户选中的联系人 */
	private LinkedHashMap<String, String> contacterMap = new LinkedHashMap<String, String>();
	/** 当前用户手机中联系人的数量 */
	private String contacterCount;
	/** 用于记录用户选择的联系人在列表中的位置 */
	private LinkedList<Integer> positionList = new LinkedList<Integer>();
	/** 手机摇动的传感器 */
	private ShakeSensor shakeSensor;
	/** 自动提示的适配器 */
	private ContacterPromptingListAdapter contacterPromptingListAdapter;
	
	/** 用户手机中的联系人信息 */
	private List<Contacter> contacters;
	/** 装配联系人列表的适配器 */
	private SmsWriteContacterListAdapter smsWriteContacterListAdapter;
	
	/** 用户的通话记录列表 */
	private List<Call> calls;
	/** 装配通讯记录的数据适配器 */
	private CallLogListAdapter callLogListAdapter;
	
	private MyHandler handler = new MyHandler(this);

	static class MyHandler extends Handler {
		WeakReference<SmsSelectSenderView> reference;

		public MyHandler(SmsSelectSenderView activity) {
			this.reference = new WeakReference<SmsSelectSenderView>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			SmsSelectSenderView activity = reference.get();
			switch (msg.what) {
			case LOADING:
				activity.viewIndex = 0;
				activity.showView();
				//禁用所有卡片按钮
				activity.rbContacter.setEnabled(false);
				activity.rbCallHistory.setChecked(false);
				break;
			case CONTACTERS_LOADED:
				activity.listView.setAdapter(activity.smsWriteContacterListAdapter);
				activity.initPromptingData();
				activity.changeCard(activity.rbContacter);
				activity.rbCallHistory.setEnabled(true);
				//设置搜索框的默认提示
				activity.contacterCount = activity.contacters.size() + "";
				String hint = String.format(
						activity.getString(R.string.sms_write_select_sender_search_hint),
						activity.contacterCount);
				activity.mactSearchBox.setHint(hint);
				activity.showView();
				break;
			case CALL_LOGS_LOADED:
				activity.listView.setAdapter(activity.callLogListAdapter);
				activity.changeCard(activity.rbCallHistory);
				activity.rbContacter.setEnabled(true);
				activity.showView();
				break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_write_select_sender);
		AppManager.getAppManager().addActivity(this);
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		shakeSensor.register();
		//如果是转发的消息，则显示消息正文
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			for(String key : bundle.keySet()){
				contacterMap.put(key, bundle.getString(key));
			}
		}
		showPhoneNumbers();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	/**
	 * 初始化系统
	 */
	private void init() {
		//初始化相关的组件
		tvTitle = (TextView)findViewById(R.id.sms_header_title);
		ivBack = (ImageView)findViewById(R.id.sms_header_icon);
		mactSearchBox = (MultiAutoCompleteTextView)findViewById(R.id.sms_write_select_sender_search_box);
		rbCallHistory = (RadioButton)findViewById(R.id.sms_write_select_sender_record_history);
		rbContacter = (RadioButton)findViewById(R.id.sms_write_select_sender_contacter);
		rbGroupSend = (RadioButton)findViewById(R.id.sms_write_select_sender_group);
		listView = (ListView)findViewById(R.id.sms_write_select_sender_list_view);
		ivLoading = (ImageView)findViewById(R.id.loading);
		loadingAnimation = AnimationUtils.loadAnimation(this, R.anim.loading);
		viewSwitcher = (ViewSwitcher)findViewById(R.id.sms_write_select_sender_view_switcher);
		ivLoading.startAnimation(loadingAnimation);
		shakeSensor = new ShakeSensor(this);
		
		//设置当前界面的标题
		tvTitle.setText(getString(R.string.sms_write_select_sender_title));
		//设置返回按钮的图片
		ivBack.setImageResource(R.drawable.sms_back_btn);
		//设置搜索框获得焦点
		mactSearchBox.requestFocus();
		//设置当前选中的卡片
		rbContacter.setChecked(true);
		rbCurrent = rbContacter;
		
		//绑定返回按钮的事件
		ivBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UIUtils.smsSendersSelected(v.getContext(), contacterMap);
			}
		});
		//绑定用户摇动手机确定发送人的事件
		shakeSensor.setOnPhoneShakeListener(new OnPhoneShakeListener() {
			public void doAfterShake() {
				UIUtils.smsSendersSelected(SmsSelectSenderView.this, contacterMap);
			}
		});
		//绑定点击联系人卡片的事件
		rbContacter.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				changeCard(rbContacter);
				handler.sendEmptyMessage(CONTACTERS_LOADED);
			}
		});
		//绑定点击通话记录卡片的事件
		rbCallHistory.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				changeCard(rbCallHistory);
				handler.sendEmptyMessage(CALL_LOGS_LOADED);
			}
		});
		//绑定点击群发卡片的事件
		rbGroupSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				changeCard(rbGroupSend);
			}
		});
		//绑定列表的点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				if(rbContacter.isChecked()){
					Contacter c = contacters.get(position);
					SparseBooleanArray selectMap = SmsWriteContacterListAdapter.selectMap;
					ContacterHolder holder = (ContacterHolder)v.getTag();
					CheckBox cb = holder.cbIsSelect;
					if(selectMap.get(position)){
						cb.setChecked(false);
						selectMap.put(position, false);
						contacterMap.remove(c.getMobilePhone());
						positionList.removeLast();
					} else {
						cb.setChecked(true);
						selectMap.put(position, true);
						contacterMap.put(c.getMobilePhone(), c.displayName());
						positionList.addLast(position);
					}
				}
				if(rbCallHistory.isChecked()){
					Call c = calls.get(position);
					contacterMap.put(c.getPhone(), c.displayName());
				}
				if(rbGroupSend.isChecked()){
					
				}
				showPhoneNumbers();
				//设置搜索框的光标的位置
				mactSearchBox.requestFocus();
				mactSearchBox.setSelection(mactSearchBox.getText().length());
			}
		});
		//绑定搜索框的内容监听事件
		mactSearchBox.setOnKeyListener(new OnKeyListener() {
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
							//改变联系人的选中状态
							if(rbContacter.isChecked()){
								if(!StringUtil.isEmpty(positionList)){
									int index = positionList.getLast();
									SmsWriteContacterListAdapter.selectMap.put(index, false);
									positionList.removeLast();
									smsWriteContacterListAdapter.notifyDataSetChanged();
								}
							}
						}
						break;
					}
				}
				return false;
			}
		});
		//绑定自动提示输入框的提示项的点击事件
		mactSearchBox.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				ContacterHolder contacterHolder = (ContacterHolder)v.getTag();
				String phoneNumber = contacterHolder.tvPhone.getText().toString();
				String contacterName = contacterHolder.tvContacterName.getText().toString();
				int sizeBefore = contacterMap.size();
				contacterMap.put(phoneNumber, contacterName);
				int sizeAfter = contacterMap.size();
				if(sizeBefore != sizeAfter){
					Integer originalPosition = (Integer)contacterPromptingListAdapter.getItem(position);
					positionList.add(originalPosition);
					//将对应的选项选中
					SmsWriteContacterListAdapter.selectMap.put(originalPosition, true);
					smsWriteContacterListAdapter.notifyDataSetChanged();
				}
				showPhoneNumbers();
			}
		});
		
		loadData();
	}
	
	/**
	 * 块状显示用户选中的联系人信息
	 */
	private void showPhoneNumbers(){
		mactSearchBox.getText().clear();
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
			mactSearchBox.getText().insert(mactSearchBox.getSelectionStart(), ss);
		}
	}
	
	/**
	 * 加载数据
	 */
	private void loadData(){
		new Thread(){
			public void run(){
				loadCallLogData();
				loadContactersData();
				handler.sendEmptyMessage(CONTACTERS_LOADED);
			}
		}.start();
	}
	
	/**
	 * 加载并显示用户的联系人信息
	 */
	private void loadContactersData(){
		if(StringUtil.isEmpty(SmsView.contactersInPhone)) {
			SmsView.contactersInPhone = (ArrayList<Contacter>) PhoneUtil.findAllContacters(this);
		}
		contacters = SmsView.contactersInPhone;
		smsWriteContacterListAdapter = new SmsWriteContacterListAdapter(this,
				R.layout.select_contactor_item, contacters);
	}
	
	/**
	 * 加载并显示用户的通话记录
	 */
	private void loadCallLogData(){
		if(StringUtil.isEmpty(calls)) calls = PhoneUtil.findAllCallLogs(this);
		callLogListAdapter = new CallLogListAdapter(this, R.layout.call_record_item, calls);
	}
	
	/**
	 * 改变当前的卡片按钮的样式
	 * @param button
	 */
	private void changeCard(RadioButton button){
		rbCurrent.setEnabled(true);
		rbCurrent = button;
		button.setEnabled(false);
	}
	
	/**
	 * 显示指定的界面
	 */
	private void showView(){
		if(viewIndex < 0){
			viewSwitcher.showPrevious();
		} else if(viewIndex == 0) {
			viewSwitcher.showNext();
		}
		viewIndex++;
	}

	@Override
	protected void onPause() {
		shakeSensor.stop();
		super.onPause();
	}

	@Override
	protected void onStop() {
		shakeSensor.stop();
		super.onStop();
	}
	
	/**
	 * 初始化自动提示的数据
	 */
	private void initPromptingData(){
		contacterPromptingListAdapter = new ContacterPromptingListAdapter(
				this, R.layout.select_contactor_item, SmsView.contactersInPhone);
		mactSearchBox.setAdapter(contacterPromptingListAdapter);
		mactSearchBox.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		mactSearchBox.setThreshold(1);
	}

}

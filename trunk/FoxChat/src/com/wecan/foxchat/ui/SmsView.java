package com.wecan.foxchat.ui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ViewSwitcher;

import com.wecan.foxchat.AppManager;
import com.wecan.foxchat.R;
import com.wecan.foxchat.adapter.SmsContacterListAdapter;
import com.wecan.foxchat.entity.Contacter;
import com.wecan.foxchat.entity.Sms;
import com.wecan.foxchat.exception.NotFoundPhoneNumberException;
import com.wecan.foxchat.observer.SmsObserver;
import com.wecan.foxchat.observer.listener.OnSmsListChangedListener;
import com.wecan.foxchat.utils.PhoneUtil;
import com.wecan.foxchat.utils.UIUtils;

/**
 * 用户短信的界面
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-7
 */
public class SmsView extends Activity {
	
	/** 用户的手机中的联系人信息 */
	public static ArrayList<Contacter> contactersInPhone;
	
	/** 正在加载数据 */
	private static final int LOADING = 0;
	/** 短信加载完毕 */
	private static final int SMS_LOADED = 1;
	
	/** 显示联系人的短信的列表 */
	private ListView smsContacterListView;
	/** 根据联系人分好组的短信的数据 */
	private List<Contacter> smsContacters;
	/** 联系人信息的数据适配器 */
	private SmsContacterListAdapter smsContacterListAdapter;
	/** 界面切换的容器 */
	private ViewSwitcher viewSwitcher;
	/** 旋转的动画 */
	private Animation loadingAnimation;
	/** 加载中的图片容器 */
	private ImageView ivLoading;
	/** 按照联系人显示短信列表的按钮 */
	private RadioButton rbToSmsListView;
	/** 跳转到写短信界面的按钮 */
	private RadioButton rbToSmsWriteView;
	/** 主菜单的图标 */
	private ImageView ivMainMenu;
	
	/** 系统中是否有短信 */
	private boolean isSmsExist;
	/** 监听短信信箱变化的观察者 */
	private SmsObserver smsObserver;
	
	private MyHandler handler = new MyHandler(this);
	
	static class MyHandler extends Handler{
		WeakReference<SmsView> reference;
		
		public MyHandler(SmsView activity){
			this.reference = new WeakReference<SmsView>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			SmsView activity = reference.get();
			switch (msg.what) {
			case LOADING:
				activity.viewSwitcher.showPrevious();
				activity.initSmsContacterList();
				break;
			case SMS_LOADED:
				activity.viewSwitcher.showNext();
				activity.showSmsContacterList();
				break;
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_contactor_list);
		AppManager.getAppManager().addActivity(this);
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//使列表按钮获得焦点
		rbToSmsListView.requestFocus();
		rbToSmsListView.setChecked(true);
	}
	
	/**
	 * 初始化用户的短信列表
	 */
	private void init() {
		//初始化加载中的动画
		loadingAnimation = AnimationUtils.loadAnimation(this, R.anim.loading);
		ivLoading = (ImageView)findViewById(R.id.sms_contacter_loading);
		ivLoading.startAnimation(loadingAnimation);
		viewSwitcher = (ViewSwitcher)findViewById(R.id.sms_contactor_viewswitcher);
		smsObserver = new SmsObserver(handler);
		rbToSmsListView = (RadioButton)findViewById(R.id.sms_footer_sms_list);
		rbToSmsWriteView = (RadioButton)findViewById(R.id.sms_footer_write_sms);
		ivMainMenu = (ImageView)findViewById(R.id.sms_header_icon);
		
		//在新的线程中初始化短信列表
		initSmsContacterList();
		//绑定点击主菜单图标显示主菜单的事件
		ivMainMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UIUtils.showMainMenu(v.getContext());
			}
		});
		//绑定短信变化的事件
		smsObserver.setOnSmsListChangedListener(new OnSmsListChangedListener() {
			public void doAfterSmsListChanged() {
				handler.sendEmptyMessage(LOADING);
			}
		});
		//绑定刷新短信列表数据的事件
		rbToSmsListView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				handler.sendEmptyMessage(LOADING);
			}
		});
		//绑定点击跳转到写短信界面的事件
		rbToSmsWriteView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UIUtils.toWriteSmsView(v.getContext());
			}
		});
		//注册短信变化的监听器
		getContentResolver().registerContentObserver(
				Uri.parse(PhoneUtil.SMS_URI_ALL), true, smsObserver);
	}

	/**
	 * 初始化联系人的短信列表的数据
	 */
	private void initSmsContacterList() {
		new Thread(){
			public void run(){
				//初始化短信列表
				isSmsExist = initSmsDatas();
				handler.sendEmptyMessage(SMS_LOADED);
			}
		}.start();
	}
	
	/**
	 * 显示联系人的短信列表
	 */
	private void showSmsContacterList(){
		//初始化显示的界面
		if(isSmsExist){
			if(smsContacterListAdapter == null){
				smsContacterListView = (ListView)findViewById(R.id.sms_contactor_listview);
				smsContacterListAdapter = new SmsContacterListAdapter(this, smsContacters, R.layout.sms_contactor_item);
				smsContacterListView.setAdapter(smsContacterListAdapter);
				
				//设置信息列表的点击事件
				smsContacterListView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
						Contacter contacter = smsContacters.get(position);
						if(contacter == null) return;
						//设置该列表中的短信为已读状态
						List<Sms> smsList = contacter.getSmsList();
						Sms[] smsArray = new Sms[smsList.size()];
						for(int i = 0; i < smsList.size(); i++){
							smsArray[i] = smsList.get(i);
						}
						UIUtils.toSmsDetailActivity(view.getContext(),
								contacter.getMobilePhone(), contacter.displayName());
					}
				});
			} else {
				smsContacterListAdapter = new SmsContacterListAdapter(this, smsContacters, R.layout.sms_contactor_item);
				smsContacterListView.setAdapter(smsContacterListAdapter);
			}
		}
	}
	
	/**
	 * 初始化短信的数据
	 */
	private boolean initSmsDatas() {
		try {
			smsContacters = PhoneUtil.findContactersWithSmsInGroup(this);
		} catch (NotFoundPhoneNumberException e) {
			e.printStackTrace();
		}
		/*List<Sms> list = PhoneUtil.getSmsInPhone(this);//获得手机中的所有短信
		if(StringUtil.isEmpty(list)) return false;
		//将这些短信按照联系人进行分组
		for(Sms sms : list){
			if(StringUtil.isEmpty(sms.getMobilePhone())) continue;
			Contacter c = findContacterByPhone(sms.getMobilePhone());
			if(c == null){//不存在
				try {
					c = PhoneUtil.findContacterByPhone(this, sms.getMobilePhone());
				} catch (NotFoundPhoneNumberException e) {
					e.printStackTrace();
				}
				if(c == null){
					c = new Contacter();
					c.setMobilePhone(sms.getMobilePhone());
				}
				smsContacters.add(c);
			}
			c.addSms(sms);
		}*/
		//处理联系人的
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			UIUtils.showMainMenu(this);
			break;
		}
		return true;
	}

}

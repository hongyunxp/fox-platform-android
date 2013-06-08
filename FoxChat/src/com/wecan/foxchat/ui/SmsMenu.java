package com.wecan.foxchat.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.wecan.foxchat.AppManager;
import com.wecan.foxchat.R;
import com.wecan.foxchat.entity.Sms;
import com.wecan.foxchat.exception.NotFoundSmsException;
import com.wecan.foxchat.utils.PhoneUtil;
import com.wecan.foxchat.utils.UIUtils;
import com.wecan.veda.utils.StringUtil;

/**
 * 短信菜单选项的界面
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-9
 */
public class SmsMenu extends Activity {
	
	/** 删除短信的按钮 */
	private Button btnSmsDelete;
	/** 转发信息的按钮 */
	private Button btnSmsTransmit;
	/** 复制内容的按钮 */
	private Button btnSmsCopy;
	/** 显示信息详情的按钮 */
	private Button btnSmsShowDetail;
	/** 剪切板管理器 */
	private ClipboardManager clipboard;
	
	/** 短信的id号，由短信列表界面传过来 */
	private int smsId;
	/** 当前短信列表中短信的数量 */
	private int currentSmsCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_menu);
		AppManager.getAppManager().addActivity(this);
		init();
	}

	/**
	 * 初始化菜单界面
	 */
	private void init() {
		//初始化相关的组件
		btnSmsCopy = (Button)findViewById(R.id.sms_menu_copy);
		btnSmsDelete = (Button)findViewById(R.id.sms_menu_delete);
		btnSmsShowDetail = (Button)findViewById(R.id.sms_menu_detail);
		btnSmsTransmit = (Button)findViewById(R.id.sms_menu_transmit);
		clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		//获取短信的id号
		Bundle bundle = getIntent().getExtras();
		smsId = bundle.getInt("smsId");
		currentSmsCount = bundle.getInt("currentSmsCount");
		
		//绑定复制短信内容的事件
		btnSmsCopy.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Sms sms = PhoneUtil.findSmsById(v.getContext(), smsId);
				clipboard.setText(sms.getContent());
				UIUtils.toast(v.getContext(), R.string.tip_sms_content_copy_success);
				AppManager.getAppManager().finishActivity();
			}
		});
		//绑定删除短信的事件
		btnSmsDelete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					PhoneUtil.deleteSmsIn(v.getContext(), smsId+"");
				} catch (NotFoundSmsException e) {
					UIUtils.toast(v.getContext(), e.getMessage());
				}
				UIUtils.toast(v.getContext(), R.string.tip_sms_delete_success);
				if(currentSmsCount == 1){
					UIUtils.toSmsContacterView(v.getContext());
				} else {
					AppManager.getAppManager().finishActivity();
				}
			}
		});
		//绑定显示短信详情的事件
		btnSmsShowDetail.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UIUtils.toast(v.getContext(), "正在获取短信详情");
				AppManager.getAppManager().finishActivity();
			}
		});
		//绑定转发短信的事件
		btnSmsTransmit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Sms sms = PhoneUtil.findSmsById(v.getContext(), smsId);
				String smsContent = StringUtil.concat(new Object[]{
						getString(R.string.sms_detail_transmit_tip), sms.getContent() 
				});
				UIUtils.toWriteSmsView(v.getContext(), smsContent);
				AppManager.getAppManager().finishActivity();
			}
		});
	}
	
}

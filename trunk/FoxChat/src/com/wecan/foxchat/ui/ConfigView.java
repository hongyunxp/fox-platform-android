package com.wecan.foxchat.ui;

import com.wecan.foxchat.R;
import com.wecan.foxchat.utils.Config;
import com.wecan.foxchat.utils.PhoneUtil;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

/**
 * 配置系统参数的界面
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-14
 */
public class ConfigView extends PreferenceActivity implements
		OnPreferenceChangeListener {
	
	/** 是否启用用户的个性签名 */
	private Preference pSignState;
	/** 用户的个性签名 */
	private Preference pMySign;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(Config.CONFIG_FILE);
		addPreferencesFromResource(R.layout.main_setting);
		init();
	}

	/**
	 * 初始化设置界面
	 */
	private void init() {
		//初始化设置的组件
		pMySign = findPreference(Config.MY_SIGN);
		pSignState = findPreference(Config.SIGN_STATE);
		Boolean signState = Config.getConfig(this).getBoolean(Config.SIGN_STATE);
		String mySign = PhoneUtil.buildSmsAppendWords(this);
		pMySign.setSummary(mySign);
		if(!signState){
			pMySign.setEnabled(false);
		} else {
			pMySign.setEnabled(true);
		}
		
		//绑定监听器
		pSignState.setOnPreferenceChangeListener(this);
		pMySign.setOnPreferenceChangeListener(this);
	}

	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if(preference == pMySign){
			pMySign.setSummary(String.valueOf(newValue));
		}
		return true;
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if(preference == pSignState){
			pMySign.setEnabled(!pMySign.isEnabled());
			if(pMySign.isEnabled()){
				String mySign = PhoneUtil.buildSmsAppendWords(this);
				pMySign.setSummary(mySign);
			}
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

}

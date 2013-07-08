package com.foxchan.foxdiary.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foxchan.foxdiary.core.AppConfig;
import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.core.widgets.FakeActivity;
import com.foxchan.foxdiary.exception.DiaryWordsException;
import com.foxchan.foxdiary.utils.Constants;
import com.foxchan.foxutils.data.StringUtils;

/**
 * 文字日记界面
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-4-30
 */
public class DiaryWriteWordsView extends FakeActivity {
	
	private DiaryWriteView diaryWriteView;
	private AppConfig config;
	private View layoutView;
	/** 正文输入框 */
	private EditText etContent;
	/** 清除正文部份文字的按钮 */
	private LinearLayout llClearContent;
	/** 剩余字数的文本框 */
	private TextView tvLeftWords;
	/** 用户输入的正文内容 */
	private String content;

	public DiaryWriteWordsView(DiaryWriteView diaryWriteView) {
		this.diaryWriteView = diaryWriteView;
		this.layoutView = diaryWriteView.getLayoutInflater().inflate(R.layout.diary_write_text, null);
	}

	@Override
	public View layoutView() {
		return this.layoutView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//初始化相关组件
		etContent = (EditText)layoutView.findViewById(R.id.diary_write_content_box);
		llClearContent = (LinearLayout)layoutView.findViewById(R.id.diary_write_text_clear);
		tvLeftWords = (TextView)layoutView.findViewById(R.id.diary_write_content_left_words);
		//初始化用户输入的字符和剩余字符数量
		etContent.setText("");
		content = etContent.getText().toString();
		int leftWordsNumber = Constants.DIARY_WORDS_MAX - content.length();
		tvLeftWords.setText(String.format(diaryWriteView.getResources()
				.getString(R.string.diary_write_left_words), leftWordsNumber));
		//绑定输入框的输入事件
		etContent.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				content = s.toString();
				int leftWordsCount = Constants.DIARY_WORDS_MAX - s.length();
				tvLeftWords.setText(String.format(diaryWriteView.getResources()
						.getString(R.string.diary_write_left_words), leftWordsCount));
				config.setProperty(Constants.DIARY_TEMP_WORDS, content);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		//设置输入框的最大输入字数
		InputFilter[] filters = new InputFilter[1];
		filters[0] = new InputFilter.LengthFilter(Constants.DIARY_WORDS_MAX);
		etContent.setFilters(filters);
		//绑定清除正文内容的按钮的事件
		llClearContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				etContent.setText("");
			}
		});
		config = AppConfig.getInstance(diaryWriteView);
	}

	@Override
	public void onResume() {
		String wordsContent = config.getProperty(Constants.DIARY_TEMP_WORDS);
		if(!StringUtils.isEmpty(wordsContent)){
			etContent.setText(wordsContent);
			etContent.setSelection(wordsContent.length());
			config.removeProperties(Constants.DIARY_TEMP_WORDS);
		}
		super.onResume();
	}

	@Override
	public void onDestroy() {
		config.removeProperties(Constants.DIARY_TEMP_WORDS);
		super.onDestroy();
	}

	public String getContent() {
		return content;
	}

	public EditText getEtContent() {
		return etContent;
	}
	
	/**
	 * 判断日记的文字部分是否存在
	 * @return	如果日记的文字部分的数据正确无误则返回true，否则返回false
	 */
	public boolean isWordsExist() {
		if(StringUtils.isEmpty(content)){
			return false;
		}
		return true;
	}
	
}
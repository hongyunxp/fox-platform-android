package com.foxchan.foxdiary.core.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foxchan.foxdiary.core.R;
import com.foxchan.foxutils.data.StringUtils;
import com.foxchan.foxutils.tool.PhoneUtils;

/**
 * 给用户确认信息的弹出框
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-6-14
 */
public class FoxConfirmDialog extends Dialog {
	
	/** 确认对话框最外面的容器 */
	private LinearLayout llDialog;
	/** 确认框的标题 */
	private TextView tvTitle;
	/** 确认框的正文 */
	private TextView tvContent;
	/** 确认按钮 */
	private Button btnPositive;
	/** 取消按钮 */
	private Button btnNegative;

	public FoxConfirmDialog(Context context) {
		super(context, R.style.FoxDialog);
		setCanceledOnTouchOutside(true);
		setContentView(R.layout.widget_confirm_dialog);
		tvTitle = (TextView)findViewById(R.id.widget_confirm_dialog_title);
		tvContent = (TextView)findViewById(R.id.widget_confirm_dialog_content);
		btnNegative = (Button)findViewById(R.id.widget_confirm_dialog_negative_button);
		btnPositive = (Button)findViewById(R.id.widget_confirm_dialog_positive_button);
		llDialog = (LinearLayout)findViewById(R.id.widget_confirm_dialog_box);
		int width = PhoneUtils.getWindowWidth((Activity)context);
		llDialog.setLayoutParams(new FrameLayout.LayoutParams(width, FrameLayout.LayoutParams.WRAP_CONTENT));
	}
	
	public FoxConfirmDialog(Context context, String content){
		this(context, null, content);
	}
	
	public FoxConfirmDialog(Context context, String title, String content){
		this(context);
		//设置确认框的标题和正文
		setTitle(title);
		setContent(content);
	}

	/**
	 * 设置确定按钮点击时的监听器
	 * @param onPositiveButtonClickListener	确定按钮点击时的监听器
	 * @return								返回消息确认对话框
	 */
	public FoxConfirmDialog setOnPositiveButtonClickListener(
			final DialogInterface.OnClickListener onPositiveButtonClickListener) {
		btnPositive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hide();
				onPositiveButtonClickListener.onClick(FoxConfirmDialog.this, 0);
			}
		});
		return this;
	}

	/**
	 * 设置取消按钮点击时的监听器
	 * @param onNegativeButtonClickListener	取消按钮点击时的监听器
	 * @return								返回消息确认对话框
	 */
	public FoxConfirmDialog setOnNegativeButtonClickListener(
			final DialogInterface.OnClickListener onNegativeButtonClickListener) {
		btnNegative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hide();
				onNegativeButtonClickListener.onClick(FoxConfirmDialog.this, 0);
			}
		});
		return this;
	}

	/**
	 * 设置确认框的标题
	 * @param title	标题
	 */
	public void setTitle(String title) {
		if(!StringUtils.isEmpty(title)){
			this.tvTitle.setText(title);
		}
	}

	/**
	 * 设置确认框的内容
	 * @param content	内容
	 */
	public void setContent(String content) {
		this.tvContent.setText(content);
	}
	
	/**
	 * 设置确认框的标题和内容
	 * @param title		标题
	 * @param content	内容
	 */
	public void setTitleAndContent(String title, String content){
		setTitle(title);
		setContent(content);
	}

}

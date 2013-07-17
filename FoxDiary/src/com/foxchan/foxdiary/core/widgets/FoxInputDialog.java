package com.foxchan.foxdiary.core.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foxchan.foxdiary.core.R;
import com.foxchan.foxutils.tool.PhoneUtils;

/**
 * 获得用户输入的对话框
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013年7月16日
 */
public class FoxInputDialog extends Dialog {
	
	/** 对话框最外面的容器 */
	private LinearLayout llDialog;
	/** 确认框的标题 */
	private TextView tvTitle;
	/** 确认框的正文输入框 */
	private EditText etContent;
	/** 确认按钮 */
	private Button btnPositive;
	/** 取消按钮 */
	private Button btnNegative;

	public FoxInputDialog(Context context) {
		super(context, R.style.FoxDialog);
		setCanceledOnTouchOutside(true);
		setContentView(R.layout.widget_input_dialog);
		tvTitle = (TextView)findViewById(R.id.widget_input_dialog_title);
		etContent = (EditText)findViewById(R.id.widget_input_dialog_content);
		btnNegative = (Button)findViewById(R.id.widget_input_dialog_negative_button);
		btnPositive = (Button)findViewById(R.id.widget_input_dialog_positive_button);
		llDialog = (LinearLayout)findViewById(R.id.widget_input_dialog_box);
		int width = PhoneUtils.getWindowWidth((Activity)context);
		llDialog.setLayoutParams(new FrameLayout.LayoutParams(width, FrameLayout.LayoutParams.WRAP_CONTENT));
	}
	
	public FoxInputDialog(Context context, String title){
		this(context);
		this.tvTitle.setText(title);
	}
	
	/**
	 * 绑定点击确定按钮的事件监听器
	 * @param onPositiveButtonClickListener	确定按钮的事件监听器
	 * @return								返回当前的输入框对象
	 */
	public FoxInputDialog setOnPositiveButtonClickListener(
			final FoxInputDialog.OnClickListener onPositiveButtonClickListener){
		btnPositive.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hide();
				onPositiveButtonClickListener.onClick(etContent.getText().toString(), FoxInputDialog.this);
			}
		});
		return this;
	}
	
	/**
	 * 绑定点击取消按钮的事件监听器
	 * @param onNegativeButtonClickListener	取消按钮的事件监听器
	 * @return								返回当前的输入框对象
	 */
	public FoxInputDialog setOnNegativeButtonClickListener(
			final FoxInputDialog.OnClickListener onNegativeButtonClickListener){
		btnNegative.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hide();
				onNegativeButtonClickListener.onClick(etContent.getText().toString(), FoxInputDialog.this);
			}
		});
		return this;
	}
	
	/**
	 * 按钮的点击事件
	 * @author foxchan@live.cn
	 * @version 1.0.0
	 * @create 2013年7月17日
	 */
	public interface OnClickListener{
		
		/**
		 * 当按钮被点击时将触发该方法
		 * @param content	输入框的内容
		 * @param dialog	输入框对象
		 */
		void onClick(String content, FoxInputDialog dialog);
		
	}

}

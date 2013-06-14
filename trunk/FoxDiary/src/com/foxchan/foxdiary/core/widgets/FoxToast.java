package com.foxchan.foxdiary.core.widgets;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.foxchan.foxdiary.core.R;

/**
 * 自定义的消息提示框，使用方法与Android的Toast提示框相同
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-6-13
 */
public class FoxToast {
	
	/** 自定义的消息框的布局文件 */
	private View view;
	/** 显示正文的文本框 */
	private TextView tvContent;
	
	private static FoxToast foxToast;
	
	/**
	 * 构造一个消息框
	 * @param context
	 */
	private FoxToast(Context context){
		if(view == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.widget_toast, null);
		}
		if(tvContent == null){
			tvContent = (TextView)view.findViewById(R.id.widget_toast_content);
		}
	}
	
	private static void getInstance(Context context){
		if(foxToast == null){
			foxToast = new FoxToast(context);
		}
	}
	
	/**
	 * 显示消息
	 * @param context
	 * @param text		显示的内容
	 * @param duration	显示的时间长短，与Toast的取值相同
	 */
	public static void showToast(Context context, CharSequence text, int duration){
		getInstance(context);
		foxToast.tvContent.setText(text);
		Toast toast = new Toast(context);
		toast.setDuration(duration);
		toast.setView(foxToast.view);
		toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL, 0, 0);
		toast.show();
	}
	
	/**
	 * 显示消息
	 * @param context
	 * @param resource	显示的内容的资源ID
	 * @param duration	显示的时间长短，与Toast的取值相同
	 */
	public static void showToast(Context context, int resource, int duration){
		String text = context.getString(resource);
		showToast(context, text, duration);
	}

}

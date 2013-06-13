package com.foxchan.foxdiary.exception;

import android.content.Context;

/**
 * 该应用程序的基本异常类
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-6-13
 */
public class DiaryException extends Exception {

	private static final long serialVersionUID = -2419408547198265249L;

	public DiaryException() {
		super();
	}

	public DiaryException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public DiaryException(String detailMessage) {
		super(detailMessage);
	}
	
	public DiaryException(Context context, int resource) {
		this(context.getString(resource));
	}

	public DiaryException(Throwable throwable) {
		super(throwable);
	}
	
}

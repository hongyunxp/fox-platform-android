package com.foxchan.foxdiary.exception;

import android.content.Context;

/**
 * 与日记的文字相关的Exception
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-6-13
 */
public class DiaryWordsException extends DiaryException {

	private static final long serialVersionUID = 7183764760795207998L;

	public DiaryWordsException() {
		super();
	}

	public DiaryWordsException(Context context, int resource) {
		super(context, resource);
	}

	public DiaryWordsException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public DiaryWordsException(String detailMessage) {
		super(detailMessage);
	}

	public DiaryWordsException(Throwable throwable) {
		super(throwable);
	}

}

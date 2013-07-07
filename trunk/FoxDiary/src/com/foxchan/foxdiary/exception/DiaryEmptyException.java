package com.foxchan.foxdiary.exception;

import android.content.Context;

/**
 * 如果日记没有任何内容，将抛出该异常
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013年7月7日
 */
public class DiaryEmptyException extends DiaryException {

	private static final long serialVersionUID = 1846134654676618602L;

	public DiaryEmptyException() {
		super();
	}

	public DiaryEmptyException(Context context, int resource) {
		super(context, resource);
	}

	public DiaryEmptyException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public DiaryEmptyException(String detailMessage) {
		super(detailMessage);
	}

	public DiaryEmptyException(Throwable throwable) {
		super(throwable);
	}

}

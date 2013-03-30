package com.foxchan.foxui.exception;

/**
 * FoxUI中的Exception基础类
 * @author gulangxiangjie@gmail.com
 * @create 2013-3-30
 */
public class FoxUIException extends Exception {

	private static final long serialVersionUID = -966481389978754046L;

	public FoxUIException() {
		super();
	}

	public FoxUIException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public FoxUIException(String detailMessage) {
		super(detailMessage);
	}

	public FoxUIException(Throwable throwable) {
		super(throwable);
	}

}

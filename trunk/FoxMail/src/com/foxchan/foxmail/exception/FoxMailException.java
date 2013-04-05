package com.foxchan.foxmail.exception;

/**
 * 邮件应用中的异常的基类
 * @author gulangxiangjie@gmail.com
 * @create 2013-4-5
 */
public class FoxMailException extends Exception {

	private static final long serialVersionUID = -5481054460419793809L;

	public FoxMailException() {
		super();
	}

	public FoxMailException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public FoxMailException(String detailMessage) {
		super(detailMessage);
	}

	public FoxMailException(Throwable throwable) {
		super(throwable);
	}

}

package com.foxchan.foxmail.exception;

/**
 * 无法解析的邮件地址类型的异常
 * @author gulangxiangjie@gmail.com
 * @create 2013-4-5
 */
public class ErrorEmailAddressTypeException extends FoxMailException {

	private static final long serialVersionUID = 1394439088220991257L;

	public ErrorEmailAddressTypeException() {
		super();
	}

	public ErrorEmailAddressTypeException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ErrorEmailAddressTypeException(String detailMessage) {
		super(detailMessage);
	}

	public ErrorEmailAddressTypeException(Throwable throwable) {
		super(throwable);
	}

}

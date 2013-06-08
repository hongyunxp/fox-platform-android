package com.wecan.foxchat.exception;

/**
 * 如果被检测的手机号不符合手机号的要求则抛出该异常
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-8
 */
public class NotPhoneNumberException extends AndroidException {

	private static final long serialVersionUID = -5139103149953351717L;

	public NotPhoneNumberException() {
		super();
	}

	public NotPhoneNumberException(int content) {
		super(content);
	}

	public NotPhoneNumberException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NotPhoneNumberException(String detailMessage) {
		super(detailMessage);
	}

	public NotPhoneNumberException(Throwable throwable) {
		super(throwable);
	}

}

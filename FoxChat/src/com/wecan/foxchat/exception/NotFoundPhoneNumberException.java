package com.wecan.foxchat.exception;

/**
 * 当没有找到电话号码时将抛出该异常
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-9
 */
public class NotFoundPhoneNumberException extends AndroidException {

	private static final long serialVersionUID = 8427525291315116344L;

	public NotFoundPhoneNumberException() {
		super();
	}

	public NotFoundPhoneNumberException(int content) {
		super(content);
	}

	public NotFoundPhoneNumberException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NotFoundPhoneNumberException(String detailMessage) {
		super(detailMessage);
	}

	public NotFoundPhoneNumberException(Throwable throwable) {
		super(throwable);
	}

}

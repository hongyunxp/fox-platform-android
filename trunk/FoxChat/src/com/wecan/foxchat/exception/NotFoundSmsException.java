package com.wecan.foxchat.exception;

/**
 * 当系统没有找到必要的短信时将抛出该异常
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-9
 */
public class NotFoundSmsException extends AndroidException {

	private static final long serialVersionUID = -8093443791365897121L;

	public NotFoundSmsException() {
		super();
	}

	public NotFoundSmsException(int content) {
		super(content);
	}

	public NotFoundSmsException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NotFoundSmsException(String detailMessage) {
		super(detailMessage);
	}

	public NotFoundSmsException(Throwable throwable) {
		super(throwable);
	}

}

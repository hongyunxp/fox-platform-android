package com.wecan.foxchat.exception;

/**
 * 如果短信的内容为空将抛出该异常
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-8
 */
public class SmsEmptyException extends AndroidException {

	private static final long serialVersionUID = -1161353064996992630L;

	public SmsEmptyException() {
		super();
	}

	public SmsEmptyException(int content) {
		super(content);
	}

	public SmsEmptyException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public SmsEmptyException(String detailMessage) {
		super(detailMessage);
	}

	public SmsEmptyException(Throwable throwable) {
		super(throwable);
	}

}

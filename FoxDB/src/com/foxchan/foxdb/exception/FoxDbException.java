package com.foxchan.foxdb.exception;

/**
 * Foxer框架在运行时抛出的异常
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-12
 */
public class FoxDbException extends RuntimeException {

	private static final long serialVersionUID = -5354798234121189889L;

	public FoxDbException() {
		super();
	}

	public FoxDbException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public FoxDbException(String detailMessage) {
		super(detailMessage);
	}

	public FoxDbException(Throwable throwable) {
		super(throwable);
	}

}

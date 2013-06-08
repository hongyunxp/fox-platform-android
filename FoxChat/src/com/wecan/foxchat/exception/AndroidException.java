package com.wecan.foxchat.exception;

/**
 * 适应于移动开发的异常，可以接受资源文件中的字符串信息
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-8
 */
public class AndroidException extends Exception {

	private static final long serialVersionUID = 7240915863256227198L;
	private int content;

	public AndroidException() {
		super();
	}

	public AndroidException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public AndroidException(String detailMessage) {
		super(detailMessage);
	}
	
	public AndroidException(int content) {
		this.content = content;
	}
	
	public int getMessageInt(){
		return this.content;
	}

	public AndroidException(Throwable throwable) {
		super(throwable);
	}

}

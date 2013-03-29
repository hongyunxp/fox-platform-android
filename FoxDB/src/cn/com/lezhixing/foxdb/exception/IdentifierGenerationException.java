package cn.com.lezhixing.foxdb.exception;

/**
 * 当对象的主键创建失败时，将抛出该异常
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-12
 */
public class IdentifierGenerationException extends FoxDbException {

	private static final long serialVersionUID = 4085210019454545679L;

	public IdentifierGenerationException() {
		super();
	}

	public IdentifierGenerationException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public IdentifierGenerationException(String detailMessage) {
		super(detailMessage);
	}

	public IdentifierGenerationException(Throwable throwable) {
		super(throwable);
	}

}

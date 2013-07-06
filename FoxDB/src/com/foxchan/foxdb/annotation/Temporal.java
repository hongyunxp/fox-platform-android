package com.foxchan.foxdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *	 注解：日期注解，该注解只能用于标准的<i>java.util.Date</i>和<i>java.util.Calendar</i>
 * </p>
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Temporal {
	
	/** 时间保存的类型，默认为保存完整的日期信息 */
	TemporalType value() default TemporalType.TIMESTAMP;

}

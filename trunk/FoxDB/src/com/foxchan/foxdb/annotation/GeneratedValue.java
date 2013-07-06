package com.foxchan.foxdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主键使用的策略
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface GeneratedValue {
	
	/** 数据表的主键增长策略，有自增长和UUID两种，默认采用自增长的方式 */
	GeneratedType strategy() default GeneratedType.IDENTITY;

}

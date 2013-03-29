package cn.com.lezhixing.foxdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 	注解：数据列。该注解对应了数据库中的列的信息
 * </p>
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Column {
	
	/** 数据列的名称，如果没有设置该属性，数据列的名称将采用属性的名称 */
	String name() default "";
	/** 该列是否可以为空 */
	boolean nullable() default true;
	/** 该列的默认值 */
	String defaultValue() default "";

}

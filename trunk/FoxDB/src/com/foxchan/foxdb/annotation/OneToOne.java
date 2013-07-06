package com.foxchan.foxdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一对一的注解。该注解被用于表示两个对象之间一一对应的映射关系。下面是该注解的使用范例：
 * <p>示例：</p>
 * <p>// On Customer class:</p>
 * <p>
 * {@code @OneToOne}<br/>
 * {@code public CustomerRecord getCustomerRecord() { return customerRecord; }}<br/>
 * </p>
 * <p>// On CustomerRecord class:</p>
 * <p>
 * {@code @OneToOne(mappedBy="customerRecord")}<br/>
 * {@code public Customer getCustomer() { return customer; }}
 * </p>
 * @author foxchan@live.cn
 * @create 2013-4-26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface OneToOne {
	
	/**
	 * 获得相关对象的级联方式
	 * @return	返回相关对象的级联方式的数据
	 */
	CascadeType[] cascade() default CascadeType.REFRESH;
	
	/**
	 * 获得相关实体的获取策略
	 * @return	返回相关实体的获取策略
	 */
	FetchType fetch() default FetchType.LAZY;
	
	/**
	 * 该映射关系中多的一方的映射的属性名称
	 * @return	返回该映射关系中多的一方的映射的属性名称
	 */
	String mappedBy() default "";

}

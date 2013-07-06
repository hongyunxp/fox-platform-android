package com.foxchan.foxdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多对一的注解。该注解被用来表示多个实体对象与一个共同的实体对象之间的关系，
 * 该注解将被用在需要表示存在这种多对一关系的多数的一方的类属性上。该属性必须
 * 与{@link OneToMany}注解成对出现和使用，下面是使用方式的示例：
 * <p>示例：</p>
 * <p>
 * @ManyToOne(optional=false)<br/>
 * @JoinColumn(name="CUST_ID", nullable=false, updatable=false)<br/>
 * public Customer getCustomer() { return customer; }<br/>
 * </p>
 * @author foxchan@live.cn
 * @create 2013-4-24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface ManyToOne {
	
	/**
	 * 获得相关对象的级联方式
	 * @return	返回相关对象的级联方式的数据
	 */
	CascadeType[] cascade() default CascadeType.REFRESH;
	
	/**
	 * 获得相关实体的获取策略
	 * @return	返回相关实体的获取策略
	 */
	FetchType fetch() default FetchType.EAGER;
	
	/**
	 * 相关对象之间的关联关系是否是可选的
	 * @return	如果相关对象之间的关联关系是可选的则返回true，否则返回false
	 */
	boolean optional() default true;

}

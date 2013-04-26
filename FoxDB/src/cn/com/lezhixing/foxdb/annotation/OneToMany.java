package cn.com.lezhixing.foxdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一对一的注解。该注解被用于表示实体之间的一个对象与多个相关对象之间的关系。
 * 该对象将被使用在表示一对多关系的一的一方的类属性上，同时该注解必须和注解
 * {@link ManyToOne}同时出现和使用，下面是该注解的使用示例：
 * <p>示例：</p>
 * <p>// In Customer class:</p>
 * <p>
 * {@code @OneToMany(cascade=ALL, mappedBy="customer")}<br/>
 * {@code public Set<Order> getOrders() { return orders; }<br/>
 * </p>
 * <p>// In Order class:</p>
 * <p>
 * {@code @ManyToOne}<br/>
 * {@code @JoinColumn(name="CUST_ID", nullable=false)}<br/>
 * {@code public Customer getCustomer() { return customer; }<br/>
 * </p>
 * @author foxchan@live.cn
 * @create 2013-4-24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface OneToMany {
	
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

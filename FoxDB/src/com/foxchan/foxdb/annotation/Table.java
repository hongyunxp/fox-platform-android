package com.foxchan.foxdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 		注解：数据表，被用于设置被标注的实体的数据表信息，如果没有<i>Table</i>注解,<br/>
 * </p>
 * <p>示例：</p>
 * <code>@Entity</code><br/>
 * <code>@Table(name="CUST")</code><br/>
 * <code>public class Customer { ... }</code>
 * <p></p>
 * 
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
	
	/** （可选的）数据表的名称，默认取值为entity的名称。 */
	String name() default "";

}

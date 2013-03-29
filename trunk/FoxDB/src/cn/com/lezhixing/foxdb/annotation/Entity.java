package cn.com.lezhixing.foxdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>注解：表明该类是一个实体类，该注解被用于实体类</p>
 * <p>示例：</p>
 * <code>@Entity</code><br/>
 * <code>public class Customer { ... }</code>
 * <p></p>
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
	
	/** （可选的）实体的名称，是实体类的唯一表示名称，该名称将被用于数据查询。 */
	String name() default "";
	
}

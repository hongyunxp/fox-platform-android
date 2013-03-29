package cn.com.lezhixing.foxdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解：枚举类型
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Enumerated {
	
	/** 枚举类型的保存的取值 */
	EnumType value();

}

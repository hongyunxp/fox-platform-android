package com.foxchan.foxdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 	注解：实体的主键映射，使用了<i>Id</i>注解的属性或者域必须是SQLLite数据库支持的数据类型
 * </p>
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Id {

}

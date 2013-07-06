package com.foxchan.foxdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解：瞬时属性，运用了该注解的属性将不会被持久化到数据库中
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Transient {

}

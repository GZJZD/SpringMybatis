package com.web.datebase.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义标签
 * 用于自动切换数据源
 * @author riseSun
 *
 * 2017年12月24日上午11:40:45
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.TYPE,ElementType.METHOD})
public @interface TransFormDataSource {
	String name() default "";
}

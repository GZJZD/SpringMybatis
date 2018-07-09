package com.web.database.config;

import java.lang.annotation.*;

/**
 * 自定义标签
 * 用于自动切换数据源
 * @author riseSun
 *
 * 2017年12月24日上午11:40:45
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.TYPE,ElementType.METHOD})
@Documented
public @interface TransFormDataSource {
	String name() default "";
}

package com.web.datebase.config;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态切换数据源
 * @author riseSun
 *
 * 2017年12月18日下午11:10:42
 */
@Aspect
public class MultipleDataSource extends AbstractRoutingDataSource {
	
	
    private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<String>();
    
    //切入点
    @Pointcut("@annotation(com.web.datebase.config.TransFormDataSource)")
    public void aspect() {}
    
    /**
     * 指定数据源
     * @param dataSource
     * @author riseSun
    
     * 2017年12月18日下午11:10:34
     */
    @Before("aspect() && @annotation(transFormDataSource)")
    public static void setDataSourceKey(TransFormDataSource transFormDataSource) {

        dataSourceKey.set(transFormDataSource.name());
    }
    /**
     * 移除当前使用的数据源，切换到系统默认的数据源
     * 
     * @author riseSun
    
     * 2017年12月23日下午4:00:15
     */
    @After("aspect()")
    public static void removeDataSourceKey() {
    	dataSourceKey.remove();
    }
    
    
    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceKey.get();
    }
}
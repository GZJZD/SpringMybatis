package com.web.datebase.config;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态切换数据源
 */
@Aspect
public class MultipleDataSource extends AbstractRoutingDataSource {
	
	
    private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<String>();
    
    //切入点
    @Pointcut("@annotation(com.web.datebase.config.TransFormDataSource)")
    public void aspect() {}



    /**
     * 指定数据源
     */
    @Before("aspect() && @annotation(transFormDataSource)")
    public static void setDataSourceKey(TransFormDataSource transFormDataSource) {
        try {
               dataSourceKey.set(transFormDataSource.name());
        }catch (Exception e){
            e.printStackTrace();
         }
    }

    /**
     * 移除当前使用的数据源，切换到系统默认的数据源
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
package com.web.tcp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 返回线程池
 * Created by may on 2018/4/29.
 */
public class ThreadPoolUtil {
    private  static ThreadPoolUtil instance;
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
    public static ThreadPoolUtil getInstance(){
        if(instance==null){
            synchronized (ThreadPoolUtil.class){
                if(instance==null){
                    instance=new ThreadPoolUtil();
                }
            }
        }
        return instance;
    }

    public static ExecutorService getThreadPoole(){
        return fixedThreadPool;

    }
}

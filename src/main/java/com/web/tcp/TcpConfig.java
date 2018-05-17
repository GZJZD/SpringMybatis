package com.web.tcp;

import com.web.tcp.tcpthread.NetworkManger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * tomcat 容器启时加载
 * 此处用的是注解方法启动容器
 */
@Service("tcpConfig")
public class TcpConfig {

        public List<String> phoneBlacklist = new ArrayList<String>();
        //初始化3个线程执行对应的连接操作
        private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        @PostConstruct
        public static void init(){
            fixedThreadPool.execute( new NetworkManger("116.62.195.204",12000,"orders75"));
            fixedThreadPool.execute( new NetworkManger("116.62.195.204",12001,"orders76"));
        }

}

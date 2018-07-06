package com.web.tcp;

import com.web.tcp.tcpthread.NetworkManger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
        private static NetworkManger orders75;
        private static NetworkManger orders76;
        private static NetworkManger orders77;
        @PostConstruct
        public static void init(){


            orders77= new NetworkManger("192.168.3.114",12001,"orders77");
            fixedThreadPool.execute( orders77);
            orders75= new NetworkManger("116.62.195.204",12000,"orders75");
            orders76= new NetworkManger("116.62.195.204",12001,"orders76");
            fixedThreadPool.execute( orders76);
            fixedThreadPool.execute( orders75);

        }
        @PreDestroy
        public static void destroy(){
            orders77.disconnection();
            orders76.disconnection();
            orders75.disconnection();

        }

}

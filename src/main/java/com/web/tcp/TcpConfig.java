package com.web.tcp;

import com.web.tcp.tcpthread.MyThread;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
/**
 * tomcat 容器启时加载
 * 此处用的是注解方法启动容器
 */
@Service("tcpConfig")
public class TcpConfig {

        public List<String> phoneBlacklist = new ArrayList<String>();

        @PostConstruct
        public static void init(){
            MyThread thread = new MyThread();
            thread.start();
        }

}

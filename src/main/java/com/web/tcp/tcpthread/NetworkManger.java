package com.web.tcp.tcpthread;

import com.web.tcp.DataParserServiceImpl;
import com.web.tcp.PlatformSocket;
import com.web.tcp.ThreadPoolUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkManger extends Thread {
    //由香港提供的数据是固定以HEAD;开头的
    private Pattern compile = Pattern.compile("HEAD;");
    //一条完整的交易数据的分号数量是14个
    private final int MAX_SEMICOLONS = 14;
    //平台套接字对象

    private  Socket socket = null;
    //时间间隔
    private static int timeNum = 0;
    //平台名字
    private String platformName;
    //ip
    private String ip;
    //端口
    private Integer port;

    private static boolean isConnect = true;
    public  NetworkManger(String ip, Integer port, String platformName) {
        this.ip = ip;
        this.port = port;
        this.platformName = platformName;
    }

    @Override
    public synchronized void run() {
        while (isConnect) {
            // 与服务端建立连接
            try {
                ConnectToServerByTcp();
                //接收数据
                receiveData();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 接收tcp的数据
     *
     * @Author: May
     * @Date: 16:55 2018/5/17
     */
    public synchronized void receiveData() {
        try {
            InputStream inputStream = socket.getInputStream();
            int content = -1;
            //用来装载数据
            StringBuilder sBuilder = new StringBuilder();
            //分号数量
            int semicolons = 0;
            Matcher matcher = null;
            // 循环append服务端数据
            while ((content = inputStream.read()) != -1) {
                char ch = (char) content;
                if (ch == ';') {
                    ++semicolons;
                }
                sBuilder.append(ch);

                if (semicolons == 14) {
                    matcher = compile.matcher(sBuilder);
                    if (matcher.find()) {
                        int start = matcher.start();
                        if (start != 0) {
                            sBuilder.delete(0, start);
                            semicolons = 0;
                            for (int i = 0; i < sBuilder.length(); i++) {
                                if (sBuilder.charAt(i) == ';')
                                    ++semicolons;
                            }
                        }
                        if (start == 0 || semicolons == MAX_SEMICOLONS) {

                            //System.out.println(sBuilder.toString());
                            //完成一次交易数据的监听,将数据交于其他线程处理
//                             madeOrderThreadPool.execute(new DealServiceImpl(sBuilder.toString(),platformSocket.getPlatformName()));
                            ThreadPoolUtil.getInstance().getThreadPoole().execute(new DataParserServiceImpl(sBuilder.toString(), this.platformName));
                            semicolons = 0;
                            sBuilder.delete(0, sBuilder.length());
                        }
//                        continue;
                    }
                }
            }

        } catch (SocketTimeoutException e) {
            System.out.println("服务器连接超时,重新连接ing");
            int startTime = 5000;
            NetworkManger networkManger = new NetworkManger(ip, port, platformName);
            try {
                if (socket != null) {
                    socket.close();
                    System.out.println("socket 删除成功");
                }

                sleep(startTime);
                networkManger.run();
                System.out.println("服务器重启完成");
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error lest");
        }
    }


    public synchronized Socket ConnectToServerByTcp() throws InterruptedException {
// 建立通讯连接
        boolean connectOk = true;
        boolean test = true;
        try {
            // 创建一个流套接字并将其连接到指定主机上的指定端口号
            if (test) {
                socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress(ip, port);
                //数据接收的响应时间设置
                // socket.setSoTimeout(7000);
                socket.connect(socketAddress, 5000); // 连接超时限制在5秒
                //       otherSocket.setSoTimeout(1000 * timeOutSecond);//设置读操作超时时间5到180秒
                test = false;
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            timeNum += 2;
            connectOk = false;
            test = true;
            LogManager.getLogger(NetworkManger.class).error("连接超时," + timeNum + "秒后进行重连");

                Thread.sleep(timeNum*1000);

        } catch (ConnectException e) {
            timeNum += 2;
            test = true;
            connectOk = false;
            e.printStackTrace();
            LogManager.getLogger(NetworkManger.class).error("连接失败" + timeNum + "秒后进行重连");
            Thread.sleep(timeNum*1000);

        } catch (IOException e) {
            timeNum += 2;
            test = true;
            connectOk = false;
            e.printStackTrace();
            try {
                Thread.sleep(timeNum*1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        if (!connectOk) {
            if (socket != null) {
                try {


                    socket.close();
                } catch (IOException e) {
                    timeNum += 2;
                    Thread.sleep(timeNum * 1000);
                    e.printStackTrace();
                }
                socket = null;
                test = true;
                ConnectToServerByTcp();

            }
            //
            if (timeNum > 0)
                timeNum = 0;
        }
        return socket;
    }

    /*
     *
     *   tomcat关闭时断开连接
     * @author may
     * @date 2018/6/6 15:28
     * @param
     * @return
     */
    public  void  disconnection (){

        try {
            this.socket.close();
            isConnect  = false;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}

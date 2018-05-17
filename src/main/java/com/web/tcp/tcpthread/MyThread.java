package com.web.tcp.tcpthread;

import com.web.tcp.DealServiceImpl;
import com.web.tcp.PlatformSocket;
import com.web.tcp.ThreadPoolUtil;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyThread  extends Thread{
    //由香港提供的数据是固定以HEAD;开头的
    private Pattern compile = Pattern.compile("HEAD;");
    //一条完整的交易数据的分号数量是11个
    private final int MAX_SEMICOLONS = 11;
    //平台套接字对象
    private PlatformSocket platformSocket;
    private static Socket socket = null;
    //时间间隔
    private static int timeNum = 0;
    public static final String TEST_TCP = "192.168.3.176";
    public static final String DEV_TCP = "116.62.195.204";
    public static final String ip = "192.168.3.119";
    public static final Integer port = 12000;
    public MyThread(){

    }

    @Override
    public void run() {

        // 与服务端建立连接
        try {
             ConnectToServerByTcp();
            InputStream inputStream = socket.getInputStream();
            int content = -1;
            //用来装载数据
            StringBuilder sBuilder = new StringBuilder();
            //分号数量
            int semicolons = 0;
            Matcher matcher = null;
            // 循环append服务端数据
            while((content = inputStream.read()) != -1){
                char ch = (char) content;
                if (ch == ';') {
                    ++semicolons;
                }
                sBuilder.append(ch);

                if(semicolons == 11){
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
                            System.out.println(sBuilder.toString()+"*****************************");
                            //System.out.println(sBuilder.toString());
                            //完成一次交易数据的监听,将数据交于其他线程处理
//                             madeOrderThreadPool.execute(new DealServiceImpl(sBuilder.toString(),platformSocket.getPlatformName()));
                            ThreadPoolUtil.getInstance().getThreadPoole().execute(new DealServiceImpl(sBuilder.toString(), "DZ"));
                            semicolons = 0;
                            sBuilder.delete(0, sBuilder.length());
                        }
//                        continue;
                    }
                }
            }

        } catch (SocketTimeoutException e){
            System.out.println("服务器连接超时,重新连接ing");
            int startTime = 5000;
            MyThread myThread = new MyThread();
            try {
                if (socket != null) {
                    socket.close();
                    System.out.println("socket 删除成功");
                }

                sleep(startTime);
                myThread.run();
                System.out.println("服务器重启完成");
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } catch (Exception e){
            e.printStackTrace();
            System.out.println("error lest");
        }


    }






    public static Socket ConnectToServerByTcp() {
// 建立通讯连接
        boolean connectOk = true;
        try {
            // 创建一个流套接字并将其连接到指定主机上的指定端口号
            socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(ip, 12000);
            //数据接收的响应时间设置
          //  socket.setSoTimeout(10000);
            socket.connect(socketAddress,10000); // 连接超时限制在5秒
            //       otherSocket.setSoTimeout(1000 * timeOutSecond);//设置读操作超时时间5到180秒

        }catch (SocketTimeoutException e) {
            e.printStackTrace();
            timeNum += 2;
            connectOk = false;
            Logger.getLogger(MyThread.class).error("连接超时," + timeNum + "秒后进行重连");
        }
        catch (ConnectException e) {
            connectOk = false;
            e.printStackTrace();
            Logger.getLogger(MyThread.class).error("连接失败" + timeNum + "秒后进行重连");
        } catch (IOException e) {
            connectOk = false;
            e.printStackTrace();
        }
        if (!connectOk) {
            if (socket != null) {
                try {
                    Thread.sleep(timeNum * 1000);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                socket = null;
                ConnectToServerByTcp();
            }
            //
            if (timeNum > 0)
                timeNum = 0;
        }
        return socket;
    }


}

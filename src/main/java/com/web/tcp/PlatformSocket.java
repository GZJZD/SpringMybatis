package com.web.tcp;

import java.net.Socket;

public class PlatformSocket {

    //套接字对象
    private Socket socket;
    //客户数据平台名称
    private String platformName;
    //构造方法
    public PlatformSocket(Socket socket, String platformName) {
        super();
        this.socket = socket;
        this.platformName = platformName;
    }


    //set.get...
    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    public String getPlatformName() {
        return platformName;
    }
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
}

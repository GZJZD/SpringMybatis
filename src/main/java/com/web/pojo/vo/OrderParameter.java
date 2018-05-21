package com.web.pojo.vo;


import java.io.Serializable;

/**
 * 传送交易对象的实体
 *@Author: May
 *@param
 *@Date: 21:16 2018/4/23
 */

public class OrderParameter implements Serializable{
    //交易账号
    private String dealAccount;
    //交易手数
    private Double handNum;
    //是否为市价(1:市价，0为限价)
    private int orderPoint;
    //如果为限价，交易的市价 + - customerPointNum
    private Double customerPointNum;
    //对接号码:开仓单号-跟单id-交易记录自增id
    private String signalNumber;

    //多空(0=多,1=空)
    private Integer cmd;
    //开平(0=开,1=平)
    private Integer openClose;
    //时间
    private String createTime;

    public String getDealAccount() {
        return dealAccount;
    }

    public void setDealAccount(String dealAccount) {
        this.dealAccount = dealAccount;
    }

    public Double getHandNum() {
        return handNum;
    }

    public void setHandNum(Double handNum) {
        this.handNum = handNum;
    }

    public int getOrderPoint() {
        return orderPoint;
    }

    public void setOrderPoint(int orderPoint) {
        this.orderPoint = orderPoint;
    }

    public Double getCustomerPointNum() {
        return customerPointNum;
    }

    public void setCustomerPointNum(Double customerPointNum) {
        this.customerPointNum = customerPointNum;
    }

    public String getSignalNumber() {
        return signalNumber;
    }

    public void setSignalNumber(String signalNumber) {
        this.signalNumber = signalNumber;
    }



    public Integer getCmd() {
        return cmd;
    }

    public void setCmd(Integer cmd) {
        this.cmd = cmd;
    }

    public Integer getOpenClose() {
        return openClose;
    }

    public void setOpenClose(Integer openClose) {
        this.openClose = openClose;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

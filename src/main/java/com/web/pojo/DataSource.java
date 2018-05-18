package com.web.pojo;

import java.util.Date;
/**
        * Socket交易数据实体
        * @author riseSun
        *
        * 2018年1月4日上午12:13:54
        */

public class DataSource {

    //头
    private String head;
    //账号
    private String login;
    //开仓单号
    private String openOrderNum;
    //新开仓单号
    private String newOpenOrderNum;
    //商品
    private String varietyCode;
    //手数
    private Double counts;
    //价位
    private Double price;
    //时间
    //private Date createTime;
    private Date createTime;
    //多空(0=多,1=空)
    private Integer cmd;
    //开平(0=开,1=平)
    private Integer openClose;
    //平仓盈亏
    private Double profit;
    //数据源的平台
    private String platformName;


    //set,get...
    public String getHead() {
        return head;
    }
    public void setHead(String head) {
        this.head = head;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getOpenOrderNum() {
        return openOrderNum;
    }
    public void setOpenOrderNum(String openOrderNum) {
        this.openOrderNum = openOrderNum;
    }
    public String getNewOpenOrderNum() {
        return newOpenOrderNum;
    }
    public void setNewOpenOrderNum(String newOpenOrderNum) {
        this.newOpenOrderNum = newOpenOrderNum;
    }
    public String getVarietyCode() {
        return varietyCode;
    }
    public void setVarietyCode(String varietyCode) {
        this.varietyCode = varietyCode;
    }
    public Double getCounts() {
        return counts;
    }
    public void setCounts(Double counts) {
        this.counts = counts;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
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
    public Double getProfit() {
        return profit;
    }
    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    @Override
    public String toString() {
        return "DataSource{" +
                "head='" + head + '\'' +
                ", login='" + login + '\'' +
                ", openOrderNum='" + openOrderNum + '\'' +
                ", newOpenOrderNum='" + newOpenOrderNum + '\'' +
                ", varietyCode='" + varietyCode + '\'' +
                ", counts=" + counts +
                ", price=" + price +
                ", createTime=" + createTime +
                ", cmd=" + cmd +
                ", openClose=" + openClose +
                ", profit=" + profit +
                ", platformName='" + platformName + '\'' +
                '}';
    }
}

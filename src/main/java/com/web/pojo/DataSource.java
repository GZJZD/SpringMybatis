package com.web.pojo;


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
    private String ticket;
    //新开仓单号
    private String newTicket;
    //商品
    private String varietyCode;
    //手数
    private Double handNumber;
    //价位
    private Double price;
    //时间
    private String createTime;
    //多空(0=多,1=空)
    private Integer cmd;
    //开平(0=开,1=平)
    private Integer openClose;
    //平仓盈亏
    private Double profit;
    //数据源的平台
    private String platformName;
    //代理人
    private String agencyName;
    //手续费
    private Double commission;
    //止盈
    private Double stopProfit;
    //止损
    private Double stopLoss;


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

    public String getTicket() {
        return ticket;
    }
    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
    public String getNewTicket() {
        return newTicket;
    }
    public void setNewTicket(String newTicket) {
        this.newTicket = newTicket;
    }
    public String getVarietyCode() {
        return varietyCode;
    }
    public void setVarietyCode(String varietyCode) {
        this.varietyCode = varietyCode;
    }
    public Double getHandNumber() {
        return handNumber;
    }
    public void setHandNumber(Double handNumber) {
        this.handNumber = handNumber;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getStopProfit() {
        return stopProfit;
    }

    public void setStopProfit(Double stopProfit) {
        this.stopProfit = stopProfit;
    }

    public Double getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(Double stopLoss) {
        this.stopLoss = stopLoss;
    }

    @Override
    public String toString() {
        return "DataSource{" +
                "head='" + head + '\'' +
                ", login='" + login + '\'' +
                ", ticket='" + ticket + '\'' +
                ", newTicket='" + newTicket + '\'' +
                ", varietyCode='" + varietyCode + '\'' +
                ", handNumber=" + handNumber +
                ", price=" + price +
                ", createTime=" + createTime +
                ", cmd=" + cmd +
                ", openClose=" + openClose +
                ", profit=" + profit +
                ", platformName='" + platformName + '\'' +
                '}';
    }
}

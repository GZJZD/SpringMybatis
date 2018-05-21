package com.web.pojo;

import java.io.Serializable;
import java.util.Date;

public class FollowOrderClientDetail implements Serializable {
    private Long id;
    //客户名
    private String clientName;
    //品种名称
    private String varietyName;
    //交易方向:买入/卖出
    private Integer tradeDirection;
    //手数
    private Double handNumber;
    //开仓价
    private Double openPrice;
    //开仓时间
    private Date openTime;
    //平仓价
    private Double closePrice;
    //平仓时间
    private Date closeTime;
    //手续费
    private Double poundage;
    //盈亏
    private Double profitLoss;
    //客户盈亏
    private Double clientProfit;
    //开仓单号
    private Integer openOrderNumber;
    //修改时间
    private Date updateTime;
    //跟单id
    private Long followOrderId;
    //版本
    private Date version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getVarietyName() {
        return varietyName;
    }

    public void setVarietyName(String varietyName) {
        this.varietyName = varietyName;
    }

    public Integer getTradeDirection() {
        return tradeDirection;
    }

    public void setTradeDirection(Integer tradeDirection) {
        this.tradeDirection = tradeDirection;
    }

    public Double getHandNumber() {
        return handNumber;
    }

    public void setHandNumber(Double handNumber) {
        this.handNumber = handNumber;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Double getPoundage() {
        return poundage;
    }

    public void setPoundage(Double poundage) {
        this.poundage = poundage;
    }

    public Double getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(Double profitLoss) {
        this.profitLoss = profitLoss;
    }

    public Double getClientProfit() {
        return clientProfit;
    }

    public void setClientProfit(Double clientProfit) {
        this.clientProfit = clientProfit;
    }

    public Integer getOpenOrderNumber() {
        return openOrderNumber;
    }

    public void setOpenOrderNumber(Integer openOrderNumber) {
        this.openOrderNumber = openOrderNumber;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getFollowOrderId() {
        return followOrderId;
    }

    public void setFollowOrderId(Long followOrderId) {
        this.followOrderId = followOrderId;
    }

    public Date getVersion() {
        return version;
    }

    public void setVersion(Date version) {
        this.version = version;
    }
}
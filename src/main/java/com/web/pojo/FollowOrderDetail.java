package com.web.pojo;

import java.io.Serializable;
import java.util.Date;
/**
 * 客户
 *@Author: May
 *@param
 *@Date: 15:46 2018/5/21
 */
public class FollowOrderDetail implements Serializable {
    private Long id;
    //客户名
    private String clientName;
    //品种名称
    private String varietyName;
    //交易方向:买入/卖出
    private Integer tradeDirection;
    //手数
    private Double handNumber;
    //剩下手数
    private Double remainHandNumber;
    //交易id
    private Long followOrderTradeRecordId;
    //开仓价
    private Double openPrice;
    //开仓时间
    private String openTime;
    //平仓价
    private Double closePrice;
    //平仓时间
    private String closeTime;
    //手续费
    private Double poundage;
    //盈亏
    private Double profitLoss;
    //客户盈亏
    private Double clientProfit;
    //开仓单号
    private String ticket;
    //修改人id
    private Long updateByUser;
    //修改时间
    private String updateDate;
    //创建时间
    private String createDate;
    //跟单id
    private Long followOrderId;
    //版本
    private Integer version=0;

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

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

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getTicket() {
        return ticket;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
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

    public Long getFollowOrderId() {
        return followOrderId;
    }

    public void setFollowOrderId(Long followOrderId) {
        this.followOrderId = followOrderId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getUpdateByUser() {
        return updateByUser;
    }

    public void setUpdateByUser(Long updateByUser) {
        this.updateByUser = updateByUser;
    }

    public Double getRemainHandNumber() {
        return remainHandNumber;
    }

    public void setRemainHandNumber(Double remainHandNumber) {
        this.remainHandNumber = remainHandNumber;
    }

    public Long getFollowOrderTradeRecordId() {
        return followOrderTradeRecordId;
    }

    public void setFollowOrderTradeRecordId(Long followOrderTradeRecordId) {
        this.followOrderTradeRecordId = followOrderTradeRecordId;
    }
}
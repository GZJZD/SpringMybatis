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
    //实际手数
    private Integer handNumber;
    //剩下手数
    private Integer remainHandNumber;
    //应交易的数量
    private Integer originalHandNumber;
    //交易id
    private Long followOrderTradeRecordId;
    //交易账号id
    private Long accountId;
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
    private Double profitLoss=0.0;
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

    public Integer getHandNumber() {
        return handNumber;
    }

    public void setHandNumber(Integer handNumber) {
        this.handNumber = handNumber;
    }

    public Integer getRemainHandNumber() {
        return remainHandNumber;
    }

    public void setRemainHandNumber(Integer remainHandNumber) {
        this.remainHandNumber = remainHandNumber;
    }

    public Integer getOriginalHandNumber() {
        return originalHandNumber;
    }

    public void setOriginalHandNumber(Integer originalHandNumber) {
        this.originalHandNumber = originalHandNumber;
    }

    public Long getFollowOrderTradeRecordId() {
        return followOrderTradeRecordId;
    }

    public void setFollowOrderTradeRecordId(Long followOrderTradeRecordId) {
        this.followOrderTradeRecordId = followOrderTradeRecordId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
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

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Long getUpdateByUser() {
        return updateByUser;
    }

    public void setUpdateByUser(Long updateByUser) {
        this.updateByUser = updateByUser;
    }

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
}
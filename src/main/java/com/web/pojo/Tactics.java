package com.web.pojo;

import java.io.Serializable;
import java.util.Date;
/**
 * 策略
 *@Author: May
 *@param
 *@Date: 10:40 2018/5/21
 */
public class Tactics implements Serializable{
    private Long id;
    //策略名称
    private String tacticsName;
    //账号的id
    private Account account;
    //品种
    private Variety variety;
    //跟单方式:用户or净头寸
    private Integer followManner;
    //最大止盈
    private Double maxProfit;
    //止盈点/手
    private Double maxProfitNumber;
    //最大止损
    private Double maxLoss;
    //止损点/手
    private Double maxLossNumber;
    //账户止损
    private Double accountLoss;
    //下单点位:市价or限价
    private Integer orderPoint;
    //客户点位:好or差
    private Integer clientPoint;
    //点位
    private Double clientPointNumber;
    //净头寸方向:正向/方向
    private Integer netPositionDirection;
    //净头寸变化
    private Integer netPositionChange;
    //手数
    private Integer netPositionFollowNumber;
    //净头寸的值
    private Double netPositionSum;
    //持仓值
    private Integer netPositionHoldNumber;
    //判断是否正在交易
    private Integer netPositionStatus;
    //版本
    private Integer version=0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTacticsName() {
        return tacticsName;
    }

    public void setTacticsName(String tacticsName) {
        this.tacticsName = tacticsName;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Variety getVariety() {
        return variety;
    }

    public void setVariety(Variety variety) {
        this.variety = variety;
    }

    public Integer getFollowManner() {
        return followManner;
    }

    public void setFollowManner(Integer followManner) {
        this.followManner = followManner;
    }

    public Double getMaxProfit() {
        return maxProfit;
    }

    public void setMaxProfit(Double maxProfit) {
        this.maxProfit = maxProfit;
    }

    public Double getMaxProfitNumber() {
        return maxProfitNumber;
    }

    public void setMaxProfitNumber(Double maxProfitNumber) {
        this.maxProfitNumber = maxProfitNumber;
    }

    public Double getMaxLoss() {
        return maxLoss;
    }

    public void setMaxLoss(Double maxLoss) {
        this.maxLoss = maxLoss;
    }

    public Double getMaxLossNumber() {
        return maxLossNumber;
    }

    public void setMaxLossNumber(Double maxLossNumber) {
        this.maxLossNumber = maxLossNumber;
    }

    public Double getAccountLoss() {
        return accountLoss;
    }

    public void setAccountLoss(Double accountLoss) {
        this.accountLoss = accountLoss;
    }

    public Integer getOrderPoint() {
        return orderPoint;
    }

    public void setOrderPoint(Integer orderPoint) {
        this.orderPoint = orderPoint;
    }

    public Integer getClientPoint() {
        return clientPoint;
    }

    public void setClientPoint(Integer clientPoint) {
        this.clientPoint = clientPoint;
    }

    public Double getClientPointNumber() {
        return clientPointNumber;
    }

    public void setClientPointNumber(Double clientPointNumber) {
        this.clientPointNumber = clientPointNumber;
    }

    public Integer getNetPositionDirection() {
        return netPositionDirection;
    }

    public void setNetPositionDirection(Integer netPositionDirection) {
        this.netPositionDirection = netPositionDirection;
    }

    public Integer getNetPositionChange() {
        return netPositionChange;
    }

    public void setNetPositionChange(Integer netPositionChange) {
        this.netPositionChange = netPositionChange;
    }

    public Integer getNetPositionFollowNumber() {
        return netPositionFollowNumber;
    }

    public void setNetPositionFollowNumber(Integer netPositionFollowNumber) {
        this.netPositionFollowNumber = netPositionFollowNumber;
    }

    public Double getNetPositionSum() {
        return netPositionSum;
    }

    public void setNetPositionSum(Double netPositionSum) {
        this.netPositionSum = netPositionSum;
    }

    public Integer getNetPositionHoldNumber() {
        return netPositionHoldNumber;
    }

    public void setNetPositionHoldNumber(Integer netPositionHoldNumber) {
        this.netPositionHoldNumber = netPositionHoldNumber;
    }

    public Integer getNetPositionStatus() {
        return netPositionStatus;
    }

    public void setNetPositionStatus(Integer netPositionStatus) {
        this.netPositionStatus = netPositionStatus;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
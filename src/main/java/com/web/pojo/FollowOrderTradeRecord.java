package com.web.pojo;

import java.io.Serializable;
import java.util.Date;
/**
 * 跟单交易记录
 *@Author: May
 *@param
 *@Date: 15:46 2018/5/21
 */
public class FollowOrderTradeRecord implements Serializable {
    private Long id;

    //品种id
    private Long varietyId;
    //品种代码
    private String varietyCode;
    //手数
    private Double handNumber;
    //交易时间
    private String tradeTime;
    //交易方向:多/空
    private Integer tradeDirection;
    //交易类型:开仓/平仓
    private Integer openCloseType;
    //市场价
    private Double marketPrice;

    //交易账号id
    private Long accountId;
    //跟单id
    private Long followOrderId;

    //开仓单号
    private String ticket;
    //新开仓单号
    private String newTicket;
    //客户姓名
    private String clientName;
    //手续费
    private Double poundage;
    //客户净头寸id
    private Long clientNetPositionId;
    //修改时间
    private String updateDate;
    //创建时间
    private String createDate;
    //版本
    private Integer version=0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVarietyId() {
        return varietyId;
    }

    public void setVarietyId(Long varietyId) {
        this.varietyId = varietyId;
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

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public Integer getTradeDirection() {
        return tradeDirection;
    }

    public void setTradeDirection(Integer tradeDirection) {
        this.tradeDirection = tradeDirection;
    }

    public Integer getOpenCloseType() {
        return openCloseType;
    }

    public void setOpenCloseType(Integer openCloseType) {
        this.openCloseType = openCloseType;
    }

    public Double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getFollowOrderId() {
        return followOrderId;
    }

    public void setFollowOrderId(Long followOrderId) {
        this.followOrderId = followOrderId;
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

    public Double getPoundage() {
        return poundage;
    }

    public void setPoundage(Double poundage) {
        this.poundage = poundage;
    }

    public Long getClientNetPositionId() {
        return clientNetPositionId;
    }

    public void setClientNetPositionId(Long clientNetPositionId) {
        this.clientNetPositionId = clientNetPositionId;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
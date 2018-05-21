package com.web.pojo;

import java.io.Serializable;
import java.util.Date;

public class FollowOrderTradeRecord implements Serializable {
    private Long id;
    //跟单人id
    private Long documentary_id;
    //品种id
    private Long varietyId;
    //品种代码
    private String varietyCode;
    //手数
    private Double handNumber;
    //交易时间
    private Date tradeTime;
    //交易方向:多/空
    private Integer tradeDirection;
    //交易类型:开仓/平仓
    private Integer openCloseType;
    //市场价
    private Double marketPrice;
    //对接号:开仓单号-跟单编号-自增Id
    private String signalNumber;
    //交易账号id
    private Long accountId;
    //跟单id
    private Long followOrderId;
    //交易状态:成功/失败
    private Integer tradeStatus;
    //开仓单号
    private String openOrderNumber;
    //新开仓单号
    private String newOpenOrderNumber;
    //手续费
    private Double poundage;
    //版本
    private Integer version=0;

    @Override
    public String toString() {
        return "FollowOrderTradeRecord{" +
                "id=" + id +
                ", documentary_id=" + documentary_id +
                ", varietyId=" + varietyId +
                ", varietyCode='" + varietyCode + '\'' +
                ", handNumber=" + handNumber +
                ", tradeTime=" + tradeTime +
                ", tradeDirection=" + tradeDirection +
                ", openCloseType=" + openCloseType +
                ", marketPrice=" + marketPrice +
                ", signalNumber='" + signalNumber + '\'' +
                ", accountId=" + accountId +
                ", followOrderId=" + followOrderId +
                ", tradeStatus=" + tradeStatus +
                ", openOrderNumber='" + openOrderNumber + '\'' +
                ", newOpenOrderNumber='" + newOpenOrderNumber + '\'' +
                ", poundage=" + poundage +
                ", version=" + version +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentary_id() {
        return documentary_id;
    }

    public void setDocumentary_id(Long documentary_id) {
        this.documentary_id = documentary_id;
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

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
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

    public String getSignalNumber() {
        return signalNumber;
    }

    public void setSignalNumber(String signalNumber) {
        this.signalNumber = signalNumber;
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

    public Integer getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(Integer tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getOpenOrderNumber() {
        return openOrderNumber;
    }

    public void setOpenOrderNumber(String openOrderNumber) {
        this.openOrderNumber = openOrderNumber;
    }

    public String getNewOpenOrderNumber() {
        return newOpenOrderNumber;
    }

    public void setNewOpenOrderNumber(String newOpenOrderNumber) {
        this.newOpenOrderNumber = newOpenOrderNumber;
    }

    public Double getPoundage() {
        return poundage;
    }

    public void setPoundage(Double poundage) {
        this.poundage = poundage;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
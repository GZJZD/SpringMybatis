package com.web.pojo.vo;

import java.io.Serializable;

//此类用于与交易系统的开仓实体
public class OrderTrade implements Serializable {
    //用于标识接口对应的操作
    private String typeId;
    //交易记录id
    private Long requestId;
    //交易账号的交易平台
    private String brokerId;
    //交易账号的名字
    private String userId;
    //交易平台所对应的品种合约代码
    private String instrumentId;
    //买卖方向
    private Integer orderDirection;
    //开仓交易数量(可少)，开仓填这个
    private Double volumeTotalOriginal;
    //平仓交易手数(必须全平)，平仓填这个
    private Double orderVolume;
    //超时时长
    private Integer orderTimeout;

    public Double getOrderVolume() {
        return orderVolume;
    }

    public void setOrderVolume(Double orderVolume) {
        this.orderVolume = orderVolume;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public Integer getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(Integer orderDirection) {
        this.orderDirection = orderDirection;
    }

    public Double getVolumeTotalOriginal() {
        return volumeTotalOriginal;
    }

    public void setVolumeTotalOriginal(Double volumeTotalOriginal) {
        this.volumeTotalOriginal = volumeTotalOriginal;
    }

    public Integer getOrderTimeout() {
        return orderTimeout;
    }

    public void setOrderTimeout(Integer orderTimeout) {
        this.orderTimeout = orderTimeout;
    }
}

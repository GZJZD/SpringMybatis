package com.web.pojo.vo;

import java.io.Serializable;

/*
 *
 * 此类用于交易信息返回结果
 * @author may
 * @date 2018/5/25 16:50
 */
public class OrderMsgResult implements Serializable {
    private String typeId;
    //对应的交易id
    private Integer requestId;
    //返回码
    private Integer errcode;
    //对返回码的文本描述
    private String ermsg;
    //实际成交的的数量:开仓返回码,平仓的返回码没有
    private Double tradeVolume;
    //价格
    private Double tradePrice;
    //手续费
    private Double tradeCommission;
    //日期
    private String tradeDate;
    //时间
    private String tradeTime;

    public Double getTradeVolume() {
        return tradeVolume;
    }

    public void setTradeVolume(Double tradeVolume) {
        this.tradeVolume = tradeVolume;
    }

    public Double getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(Double tradePrice) {
        this.tradePrice = tradePrice;
    }

    public Double getTradeCommission() {
        return tradeCommission;
    }

    public void setTradeCommission(Double tradeCommission) {
        this.tradeCommission = tradeCommission;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErmsg() {
        return ermsg;
    }

    public void setErmsg(String ermsg) {
        this.ermsg = ermsg;
    }
}

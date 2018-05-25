package com.web.pojo;

import com.web.util.common.BaseUtil;

/**
 * 客户列表
 */
public class OrderUser  extends BaseUtil {
    /**
     * 用户编号
     */
    private String userCode;

    /**
     *开仓单号
     */
     private String ticket;

    /**
     * 商品
     */
    private String produceCode;

    /**
     * 多空
     */
    private Integer longShort;

    /**
     * 手数
     */
    private Double handNumber;
    /**
     * 开仓价
     */
     private  Double openPrice;
    /**
     * 平仓价位
     */
    private Double closePrice;
    /**
     * 开仓时间
     */
    private String openTime;

    /**
     * 平仓时间
     */
    private String  closeTime;
    /**
     * 平仓盈亏
     */
    private Double profit;
    /**
     * 价位
     */
    private Double price;

    /**
     * 平台
     */
    private String platFormCode;

    /**
     * 代理人
     */
    private String agencyName;


    /**************后期可能新追加字段*******************/

    /**
     * 买入价
     */
    private Double bid;
    /**
     * 卖出价
     */
    private Double ask;

    /**
     * 每日高价
     */
    private Double high;
    /**
     * 每日最低价
     */
    private Double low;

    /**
     * 入金
     */
    private Double inMoney;
    /**
     * 出金
     */
    private Double outMoney;


    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getProduceCode() {
        return produceCode;
    }

    public void setProduceCode(String produceCode) {
        this.produceCode = produceCode;
    }

    public Integer getLongShort() {
        return longShort;
    }

    public void setLongShort(Integer longShort) {
        this.longShort = longShort;
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

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


    public String getPlatFormCode() {
        return platFormCode;
    }

    public void setPlatFormCode(String platFormCode) {
        this.platFormCode = platFormCode;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public Double getBid() {
        return bid;
    }

    public void setBid(Double bid) {
        this.bid = bid;
    }

    public Double getAsk() {
        return ask;
    }

    public void setAsk(Double ask) {
        this.ask = ask;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getInMoney() {
        return inMoney;
    }

    public void setInMoney(Double inMoney) {
        this.inMoney = inMoney;
    }

    public Double getOutMoney() {
        return outMoney;
    }

    public void setOutMoney(Double outMoney) {
        this.outMoney = outMoney;
    }
}

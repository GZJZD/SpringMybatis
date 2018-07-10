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
    private String productCode;

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

    /**
     * 手续费
     */
    private Double commission;

    /**
     *  止盈
     */
    private Double stopProfit;

    /**
     * 止损
     */
    private Double stopLoss;
    public OrderUser() {

    }

    public OrderUser(String userCode, String ticket, String productCode, Integer longShort, Double handNumber, Double openPrice, Double closePrice, String openTime, String closeTime, Double profit, Double price, String platFormCode, String agencyName, Double commission, Double stopProfit, Double stopLoss) {
        this.userCode = userCode;
        this.ticket = ticket;
        this.productCode = productCode;
        this.longShort = longShort;
        this.handNumber = handNumber;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.profit = profit;
        this.price = price;
        this.platFormCode = platFormCode;
        this.agencyName = agencyName;
        this.commission = commission;
        this.stopProfit = stopProfit;
        this.stopLoss = stopLoss;
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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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
}

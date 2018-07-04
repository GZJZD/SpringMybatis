package com.web.datebase.entity;

/**
 * 价格实体类
 */
public class Prices {

    /**
     * 商品代码
     */
    private String symbol;
    /*
    *
    * 买入价
    * */
    private Double bid;
    /*
    * 卖出价
    * */
    private  Double ask;
    /**
     * 当前最高价
     */
    private Double high;
    /**
     * 当前最低价
     */
    private Double low;


    public Prices() {

    }

    public Prices(String symbol, Double bid, Double ask, Double high, Double low) {
        this.symbol = symbol;
        this.bid = bid;
        this.ask = ask;
        this.high = high;
        this.low = low;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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
}

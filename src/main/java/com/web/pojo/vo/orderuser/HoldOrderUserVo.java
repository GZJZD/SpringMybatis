package com.web.pojo.vo.orderuser;

import com.web.pojo.OrderUser;

/**
 * 明细列表中的 持仓记录
 */
public class HoldOrderUserVo extends OrderUser {


    /**
     * 市价
     */
    private Double marketPrice;
    /**
     * 盈亏
     */
    private Double gain_and_loss;


    public HoldOrderUserVo() {

    }

    public HoldOrderUserVo(Double marketPrice, Double gain_and_loss) {
        this.marketPrice = marketPrice;
        this.gain_and_loss = gain_and_loss;
    }

    public HoldOrderUserVo(String userCode, String ticket, String productCode, Integer longShort, Double handNumber, Double openPrice, Double closePrice, String openTime, String closeTime, Double profit, Double price, String platFormCode, String agencyName, Double commission, Double stopProfit, Double stopLoss, Double marketPrice, Double gain_and_loss) {
        super(userCode, ticket, productCode, longShort, handNumber, openPrice, closePrice, openTime, closeTime, profit, price, platFormCode, agencyName, commission, stopProfit, stopLoss);
        this.marketPrice = marketPrice;
        this.gain_and_loss = gain_and_loss;
    }

    public Double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Double getGain_and_loss() {
        return gain_and_loss;
    }

    public void setGain_and_loss(Double gain_and_loss) {
        this.gain_and_loss = gain_and_loss;
    }
}

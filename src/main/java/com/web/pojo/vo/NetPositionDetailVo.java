package com.web.pojo.vo;

import com.web.util.StatusUtil;

/**
 * 净头寸明细的映射
 * Created by may on 2018/5/24.
 */
public class NetPositionDetailVo {
    //净头寸值
    private Double netPositionSum;
    //品种名称
    private String varietyName;
    //开平类型
    private Integer openCloseType;
    //手数
    private Double handNumber;
    //多空方向
    private Integer tradeDirection;
    //价格
    private Double marketPrice;
    //交易时间
    private String tradeTime;
    //手续费
    private Double poundage;
    //盈亏
    private Double profit;
    //剩下手数
    private Double remainHandNumber;
    //明细id
    private Long detailId;

    @Override
    public String toString() {
        return "NetPositionDetailVo{" +
                "netPositionSum=" + netPositionSum +
                ", varietyName='" + varietyName + '\'' +
                ", openCloseType=" + openCloseType +
                ", handNumber=" + handNumber +
                ", tradeDirection=" + tradeDirection +
                ", marketPrice=" + marketPrice +
                ", tradeTime='" + tradeTime + '\'' +
                ", poundage=" + poundage +
                ", profit=" + profit +
                '}';
    }

    public Double getNetPositionSum() {
        return netPositionSum;
    }

    public void setNetPositionSum(Double netPositionSum) {
        this.netPositionSum = netPositionSum;
    }

    public String getVarietyName() {
        return varietyName;
    }

    public void setVarietyName(String varietyName) {
        this.varietyName = varietyName;
    }

    public String getOpenCloseType() {

        return openCloseType == StatusUtil.CLOSE.getIndex()? StatusUtil.CLOSE.getName() : StatusUtil.OPEN.getName();
    }

    public Double getRemainHandNumber() {
        return remainHandNumber;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public void setRemainHandNumber(Double remainHandNumber) {
        this.remainHandNumber = remainHandNumber;
    }

    public void setOpenCloseType(Integer openCloseType) {
        this.openCloseType = openCloseType;
    }

    public Double getHandNumber() {
        return handNumber;
    }

    public void setHandNumber(Double handNumber) {
        this.handNumber = handNumber;
    }

    public String getTradeDirection() {
        return tradeDirection == StatusUtil.SELL.getIndex()? "卖出" : "买入";
    }

    public void setTradeDirection(Integer tradeDirection) {
        this.tradeDirection = tradeDirection;
    }

    public Double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public Double getPoundage() {
        return poundage;
    }

    public void setPoundage(Double poundage) {
        this.poundage = poundage;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }
}
package com.web.pojo.vo.followOrder;

import com.web.common.FollowOrderEnum;

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
    private String handNumber;
    //多空方向
    private Integer tradeDirection;
    //价格
    private Double marketPrice;
    //交易时间
    private String tradeTime;
    //手续费
    private Double poundage;
    //盈亏
    private Double profit=0.0;
    //剩下手数
    private Integer remainHandNumber=0;
    //明细id
    private Long detailId;
    //客户名字
    private String userName;
    //是否跟单
    private Integer followOrderClientStatus;


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

        return openCloseType == FollowOrderEnum.FollowStatus.CLOSE.getIndex() ? FollowOrderEnum.FollowStatus.CLOSE.getName() : FollowOrderEnum.FollowStatus.OPEN.getName();
    }

    public void setOpenCloseType(Integer openCloseType) {
        this.openCloseType = openCloseType;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getTradeDirection() {
        return tradeDirection == FollowOrderEnum.FollowStatus.SELL.getIndex() ? "卖出" : "买入";
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getFollowOrderClientStatus() {
        return followOrderClientStatus;
    }

    public void setFollowOrderClientStatus(Integer followOrderClientStatus) {
        this.followOrderClientStatus = followOrderClientStatus;
    }

    public String getHandNumber() {
        return handNumber;
    }

    public void setHandNumber(String handNumber) {
        this.handNumber = handNumber;
    }

    public Integer getRemainHandNumber() {
        return remainHandNumber;
    }

    public void setRemainHandNumber(Integer remainHandNumber) {
        this.remainHandNumber = remainHandNumber;
    }
}

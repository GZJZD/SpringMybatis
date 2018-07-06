package com.web.pojo.vo;

import com.web.pojo.FollowOrder;

/**
 * 跟单映射表
 *@Author: May
 *@param
 *@Date: 10:46 2018/5/21
 */
public class FollowOrderVo {


    //策略
    private FollowOrder followOrder;

    //持仓盈亏
    private Double positionGainAndLoss=0.0;

    //平仓盈亏
    private Double offsetGainAndLoss=0.0;

    private Double offsetHandNumber;//平仓手数
    //累计盈亏:平仓盈亏+持仓盈亏
    private Double gainAndLossTotal;

    //手续费总和
    private Double poundageTotal=0.0;

    //客户盈亏
    private Double clientProfit=0.0;

    //盈亏率:总平仓盈亏除以手数
    private Double profitAndLossRate;

    //跟单成功率:成功的交易数
    private Integer successTotal;
    //全部的跟单数
    private Integer allTotal=0;
    //手数
    private Double handNumberTotal=0.0;

    /**********************************************     跟单明细中跟单数据字段     *************************************************************************/
    private String clientName;

    /**********************************************     跟单数据END     *************************************************************************/




    public FollowOrder getFollowOrder() {
        return followOrder;
    }

    public void setFollowOrder(FollowOrder followOrder) {
        this.followOrder = followOrder;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Double getPositionGainAndLoss() {
        return positionGainAndLoss;
    }

    public void setPositionGainAndLoss(Double positionGainAndLoss) {
        this.positionGainAndLoss = positionGainAndLoss;
    }

    public Double getOffsetGainAndLoss() {
        return offsetGainAndLoss;
    }

    public void setOffsetGainAndLoss(Double offsetGainAndLoss) {
        this.offsetGainAndLoss = offsetGainAndLoss;
    }

    public Double getGainAndLossTotal() {
        return gainAndLossTotal;
    }

    public void setGainAndLossTotal(Double gainAndLossTotal) {
        this.gainAndLossTotal = gainAndLossTotal;
    }

    public Double getPoundageTotal() {
        return poundageTotal;
    }

    public void setPoundageTotal(Double poundageTotal) {
        this.poundageTotal = poundageTotal;
    }

    public Double getClientProfit() {
        return clientProfit;
    }

    public void setClientProfit(Double clientProfit) {
        this.clientProfit = clientProfit;
    }

    public Double getProfitAndLossRate() {
        return profitAndLossRate;
    }

    public void setProfitAndLossRate(Double profitAndLossRate) {
        this.profitAndLossRate = profitAndLossRate;
    }

    public Integer getSuccessTotal() {
        return successTotal;
    }

    public void setSuccessTotal(Integer successTotal) {
        this.successTotal = successTotal;
    }

    public Integer getAllTotal() {
        return allTotal;
    }

    public void setAllTotal(Integer allTotal) {
        this.allTotal = allTotal;
    }

    public Double getHandNumberTotal() {
        return handNumberTotal;
    }

    public void setHandNumberTotal(Double handNumberTotal) {
        this.handNumberTotal = handNumberTotal;
    }

    public Double getOffsetHandNumber() {
        return offsetHandNumber;
    }

    public void setOffsetHandNumber(Double offsetHandNumber) {
        this.offsetHandNumber = offsetHandNumber;
    }

    @Override
    public String toString() {
        return "FollowOrderVo{" +
                "followOrder=" + followOrder +
                ", positionGainAndLoss=" + positionGainAndLoss +
                ", offsetGainAndLoss=" + offsetGainAndLoss +
                ", gainAndLossTotal=" + gainAndLossTotal +
                ", poundageTotal=" + poundageTotal +
                ", clientProfit=" + clientProfit +
                ", profitAndLossRate=" + profitAndLossRate +
                ", successTotal=" + successTotal +
                ", allTotal=" + allTotal +
                ", handNumberTotal=" + handNumberTotal +
                '}';
    }
}
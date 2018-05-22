package com.web.pojo.vo;

import com.web.pojo.FollowOrder;

/**
 * 跟单
 *@Author: May
 *@param
 *@Date: 10:46 2018/5/21
 */
public class FollowOrderVO {
    private Long id;
    //策略
    private FollowOrder tactics;

    //持仓盈亏
    private Double positionGainAndLoss;

    //平仓盈亏
    private Double offsetGainAndLoss;

    //累计盈亏:平仓盈亏+持仓盈亏
    private Double gainAndLossTotal;

    //手续费总和
    private Double poundageTotal;

    //客户盈亏
    private Double clientProfit;

    //盈亏率:总平仓盈亏除以总手续费
    private Double profitAndLossRate;

    //跟单成功率:成功/全部交易
    private String successRate;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FollowOrder getTactics() {
        return tactics;
    }

    public void setTactics(FollowOrder tactics) {
        this.tactics = tactics;
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


    public String getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(String successRate) {
        this.successRate = successRate;
    }


}
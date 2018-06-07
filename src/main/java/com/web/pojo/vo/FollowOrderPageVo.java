package com.web.pojo.vo;

//用于映射页面交易的总数据

public class FollowOrderPageVo {
    //手续费总和
    private Double poundageTotalSum=0.0;
    //持仓盈亏总和
    private Double positionGainAndLossTotalSum=0.0;
    //平仓盈亏总和
    private Double offsetGainAndLossTotalSum=0.0;
    //客户盈亏总和
    private Double clientProfitTotalSum=0.0;

    //盈亏效率:总平仓盈亏除以总手续费
    private Double profitAndLossRateTotalSum=0.0;

    public Double getPoundageTotalSum() {
        return poundageTotalSum;
    }

    public void setPoundageTotalSum(Double poundageTotalSum) {
        this.poundageTotalSum = poundageTotalSum;
    }

    public Double getPositionGainAndLossTotalSum() {
        return positionGainAndLossTotalSum;
    }

    public void setPositionGainAndLossTotalSum(Double positionGainAndLossTotalSum) {
        this.positionGainAndLossTotalSum = positionGainAndLossTotalSum;
    }

    public Double getOffsetGainAndLossTotalSum() {
        return offsetGainAndLossTotalSum;
    }

    public void setOffsetGainAndLossTotalSum(Double offsetGainAndLossTotalSum) {
        this.offsetGainAndLossTotalSum = offsetGainAndLossTotalSum;
    }

    public Double getClientProfitTotalSum() {
        return clientProfitTotalSum;
    }

    public void setClientProfitTotalSum(Double clientProfitTotalSum) {
        this.clientProfitTotalSum = clientProfitTotalSum;
    }

    public Double getProfitAndLossRateTotalSum() {
        return profitAndLossRateTotalSum;
    }

    public void setProfitAndLossRateTotalSum(Double profitAndLossRateTotalSum) {
        this.profitAndLossRateTotalSum = profitAndLossRateTotalSum;
    }
}

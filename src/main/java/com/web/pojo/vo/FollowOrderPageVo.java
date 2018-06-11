package com.web.pojo.vo;

//用于映射页面交易的总数据

public class FollowOrderPageVo {
    /***************************************     跟单头所用字段      ******************************************/
    private Double historyHandNumber; //历史跟单手数
    private Double historyProfit; //历史收益

    private Double holdPositionHandNumber; //持仓手数
    private Double holdPositionProfit; //持仓收益

    //盈亏效率:所有平仓单的平仓盈亏除以手数
    private Double profitAndLossRateTotalSum=0.0;

    //胜率：所有平仓单中盈利单数除以总平仓单数
    private Double winRate;//胜率

    /**************************************     跟单头所用字段 END     ******************************************/

    /***************************************     查询条件     ******************************************/
    private Long varietyId;//品种id
    private Long accountId; //账号ID
    private String  startTime; //起始时间
    private String endTime; //结束时间
    /***************************************     查询条件end     ******************************************/

    //手续费总和
    private Double poundageTotalSum=0.0;
    //持仓盈亏总和
    private Double positionGainAndLossTotalSum=0.0;
    //平仓盈亏总和
    private Double offsetGainAndLossTotalSum=0.0;
    //客户盈亏总和
    private Double clientProfitTotalSum=0.0;

    public Double getHistoryHandNumber() {
        return historyHandNumber;
    }

    public void setHistoryHandNumber(Double historyHandNumber) {
        this.historyHandNumber = historyHandNumber;
    }

    public Double getHistoryProfit() {
        return historyProfit;
    }

    public void setHistoryProfit(Double historyProfit) {
        this.historyProfit = historyProfit;
    }

    public Double getHoldPositionHandNumber() {
        return holdPositionHandNumber;
    }

    public void setHoldPositionHandNumber(Double holdPositionHandNumber) {
        this.holdPositionHandNumber = holdPositionHandNumber;
    }

    public Double getHoldPositionProfit() {
        return holdPositionProfit;
    }

    public void setHoldPositionProfit(Double holdPositionProfit) {
        this.holdPositionProfit = holdPositionProfit;
    }

    public Double getProfitAndLossRateTotalSum() {
        return profitAndLossRateTotalSum;
    }

    public void setProfitAndLossRateTotalSum(Double profitAndLossRateTotalSum) {
        this.profitAndLossRateTotalSum = profitAndLossRateTotalSum;
    }

    public Double getWinRate() {
        return winRate;
    }

    public void setWinRate(Double winRate) {
        this.winRate = winRate;
    }

    public Long getVarietyId() {
        return varietyId;
    }

    public void setVarietyId(Long varietyId) {
        this.varietyId = varietyId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

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
}
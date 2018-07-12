package com.web.pojo.vo;

//用于映射页面交易的总数据

import java.util.List;

public class FollowOrderPageVo {

    private List<FollowOrderVo> followOrderVoList;
    /***************************************     跟单头所用字段      ******************************************/
    private Integer historyHandNumber=0; //历史平仓跟单手数
    private Double historyProfit=0.0; //历史平仓收益

    private Integer holdPositionHandNumber=0; //持仓手数
    private Double holdPositionProfit=0.0; //持仓收益

    //盈亏效率:所有平仓单的平仓盈亏除以平仓手数
    private Double profitAndLossRate=0.0;

    //胜率：所有平仓单中盈利单数除以总平仓单数
    private Double winRate=0.0;






    //总平仓单数
    private Integer closePositionTotalNumber=0;
    //盈利单数
    private Integer closePositionWinSum=0;

    /**************************************     跟单头所用字段 END     ******************************************/


    public List<FollowOrderVo> getFollowOrderVoList() {
        return followOrderVoList;
    }

    public void setFollowOrderVoList(List<FollowOrderVo> followOrderVoList) {
        this.followOrderVoList = followOrderVoList;
    }

    public Integer getHistoryHandNumber() {
        return historyHandNumber;
    }

    public void setHistoryHandNumber(Integer historyHandNumber) {
        this.historyHandNumber = historyHandNumber;
    }

    public Integer getHoldPositionHandNumber() {
        return holdPositionHandNumber;
    }

    public void setHoldPositionHandNumber(Integer holdPositionHandNumber) {
        this.holdPositionHandNumber = holdPositionHandNumber;
    }

    public Double getHistoryProfit() {
        return historyProfit;
    }

    public void setHistoryProfit(Double historyProfit) {
        this.historyProfit = historyProfit;
    }



    public Double getHoldPositionProfit() {
        return holdPositionProfit;
    }

    public void setHoldPositionProfit(Double holdPositionProfit) {
        this.holdPositionProfit = holdPositionProfit;
    }

    public Double getProfitAndLossRate() {
        return profitAndLossRate;
    }

    public void setProfitAndLossRate(Double profitAndLossRate) {
        this.profitAndLossRate = profitAndLossRate;
    }

    public Double getWinRate() {
        return winRate;
    }

    public void setWinRate(Double winRate) {
        this.winRate = winRate;
    }

    public Integer getClosePositionTotalNumber() {
        return closePositionTotalNumber;
    }

    public void setClosePositionTotalNumber(Integer closePositionTotalNumber) {
        this.closePositionTotalNumber = closePositionTotalNumber;
    }

    public Integer getClosePositionWinSum() {
        return closePositionWinSum;
    }

    public void setClosePositionWinSum(Integer closePositionWinSum) {
        this.closePositionWinSum = closePositionWinSum;
    }
}

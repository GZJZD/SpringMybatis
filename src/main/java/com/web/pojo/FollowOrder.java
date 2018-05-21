package com.web.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 跟单
 *@Author: May
 *@param
 *@Date: 10:46 2018/5/21
 */
public class FollowOrder implements Serializable {
    private Long id;
    //策略
    private Tactics tactics;
    //状态:跟单启用、暂停、停用
    private Integer followOrderStatus;
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
    //开始时间
    private Date startTime;
    //跟单成功率:成功/全部交易
    private String successRate;
    //创建人
    private Long documentary_id;
    //修改时间
    private Date updateTime;
    //修改人
    private Long update_documentary_id;
    //版本
    private Integer version=0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tactics getTactics() {
        return tactics;
    }

    public void setTactics(Tactics tactics) {
        this.tactics = tactics;
    }

    public Integer getFollowOrderStatus() {
        return followOrderStatus;
    }

    public void setFollowOrderStatus(Integer followOrderStatus) {
        this.followOrderStatus = followOrderStatus;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(String successRate) {
        this.successRate = successRate;
    }

    public Long getDocumentary_id() {
        return documentary_id;
    }

    public void setDocumentary_id(Long documentary_id) {
        this.documentary_id = documentary_id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdate_documentary_id() {
        return update_documentary_id;
    }

    public void setUpdate_documentary_id(Long update_documentary_id) {
        this.update_documentary_id = update_documentary_id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
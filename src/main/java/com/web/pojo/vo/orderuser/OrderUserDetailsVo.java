package com.web.pojo.vo.orderuser;

import com.web.pojo.OrderUser;

import java.util.List;

/**
 * 用户明细Vo映射类
 */
public class OrderUserDetailsVo {
    private String platFormCode;//平台名称
    private String  loginTime; //注册时间
    private String lastOrderTime;//最近下单时间
    private String agencyName;//代理人
    private Integer doOrderDays;//做单天数
    private Long countNumber;//做单数
    private Double winRate;//胜率
    private Double cmmission;//手续费
    private Double inMoney;//入金
    private Double outMoney;//出金
    private Double remainMoney;//账户余额
    private String createTime;//创建时间
    private Double position_gain_and_loss;//持仓盈亏
    private Double offset_gain_and_loss;//平仓盈亏
    private Double everyHandNumber;//每单手数
    private List<HoldOrderUserVo>holdList;//持仓记录
    private List<OrderUser>profitList; //平仓记录
    private Long lossNumber ; //亏损次数
    private Long  profitNumber; //盈利次数
    private Double profitVal; //盈利值
    private  Double lossVal;//亏损值
    private Double handNumber;// 手数学
    private Double profitAndLossRatio; //盈亏比
    private Double profitAndLossEfficiency ;// 盈亏效率


    public Double getProfitAndLossEfficiency() {
        return profitAndLossEfficiency;
    }

    public void setProfitAndLossEfficiency(Double profitAndLossEfficiency) {
        this.profitAndLossEfficiency = profitAndLossEfficiency;
    }

    public Double getProfitAndLossRatio() {
        return profitAndLossRatio;
    }

    public void setProfitAndLossRatio(Double profitAndLossRatio) {
        this.profitAndLossRatio = profitAndLossRatio;
    }

    public Double getProfitVal() {
        return profitVal;
    }

    public void setProfitVal(Double profitVal) {
        this.profitVal = profitVal;
    }

    public Double getLossVal() {
        return lossVal;
    }

    public void setLossVal(Double lossVal) {
        this.lossVal = lossVal;
    }

    public Double getHandNumber() {
        return handNumber;
    }

    public void setHandNumber(Double handNumber) {
        this.handNumber = handNumber;
    }

    public Long getLossNumber() {
        return lossNumber;
    }

    public void setLossNumber(Long lossNumber) {
        this.lossNumber = lossNumber;
    }

    public Long getProfitNumber() {
        return profitNumber;
    }

    public void setProfitNumber(Long profitNumber) {
        this.profitNumber = profitNumber;
    }

    public Long getCountNumber() {
        return countNumber;
    }

    public void setCountNumber(Long countNumber) {
        this.countNumber = countNumber;
    }

    public Double getPosition_gain_and_loss() {
        return position_gain_and_loss;
    }

    public void setPosition_gain_and_loss(Double position_gain_and_loss) {
        this.position_gain_and_loss = position_gain_and_loss;
    }

    public Double getOffset_gain_and_loss() {
        return offset_gain_and_loss;
    }

    public void setOffset_gain_and_loss(Double offset_gain_and_loss) {
        this.offset_gain_and_loss = offset_gain_and_loss;
    }

    public String getPlatFormCode() {
        return platFormCode;
    }

    public void setPlatFormCode(String platFormCode) {
        this.platFormCode = platFormCode;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getLastOrderTime() {
        return lastOrderTime;
    }

    public void setLastOrderTime(String lastOrderTime) {
        this.lastOrderTime = lastOrderTime;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public Integer getDoOrderDays() {
        return doOrderDays;
    }

    public void setDoOrderDays(Integer doOrderDays) {
        this.doOrderDays = doOrderDays;
    }

    public Double getWinRate() {
        return winRate;
    }

    public void setWinRate(Double winRate) {
        this.winRate = winRate;
    }

    public Double getCmmission() {
        return cmmission;
    }

    public void setCmmission(Double cmmission) {
        this.cmmission = cmmission;
    }

    public Double getInMoney() {
        return inMoney;
    }

    public void setInMoney(Double inMoney) {
        this.inMoney = inMoney;
    }

    public Double getOutMoney() {
        return outMoney;
    }

    public void setOutMoney(Double outMoney) {
        this.outMoney = outMoney;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<HoldOrderUserVo> getHoldList() {
        return holdList;
    }

    public void setHoldList(List<HoldOrderUserVo> holdList) {
        this.holdList = holdList;
    }

    public List<OrderUser> getProfitList() {
        return profitList;
    }

    public void setProfitList(List<OrderUser> profitList) {
        this.profitList = profitList;
    }

    public Double getRemainMoney() {
        return remainMoney;
    }

    public void setRemainMoney(Double remainMoney) {
        this.remainMoney = remainMoney;
    }

    public Double getEveryHandNumber() {
        return everyHandNumber;
    }

    public void setEveryHandNumber(Double everyHandNumber) {
        this.everyHandNumber = everyHandNumber;
    }
}

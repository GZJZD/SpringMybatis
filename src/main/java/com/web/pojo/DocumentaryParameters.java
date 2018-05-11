package com.web.pojo;



import java.io.Serializable;

/**
 * 平台跟单参数
 * @author may
 *
 */

public class DocumentaryParameters implements Serializable {
    public static final int ORDERPOINT_NO = 0;//限价
    public static final int ORDERPOINT_YES = 1;//市价
     public static final int TRADE_STATUS_NO = 0;//正在交易
    public static final int TRADE_STATUS_YES = 1;//交易暂停
    public static final Boolean STRATEGY_TYPE_NO = false;//反向跟单
    public static final Boolean STRATEGY_TYPE_YES = true;//正向跟单

    private Long id;
    //跟单类型[正/反],真为正向跟单
    private Boolean type;
    //净头寸变化,
    private Integer changeNum;
    //跟多少手
    private Integer followNum;
    //下单点位 1:市价，0表示：限价
    private Integer orderPoint;
    //下单点位：比客户好
    private Boolean customerPoint;
    //下单点位：点数
    private Double customerPointNum;
    //最大止盈
    private Integer bigCheckSurplus;
    //最大止损
    private Integer bigStopLoss;
    //账户止损
    private Integer accountStopLoss;
    //当前净头寸的值
    private Double headNum;
    //持仓的值
    private Integer holdNum;
    //判断是否正在交易
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public Integer getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(Integer changeNum) {
        this.changeNum = changeNum;
    }

    public Integer getFollowNum() {
        return followNum;
    }

    public void setFollowNum(Integer followNum) {
        this.followNum = followNum;
    }

    public Integer getOrderPoint() {
        return orderPoint;
    }

    public void setOrderPoint(Integer orderPoint) {
        this.orderPoint = orderPoint;
    }

    public Boolean getCustomerPoint() {
        return customerPoint;
    }

    public void setCustomerPoint(Boolean customerPoint) {
        this.customerPoint = customerPoint;
    }

    public Double getCustomerPointNum() {
        return customerPointNum;
    }

    public void setCustomerPointNum(Double customerPointNum) {
        this.customerPointNum = customerPointNum;
    }

    public Integer getBigCheckSurplus() {
        return bigCheckSurplus;
    }

    public void setBigCheckSurplus(Integer bigCheckSurplus) {
        this.bigCheckSurplus = bigCheckSurplus;
    }

    public Integer getBigStopLoss() {
        return bigStopLoss;
    }

    public void setBigStopLoss(Integer bigStopLoss) {
        this.bigStopLoss = bigStopLoss;
    }

    public Integer getAccountStopLoss() {
        return accountStopLoss;
    }

    public void setAccountStopLoss(Integer accountStopLoss) {
        this.accountStopLoss = accountStopLoss;
    }

    public Double getHeadNum() {
        return headNum;
    }

    public void setHeadNum(Double headNum) {
        this.headNum = headNum;
    }

    public Integer getHoldNum() {
        return holdNum;
    }

    public void setHoldNum(Integer holdNum) {
        this.holdNum = holdNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public DocumentaryParameters(Long id, Boolean type, Integer changeNum, Integer followNum,
                                 Integer orderPoint, Boolean customerPoint, Double customerPointNum,
                                 Integer bigCheckSurplus, Integer bigStopLoss, Integer accountStopLoss,
                                 Double headNum, Integer holdNum, Integer status) {
        this.id = id;
        this.type = type;
        this.changeNum = changeNum;
        this.followNum = followNum;
        this.orderPoint = orderPoint;
        this.customerPoint = customerPoint;
        this.customerPointNum = customerPointNum;
        this.bigCheckSurplus = bigCheckSurplus;
        this.bigStopLoss = bigStopLoss;
        this.accountStopLoss = accountStopLoss;
        this.headNum = headNum;
        this.holdNum = holdNum;
        this.status = status;
    }
}
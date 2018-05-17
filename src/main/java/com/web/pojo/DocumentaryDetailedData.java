package com.web.pojo;

import java.util.Date;

/**
 * Created by may on 2018/4/23.
 * 跟单明细数据
 */
public class DocumentaryDetailedData {
    public static final int DETAILED_STATUS_NO=0;//跟单失败
    public static final int DETAILED_STATUS_YES=1;//跟单成功
    private Long id;
    //用户名or净头寸
    private String login;
    //品种
    private String varietyCode;
    //和TCP数据一样，开仓单号，唯一标识,通过这个唯一标识知道是哪个客户数据
  //  private String openOrderNum;
    //交易单号
   // private String tradeNum;
    //多空(0=多,1=空),类型
    private Integer cmd;
    //手数
    private Double counts;
    //开仓价格
    private Double openPrice;
    //开仓时间
    private Date openTime;
    //平仓时间
    private Double closePrice;
    //平仓时间
    private Date closeTime;
    //手续费
    private Double poundage;
    //跟单是否成功
    private Integer status;
    //策略的id,品种从策略里面取出,后期数据库进行关联
    // private Tactics Tactics;
    //剩余的手数
    private Double leftoverCounts;
    //已经平仓过的盈亏,要等交易系统把价格传过来才知道
    private Double alreadyProfit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getVarietyCode() {
        return varietyCode;
    }

    public void setVarietyCode(String varietyCode) {
        this.varietyCode = varietyCode;
    }

    public Integer getCmd() {
        return cmd;
    }

    public void setCmd(Integer cmd) {
        this.cmd = cmd;
    }

    public Double getCounts() {
        return counts;
    }

    public void setCounts(Double counts) {
        this.counts = counts;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Double getPoundage() {
        return poundage;
    }

    public void setPoundage(Double poundage) {
        this.poundage = poundage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getLeftoverCounts() {
        return leftoverCounts;
    }

    public void setLeftoverCounts(Double leftoverCounts) {
        this.leftoverCounts = leftoverCounts;
    }

    public Double getAlreadyProfit() {
        return alreadyProfit;
    }

    public void setAlreadyProfit(Double alreadyProfit) {
        this.alreadyProfit = alreadyProfit;
    }
}

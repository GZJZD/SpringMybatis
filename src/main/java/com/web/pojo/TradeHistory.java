package com.web.pojo;

import java.util.Date;

/**
 * 交易信息历史
 *@Author: May
 *@param
 *@Date: 12:13 2018/5/11
 */
public class TradeHistory {
    private Long id;
    //商品
    private String varietyCode;
    //开平
    private Integer openClose;
    //多空
    private Integer cmd;
    //手数
    private Double counts;
    //价格
    private Double price;
    //交易时间
    private Date createTime;
    //手续费
    private Double poundage;
    //交易是否成功
    private Integer status;
    //TCP的新开仓单号
    private String newOpenOrderNum;
    //版本
    private Date version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVarietyCode() {
        return varietyCode;
    }

    public void setVarietyCode(String varietyCode) {
        this.varietyCode = varietyCode;
    }

    public Integer getOpenClose() {
        return openClose;
    }

    public void setOpenClose(Integer openClose) {
        this.openClose = openClose;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getNewOpenOrderNum() {
        return newOpenOrderNum;
    }

    public void setNewOpenOrderNum(String newOpenOrderNum) {
        this.newOpenOrderNum = newOpenOrderNum;
    }

    public Date getVersion() {
        return version;
    }

    public void setVersion(Date version) {
        this.version = version;
    }

}
package com.web.pojo.vo;

public class FiltrateParameter {
    /********************************
     * 过滤条件所用字段               *
     * ******************************/
    /**
     * 启始时间
     */
    private  String startTime;

    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 合约
     */
    private String contract;
    /**
     * 商品
     */
    private String productCode;

    public FiltrateParameter() {

    }

    public FiltrateParameter(String startTime, String endTime, String contract, String productCode) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.contract = contract;
        this.productCode = productCode;
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

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}

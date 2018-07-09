package com.web.datebase.entity;

/**
 * 用于存放 各个平台的用户数据
 */
public class PlatFromUsers {
    /**
     * 登录id
     */
    private  Long LOGIN;
    /**
     * 注册时间
     */
    private String REGDATE;
    /**
     *代理人
     */
    private String AGENT;
    /**
     * 姓名
     */
    private String NAME;
    /**
     * 入金
     */
    private Double DEPOSIT;
    /**
     * 出金
     */
    private Double WITHDRAWAL;
    /**
     *余额
     */
    private Double BALANCE;


    public Long getLOGIN() {
        return LOGIN;
    }

    public void setLOGIN(Long LOGIN) {
        this.LOGIN = LOGIN;
    }

    public String getREGDATE() {
        return REGDATE;
    }

    public void setREGDATE(String REGDATE) {
        this.REGDATE = REGDATE;
    }

    public String getAGENT() {
        return AGENT;
    }

    public void setAGENT(String AGENT) {
        this.AGENT = AGENT;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Double getDEPOSIT() {
        return DEPOSIT;
    }

    public void setDEPOSIT(Double DEPOSIT) {
        this.DEPOSIT = DEPOSIT;
    }

    public Double getWITHDRAWAL() {
        return WITHDRAWAL;
    }

    public void setWITHDRAWAL(Double WITHDRAWAL) {
        this.WITHDRAWAL = WITHDRAWAL;
    }

    public Double getBALANCE() {
        return BALANCE;
    }

    public void setBALANCE(Double BALANCE) {
        this.BALANCE = BALANCE;
    }
}

package com.web.pojo;

/**
 * 客户列表
 */
public class OrderUser {

    /**
     * 用户编号
     */
    private String userCode;

    /**
     * 平台
     */
    private Integer platformCode;

    /**
     * 代理人
     */
    private String agencyName;



    /**
     * 持仓数
     */
    private Integer tackeNumber;
    /**
     * 平仓盈亏
     */
    private String closeGainLoss;
    /**
     * 品种code
     */
    private Integer produceCode;

    /**
     * 开仓时间
     */
    private String openTime;

    /**
     * 平仓时间
     */
    private String  closeTime;

    /**
     * 入金
     */
    private Double inMoney;
    /**
     * 出金
     */
    private Double outMoney;

}

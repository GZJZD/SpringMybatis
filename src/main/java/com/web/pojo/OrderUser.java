package com.web.pojo;

import com.web.util.BaseUtil;

/**
 * 客户列表
 */
public class OrderUser  extends BaseUtil {

    /**
     * 用户编号
     */
    private Long userCode;

    /**
     * 平台
     */
    private Long platformCode;

    /**
     * 代理人
     */
    private String agencyName;

    /**
     * 持仓数
     */
    private Long holdNumber;
    /**
     * 平仓盈亏
     */
    private Double closeGainLoss;
    /**
     * 品种
     */
    private Long produceCode;

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


    /**
     * 手数
     */
    private Double handNumber;
    /**
     * 价位
     */
    private Double price;

}

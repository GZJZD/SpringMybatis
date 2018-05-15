package com.web.service;


import com.web.pojo.DealData;
import com.web.pojo.Strategy;
import com.web.pojo.vo.OrderDealData;

/**
 * 净头寸的策略判断
 * Created by may on 2018/5/8.
 */
public interface ICheckStrategyService {
    /** 返回策略
     *@Author: May
     *@param
     *@Date: 12:48 2018/5/8
     */
     Strategy getStrategy();

    /**
     * 判断下单条件
     *@Author: May
     *@param data
     *@Date: 14:24 2018/4/23
     */
    public void judgingOrderConditions(DealData data);
}

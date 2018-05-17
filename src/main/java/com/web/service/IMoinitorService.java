package com.web.service;


import com.web.pojo.DataSource;
import com.web.pojo.Tactics;
import com.web.pojo.vo.OrderParameter;

/**
 * 跟单模块
 * Created by may on 2018/5/8.
 */
public interface IMoinitorService {
    /** 返回策略
     *@Author: May
     *@param
     *@Date: 12:48 2018/5/8
     */
     Tactics getTactics();

    /**
     * 实现交易逻辑
     *@Author: May
     *@param data
     *@Date: 14:24 2018/4/23
     */
    public void madeAnOrder(DataSource data);
}

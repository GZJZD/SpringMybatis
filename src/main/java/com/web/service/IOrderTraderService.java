package com.web.service;


import com.web.pojo.Account;
import com.web.pojo.vo.OrderParameter;

/**
 * 此类用于发送消息到交易系统
 * Created by may on 2018/4/25.
 */
public interface IOrderTraderService {

    /**
     * 发送登录信息
     *@Author: May
     *@param
     *@Date: 14:14 2018/4/25
     */
     void login(Account account);

    /**
     * 删除交易账号时，发送注销
     *@Author: May
     *@param
     *@Date: 14:15 2018/4/25
     */
     void loginOut(Account account);

    /**
     *保单
     *@Author: May
     *@param
     *@Date: 14:19 2018/4/25
     */
     void addOrder(OrderParameter orderParameter);

    /**
     * 撤单
     *@Author: May
     *@param
     *@Date: 14:23 2018/4/25
     */
     void cancelOrder();

}

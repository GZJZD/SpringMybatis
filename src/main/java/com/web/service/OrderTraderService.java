package com.web.service;


import com.web.pojo.vo.OrderTrade;
import com.web.pojo.vo.UserLogin;

/**
 * 此类用于发送消息到交易系统
 * Created by may on 2018/4/25.
 */
public interface OrderTraderService {

    /**
     * 发送登录信息
     *@Author: May
     *@param
     *@Date: 14:14 2018/4/25
     */
     void userLogin(UserLogin userLogin);

    /**
     * 删除交易账号时，发送注销
     *@Author: May
     *@param
     *@Date: 14:15 2018/4/25
     */
     void userLogout(UserLogin userLogin);

    /**
     * 开仓
     *@Author: May
     *@param
     *@Date: 14:19 2018/4/25
     */
     void orderOpen(OrderTrade orderTrade);

    /**
     * 平仓
     *@Author: May
     *@param
     *@Date: 14:23 2018/4/25
     */
     void orderClose(OrderTrade orderTrade);

}

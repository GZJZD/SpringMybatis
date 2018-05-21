package com.web.service;


import com.web.pojo.DataSource;
import com.web.pojo.FollowOrder;
import com.web.pojo.Tactics;
import com.web.pojo.vo.OrderParameter;

import java.util.List;

/**
 * 跟单模块
 * Created by may on 2018/5/8.
 */
public interface IFollowOrderService {


    void save(FollowOrder followOrder);
    /** 返回跟单集合
     *@Author: May
     *@param
     *@Date: 12:48 2018/5/8
     */
     FollowOrder getListFollowOrder();
    /** 返回跟单
     *@Author: May
     *@param
     *@Date: 12:48 2018/5/8
     */
     FollowOrder getFollowOrder(Long id);

    /**
     * 实现交易逻辑
     *@Author: May
     *@param data
     *@Date: 14:24 2018/4/23
     */
    void madeAnOrder(DataSource data);
    /**判断该账号是否登录了
     *@Author: May
     *@param
     *@Date: 12:12 2zero18/5/1zero
     */
    void checkLogin(Tactics tactics, FollowOrder followOrder);


}

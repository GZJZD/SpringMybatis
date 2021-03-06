package com.web.service;


import com.web.pojo.DataSource;
import com.web.pojo.FollowOrder;
import com.web.pojo.FollowOrderClient;
import com.web.pojo.FollowOrderTradeRecord;
import com.web.pojo.vo.followOrder.FollowOrderPageVo;
import com.web.pojo.vo.followOrder.FollowOrderQuery;

import java.util.List;


/**
 * 跟单模块
 * Created by may on 2018/5/8.
 */
public interface FollowOrderService {


    void save(FollowOrder followOrder);
    void updateFollowOrder(FollowOrder followOrder);

    //修改跟单的状态
    void updateFollowOrderStatus(Long followOrderId,Integer status);


    FollowOrder getFollowOrder(Long id);

    FollowOrderPageVo getListFollowOrderVo( FollowOrderQuery followOrderQuery);

    void madeAnOrder(DataSource data);


    void checkLogin( FollowOrder followOrder);

    void createFollowOrder(FollowOrder followOrder, List<FollowOrderClient> followOrderClients);

    //设置持仓值
    void updateHoldNumByTradeAndFollowOrder(FollowOrder followOrder, FollowOrderTradeRecord followOrderTradeRecord);


    void closeAllOrderByFollowOrderId(Long followOrderId);

    void manuallyClosePosition(Long detailId);


    List<FollowOrder> selectListFollowOrder(FollowOrderQuery followOrderQuery);
    int findAccountStatusByAccountId(Long accountId);
    List<FollowOrder> getNOStopFollowOrder();

}

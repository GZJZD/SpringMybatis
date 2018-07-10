package com.web.util;

import com.web.common.FollowOrderEnum;
import com.web.pojo.*;
import com.web.service.FollowOrderService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 初始化死策略
 * Created by may on 2018/5/11.
 */
public  class FollowOrderGenerateUtil {
    private static FollowOrder followOrder = new FollowOrder();
    @Autowired
    private FollowOrderService
            followOrderService = (FollowOrderService) ApplicationContextHolder
            .getBeanByName("followOrderServiceImpl");
    /**
     * 初始化策略
     *
     * @param
     * @Author: May
     * @Date: 10:21 2018/5/9
     */

    public  FollowOrder getFollowOrder() {

        followOrder.setAccount(FollowOrderGenerateUtil.getAccount());
        //设计品种
        Variety variety = new Variety(1L, "黄金", "XAUUSD.e", "NYMEX");
        followOrder.setVariety(variety);
        followOrder.setVersion(0);
        //设计状态
        //  Tactics.setStatus(FollowOrderEnum.FollowStatus.Tactics_STOP.getIndex());
        //设计策略的跟单参数,反向跟单,净头寸每变化10手跟1手
        followOrder.setFollowManner(FollowOrderEnum.FollowStatus.FOLLOWMANNER_NET_POSITION.getIndex());
        followOrder.setMaxLoss(1);
        followOrder.setMaxProfit(1);
        followOrder.setAccountLoss(1);
        followOrder.setOrderPoint(1);
        followOrder.setNetPositionDirection(FollowOrderEnum.FollowStatus.DIRECTION_REVERSE.getIndex());
        followOrder.setNetPositionChange(10);
        followOrder.setNetPositionFollowNumber(1);
        followOrder.setNetPositionSum(0.0);
        followOrder.setNetPositionHoldNumber(0);
        followOrder.setFollowOrderName("净头寸初始化策略");
        followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_PAUSE.getIndex());
        followOrder.setFollowOrderStatus(FollowOrderEnum.FollowStatus.FOLLOW_ORDER_STOP.getIndex());
        followOrderService.save(followOrder);
        followOrderService.checkLogin(followOrder);

        return followOrder;
    }

    public static Account getAccount() {
        Platform tradePlatform = new Platform(1L, "8001");
        Account account = new Account(1L, "text1", "text1", tradePlatform);
        return account;
    }


}

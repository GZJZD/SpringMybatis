package com.web.util;

import com.web.pojo.*;
import com.web.service.IFollowOrderService;
import com.web.service.imp.FollowOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 初始化死策略
 * Created by may on 2018/5/11.
 */
public abstract class FollowOrderGenerateUtil {
    private static FollowOrder followOrder = new FollowOrder();

    /**
     * 初始化策略
     *
     * @param
     * @Author: May
     * @Date: 10:21 2018/5/9
     */

    public static FollowOrder getFollowOrder() {

        followOrder.setAccount(FollowOrderGenerateUtil.getAccount());
        //设计品种
        Variety variety = new Variety(1L, "黄金", "XAUUSD.e", "NYMEX");
        followOrder.setVariety(variety);
        followOrder.setVersion(0);
        //设计状态
        //  Tactics.setStatus(StatusUtil.Tactics_STOP.getIndex());
        //设计策略的跟单参数,反向跟单,净头寸每变化10手跟1手
        followOrder.setFollowManner(StatusUtil.FOLLOWMANNER_NET_POSITION.getIndex());
        followOrder.setNetPositionDirection(StatusUtil.DIRECTION_REVERSE.getIndex());
        followOrder.setNetPositionChange(10);
        followOrder.setNetPositionFollowNumber(1);
        followOrder.setNetPositionSum(0.0);
        followOrder.setNetPositionHoldNumber(0);
        followOrder.setFollowOrderName("净头寸初始化策略");
        followOrder.setNetPositionStatus(StatusUtil.TRADING_PAUSE.getIndex());
        followOrder.setFollowOrderStatus(StatusUtil.FOLLOW_ORDER_STOP.getIndex());
        return followOrder;
    }

    public static Account getAccount() {
        TradePlatform tradePlatform = new TradePlatform(1L, "EF");
        Account account = new Account(1L, "xiao", "123", tradePlatform);
        return account;
    }


}

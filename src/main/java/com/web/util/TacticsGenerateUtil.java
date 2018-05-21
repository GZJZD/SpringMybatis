package com.web.util;

import com.web.pojo.*;

/**
 * 初始化死策略
 * Created by may on 2018/5/11.
 */
public abstract class TacticsGenerateUtil {
    /**
     * 初始化策略
     *@Author: May
     *@param
     *@Date: 10:21 2018/5/9
     */
    public static Tactics getTactics(){
        Tactics tactics =new Tactics();
        tactics.setId(1L);

        TradePlatform tradePlatform = new TradePlatform(1L,"EF");
        //设计交易账号
        Account account = new Account(1L,"xiao","123",tradePlatform);
        tactics.setAccount(account);
        //设计品种
        Variety variety=new Variety(1L,"黄金","XAUUSD.e");
        tactics.setVariety(variety);
        tactics.setVersion(0);
        //设计状态
      //  Tactics.setStatus(StatusUtil.Tactics_STOP.getIndex());
        //设计策略的跟单参数,反向跟单,净头寸每变化10手跟1手
        tactics.setFollowManner(StatusUtil.FOLLOWMANNER_NET_POSITION.getIndex());
        tactics.setNetPositionDirection(StatusUtil.DIRECTION_REVERSE.getIndex());
        tactics.setNetPositionChange(10);
        tactics.setNetPositionFollowNumber(1);
        tactics.setNetPositionSum(0.0);
        tactics.setNetPositionHoldNumber(0);
        tactics.setTacticsName("净头寸初始化策略");
        tactics.setNetPositionStatus(StatusUtil.TRADING_PAUSE.getIndex());
        return tactics;
    }
    public static FollowOrder getFollowOrder(){
        FollowOrder followOrder = new FollowOrder();
        Tactics tactics = TacticsGenerateUtil.getTactics();
        followOrder.setTactics(tactics);
        followOrder.setId(1L);
        followOrder.setFollowOrderStatus(StatusUtil.FOLLOW_ORDER_STOP.getIndex());
        return followOrder;
    }
}

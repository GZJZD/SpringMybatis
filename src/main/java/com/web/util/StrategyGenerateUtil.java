package com.web.util;

import com.web.pojo.*;

/**
 * 初始化死策略
 * Created by may on 2018/5/11.
 */
public abstract class StrategyGenerateUtil {
    /**
     * 初始化策略
     *@Author: May
     *@param
     *@Date: 10:21 2018/5/9
     */
    public static Strategy getStrategy(){
        Strategy strategy =new Strategy();
        strategy.setId(1L);
        CustomerPlatform customerPlatform = new CustomerPlatform(1L,"DZ");
        strategy.setCustomerPlatform(customerPlatform);
        TradePlatform tradePlatform = new TradePlatform(1L,"EF");
        //设计交易账号
        Account account = new Account(1L,"xiao","123",tradePlatform);
        strategy.setAccount(account);
        //设计品种
        Variety variety=new Variety(1L,"黄金","XAUUSD.e");
        strategy.setVariety(variety);
        //设计状态
        strategy.setStatus(Strategy.STRATEGY_STATUS_NO);
        //设计平台的跟单参数,反向跟单,净头寸每变化10手跟1手
        DocumentaryParameters documentaryParameters = new DocumentaryParameters(1L,DocumentaryParameters.
                STRATEGY_TYPE_NO,10,1,1,null,null,0,0,0,-20.0,2,DocumentaryParameters.TRADE_STATUS_YES);
        strategy.setDocumentaryParameters(documentaryParameters);

        return strategy;
    }
}

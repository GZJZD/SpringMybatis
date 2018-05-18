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
        Tactics Tactics =new Tactics();
        Tactics.setId(1L);

        TradePlatform tradePlatform = new TradePlatform(1L,"EF");
        //设计交易账号
        Account account = new Account(1L,"xiao","123",tradePlatform);
        Tactics.setAccount(account);
        //设计品种
        Variety variety=new Variety(1L,"黄金","XAUUSD.e");
        Tactics.setVariety(variety);
        //设计状态
        Tactics.setStatus(StatusUtil.Tactics_STOP.getIndex());
        //设计平台的跟单参数,反向跟单,净头寸每变化10手跟1手
        DocumentaryParameters documentaryParameters = new DocumentaryParameters(1L,StatusUtil.DIRECTION_REVERSE.getIndex(),10,1,1,
                null,null,0,0,0,-20.0,2,StatusUtil.TRADING_PAUSE.getIndex());
        Tactics.setDocumentaryParameters(documentaryParameters);

        return Tactics;
    }
}

package com.web.service;

import com.web.pojo.FollowOrderTradeRecord;

/**
 * Created by may on 2018/5/20.
 */
public interface IFollowOrderTradeRecordService {

    void save(FollowOrderTradeRecord followOrderTradeRecord);

    FollowOrderTradeRecord getTradeRecord(Long id);

    void updateTradeRecord(FollowOrderTradeRecord followOrderTradeRecord);


    /**
     * 返回的交易信息，修改交易记录
     *@Author: May
     *@param msg
     *@Date: 16:38 2018/5/22
     */
    void updateRecordByComeBackTradeMsg(String msg);

    /**
     * 通过交易返回的对接号找到对应的交易记录
     *@Author: May
     *@param
     *@Date: 16:45 2018/5/22
     */
    FollowOrderTradeRecord getTradeRecordBySignalNumber(String signalNumber);

}

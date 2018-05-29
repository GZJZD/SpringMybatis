package com.web.service;

import com.web.pojo.FollowOrderTradeRecord;
import com.web.pojo.vo.OrderMsgResult;

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
     *@param orderMsgResult
     *@Date: 16:38 2018/5/22
     */
    void updateRecordByComeBackTradeMsg(OrderMsgResult orderMsgResult);


    /*
     *
     *  找到对应跟单的总交易数量
     * @author may
     * @date 2018/5/25 15:44
     * @param   followOrderId
     * @return
     */
    int getFollowOrderTotalAmount(Long followOrderId);
    /*
     *
     *   找到对应跟单的成功交易数量
     * @author may
     * @date 2018/5/25 15:50
     * @param
     * @return
     */
    int getFollowOrderSuccessTotalAmount(Long followOrderId);

}

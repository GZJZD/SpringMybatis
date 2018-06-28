package com.web.service;

import com.web.pojo.FollowOrderTradeRecord;
import com.web.pojo.vo.NetPositionDetailVo;
import com.web.pojo.vo.OrderMsgResult;

import java.util.List;
import java.util.Map;

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

    /*
     * 找到对应的跟单记录
     * @date 2018/6/19 14:48
     * @param  ticket：客户ticket
     *  @param  time：客户的交易时间
     * @return
     */
    List<FollowOrderTradeRecord> getListOrderTradeByTicketAndTime(String ticket,String time);

    /*
     * 获取对应跟单的客户数据
     * @param
     * @return
     */
    List<NetPositionDetailVo> getListClientNetPosition(Long followOrderId, Integer status, String clientName, Integer openOrCloseStatus);


    List<Map<String, Object>> getListClient(Long followOrderId, Integer status, String clientName, Integer openOrCloseStatus);

    List<?> getListClientFollowOrderTrade(String endTime, String startTime, Long followOrderId);
}

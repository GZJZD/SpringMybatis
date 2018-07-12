package com.web.service;

import com.web.pojo.FollowOrderDetail;
import com.web.pojo.FollowOrderTradeRecord;
import com.web.pojo.vo.FollowOrderPageVo;
import com.web.pojo.vo.FollowOrderVo;
import com.web.pojo.vo.OrderMsgResult;

import java.util.List;

/**
 * 跟单明细
 * Created by may on 2018/5/23.
 */
public interface FollowOrderDetailService {

    void save(FollowOrderDetail followOrderDetail);

   /*  
    *    
    * 客户平仓，通过开仓单号找到对应的明细
    * @author may  
    * @date 2018/5/25 14:37  
    * @param   
    * @return   
    */  
    FollowOrderDetail getFollowOrderDetailByTicket(String ticket, Long followOrderId);

    FollowOrderDetail getFollowOrderDetail(Long id);

    List<FollowOrderDetail> getDetailListByFollowOrderId(Long followOrderId, String endTime, String startTime, Integer status);
    /**
     * 通过对应的跟单id找到对应的明细，时间倒序
     *@Author: May
     *@Date: 11:22 2018/5/23
     * @param followOrderId
     * @param endTime
     * @param startTime
     * @param status
     */
    List<?> findDetailList(Long followOrderId, String endTime, String startTime, Integer status);

    /**净头寸平仓
     * 找到对应的开多 or  开空 明细，并且剩下手数不为0的明细记录
     *@Author: May
     *@param
     *@Date: 13:57 2018/5/23
     */
    List<FollowOrderDetail> getDetailListByOrderIdAndDirection(Long followOrderId,Integer direction);



   /*
    *
    *   获取未平的所有明细
    * @author may
    * @date 2018/6/5 18:29
    * @param
    * @return
    */
    List<FollowOrderDetail> getNOCloseDetailListByFollowOrderId(Long followOrderId);


    void updateDetail(FollowOrderDetail followOrderDetail);




    void createDetail(FollowOrderTradeRecord followOrderTradeRecord, OrderMsgResult orderMsgResult);

    List<FollowOrderDetail> getFollowOrderDetailByUserCode(Long followOrderId, String endTime, String startTime, String clientName);
    FollowOrderVo getAccountCountAndOffsetGainAndLossBYAccountId(Long accountId);
}

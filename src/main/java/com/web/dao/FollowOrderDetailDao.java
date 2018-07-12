package com.web.dao;


import com.web.pojo.FollowOrderDetail;
import com.web.pojo.vo.FollowOrderPageVo;
import com.web.pojo.vo.FollowOrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FollowOrderDetailDao {
    int deleteByPrimaryKey(Long id);

    int insert(FollowOrderDetail record);

    FollowOrderDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKey(FollowOrderDetail record);

    List<FollowOrderDetail> getDetailListByFollowOrderId(@Param("followOrderId") Long followOrderId, @Param("endTime") String endTime, @Param("startTime") String startTime, @Param("status") Integer status);

    /*
     *
     *   获取未平的所有明细
     * @author may
     * @date 2018/6/5 18:29
     * @param
     * @return
     */
    List<FollowOrderDetail> getNOCloseDetailListByFollowOrderId(Long followOrderId);

    List<FollowOrderDetail> getDetailListByOrderIdAndDirection(@Param("followOrderId") Long followOrderId, @Param("direction") Integer direction);

    FollowOrderDetail getFollowOrderDetailByTicket(@Param("ticket") String ticket, @Param("followOrderId") Long followOrderId);





    List<FollowOrderDetail> getFollowOrderDetailByUserCode(@Param("followOrderId") Long followOrderId, @Param("endTime") String endTime, @Param("startTime") String startTime, @Param("clientName") String clientName);

    FollowOrderVo getAccountCountAndOffsetGainAndLossBYAccountId(Long accountId);

}
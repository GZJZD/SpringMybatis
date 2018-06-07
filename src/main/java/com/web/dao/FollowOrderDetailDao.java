package com.web.dao;


import com.web.pojo.FollowOrderDetail;
import com.web.pojo.vo.FollowOrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FollowOrderDetailDao {
    int deleteByPrimaryKey(Long id);

    int insert(FollowOrderDetail record);

    FollowOrderDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKey(FollowOrderDetail record);

    List<FollowOrderDetail> getDetailListByFollowOrderId(Long followOrderId);

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

    FollowOrderDetail getFollowOrderDetailByTicket(String ticket);

    Double getOffsetGainAndLossByFollowOrderId (Long followOrderId);

    FollowOrderVo getCommissionTotalAndHandNumTotal(Long followOrderId);

    FollowOrderVo getTotal(Long followOrderId);

}
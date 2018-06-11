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

    /*
    * WEB页面中：历史跟单手数，历史收益，总平仓单数
    * */
    FollowOrderPageVo getFollowOrderPageVoIsClose();
    /*
    * 已经平仓而且盈利得单数
    * */
    Integer getFollowOrderPageVoIsCloseProfitNum();

    /*
    * WEB页面中：持仓手数，持仓收益
    * */
    FollowOrderPageVo getFollowOrderPageVoIsOpen();
}
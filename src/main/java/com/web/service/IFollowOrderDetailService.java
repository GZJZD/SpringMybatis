package com.web.service;

import com.web.pojo.FollowOrderDetail;

import java.util.List;

/**
 * 跟单明细
 * Created by may on 2018/5/23.
 */
public interface IFollowOrderDetailService {

    void save(FollowOrderDetail followOrderDetail);
    FollowOrderDetail getFollowOrderDetail(Long id);
    /**
     * 通过对应的跟单id找到对应的明细，时间倒序
     *@Author: May
     *@param
     *@Date: 11:22 2018/5/23
     */
    List<FollowOrderDetail> getDetailListByFollowOrderId(Long followOrderId);

    /**净头寸平仓
     * 找到对应的开多 or  开空 明细，并且剩下手数不为0的明细记录
     *@Author: May
     *@param
     *@Date: 13:57 2018/5/23
     */
    List<FollowOrderDetail> getDetailListByOrderIdAndDirection(Long followOrderId,Integer direction);

    void updateDetail(FollowOrderDetail followOrderDetail);
}

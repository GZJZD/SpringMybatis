package com.web.service;

import com.web.pojo.FollowOrder;
import com.web.pojo.FollowOrderClient;
import com.web.pojo.vo.followOrder.FollowOrderClientParamVo;

import java.util.List;

/**
 * 跟单与客户之间的联系
 * Created by may on 2018/5/19.
 */
public interface FollowOrderClientService {

    /**通过客户编号找到相关联的了跟单
     *@Author: May
     *@param
     *@Date: 10:39 2018/5/21
     */
    List<Long> getListByUserCodeAndPlatformCode(String userCode,String platformCode);

    List<FollowOrderClient> getListByFollowOrderId(Long followOrderId);
    List<String> getListUserCodeByFollowOrderId(Long followOrderId);
    List<String> getListUserNameByFollowOrderId(Long followOrderId);
    List<FollowOrderClientParamVo> getListFollowOrderClientParamVo(Long followOrderId);

    void deleteByFollowOrderId(Long followOrderId);
    FollowOrderClient getByUserCodeAndPlatformCode(String userCode, String platformCode, Long followOrderId);

    void saveListFollowOrderClient(List<FollowOrderClient> FollowOrderClients, FollowOrder followOrder);

    void update(FollowOrderClient record);


    FollowOrderClient getFollowOrderClient(Long followOrderClientId);
}

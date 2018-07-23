package com.web.service;

import com.web.pojo.FollowOrderClient;

import java.util.List;
import java.util.Map;

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

    List<FollowOrderClient> getListByFollowOrderId(Long followOrderId,Integer NODeleteOrAllStatus);

    List<Map<String,Object>> getListUserNameByFollowOrderId(Long followOrderId);
    List<Map<String,Object>> getListFollowOrderClientParamVo(Long followOrderId);

    void deleteByFollowOrderId(Long followOrderId);

    FollowOrderClient getByUserCodeAndPlatformCode(String userCode, String platformCode, Long followOrderId);

    List<FollowOrderClient> getAllByUserCodeAndPlatformCode(String userCode, String platformCode, Long followOrderId);

    void saveListFollowOrderClient(List<FollowOrderClient> FollowOrderClients, Long followOrderId);

    void update(FollowOrderClient record);

    FollowOrderClient getFollowOrderClient(Long followOrderClientId);

    String getUserName(String userCode, String platformCode);

    void updateListFollowOrderClient(List<FollowOrderClient> followOrderClients);

    void deleteClient(Long followOrderClientId);

    List<String> checkAddClient(List<FollowOrderClient> followOrderClients, Long followOrderId);
}

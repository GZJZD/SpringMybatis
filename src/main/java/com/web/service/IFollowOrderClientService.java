package com.web.service;

import com.web.pojo.FollowOrderClient;

import java.util.List;

/**
 * 跟单与客户之间的联系
 * Created by may on 2018/5/19.
 */
public interface IFollowOrderClientService {
    FollowOrderClient get(Long id);
    /**通过客户的id找到相关联的了跟单
     *@Author: May
     *@param
     *@Date: 10:39 2018/5/21
     */
    List<FollowOrderClient> getListByClientId(Long clientId);
}

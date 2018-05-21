package com.web.dao;


import com.web.pojo.FollowOrderClient;

import java.util.List;

public interface FollowOrderClientDao {
    int deleteByPrimaryKey(Long id);

    int insert(FollowOrderClient record);

    FollowOrderClient selectByPrimaryKey(Long id);

    int updateByPrimaryKey(FollowOrderClient record);
    /**通过客户的id找到关联的跟单
     *@Author: May
     *@param
     *@Date: 10:39 2018/5/21
     */
    List<FollowOrderClient> selectByClientId(Long clientId);

}
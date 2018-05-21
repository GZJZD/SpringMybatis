package com.web.dao;


import com.web.pojo.FollowOrder;

import java.util.List;

public interface FollowOrderDao {
    int deleteByPrimaryKey(Long id);

    int insert(FollowOrder record);

    FollowOrder selectByPrimaryKey(Long id);

    List<FollowOrder> selectAll();

    int updateByPrimaryKey(FollowOrder record);
}
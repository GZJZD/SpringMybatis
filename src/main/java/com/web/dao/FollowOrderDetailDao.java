package com.web.dao;


import com.web.pojo.FollowOrderDetail;

import java.util.List;

public interface FollowOrderDetailDao {
    int deleteByPrimaryKey(Long id);

    int insert(FollowOrderDetail record);

    FollowOrderDetail selectByPrimaryKey(Long id);

    List<FollowOrderDetail> selectAll();

    int updateByPrimaryKey(FollowOrderDetail record);
}
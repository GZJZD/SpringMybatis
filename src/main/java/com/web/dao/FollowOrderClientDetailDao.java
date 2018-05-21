package com.web.dao;


import com.web.pojo.FollowOrderClientDetail;

import java.util.List;

public interface FollowOrderClientDetailDao {
    int deleteByPrimaryKey(Long id);

    int insert(FollowOrderClientDetail record);

    FollowOrderClientDetail selectByPrimaryKey(Long id);

    List<FollowOrderClientDetail> selectAll();

    int updateByPrimaryKey(FollowOrderClientDetail record);
}
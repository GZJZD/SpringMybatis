package com.web.dao;


import com.web.pojo.FollowOrder;
import com.web.util.query.QueryObject;

import java.util.List;

public interface FollowOrderDao {
    int deleteByPrimaryKey(Long id);

    int insert(FollowOrder record);

    FollowOrder selectByPrimaryKey(Long id);

    List<FollowOrder> selectAll(QueryObject queryObject);

    int updateByPrimaryKey(FollowOrder record);
    int queryForCount(QueryObject qo);
}
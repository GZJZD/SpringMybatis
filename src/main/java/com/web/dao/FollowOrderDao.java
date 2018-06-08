package com.web.dao;


import com.web.pojo.FollowOrder;
import com.web.util.query.QueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FollowOrderDao {
    int deleteByPrimaryKey(Long id);

    int insert(FollowOrder record);

    FollowOrder selectByPrimaryKey(Long id);

    //分页
    List<FollowOrder> selectAll(QueryObject queryObject);

    List<FollowOrder> selectListFollowOrder(@Param("varietyId") Long varietyId, @Param("accountId") Long accountId);
    int updateByPrimaryKey(FollowOrder record);
    int queryForCount(QueryObject qo);

    void updateFollowOrderStatus(@Param("followOrderId") Long followOrderId, @Param("status") Integer status, @Param("startTime") String startTime);
}
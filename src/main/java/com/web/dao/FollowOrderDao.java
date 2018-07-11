package com.web.dao;


import com.web.pojo.FollowOrder;
import com.web.pojo.vo.FollowOrderPageVo;
import com.web.util.query.QueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FollowOrderDao {
    int deleteByPrimaryKey(Long id);

    int insert(FollowOrder record);

    FollowOrder selectByPrimaryKey(Long id);

    List<FollowOrder> selectAll(QueryObject queryObject);

    List<FollowOrder> selectListFollowOrder(FollowOrderPageVo followOrderPageVo);
    int updateByPrimaryKey(FollowOrder record);
    int queryForCount(QueryObject qo);

    int updateFollowOrderStatus(@Param("followOrderId") Long followOrderId, @Param("status") Integer status, @Param("startTime") String startTime);


    List<FollowOrder> findFollowOrderStart(@Param("followOrderIds") List<Long> followOrderIds, @Param("varietyCode") String varietyCode);

    int findAccountStatusByAccountId(Long accountId);

    List<FollowOrder>  getNOStopFollowOrder();
}
package com.web.dao;


import com.web.pojo.FollowOrderTradeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FollowOrderTradeRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(FollowOrderTradeRecord record);

    FollowOrderTradeRecord selectByPrimaryKey(Long id);
    List<FollowOrderTradeRecord> getListOrderTradeByTicketAndTime(@Param("ticket") String ticket, @Param("time") String time);
    List<FollowOrderTradeRecord> selectAll();

    int updateByPrimaryKey(FollowOrderTradeRecord record);

    int getFollowOrderTotalAmount(Long followOrderId);

    int getFollowOrderSuccessTotalAmount(Long followOrderId);
}
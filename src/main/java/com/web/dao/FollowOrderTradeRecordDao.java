package com.web.dao;


import com.web.pojo.FollowOrderTradeRecord;
import com.web.pojo.vo.FollowOrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FollowOrderTradeRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(FollowOrderTradeRecord record);

    FollowOrderTradeRecord selectByPrimaryKey(Long id);
    List<FollowOrderTradeRecord> getListOrderTradeByTicketAndTime(@Param("ticket") String ticket, @Param("time") String time);
    List<FollowOrderTradeRecord> selectAll();

    int updateByPrimaryKey(FollowOrderTradeRecord record);

    List<FollowOrderVo> getFollowOrderTradeTotalCount(@Param("followOrderId") Long followOrderId, @Param("userCode") boolean userCode, @Param("endTime") String endTime, @Param("startTime") String startTime);

    List<FollowOrderVo> getFollowOrderSuccessTradeTotal(@Param("followOrderId") Long followOrderId, @Param("userCode") boolean userCode, @Param("endTime") String endTime, @Param("startTime") String startTime);

}
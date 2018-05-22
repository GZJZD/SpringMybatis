package com.web.dao;



import com.web.pojo.FollowOrderTradeRecord;

import java.util.List;

public interface FollowOrderTradeRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(FollowOrderTradeRecord record);

    FollowOrderTradeRecord selectByPrimaryKey(Long id);

    List<FollowOrderTradeRecord> selectAll();

    int updateByPrimaryKey(FollowOrderTradeRecord record);


    FollowOrderTradeRecord getTradeRecordBySignalNumber(String signalNumber);
}
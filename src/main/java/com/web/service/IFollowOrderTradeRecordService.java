package com.web.service;

import com.web.pojo.FollowOrderTradeRecord;

/**
 * Created by may on 2018/5/20.
 */
public interface IFollowOrderTradeRecordService {

    void save(FollowOrderTradeRecord followOrderTradeRecord);

    FollowOrderTradeRecord getTradeRecord(Long id);

    void updateTradeRecord(FollowOrderTradeRecord followOrderTradeRecord);
}

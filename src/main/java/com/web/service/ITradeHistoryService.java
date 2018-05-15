package com.web.service;



import com.web.pojo.TradeHistory;

import java.util.List;

/**
 * 交易信息历史
 * Created by may on 2018/5/11.
 */
public interface ITradeHistoryService {


   void insert(TradeHistory record);

    TradeHistory selectByPrimaryKey(Long id);

    List<TradeHistory> selectAll();

    void updateByPrimaryKey(TradeHistory record);
}

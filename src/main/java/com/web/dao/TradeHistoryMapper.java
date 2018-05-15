package com.web.dao;


import com.web.pojo.TradeHistory;

import java.util.List;

public interface TradeHistoryMapper {

    int insert(TradeHistory record);

    TradeHistory selectByPrimaryKey(Long id);

    List<TradeHistory> selectAll();

    int updateByPrimaryKey(TradeHistory record);
}
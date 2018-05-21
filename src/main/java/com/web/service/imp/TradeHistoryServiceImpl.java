package com.web.service.imp;


import com.web.dao.TradeHistoryDao;
import com.web.pojo.TradeHistory;
import com.web.service.ITradeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by may on 2018/5/11.
 */
@Service
@Transactional
public class TradeHistoryServiceImpl implements ITradeHistoryService {
    @Autowired
    private TradeHistoryDao tradeHistoryMapper;

    @Override
    public void insert(TradeHistory record) {
        tradeHistoryMapper.insert(record);
    }

    @Override
    public TradeHistory selectByPrimaryKey(Long id) {
        return tradeHistoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TradeHistory> selectAll() {
        return tradeHistoryMapper.selectAll();
    }

    @Override
    public void updateByPrimaryKey(TradeHistory record) {
        int count = tradeHistoryMapper.updateByPrimaryKey(record);
        if(count <= 0){
            throw new RuntimeException("乐观锁异常");
        }
    }
}

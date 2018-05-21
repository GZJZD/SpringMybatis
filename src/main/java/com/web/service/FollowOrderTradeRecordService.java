package com.web.service;

import com.web.dao.FollowOrderTradeRecordDao;
import com.web.pojo.FollowOrderTradeRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by seemygo on 2018/5/20.
 */
@Service@Transactional
public class FollowOrderTradeRecordService implements IFollowOrderTradeRecordService{
    @Autowired
    private FollowOrderTradeRecordDao followOrderTradeRecordDao;
    @Override
    public void save(FollowOrderTradeRecord followOrderTradeRecord) {
        followOrderTradeRecordDao.insert(followOrderTradeRecord);
    }

    @Override
    public FollowOrderTradeRecord getTradeRecord(Long id) {
        return followOrderTradeRecordDao.selectByPrimaryKey(id);
    }

    @Override
    public void updateTradeRecord(FollowOrderTradeRecord followOrderTradeRecord) {
        int count = followOrderTradeRecordDao.updateByPrimaryKey(followOrderTradeRecord);
        followOrderTradeRecordDao.selectByPrimaryKey(followOrderTradeRecord.getId());
        System.out.println(followOrderTradeRecord.getVersion()+"======================");
        System.out.println(followOrderTradeRecord.toString());
        if(count<=0){
            throw new RuntimeException("时间戳出现异常:"+FollowOrderTradeRecord.class);
        }
    }
}

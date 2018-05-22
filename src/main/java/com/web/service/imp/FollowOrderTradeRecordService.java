package com.web.service.imp;

import com.web.dao.FollowOrderTradeRecordDao;
import com.web.pojo.FollowOrder;
import com.web.pojo.FollowOrderTradeRecord;

import com.web.service.IFollowOrderService;
import com.web.service.IFollowOrderTradeRecordService;
import com.web.util.StatusUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by may on 2018/5/20.
 */
@Service
@Transactional
public class FollowOrderTradeRecordService implements IFollowOrderTradeRecordService {
    @Autowired
    private FollowOrderTradeRecordDao followOrderTradeRecordDao;
    @Autowired
    private IFollowOrderService followOrderService;

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
        System.out.println(followOrderTradeRecord.getVersion() + "======================");
        System.out.println(followOrderTradeRecord.toString());
        if (count <= 0) {
            throw new RuntimeException("乐观锁出现异常:" + FollowOrderTradeRecord.class);
        }
    }

    @Override
    public void updateRecordByComeBackTradeMsg(String msg) {
        String[] msgArr = msg.split(";");
        int index = 0;
        FollowOrderTradeRecord tradeRecord = getTradeRecordBySignalNumber(msgArr[index++]);
        FollowOrder followOrder = followOrderService.getFollowOrder(tradeRecord.getFollowOrderId());
        if (followOrder.getFollowManner().equals(StatusUtil.FOLLOWMANNER_NET_POSITION.getIndex())) {
            //修改该跟单状态
            followOrder.setNetPositionStatus(StatusUtil.TRADING_PAUSE.getIndex());
            //修改持仓数

        }
    }

    @Override
    public FollowOrderTradeRecord getTradeRecordBySignalNumber(String signalNumber) {
        return followOrderTradeRecordDao.getTradeRecordBySignalNumber(signalNumber);
    }
}

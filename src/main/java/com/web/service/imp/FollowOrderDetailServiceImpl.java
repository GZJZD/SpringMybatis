package com.web.service.imp;

import com.web.dao.FollowOrderDetailDao;
import com.web.pojo.FollowOrderDetail;
import com.web.pojo.FollowOrderTradeRecord;
import com.web.pojo.vo.NetPositionDetailVo;
import com.web.service.IFollowOrderDetailService;
import com.web.service.IFollowOrderTradeRecordService;
import com.web.service.IVarietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by may on 2018/5/23.
 */
@Service
@Transactional
public class FollowOrderDetailServiceImpl implements IFollowOrderDetailService {
    @Autowired
    private FollowOrderDetailDao followOrderDetailDao;
    @Autowired
    private IFollowOrderTradeRecordService followOrderTradeRecordService;
    @Autowired
    private IVarietyService varietyService;

    @Override
    public void save(FollowOrderDetail followOrderDetail) {
        followOrderDetailDao.insert(followOrderDetail);
    }

    @Override
    public FollowOrderDetail getFollowOrderDetail(Long id) {
        return followOrderDetailDao.selectByPrimaryKey(id);
    }

    @Override
    public List<NetPositionDetailVo> getDetailListByFollowOrderId(Long followOrderId) {
        List<NetPositionDetailVo> netPositionDetailVos = new ArrayList<>();
        List<FollowOrderDetail> OrderDetailList = followOrderDetailDao.getDetailListByFollowOrderId(followOrderId);
        for (FollowOrderDetail followOrderDetail : OrderDetailList) {
            FollowOrderTradeRecord tradeRecord = followOrderTradeRecordService.getTradeRecord(followOrderDetail.
                    getFollowOrderTradeRecordId());

            NetPositionDetailVo netPositionDetailVo = new NetPositionDetailVo();
            netPositionDetailVo.setNetPositionSum(tradeRecord.getNetPositionSum());

            netPositionDetailVo.setVarietyName(varietyService.getVariety(tradeRecord.getVarietyId()).getVarietyName());
            netPositionDetailVo.setOpenCloseType(tradeRecord.getOpenCloseType());
            netPositionDetailVo.setHandNumber(tradeRecord.getHandNumber());
            netPositionDetailVo.setTradeDirection(tradeRecord.getTradeDirection());
            netPositionDetailVo.setMarketPrice(tradeRecord.getMarketPrice());
            netPositionDetailVo.setTradeTime(tradeRecord.getTradeTime());
            netPositionDetailVo.setPoundage(tradeRecord.getPoundage());

            netPositionDetailVo.setRemainHandNumber(
                    followOrderDetail.getRemainHandNumber() == null? 0.0 : followOrderDetail.getRemainHandNumber());

            netPositionDetailVo.setDetailId(followOrderDetail.getId());
            netPositionDetailVo.setProfit(followOrderDetail.getProfitLoss() == null? 0.0 : followOrderDetail.getProfitLoss());
            netPositionDetailVos.add(netPositionDetailVo);
        }
        return netPositionDetailVos;

    }

    @Override
    public List<FollowOrderDetail> getDetailListByOrderIdAndDirection(Long followOrderId, Integer direction) {
        return followOrderDetailDao.getDetailListByOrderIdAndDirection(followOrderId,direction);
    }

    @Override
    public void updateDetail(FollowOrderDetail followOrderDetail) {
        int count = followOrderDetailDao.updateByPrimaryKey(followOrderDetail);
        if(count<=0){
            throw new RuntimeException("乐观锁异常：" +FollowOrderDetail.class);
        }
    }
}

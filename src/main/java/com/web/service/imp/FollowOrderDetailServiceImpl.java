package com.web.service.imp;

import com.web.dao.FollowOrderDetailDao;
import com.web.pojo.FollowOrderDetail;
import com.web.pojo.FollowOrderTradeRecord;
import com.web.pojo.Variety;
import com.web.pojo.vo.FollowOrderPageVo;
import com.web.pojo.vo.FollowOrderVo;
import com.web.pojo.vo.NetPositionDetailVo;
import com.web.service.IFollowOrderDetailService;
import com.web.service.IFollowOrderTradeRecordService;
import com.web.service.IVarietyService;
import com.web.util.common.DoubleUtil;
import com.web.util.query.QueryObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public FollowOrderDetail getFollowOrderDetailByTicket(String ticket) {
        return followOrderDetailDao.getFollowOrderDetailByTicket(ticket);
    }

    @Override
    public FollowOrderDetail


    getFollowOrderDetail(Long id) {
        return followOrderDetailDao.selectByPrimaryKey(id);
    }

    @Override
    public List<NetPositionDetailVo> getDetailListByFollowOrderId(Long followOrderId){
        List<NetPositionDetailVo> netPositionDetailVos = new ArrayList<>();
        List<FollowOrderDetail> OrderDetailList = followOrderDetailDao.getDetailListByFollowOrderId(followOrderId);
        if(OrderDetailList != null) {
            for (FollowOrderDetail followOrderDetail : OrderDetailList) {
                FollowOrderTradeRecord tradeRecord = followOrderTradeRecordService.getTradeRecord(followOrderDetail.
                        getFollowOrderTradeRecordId());

                NetPositionDetailVo netPositionDetailVo = new NetPositionDetailVo();
                //设置净头寸
                netPositionDetailVo.setNetPositionSum(tradeRecord.getNetPositionSum());
                //设置品种
                Variety variety = varietyService.getVariety(tradeRecord.getVarietyId());
                netPositionDetailVo.setVarietyName("黄金");
                //设置开平
                netPositionDetailVo.setOpenCloseType(tradeRecord.getOpenCloseType());
                //设置手数
                if(followOrderDetail.getHandNumber() != followOrderDetail.getOriginalHandNumber() &&
                        followOrderDetail.getOriginalHandNumber() != null){
                    netPositionDetailVo.setHandNumber(followOrderDetail.getHandNumber()+
                            "("+followOrderDetail.getOriginalHandNumber()+")");
                }else{
                    netPositionDetailVo.setHandNumber((followOrderDetail.getHandNumber() ==null ?0:followOrderDetail.getHandNumber())+"");
                }

                //设置交易方向：多空
                netPositionDetailVo.setTradeDirection(tradeRecord.getTradeDirection());
                //设置市场价
                netPositionDetailVo.setMarketPrice(tradeRecord.getMarketPrice());
                //设置交易时间
                String tradeTime = tradeRecord.getTradeTime();
                if(!"".equals(tradeTime)) {
                    netPositionDetailVo.setTradeTime(tradeTime.substring(0, 4) + "-" + tradeTime.substring(4, 6) + "-" + tradeTime.substring(6, 8) + "  "
                            + tradeTime.substring(8, 10) + ":" + tradeTime.substring(10, 12) + ":" + tradeTime.substring(12, 14));
                }
                //设置手续费
                netPositionDetailVo.setPoundage(tradeRecord.getPoundage());
                //设置剩下的手数
                netPositionDetailVo.setRemainHandNumber(
                        followOrderDetail.getRemainHandNumber() == null ? 0.0 : followOrderDetail.getRemainHandNumber());
                //设置明细的id
                netPositionDetailVo.setDetailId(followOrderDetail.getId());
                //设置盈亏
                netPositionDetailVo.setProfit(followOrderDetail.getProfitLoss() == null ? 0.0 : followOrderDetail.getProfitLoss());
                netPositionDetailVos.add(netPositionDetailVo);
            }
        }
        return netPositionDetailVos;

    }

    @Override
    public List<FollowOrderDetail> getDetailListByOrderIdAndDirection(Long followOrderId, Integer direction) {
        return followOrderDetailDao.getDetailListByOrderIdAndDirection(followOrderId,direction);
    }

    @Override
    public Double getOffsetGainAndLossByFollowOrderId(Long followOrderId) {

        return followOrderDetailDao.getOffsetGainAndLossByFollowOrderId(followOrderId);
    }

    @Override
    public FollowOrderVo getCommissionTotalAndHandNumTotal(Long followOrderId) {
        return followOrderDetailDao.getCommissionTotalAndHandNumTotal(followOrderId);
    }

    @Override
    public void updateDetail(FollowOrderDetail followOrderDetail) {
        int count = followOrderDetailDao.updateByPrimaryKey(followOrderDetail);
        if(count<=0){
            throw new RuntimeException("乐观锁异常：" +FollowOrderDetail.class);
        }
    }

    /*
    * web跟单页面的头统计
    * */
    @Override
    public FollowOrderPageVo getFollowOrderPageVo() {
        FollowOrderPageVo followOrderPageVo = followOrderDetailDao.getFollowOrderPageVoIsClose();//历史跟单手数，历史收益，总平仓单数
        FollowOrderPageVo followOrderPageVoIsOpen = followOrderDetailDao.getFollowOrderPageVoIsOpen();
        if(followOrderPageVoIsOpen !=null){
            followOrderPageVo.setHoldPositionHandNumber(followOrderPageVoIsOpen.getHoldPositionHandNumber() );//持仓手数
            followOrderPageVo.setHoldPositionProfit(followOrderPageVoIsOpen.getHoldPositionProfit());//持仓收益
        }

        followOrderPageVo.setClosePositionWinSum(followOrderDetailDao.getFollowOrderPageVoIsCloseProfitNum());//盈利单数
        followOrderPageVo.setWinRate(DoubleUtil.div(Double.valueOf(followOrderPageVo.getClosePositionWinSum()),
                Double.valueOf(followOrderPageVo.getClosePositionTotalNumber()),2));//胜率
        followOrderPageVo.setProfitAndLossRate(DoubleUtil.div(followOrderPageVo.getHistoryProfit(),
                followOrderPageVo.getHistoryHandNumber(),2));//盈亏效率
        return followOrderPageVo;
    }

    @Override
    public List<FollowOrderDetail> getNOCloseDetailListByFollowOrderId(Long followOrderId) {
        return followOrderDetailDao.getNOCloseDetailListByFollowOrderId(followOrderId);
    }
}

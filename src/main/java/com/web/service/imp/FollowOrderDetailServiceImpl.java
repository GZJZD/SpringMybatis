package com.web.service.imp;

import com.web.dao.FollowOrderDetailDao;
import com.web.pojo.*;
import com.web.pojo.vo.FollowOrderClientDetailVo;
import com.web.pojo.vo.FollowOrderPageVo;
import com.web.pojo.vo.FollowOrderVo;
import com.web.pojo.vo.NetPositionDetailVo;
import com.web.service.*;
import com.web.util.common.DateUtil;
import com.web.util.common.DoubleUtil;
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
    @Autowired
    private IFollowOrderClientService followOrderClientService;
    @Autowired
    private IClientNetPositionService clientNetPositionService;
    @Autowired
    private IFollowOrderService followOrderService;

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
    public List<NetPositionDetailVo> getDetailListByFollowOrderId(Long followOrderId, String endTime, String startTime, Integer status){
        List<NetPositionDetailVo> netPositionDetailVos = new ArrayList<>();
        if(endTime != null && !"".equals(endTime)){
             endTime = DateUtil.dateToStrLong(DateUtil.getEndDate(DateUtil.strToDate(endTime)));
        }
        List<FollowOrderDetail> OrderDetailList = followOrderDetailDao.getDetailListByFollowOrderId(followOrderId,endTime,startTime,status);
        FollowOrder followOrder = followOrderService.getFollowOrder(followOrderId);
        if(OrderDetailList != null) {
            for (FollowOrderDetail followOrderDetail : OrderDetailList) {
                FollowOrderTradeRecord tradeRecord = followOrderTradeRecordService.getTradeRecord(followOrderDetail.
                        getFollowOrderTradeRecordId());
                ClientNetPosition clientNetPosition = clientNetPositionService.getClientNetPosition(tradeRecord.getClientNetPositionId());

                NetPositionDetailVo netPositionDetailVo = new NetPositionDetailVo();
                //设置净头寸
                netPositionDetailVo.setNetPositionSum(clientNetPosition==null?followOrder.getNetPositionSum():clientNetPosition.getNetPositionSum());
                //设置品种
                Variety variety = varietyService.getVariety(tradeRecord.getVarietyId());
                netPositionDetailVo.setVarietyName(variety.getVarietyName());
                //设置开平
                netPositionDetailVo.setOpenCloseType(tradeRecord.getOpenCloseType());
                //设置手数
                if(!followOrderDetail.getHandNumber().equals(followOrderDetail.getOriginalHandNumber())  &&
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
                netPositionDetailVo.setTradeTime(DateUtil.strToStr(tradeRecord.getTradeTime()));

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
    public FollowOrderVo getOffsetGainAndLossAndHandNumberByFollowOrderId(Long followOrderId) {

        return followOrderDetailDao.getOffsetGainAndLossAndHandNumberByFollowOrderId(followOrderId);
    }

    @Override
    public Double getOpenHandNumber(Long followOrderId) {
        return followOrderDetailDao.getOpenHandNumber(followOrderId);
    }

    @Override
    public Double getCommissionTotal(Long followOrderId) {
        return followOrderDetailDao.getCommissionTotal(followOrderId);
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
            followOrderPageVo.setHoldPositionProfit(followOrderPageVoIsOpen.getHoldPositionProfit()==null?0.0:followOrderPageVoIsOpen.getHoldPositionProfit());//持仓收益
        }else{
            followOrderPageVo.setHoldPositionHandNumber(0.0);
            followOrderPageVo.setHoldPositionProfit(0.0);
        }

        followOrderPageVo.setClosePositionWinSum(followOrderDetailDao.getFollowOrderPageVoIsCloseProfitNum());//盈利单数
        followOrderPageVo.setWinRate(DoubleUtil.div(Double.valueOf(followOrderPageVo.getClosePositionWinSum()),
                Double.valueOf(followOrderPageVo.getClosePositionTotalNumber()==0.0?1.0:followOrderPageVo.getClosePositionTotalNumber()),
                2));//胜率

        followOrderPageVo.setWinRate(DoubleUtil.mul(followOrderPageVo.getWinRate(),100.0));//胜率 * 100

        followOrderPageVo.setProfitAndLossRate(DoubleUtil.div(followOrderPageVo.getHistoryProfit()==null?0.0:followOrderPageVo.getHistoryProfit(),
                followOrderPageVo.getHistoryHandNumber()==0.0?1.0:followOrderPageVo.getHistoryHandNumber(),2));//盈亏效率

        return followOrderPageVo;
    }

    @Override
    public List<FollowOrderDetail> getNOCloseDetailListByFollowOrderId(Long followOrderId) {
        return followOrderDetailDao.getNOCloseDetailListByFollowOrderId(followOrderId);
    }

    /*
    * 获取客户数据
    * */
    public List<FollowOrderClientDetailVo> getListFollowOrderClientDetail(Long followOrderId,String userCode,
                                                                          Integer openOrClose,Integer followOrderClientStatus){
        //用户编号
        List<String> userCodeList = new ArrayList<>();
        List<String> userCodes = followOrderClientService.getListUserCodeByFollowOrderId(followOrderId);

        //条件查询
        if("".equals(userCode)){
            //查全部客户
        }
        

        return null;

    }
}

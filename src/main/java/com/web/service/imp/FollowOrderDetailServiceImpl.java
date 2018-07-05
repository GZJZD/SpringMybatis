package com.web.service.imp;

import com.web.common.FollowOrderEnum;
import com.web.dao.FollowOrderDetailDao;
import com.web.pojo.*;

import com.web.pojo.vo.FollowOrderPageVo;
import com.web.pojo.vo.FollowOrderVo;
import com.web.pojo.vo.NetPositionDetailVo;
import com.web.pojo.vo.OrderMsgResult;
import com.web.service.*;
import com.web.util.common.DateUtil;
import com.web.util.common.DoubleUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class FollowOrderDetailServiceImpl implements FollowOrderDetailService {
    @Autowired
    private FollowOrderDetailDao followOrderDetailDao;
    @Autowired
    private FollowOrderTradeRecordService followOrderTradeRecordService;
    @Autowired
    private VarietyService varietyService;
    @Autowired
    private FollowOrderClientService followOrderClientService;
    @Autowired
    private ClientNetPositionService clientNetPositionService;
    @Autowired
    private FollowOrderService followOrderService;
    @Autowired
    private OrderUserService orderUserService;

    private static Logger log = LogManager.getLogger(FollowOrderDetailServiceImpl.class.getName());

    @Override
    public void save(FollowOrderDetail followOrderDetail) {
        followOrderDetailDao.insert(followOrderDetail);
    }

    @Override
    public FollowOrderDetail getFollowOrderDetailByTicket(String ticket, Long followOrderId) {
        return followOrderDetailDao.getFollowOrderDetailByTicket(ticket,followOrderId);
    }

    @Override
    public FollowOrderDetail


    getFollowOrderDetail(Long id) {
        return followOrderDetailDao.selectByPrimaryKey(id);
    }

    /*
    *
    * 返回明细列表
    * @param endTime:结束时间
    * @param startTime:开始时间
    * @param status:是否平仓
    * */
    @Override
    public List<?> getDetailListByFollowOrderId(Long followOrderId, String endTime, String startTime, Integer status){
        if(endTime != null && !"".equals(endTime)){
             endTime = DateUtil.dateToStrLong(DateUtil.getEndDate(DateUtil.strToDate(endTime)));
        }
        List<FollowOrderDetail> orderDetailList = followOrderDetailDao.getDetailListByFollowOrderId(followOrderId,endTime,startTime,status);
        FollowOrder followOrder = followOrderService.getFollowOrder(followOrderId);
        if(orderDetailList != null) {
           if(followOrder.getFollowManner().equals(FollowOrderEnum.FollowStatus.FOLLOWMANNER_NET_POSITION.getIndex())){
               //净头寸
               return findNetPositionDetail(orderDetailList,followOrder);
           }else{
               //客户
               for (FollowOrderDetail followOrderDetail : orderDetailList) {
                   if(followOrderDetail.getOpenTime()!=null){
                       followOrderDetail.setOpenTime(DateUtil.strToStr(followOrderDetail.getOpenTime()));
                   }
                   if(followOrderDetail.getCloseTime()!=null){
                       followOrderDetail.setCloseTime(DateUtil.strToStr(followOrderDetail.getCloseTime()));
                   }
               }
               return orderDetailList;
           }
        }
        return null;
    }
    /*
    *
    * 返回净头寸明细
    * */
    private List<NetPositionDetailVo> findNetPositionDetail( List<FollowOrderDetail> orderDetailList ,FollowOrder followOrder){
        List<NetPositionDetailVo> netPositionDetailVos = new ArrayList<>();
        for (FollowOrderDetail followOrderDetail : orderDetailList) {
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
            log.debug("");
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
            followOrderPageVo.setHoldPositionHandNumber(DoubleUtil.div(followOrderPageVoIsOpen.getHoldPositionHandNumber(),1.0,2));//持仓手数
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
     * 客户平仓
     * */
    private void createClientCloseDetail(FollowOrderTradeRecord followOrderTradeRecord, OrderMsgResult orderMsgResult) {
        //客户平仓,
        FollowOrderDetail detail = getFollowOrderDetailByTicket(followOrderTradeRecord.getTicket(), followOrderTradeRecord.getFollowOrderId());
        OrderUser user = orderUserService.findByTicket(followOrderTradeRecord.getTicket());
        FollowOrderClient followOrderClient = followOrderClientService.findClientByIdAndName(followOrderTradeRecord.getFollowOrderId(), user.getUserCode());
        if (detail != null && detail.getCloseTime()==null) {
            log.debug("跟每单客户平仓：ticket{},"+detail.getTicket());
            if (!followOrderTradeRecord.getTicket().equals(followOrderTradeRecord.getNewTicket())&&
                    !followOrderClient.getHandNumberType().equals(FollowOrderEnum.FollowStatus.CLIENT_HAND_NUMBER_TYPE.getIndex())) {
                //开仓单号和新开仓单号不一致 和不是固定手数
                //新建开仓单号
                FollowOrderDetail detailNew = new FollowOrderDetail();
                detailNew.setHandNumber(DoubleUtil.sub(detail.getHandNumber(), followOrderTradeRecord.getHandNumber()));
                detailNew.setOriginalHandNumber(detailNew.getHandNumber());
                detailNew.setTicket(followOrderTradeRecord.getNewTicket());
                detailNew.setPoundage(detail.getPoundage());
                detailNew.setCreateDate(DateUtil.getStringDate());
                detailNew.setOpenPrice(detail.getOpenPrice());
                detailNew.setOpenTime(followOrderTradeRecord.getTradeTime());
                detailNew.setFollowOrderId(detail.getFollowOrderId());
                detailNew.setTradeDirection(detail.getTradeDirection());
                detailNew.setVarietyName(detail.getVarietyName());
                detailNew.setFollowOrderTradeRecordId(detail.getFollowOrderTradeRecordId());
                save(detailNew);
                log.debug("客户新开仓明细：newTicket{},handNumber{},"+detailNew.getTicket()+","+detailNew.getHandNumber());
                detail.setHandNumber(followOrderTradeRecord.getHandNumber());
                detail.setOriginalHandNumber(followOrderTradeRecord.getHandNumber());
            }
            //设置平仓时间
            detail.setCloseTime(orderMsgResult.getTradeDate() + orderMsgResult.getTradeTime());
            //设置平仓价格
            detail.setClosePrice(orderMsgResult.getTradePrice());
            //设置平仓盈亏
            detail.setProfitLoss(DoubleUtil.sub(DoubleUtil.mul(detail.getHandNumber(),
                    detail.getClosePrice()), DoubleUtil.mul(detail.getHandNumber(), detail.getOpenPrice())));
            //设置平仓盈亏*100:1手等于100股
            detail.setProfitLoss(DoubleUtil.mul(detail.getProfitLoss(),100.0));
            //手续费
            detail.setPoundage(DoubleUtil.add(detail.getPoundage(), orderMsgResult.getTradeCommission()));
            //交易方向
            detail.setTradeDirection(followOrderTradeRecord.getTradeDirection());
            //设置客户盈亏
            OrderUser orderUser = orderUserService.findByTicket(detail.getTicket());
            detail.setClientProfit(orderUser.getProfit());
            updateDetail(detail);
            log.debug("客户平仓明细：ticket{},handNumber{},id{},closePrice{},followOrderId{},"+detail.getTicket()+","+detail.getHandNumber()+","+detail.getId()+","+detail.getClosePrice()
            +","+detail.getFollowOrderId());

        }
    }

    /*
     *
     * 净头寸平仓
     * */
    private void createNetPositionCloseDetail(FollowOrderDetail orderDetail, FollowOrderTradeRecord followOrderTradeRecord, OrderMsgResult orderMsgResult) {
        //平仓，找到对应的开多 or  开空 明细，并且剩下手数不为0的集合
        List<FollowOrderDetail> followOrderDetails;
        if (followOrderTradeRecord.getTradeDirection().equals(FollowOrderEnum.FollowStatus.BUY.getIndex())) {
            //找到最早开仓的多单明细
            followOrderDetails = getDetailListByOrderIdAndDirection(followOrderTradeRecord.
                    getFollowOrderId(), FollowOrderEnum.FollowStatus.SELL.getIndex());
        } else {
            //找到最早的的空单明细
            followOrderDetails = getDetailListByOrderIdAndDirection(followOrderTradeRecord.
                    getFollowOrderId(), FollowOrderEnum.FollowStatus.BUY.getIndex());
        }
        //获取交易的手数
        Double tradeHandNumber;
        if (orderMsgResult.getTradeVolume() != null) {
            tradeHandNumber = orderMsgResult.getTradeVolume();
        } else {
            tradeHandNumber = followOrderTradeRecord.getHandNumber();
        }
        //交易盈亏
        Double profitLoss = 0.0;
        for (FollowOrderDetail followOrderDetail : followOrderDetails) {
            Double tradePrice;//平仓价格
            Double detailPrice;//开仓价格
            if (tradeHandNumber != 0) {
                //获取明细剩下的手数进行平仓
                Double detailHandNumber = followOrderDetail.getRemainHandNumber();
                if (detailHandNumber > tradeHandNumber) {
                    tradePrice = DoubleUtil.mul(tradeHandNumber, followOrderTradeRecord.getMarketPrice());
                    detailPrice = DoubleUtil.mul(tradeHandNumber, followOrderDetail.getOpenPrice());
                    followOrderDetail.setRemainHandNumber(DoubleUtil.sub(detailHandNumber, tradeHandNumber));
                } else {
                    followOrderDetail.setRemainHandNumber(0.0);
                    tradePrice = DoubleUtil.mul(detailHandNumber, followOrderTradeRecord.getMarketPrice());
                    detailPrice = DoubleUtil.mul(detailHandNumber, followOrderDetail.getOpenPrice());
                    tradeHandNumber = DoubleUtil.sub(tradeHandNumber, detailHandNumber);
                }
                profitLoss = DoubleUtil.add(profitLoss, DoubleUtil.sub(tradePrice,
                        detailPrice));
                updateDetail(followOrderDetail);

            }
        }
        //设置平仓时间
        orderDetail.setCloseTime(orderMsgResult.getTradeDate() + orderMsgResult.getTradeTime());
        //设置平仓价格
        orderDetail.setClosePrice(orderMsgResult.getTradePrice());
        //设置客户盈亏
        //设置平仓盈亏
        orderDetail.setProfitLoss(profitLoss);

    }
    /*
    *
    * 开仓明细
    * */
    private void createOpenDetail(FollowOrderDetail orderDetail, FollowOrderTradeRecord followOrderTradeRecord, OrderMsgResult orderMsgResult) {
        //设置手数
        if (orderMsgResult.getTradeVolume() != null) {
            //实际成交的的数量:开仓返回码,平仓的返回码没有
            orderDetail.setHandNumber(orderMsgResult.getTradeVolume());
        } else {
            orderDetail.setHandNumber(followOrderTradeRecord.getHandNumber());

        }
        FollowOrder followOrder = followOrderService.getFollowOrder(followOrderTradeRecord.getFollowOrderId());
        //跟单方式为净头寸
        if(followOrder.getFollowManner().equals(FollowOrderEnum.FollowStatus.FOLLOWMANNER_NET_POSITION.getIndex())&&followOrderTradeRecord.getOpenCloseType().equals(FollowOrderEnum.FollowStatus.OPEN.getIndex())){
            //设置剩下手数
            orderDetail.setRemainHandNumber(orderDetail.getHandNumber());
        }
        //设置原来手数
        orderDetail.setOriginalHandNumber(followOrderTradeRecord.getHandNumber());
        //设置交易方向
        orderDetail.setTradeDirection(followOrderTradeRecord.getTradeDirection());
        //设置品种的名称
        orderDetail.setVarietyName(followOrderTradeRecord.getVarietyCode());
        //设置手续费
        orderDetail.setPoundage(orderMsgResult.getTradeCommission());
        //设置开仓单号
        orderDetail.setTicket(followOrderTradeRecord.getTicket());
        //设置 创建是时间
        orderDetail.setCreateDate(DateUtil.getStringDate());
        //设置跟单id
        orderDetail.setFollowOrderId(followOrderTradeRecord.getFollowOrderId());
        //设置交易id
        orderDetail.setFollowOrderTradeRecordId(followOrderTradeRecord.getId());
        //客户编号
        OrderUser orderUser = orderUserService.findByTicket(orderDetail.getTicket());
        orderDetail.setClientName(orderUser.getUserCode());
        //客户的盈亏
        orderDetail.setClientProfit(orderUser.getProfit());
        save(orderDetail);
        log.debug("开/平仓明细：ticket{},handNumber{},id{},openPrice{},closePrice{},followOrderId{},"+orderDetail.getTicket()+","+orderDetail.getHandNumber()+","+orderDetail.getId()+","+orderDetail.getOpenPrice()
                +","+orderDetail.getClosePrice()+","+orderDetail.getFollowOrderId());
    }

    /**
     * 创建明细
     *
     * @param
     * @Author: May
     * @Date: 12:21 2018/5/23
     */
    public void createDetail(FollowOrderTradeRecord followOrderTradeRecord, OrderMsgResult orderMsgResult) {
        FollowOrderDetail orderDetail = new FollowOrderDetail();
        //判断跟单方向
        if (followOrderTradeRecord != null) {
            if (followOrderTradeRecord.getOpenCloseType().equals(FollowOrderEnum.FollowStatus.CLOSE.getIndex())) {
                if (followOrderTradeRecord.getClientNetPositionId() == null) {
                    //客户平仓
                    createClientCloseDetail(followOrderTradeRecord, orderMsgResult);
                } else {
                    //净头寸
                    createNetPositionCloseDetail(orderDetail, followOrderTradeRecord, orderMsgResult);

                    createOpenDetail(orderDetail, followOrderTradeRecord, orderMsgResult);
                }
            }else{
                //客户开仓
                //设置开仓时间
                orderDetail.setOpenTime(orderMsgResult.getTradeDate() + orderMsgResult.getTradeTime());
                //设置开仓价格
                orderDetail.setOpenPrice(orderMsgResult.getTradePrice());
                //创建明细
                createOpenDetail(orderDetail, followOrderTradeRecord, orderMsgResult);
            }
        }
    }

    /*
    *
    * 查客户的所有明细详情
    * */
    @Override
    public List<FollowOrderDetail> getFollowOrderDetailByUserCode(Long followOrderId, String endTime, String startTime, String clientName) {
        return followOrderDetailDao.getFollowOrderDetailByUserCode(followOrderId,endTime,startTime,clientName);
    }
}

package com.web.service.imp;

import com.web.dao.FollowOrderTradeRecordDao;
import com.web.pojo.FollowOrder;
import com.web.pojo.FollowOrderDetail;
import com.web.pojo.FollowOrderTradeRecord;

import com.web.pojo.vo.OrderMsgResult;
import com.web.service.IFollowOrderDetailService;
import com.web.service.IFollowOrderService;
import com.web.service.IFollowOrderTradeRecordService;
import com.web.common.FollowOrderEnum;
import com.web.util.common.DateUtil;
import com.web.util.common.DoubleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Autowired
    private IFollowOrderDetailService followOrderDetailService;

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
        if (count <= 0) {
            throw new RuntimeException("乐观锁出现异常:" + FollowOrderTradeRecord.class);
        }
    }

    /**
     *
     * @param orderMsgResult
     */
    @Override
    public void updateRecordByComeBackTradeMsg(OrderMsgResult orderMsgResult) {
        if(orderMsgResult != null) {
            FollowOrderTradeRecord tradeRecord = getTradeRecord(Long.valueOf(orderMsgResult.getRequestId()));
            if (tradeRecord != null) {
                //平仓的时候orderMsgResult.getTradeVolume()这个是为null
             //   if (orderMsgResult.getTradeVolume()==null || orderMsgResult.getTradeVolume() != 0) {
                //设置交易时间+加上日期
                tradeRecord.setTradeTime(orderMsgResult.getTradeDate()+orderMsgResult.getTradeTime());
                //设置市场价
                tradeRecord.setMarketPrice(orderMsgResult.getTradePrice());
                //设置手续费
                tradeRecord.setPoundage(orderMsgResult.getTradeCommission());
                //创建明细
                createDetail(tradeRecord, orderMsgResult);
                if (orderMsgResult.getTradeVolume() != null) {
                    //设置实际手数
                    tradeRecord.setHandNumber(orderMsgResult.getTradeVolume());
                }
                FollowOrder followOrder = followOrderService.getFollowOrder(tradeRecord.getFollowOrderId());
                    if (followOrder != null) {
                        //修改该跟单状态,如果状态是普通的交易状态 or 是两条交易信息已经返回一条就将状态改成交易暂停
                        if(followOrder.getNetPositionStatus().equals(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_START.getIndex())||
                                followOrder.getNetPositionStatus().equals(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_OPENCLOSE_ONE.getIndex())){
                            //设置成交易暂停
                            followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_PAUSE.getIndex());
                        }else if(followOrder.getNetPositionStatus().equals(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_OPENCLOSE.getIndex())){
                            //设置交易已经返回一条信息
                            followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_OPENCLOSE_ONE.getIndex());
                        }
                        if (followOrder.getFollowManner().equals(FollowOrderEnum.FollowStatus.FOLLOWMANNER_NET_POSITION.getIndex())) {
                            if(!followOrder.getFollowOrderStatus().equals(FollowOrderEnum.FollowStatus.FOLLOW_ORDER_STOP.getIndex())) {
                                //净头寸
                                //设置持仓值
                                followOrderService.updateHoldNumByTradeAndFollowOrder(followOrder, tradeRecord);
                            }else{
                                followOrder.setNetPositionSum(0.0);
                                followOrder.setNetPositionHoldNumber(0.0);
                            }
                            followOrderService.updateFollowOrder(followOrder);

                        } else {
                            //todo 客户
                        }
                    }
                if(orderMsgResult.getTradeVolume() != null && orderMsgResult.getTradeVolume() == 0.0){
                    //交易为0
                    tradeRecord.setHandNumber(0.0);
                    tradeRecord.setMarketPrice(0.0);
                    tradeRecord.setPoundage(0.0);
                    //交易失败同时告诉工作人员 todo
                }
                //设置修改时间
                tradeRecord.setUpdateDate(DateUtil.getStringDate());
                followOrderTradeRecordDao.updateByPrimaryKey(tradeRecord);
            }
        }

    }

    /**
     * 明细
     *@Author: May
     *@param
     *@Date: 12:21 2018/5/23
     */
    private void createDetail(FollowOrderTradeRecord followOrderTradeRecord,OrderMsgResult orderMsgResult){
        FollowOrderDetail orderDetail = new FollowOrderDetail();
        //判断跟单方向
        if(followOrderTradeRecord != null){
            if(followOrderTradeRecord.getNetPositionSum() == null){
                //客户
                if(followOrderTradeRecord.getOpenCloseType().equals(FollowOrderEnum.FollowStatus.CLOSE.getIndex())){
                    //平仓,
                    FollowOrderDetail detail = followOrderDetailService.
                            getFollowOrderDetailByTicket(followOrderTradeRecord.getTicket());
                    if(detail != null){
                        if(followOrderTradeRecord.getTicket().equals(followOrderTradeRecord.getNewTicket())){
                            //开仓单号和开仓单号一致就不需要新建开仓
                        }else{
                            //新建开仓单号
                        }
                    }
                }
            }else{
                //净头寸
                if(followOrderTradeRecord.getOpenCloseType().equals(FollowOrderEnum.FollowStatus.CLOSE.getIndex())){
                    //平仓，找到对应的开多 or  开空 明细，并且剩下手数不为0的集合
                    List<FollowOrderDetail> followOrderDetails ;
                    if(followOrderTradeRecord.getTradeDirection().equals(FollowOrderEnum.FollowStatus.BUY.getIndex())){
                        //找到最早开仓的多单明细
                        followOrderDetails = followOrderDetailService.getDetailListByOrderIdAndDirection(followOrderTradeRecord.
                                getFollowOrderId(), FollowOrderEnum.FollowStatus.SELL.getIndex());
                    }else{
                        //找到最早的的空单明细
                        followOrderDetails = followOrderDetailService.getDetailListByOrderIdAndDirection(followOrderTradeRecord.
                                getFollowOrderId(), FollowOrderEnum.FollowStatus.BUY.getIndex());
                    }

                    //获取交易的手数
                    Double tradeHandNumber;
                    if(orderMsgResult.getTradeVolume() != null){
                        tradeHandNumber = orderMsgResult.getTradeVolume();
                    }else{
                         tradeHandNumber = followOrderTradeRecord.getHandNumber();
                    }
                    Double profitLoss = 0.0;
                    for (FollowOrderDetail followOrderDetail : followOrderDetails) {
                        Double tradePrice;
                        Double detailPrice;
                        if(tradeHandNumber != 0){
                            Double detailHandNumber = followOrderDetail.getRemainHandNumber();
                            if(detailHandNumber > tradeHandNumber){
                                tradePrice = DoubleUtil.mul(tradeHandNumber,followOrderTradeRecord.getMarketPrice());
                                detailPrice = DoubleUtil.mul(tradeHandNumber, followOrderDetail.getOpenPrice());
                                followOrderDetail.setRemainHandNumber(DoubleUtil.sub(detailHandNumber,tradeHandNumber));
                            }else {
                                followOrderDetail.setRemainHandNumber(0.0);
                                tradePrice = DoubleUtil.mul(detailHandNumber,followOrderTradeRecord.getMarketPrice());
                                detailPrice = DoubleUtil.mul(detailHandNumber, followOrderDetail.getOpenPrice());
                                tradeHandNumber = DoubleUtil.sub(tradeHandNumber,detailHandNumber);
                            }
                            profitLoss = DoubleUtil.add(profitLoss, DoubleUtil.sub(tradePrice,
                                    detailPrice));
                            followOrderDetailService.updateDetail(followOrderDetail);
                        }
                    }
                    //设置平仓时间
                    orderDetail.setCloseTime(orderMsgResult.getTradeTime());
                    //设置平仓价格
                    orderDetail.setClosePrice(orderMsgResult.getTradePrice());

                    //设置平仓盈亏
                    orderDetail.setProfitLoss(profitLoss);
                    //todo 通过开仓单号找到客户的盈亏
                    //orderDetail.setClientProfit();
                }else{
                    //开仓
                    //设置开仓时间
                    orderDetail.setOpenTime(orderMsgResult.getTradeTime());
                    //设置开仓价格
                    orderDetail.setOpenPrice(orderMsgResult.getTradePrice());
                    //设置剩下手数
                    orderDetail.setRemainHandNumber(orderMsgResult.getTradeVolume());
                }
                //设置手数
                if(orderMsgResult.getTradeVolume()!=null){

                    orderDetail.setHandNumber(orderMsgResult.getTradeVolume());
                }else{
                    orderDetail.setHandNumber(followOrderTradeRecord.getHandNumber());

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
                followOrderDetailService.save(orderDetail);
            }
        }

    }


    @Override
    public int getFollowOrderTotalAmount(Long followOrderId) {
        return followOrderTradeRecordDao.getFollowOrderTotalAmount(followOrderId);
    }

    @Override
    public int getFollowOrderSuccessTotalAmount(Long followOrderId) {
        return followOrderTradeRecordDao.getFollowOrderSuccessTotalAmount(followOrderId);
    }

}

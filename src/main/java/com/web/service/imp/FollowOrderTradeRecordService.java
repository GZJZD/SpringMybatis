package com.web.service.imp;

import com.web.dao.FollowOrderTradeRecordDao;
import com.web.pojo.FollowOrder;
import com.web.pojo.FollowOrderDetail;
import com.web.pojo.FollowOrderTradeRecord;

import com.web.service.IFollowOrderDetailService;
import com.web.service.IFollowOrderService;
import com.web.service.IFollowOrderTradeRecordService;
import com.web.util.StatusUtil;
import com.web.util.common.DateUtil;
import com.web.util.common.DoubleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        String status = msgArr[index++];
        //交易成功
        if (StatusUtil.TRADING_SUCCESS.getIndex().equals(Integer.valueOf(status))) {
            //净头寸
            if (followOrder.getFollowManner().equals(StatusUtil.FOLLOWMANNER_NET_POSITION.getIndex())) {
                //修改该跟单状态
                followOrder.setNetPositionStatus(StatusUtil.TRADING_PAUSE.getIndex());
                //修改持仓数
                //设置净头寸的值
                followOrder.setNetPositionSum(tradeRecord.getNetPositionSum());
                Double netPositionSum = tradeRecord.getNetPositionSum();
                //设置持仓值
                int newHoldNum = (int) (netPositionSum / followOrder.getNetPositionChange());
                if (followOrder.getNetPositionDirection().equals(StatusUtil.DIRECTION_REVERSE.getIndex())) {
                    if (netPositionSum != 0 && newHoldNum != 0) {
                        //反向跟：持仓值要和净头寸的值正负相反
                        followOrder.setNetPositionHoldNumber(-newHoldNum);
                    }
                } else {
                    followOrder.setNetPositionHoldNumber(newHoldNum);
                }
                followOrderService.updateFollowOrder(followOrder);
            }else{
                //todo 客户
            }
            //设置交易时间
            tradeRecord.setTradeTime(msgArr[index++]);
            //设置市场价
            tradeRecord.setMarketPrice(Double.valueOf(msgArr[index++]));
            //设置手续费
            tradeRecord.setPoundage(Double.valueOf(msgArr[index++]));
            createDetail(tradeRecord);
        }
        //设置修改时间
        tradeRecord.setUpdateDate(DateUtil.getStringDate());
        //设置交易状态
        tradeRecord.setTradeStatus(Integer.valueOf(status));
        followOrderTradeRecordDao.updateByPrimaryKey(tradeRecord);

    }

    /**
     * 明细
     *@Author: May
     *@param
     *@Date: 12:21 2018/5/23
     */
    private void createDetail(FollowOrderTradeRecord followOrderTradeRecord){
        FollowOrderDetail orderDetail = new FollowOrderDetail();
        //判断跟单方向
        if(followOrderTradeRecord.getNetPositionSum() == null){
            //客户
        }else{
            //净头寸
            if(followOrderTradeRecord.getOpenCloseType().equals(StatusUtil.CLOSE.getIndex())){
                //平仓，找到对应的开多 or  开空 明细，并且剩下手数不为0的集合
                List<FollowOrderDetail> followOrderDetails ;
                if(followOrderTradeRecord.getTradeDirection().equals(StatusUtil.BUY.getIndex())){
                    followOrderDetails = followOrderDetailService.getDetailListByOrderIdAndDirection(followOrderTradeRecord.
                            getFollowOrderId(), StatusUtil.SELL.getIndex());
                }else{
                    followOrderDetails = followOrderDetailService.getDetailListByOrderIdAndDirection(followOrderTradeRecord.
                            getFollowOrderId(), StatusUtil.BUY.getIndex());
                }
                Double tradeHandNumber = followOrderTradeRecord.getHandNumber();
                Double profitLoss = 0.0;
                for (FollowOrderDetail followOrderDetail : followOrderDetails) {
                    if(tradeHandNumber != 0){
                        Double detailHandNumber = followOrderDetail.getRemainHandNumber();
                        if(detailHandNumber > tradeHandNumber){
                            followOrderDetail.setRemainHandNumber(DoubleUtil.sub(detailHandNumber,tradeHandNumber));
                           profitLoss = DoubleUtil.add(profitLoss, DoubleUtil.sub(followOrderTradeRecord.getMarketPrice(),
                                   followOrderDetail.getOpenPrice()));
                        }else {
                            followOrderDetail.setRemainHandNumber(0.0);
                            tradeHandNumber = DoubleUtil.sub(tradeHandNumber,detailHandNumber);
                            profitLoss = DoubleUtil.add(profitLoss, DoubleUtil.sub(followOrderTradeRecord.getMarketPrice(),
                                    followOrderDetail.getOpenPrice()));
                        }
                        followOrderDetailService.updateDetail(followOrderDetail);
                    }
                }
                //设置平仓时间
                orderDetail.setCloseTime(followOrderTradeRecord.getTradeTime());
                //设置平仓价格
                orderDetail.setClosePrice(followOrderTradeRecord.getMarketPrice());
                //设置平仓盈亏
                orderDetail.setProfitLoss(profitLoss);
                //todo 通过开仓单号找到客户的盈亏
                //orderDetail.setClientProfit();
            }else{
                //开仓
                //设置平仓时间
                orderDetail.setOpenTime(followOrderTradeRecord.getTradeTime());
                //设置平仓价格
                orderDetail.setOpenPrice(followOrderTradeRecord.getMarketPrice());
                //设置剩下手数
                orderDetail.setRemainHandNumber(followOrderTradeRecord.getHandNumber());
            }
            //设置手数
            orderDetail.setHandNumber(followOrderTradeRecord.getHandNumber());

            //设置交易方向
            orderDetail.setTradeDirection(followOrderTradeRecord.getTradeDirection());
            //设置品种的名称
            orderDetail.setVarietyName(followOrderTradeRecord.getVarietyCode());
            //设置手续费
            orderDetail.setPoundage(followOrderTradeRecord.getPoundage());
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


    @Override
    public FollowOrderTradeRecord getTradeRecordBySignalNumber(String signalNumber) {
        return followOrderTradeRecordDao.getTradeRecordBySignalNumber(signalNumber);
    }
}

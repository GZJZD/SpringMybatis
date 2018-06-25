package com.web.service.imp;

import com.web.dao.FollowOrderTradeRecordDao;
import com.web.dao.OrderUserDao;
import com.web.pojo.*;

import com.web.pojo.vo.NetPositionDetailVo;
import com.web.pojo.vo.OrderMsgResult;
import com.web.service.*;
import com.web.common.FollowOrderEnum;
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
    private OrderUserDao orderUserDao;
    @Autowired
    private OrderUserService orderUserService;
    @Autowired
    private IFollowOrderDetailService followOrderDetailService;
    @Autowired
    private IClientNetPositionService clientNetPositionService;

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
     * 修改交易记录
     * @param orderMsgResult
     */
    @Override
    public void updateRecordByComeBackTradeMsg(OrderMsgResult orderMsgResult) {
        if (orderMsgResult != null) {
            FollowOrderTradeRecord tradeRecord = getTradeRecord(Long.valueOf(orderMsgResult.getRequestId()));
            if (tradeRecord != null) {
                //平仓的时候orderMsgResult.getTradeVolume()这个是为null
                //   if (orderMsgResult.getTradeVolume()==null || orderMsgResult.getTradeVolume() != 0) {
                //设置交易时间+加上日期
                tradeRecord.setTradeTime(orderMsgResult.getTradeDate() + orderMsgResult.getTradeTime());
                //设置市场价
                tradeRecord.setMarketPrice(orderMsgResult.getTradePrice());
                //设置手续费
                tradeRecord.setPoundage(orderMsgResult.getTradeCommission());

                //设置跟单状态
                ClientNetPosition clientNetPosition = clientNetPositionService.getClientNetPosition(tradeRecord.getClientNetPositionId());
                if (orderMsgResult.getTradeVolume() != null && orderMsgResult.getTradeVolume() == 0.0) {
                    //交易为0
                    tradeRecord.setHandNumber(0.0);
                    tradeRecord.setMarketPrice(0.0);
                    tradeRecord.setPoundage(0.0);
                    //交易失败同时告诉工作人员 todo
                    if (clientNetPosition != null) {
                        clientNetPosition.setStatus(FollowOrderEnum.FollowStatus.NOT_FOLLOW_ORDER_BY_CLIENT.getIndex());
                        //修改净头寸跟单
                        clientNetPositionService.update(clientNetPosition);
                    }
                } else {
                    if (clientNetPosition != null) {
                        clientNetPosition.setStatus(FollowOrderEnum.FollowStatus.FOLLOW_ORDER_BY_CLIENT.getIndex());
                        //修改净头寸跟单
                        clientNetPositionService.update(clientNetPosition);
                    }
                    //创建明细
                    followOrderDetailService.createDetail(tradeRecord, orderMsgResult);

                }

                if (orderMsgResult.getTradeVolume() != null) {
                    //设置实际手数
                    tradeRecord.setHandNumber(orderMsgResult.getTradeVolume());
                }
                FollowOrder followOrder = followOrderService.getFollowOrder(tradeRecord.getFollowOrderId());
                if (followOrder != null) {
                    //修改该跟单状态,如果状态是普通的交易状态 or 是两条交易信息已经返回一条就将状态改成交易暂停
                    if (followOrder.getNetPositionStatus().equals(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_START.getIndex()) ||
                            followOrder.getNetPositionStatus().equals(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_OPENCLOSE_ONE.getIndex())) {
                        //设置成交易暂停
                        followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_PAUSE.getIndex());
                    } else if (followOrder.getNetPositionStatus().equals(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_OPENCLOSE.getIndex())) {
                        //设置交易已经返回一条信息
                        followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_OPENCLOSE_ONE.getIndex());
                    }
                    if (followOrder.getFollowManner().equals(FollowOrderEnum.FollowStatus.FOLLOWMANNER_NET_POSITION.getIndex())) {
                        if (!followOrder.getFollowOrderStatus().equals(FollowOrderEnum.FollowStatus.FOLLOW_ORDER_STOP.getIndex())) {
                            //净头寸
                            //设置持仓值
                            followOrderService.updateHoldNumByTradeAndFollowOrder(followOrder, tradeRecord);
                        } else {
                            followOrder.setNetPositionSum(0.0);
                            followOrder.setNetPositionHoldNumber(0.0);
                        }
                        followOrderService.updateFollowOrder(followOrder);
                    }
                }

                //设置修改时间
                tradeRecord.setUpdateDate(DateUtil.getStringDate());
                followOrderTradeRecordDao.updateByPrimaryKey(tradeRecord);
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

    @Override
    public List<FollowOrderTradeRecord> getListOrderTradeByTicketAndTime(String ticket, String time) {
        return followOrderTradeRecordDao.getListOrderTradeByTicketAndTime(ticket, time);
    }

    /*
     * 获取对应跟单的客户数据
     * @param followOrderId：跟单id
     * @return
     */
    public List<NetPositionDetailVo> getListClientNetPosition(Long followOrderId, Integer status, String clientName, Integer openOrCloseStatus) {
        List<String> userCode = orderUserDao.listUserCode();//todo demo过后删除
        // List<String> userCode =followOrderClientService.getListUserCodeByFollowOrderId(followOrderId); //正式
        if (!"-1".equals(clientName)) {
            //-1:代表查询全部客户
            userCode = new ArrayList<>();
            userCode.add(clientName);
        }
        FollowOrder followOrder = followOrderService.getFollowOrder(followOrderId);
        List<OrderUser> userList = orderUserService.findByUserIdList(userCode, followOrder.getStartTime(), null, "XAUUSD.e", openOrCloseStatus);
        List<NetPositionDetailVo> netPositionDetailVoList = new ArrayList<>();

        for (OrderUser orderUser : userList) {
            NetPositionDetailVo netPositionDetailVo = new NetPositionDetailVo();
            netPositionDetailVo.setVarietyName(orderUser.getProductCode());//设置品种名字
            netPositionDetailVo.setHandNumber(orderUser.getHandNumber() + "");//设置手数
            netPositionDetailVo.setUserName(orderUser.getUserCode());//设置用户名
            netPositionDetailVo.setTradeDirection(orderUser.getLongShort());//设置多空方向
            netPositionDetailVo.setPoundage(1.0);//设置手续费 todo 等数据过来后重新设置
            netPositionDetailVo.setProfit(orderUser.getProfit());//设置盈亏
            netPositionDetailVo.setMarketPrice(orderUser.getOpenPrice() == null ? orderUser.getClosePrice() : orderUser.getOpenPrice());//市场价
            //设置交易时间
            String tradeTime = orderUser.getOpenTime() == null ? orderUser.getCloseTime() : orderUser.getOpenTime();
            netPositionDetailVo.setTradeTime(DateUtil.strToStr(tradeTime));

            netPositionDetailVo.setOpenCloseType(orderUser.getOpenTime() == null ? FollowOrderEnum.FollowStatus.CLOSE.getIndex()
                    : FollowOrderEnum.FollowStatus.OPEN.getIndex());//设置开平
            //一条完整的交易记录：已开已平
            if (orderUser.getCloseTime() != null && orderUser.getOpenTime() != null) {
                ClientNetPosition clientNetPositionClose = clientNetPositionService.selectByTicketAndTime(orderUser.getTicket(), null, orderUser.getCloseTime(), followOrderId);
                ClientNetPosition clientNetPositionOpen = clientNetPositionService.selectByTicketAndTime(orderUser.getTicket(), orderUser.getOpenTime(), null, followOrderId);
                NetPositionDetailVo netPositionDetailVoClose = new NetPositionDetailVo();
                netPositionDetailVoClose.setVarietyName(orderUser.getProductCode());
                netPositionDetailVoClose.setHandNumber(orderUser.getHandNumber() + "");
                netPositionDetailVoClose.setUserName(orderUser.getUserCode());
                if (orderUser.getLongShort().equals(FollowOrderEnum.FollowStatus.BUY.getIndex())) {
                    //开仓与平仓方向相反
                    netPositionDetailVo.setTradeDirection(FollowOrderEnum.FollowStatus.SELL.getIndex());//开仓：空
                    netPositionDetailVoClose.setTradeDirection(FollowOrderEnum.FollowStatus.BUY.getIndex());//平仓：多

                } else {
                    netPositionDetailVo.setTradeDirection(FollowOrderEnum.FollowStatus.BUY.getIndex());//开仓：多
                    netPositionDetailVoClose.setTradeDirection(FollowOrderEnum.FollowStatus.SELL.getIndex());//平仓：空
                }
                netPositionDetailVoClose.setMarketPrice(orderUser.getClosePrice());//市场价

                //设置交易时间
                netPositionDetailVoClose.setTradeTime(DateUtil.strToStr(orderUser.getCloseTime()));
                netPositionDetailVoClose.setPoundage(1.0);//手续费
                netPositionDetailVoClose.setProfit(orderUser.getProfit());//客户盈亏
                netPositionDetailVoClose.setNetPositionSum(clientNetPositionClose.getNetPositionSum());//净头寸
                netPositionDetailVoClose.setFollowOrderClientStatus(clientNetPositionClose.getStatus());//跟单是否成功
                netPositionDetailVoClose.setOpenCloseType(FollowOrderEnum.FollowStatus.CLOSE.getIndex());//平仓
                //重置
                netPositionDetailVo.setNetPositionSum(clientNetPositionOpen.getNetPositionSum());//设置净头寸
                netPositionDetailVo.setFollowOrderClientStatus(clientNetPositionOpen.getStatus());//设置状态
                netPositionDetailVo.setProfit(0.0);//盈亏为0
                netPositionDetailVo.setOpenCloseType(FollowOrderEnum.FollowStatus.OPEN.getIndex());//开仓
                //判断状态
                findClientByStatus(status, netPositionDetailVo, netPositionDetailVoList);
                findClientByStatus(status, netPositionDetailVoClose, netPositionDetailVoList);

            } else {
                //一条不完整的记录
                ClientNetPosition clientNetPosition = clientNetPositionService.selectByTicketAndTime(orderUser.getTicket(), orderUser.getOpenTime(), orderUser.getCloseTime(), followOrderId);
                if (clientNetPosition != null) {
                    netPositionDetailVo.setNetPositionSum(clientNetPosition.getNetPositionSum());//设置净头寸
                    netPositionDetailVo.setFollowOrderClientStatus(clientNetPosition.getStatus());//状态
                    findClientByStatus(status, netPositionDetailVo, netPositionDetailVoList);
                }
            }

        }
        return netPositionDetailVoList;
    }

    /*
     * 客户的跟单状态查询
     * */
    private void findClientByStatus(Integer status, NetPositionDetailVo netPositionDetailVo, List<NetPositionDetailVo> netPositionDetailVoList) {
        //状态为已跟单or未跟单的添加
        if (status.equals(FollowOrderEnum.FollowStatus.FOLLOW_ORDER_BY_CLIENT.getIndex()) ||
                status.equals(FollowOrderEnum.FollowStatus.NOT_FOLLOW_ORDER_BY_CLIENT.getIndex())) {
            if (netPositionDetailVo.getFollowOrderClientStatus() != null && netPositionDetailVo.getFollowOrderClientStatus().
                    equals(status)) {
                netPositionDetailVoList.add(netPositionDetailVo);
            }
        } else {
            //查询所有
            netPositionDetailVoList.add(netPositionDetailVo);
        }
    }


}

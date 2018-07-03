package com.web.service.imp;

import com.web.dao.FollowOrderTradeRecordDao;
import com.web.pojo.*;

import com.web.pojo.vo.FollowOrderVo;
import com.web.pojo.vo.NetPositionDetailVo;
import com.web.pojo.vo.OrderMsgResult;
import com.web.service.*;
import com.web.common.FollowOrderEnum;
import com.web.util.common.DateUtil;
import com.web.util.common.DoubleUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by may on 2018/5/20.
 */
@Service
@Transactional
public class FollowOrderTradeRecordServiceImpl implements IFollowOrderTradeRecordService {
    @Autowired
    private FollowOrderTradeRecordDao followOrderTradeRecordDao;

    @Autowired
    private IFollowOrderService followOrderService;

    @Autowired
    private IFollowOrderClientService followOrderClientService;

    @Autowired
    private OrderUserService orderUserService;

    @Autowired
    private IFollowOrderDetailService followOrderDetailService;

    @Autowired
    private IClientNetPositionService clientNetPositionService;

    private static Logger log = LogManager.getLogger(FollowOrderTradeRecordServiceImpl.class.getName());

    @Override
    public void save(FollowOrderTradeRecord followOrderTradeRecord) {
        log.debug("新增一条交易记录：handNumber{},followOrderId{},ticket{},"
                +followOrderTradeRecord.getHandNumber()+"、"+followOrderTradeRecord.getFollowOrderId()+"、"
                +followOrderTradeRecord.getTicket());
        followOrderTradeRecordDao.insert(followOrderTradeRecord);

    }

    @Override
    public FollowOrderTradeRecord getTradeRecord(Long id) {
        return followOrderTradeRecordDao.selectByPrimaryKey(id);
    }

    @Override
    public void updateTradeRecord(FollowOrderTradeRecord followOrderTradeRecord) {
        int count = followOrderTradeRecordDao.updateByPrimaryKey(followOrderTradeRecord);
        log.debug("新增一条交易记录：handNumber{},followOrderId{},ticket{},marketPrice{},tradeTime{},"
                +followOrderTradeRecord.getHandNumber()+"、"+followOrderTradeRecord.getFollowOrderId()+"、"
                +followOrderTradeRecord.getTicket()+"、"+followOrderTradeRecord.getMarketPrice()+"、"+followOrderTradeRecord.getTradeTime());
        followOrderTradeRecordDao.selectByPrimaryKey(followOrderTradeRecord.getId());
        if (count <= 0) {
            throw new RuntimeException("乐观锁出现异常:" + FollowOrderTradeRecord.class);
        }
    }

    /**
     * 修改交易记录
     *
     * @param orderMsgResult
     */
    @Override
    public void updateRecordByComeBackTradeMsg(OrderMsgResult orderMsgResult) {
        if (orderMsgResult != null) {
            FollowOrderTradeRecord tradeRecord = getTradeRecord(Long.valueOf(orderMsgResult.getRequestId()));
            if (tradeRecord != null) {
                //设置交易时间+加上日期
                tradeRecord.setTradeTime(orderMsgResult.getTradeDate() + orderMsgResult.getTradeTime());
                //设置市场价
                tradeRecord.setMarketPrice(orderMsgResult.getTradePrice());
                //设置手续费
                tradeRecord.setPoundage(orderMsgResult.getTradeCommission());
                //设置跟单状态
                ClientNetPosition clientNetPosition = clientNetPositionService.getClientNetPosition(tradeRecord.getClientNetPositionId());
                if (clientNetPosition != null) {
                    //设置为已跟单
                    clientNetPosition.setStatus(FollowOrderEnum.FollowStatus.FOLLOW_ORDER_BY_CLIENT.getIndex());
                    //修改净头寸跟单
                    clientNetPositionService.update(clientNetPosition);
                }
                //创建明细
                followOrderDetailService.createDetail(tradeRecord, orderMsgResult);

                if (orderMsgResult.getTradeVolume() != null) {
                    //设置实际手数
                    tradeRecord.setHandNumber(orderMsgResult.getTradeVolume());
                }
                FollowOrder followOrder = followOrderService.getFollowOrder(tradeRecord.getFollowOrderId());
                if (followOrder != null && followOrder.getFollowManner().equals(FollowOrderEnum.FollowStatus.FOLLOWMANNER_NET_POSITION.getIndex())) {

                    //修改该跟单状态,如果状态是普通的交易状态 or 是两条交易信息已经返回一条就将状态改成交易暂停
                    if (followOrder.getNetPositionStatus().equals(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_START.getIndex()) ||
                            followOrder.getNetPositionStatus().equals(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_OPENCLOSE_ONE.getIndex())) {
                        //设置成交易暂停，可以进行下一笔跟单
                        followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_PAUSE.getIndex());
                    } else if (followOrder.getNetPositionStatus().equals(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_OPENCLOSE.getIndex())) {
                        //设置交易已经返回一条信息
                        followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_OPENCLOSE_ONE.getIndex());
                    }
                    //策略状态
                    if (!followOrder.getFollowOrderStatus().equals(FollowOrderEnum.FollowStatus.FOLLOW_ORDER_STOP.getIndex())) {
                        //净头寸
                        //设置持仓值
                        followOrderService.updateHoldNumByTradeAndFollowOrder(followOrder, tradeRecord);
                    } else {
                        followOrder.setNetPositionSum(0.0);
                        followOrder.setNetPositionHoldNumber(0.0);
                    }
                    log.debug("交易信息返回，修改净头寸跟单：followOrderName{},netPositionStatus{},netPositionHoldNumber{}"+
                            followOrder.getFollowOrderName()+"、" +
                          followOrder.getNetPositionStatus()+"、"+followOrder.getNetPositionHoldNumber()  );
                    followOrderService.updateFollowOrder(followOrder);

                }
                //设置修改时间
                tradeRecord.setUpdateDate(DateUtil.getStringDate());
                followOrderTradeRecordDao.updateByPrimaryKey(tradeRecord);
            }
        }

    }

    /*
     *
     * tars框架出错或者返回码不为0的情况
     * */
    @Override
    public void updateRecordByTradeFail(Long tradeRecordId) {
        FollowOrderTradeRecord tradeRecord = this.getTradeRecord(tradeRecordId);
        //设置跟单状态
        ClientNetPosition clientNetPosition = clientNetPositionService.getClientNetPosition(tradeRecord.getClientNetPositionId());

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

    }

    /*
     *   跟单总交易数
     *  @param userCode:是否已userCode为一组进行查询：true为是
     * */
    @Override
    public List<FollowOrderVo> getFollowOrderTradeTotalCount(Long followOrderId, boolean userCode, String endTime, String startTime) {
        return followOrderTradeRecordDao.getFollowOrderTradeTotalCount(followOrderId, userCode, endTime, startTime);
    }

    /*
     *   跟单总交易成功数
     *  @param userCode:是否已userCode为一组进行查询：true为是
     * */
    @Override
    public List<FollowOrderVo> getFollowOrderSuccessTradeTotal(Long followOrderId, boolean userCode, String endTime, String startTime) {
        return followOrderTradeRecordDao.getFollowOrderSuccessTradeTotal(followOrderId, userCode, endTime, startTime);
    }

    @Override
    public List<FollowOrderTradeRecord> getListOrderTradeByTicketAndTime(String ticket, String time) {
        return followOrderTradeRecordDao.getListOrderTradeByTicketAndTime(ticket, time);
    }

    /*
     *
     * 跟每单的客户数据展示列表
     * */
    @Override
    public List<Map<String, Object>> getListClient(Long followOrderId, Integer status, String clientName, Integer openOrCloseStatus) {
        List<Map<String, Object>> orderUserList = new ArrayList<>();

        List<String> userCode = followOrderClientService.getListUserCodeByFollowOrderId(followOrderId); //查找该跟单下的客户编号
        if (!"-1".equals(clientName)) {
            //-1:代表查询全部客户
            userCode = new ArrayList<>();
            userCode.add(clientName);
        }
        FollowOrder followOrder = followOrderService.getFollowOrder(followOrderId);
        List<OrderUser> userList = orderUserService.findByUserIdList(userCode, followOrder.getStartTime(), null, followOrder.getVariety().getVarietyCode(), openOrCloseStatus);
        for (OrderUser orderUser : userList) {
            if (orderUser.getOpenTime() != null) {
                Map<String, Object> orderUserMap = new HashMap<>();
                //格式化时间
                if (orderUser.getOpenTime() != null) {
                    orderUser.setOpenTime(DateUtil.strToStr(orderUser.getOpenTime()));
                }
                if (orderUser.getCloseTime() != null) {
                    orderUser.setCloseTime(DateUtil.strToStr(orderUser.getCloseTime()));
                }
                orderUserMap.put("orderUser", orderUser);
                //查找对应的明细
                FollowOrderDetail detail = followOrderDetailService.getFollowOrderDetailByTicket(orderUser.getTicket(), followOrderId);
                //查找对应的交易记录，如果没有就是跟每单的固定手数，客户拆手数进行交易
                if (detail != null) {
                    orderUserMap.put("detailId", detail.getId());
                    orderUserMap.put("status", FollowOrderEnum.FollowStatus.FOLLOW_ORDER_BY_CLIENT.getIndex());

                } else {
                    orderUserMap.put("status", FollowOrderEnum.FollowStatus.NOT_FOLLOW_ORDER_BY_CLIENT.getIndex());
                }
                //条件查询
                if (status.equals(orderUserMap.get("status"))) {
                    orderUserList.add(orderUserMap);
                } else if (status.equals(-1)) {
                    orderUserList.add(orderUserMap);
                }
            }
        }
        return orderUserList;
    }

    /*
     * 净头寸跟单的客户数据
     * @param followOrderId：跟单id
     * @return
     */
    public List<NetPositionDetailVo> getListClientNetPosition(Long followOrderId, Integer status, String clientName, Integer openOrCloseStatus) {
        List<String> userCode = followOrderClientService.getListUserCodeByFollowOrderId(followOrderId); //正式
        if (!"-1".equals(clientName)) {
            //-1:代表查询全部客户
            userCode = new ArrayList<>();
            userCode.add(clientName);
        }
        FollowOrder followOrder = followOrderService.getFollowOrder(followOrderId);
        List<OrderUser> userList = orderUserService.findByUserIdList(userCode, followOrder.getStartTime(), null, followOrder.getVariety().getVarietyCode(), openOrCloseStatus);
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
                if (clientNetPositionClose != null && clientNetPositionOpen != null) {


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
                }

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


    /*
     *
     * 跟单明细中跟单数据映射
     * */
    @Override
    public List<Map<String, Object>> getListClientFollowOrderTrade(String endTime, String startTime, Long followOrderId) {
        if (endTime != null && !endTime.isEmpty()) {
            endTime = DateUtil.dateToStrLong(DateUtil.getEndDate(DateUtil.strToDate(endTime)));
        }
        List<Map<String, Object>> orderUserTrade = new ArrayList<>();
        //以userCode为一组，查找交易成功数、
        List<FollowOrderVo> successTradeTotal = getFollowOrderSuccessTradeTotal(followOrderId, true, endTime, startTime);
        //以userCode为一组，查找交易总数、
        List<FollowOrderVo> tradeTotalCount = getFollowOrderTradeTotalCount(followOrderId, true, endTime, startTime);
        FollowOrder followOrder = followOrderService.getFollowOrder(followOrderId);
        for (FollowOrderVo followOrderVo : tradeTotalCount) {
            for (FollowOrderVo orderVo : successTradeTotal) {
                if (orderVo.getClientName().equals(followOrderVo.getClientName())) {
                    //用户名一致，修改跟单成功数
                    followOrderVo.setSuccessTotal(orderVo.getSuccessTotal());//成功数
                }
            }
        }
        for (FollowOrderVo followOrderVo : tradeTotalCount) {

            List<FollowOrderDetail> detail = followOrderDetailService.getFollowOrderDetailByUserCode(followOrderId,
                    endTime, startTime, followOrderVo.getClientName());
            for (FollowOrderDetail followOrderDetail : detail) {
                //客户盈亏
                followOrderVo.setClientProfit(DoubleUtil.add(followOrderVo.getClientProfit(), followOrderDetail.getClientProfit()));
                //手续费
                followOrderVo.setPoundageTotal(DoubleUtil.add(followOrderVo.getPoundageTotal(), followOrderDetail.getPoundage()));
                if (followOrderDetail.getCloseTime() == null) {
                    //持仓盈亏
                    followOrderVo.setPositionGainAndLoss(DoubleUtil.add(followOrderVo.getPositionGainAndLoss(), followOrderDetail.getProfitLoss()));
                } else {
                    //平仓盈亏
                    followOrderVo.setOffsetGainAndLoss(DoubleUtil.add(followOrderVo.getOffsetGainAndLoss(), followOrderDetail.getProfitLoss()));
                }
            }
            Map<String, Object> orderUserMap = new HashMap<>();
            orderUserMap.put("clientName", followOrderVo.getClientName());//客户姓名
            orderUserMap.put("varietyCode", followOrder.getVariety().getVarietyCode());//品种
            orderUserMap.put("winRate", followOrderVo.getSuccessTotal() + "/" + followOrderVo.getAllTotal());//成功率
            orderUserMap.put("clientProfit", followOrderVo.getClientProfit());//客户盈亏
            orderUserMap.put("poundageTotal", followOrderVo.getPoundageTotal());//手续费
            orderUserMap.put("positionGainAndLoss", followOrderVo.getPositionGainAndLoss());//持仓盈亏
            orderUserMap.put("offsetGainAndLoss", followOrderVo.getOffsetGainAndLoss());//平仓盈亏
            FollowOrderClient client = followOrderClientService.findClientByIdAndName(followOrderId, followOrderVo.getClientName());
            orderUserMap.put("followDirection", client.getFollowDirection());//跟单方向
            orderUserMap.put("handNumberType", client.getHandNumberType());//手数类型
            if (client.getHandNumberType().equals(FollowOrderEnum.FollowStatus.CLIENT_HAND_NUMBER_TYPE.getIndex())) {
                orderUserMap.put("followHandNumber", client.getFollowHandNumber());//跟多少手
            } else {
                orderUserMap.put("followHandNumber", "1:" + client.getFollowHandNumber());
            }
            orderUserTrade.add(orderUserMap);
        }

        return orderUserTrade;
    }
}

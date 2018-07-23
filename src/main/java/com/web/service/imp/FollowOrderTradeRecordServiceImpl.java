package com.web.service.imp;

import com.web.dao.FollowOrderTradeRecordDao;
import com.web.database.OrderHongKongService;
import com.web.database.entity.PlatFromUsers;
import com.web.pojo.*;

import com.web.pojo.vo.followOrder.FollowOrderVo;
import com.web.pojo.vo.followOrder.NetPositionDetailVo;
import com.web.pojo.vo.OrderMsgResult;
import com.web.schedule.SweepTableSchedule;
import com.web.service.*;
import com.web.common.FollowOrderEnum;
import com.web.util.common.DateUtil;
import com.web.util.common.DoubleUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * Created by may on 2018/5/20.
 */
@Service
@Transactional
@SuppressWarnings("all")
public class FollowOrderTradeRecordServiceImpl implements FollowOrderTradeRecordService {
    private static Logger log = LogManager.getLogger(FollowOrderTradeRecordServiceImpl.class.getName());
    @Autowired
    private FollowOrderTradeRecordDao followOrderTradeRecordDao;
    @Autowired
    private FollowOrderService followOrderService;
    @Autowired
    private FollowOrderClientService followOrderClientService;
    @Autowired
    private OrderUserService orderUserService;
    @Autowired
    private FollowOrderDetailService followOrderDetailService;
    @Autowired
    private ClientNetPositionService clientNetPositionService;
    @Autowired
    private ContractInfoService contractInfoService;
    @Autowired
    private UselessTicketService uselessTicketService;

    @Override
    public void save(FollowOrderTradeRecord followOrderTradeRecord) {
        log.debug("新增一条交易记录：handNumber{},followOrderId{},ticket{},"
                + followOrderTradeRecord.getHandNumber() + "、" + followOrderTradeRecord.getFollowOrderId() + "、"
                + followOrderTradeRecord.getTicket());
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
                + followOrderTradeRecord.getHandNumber() + "、" + followOrderTradeRecord.getFollowOrderId() + "、"
                + followOrderTradeRecord.getTicket() + "、" + followOrderTradeRecord.getMarketPrice() + "、" + followOrderTradeRecord.getTradeTime());
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
                        followOrder.setNetPositionHoldNumber(0);
                    }
                    followOrderService.updateFollowOrder(followOrder);
                    log.debug("交易信息返回，修改净头寸跟单：followOrderName{},netPositionStatus{},netPositionHoldNumber{}" +
                            followOrder.getFollowOrderName() + "、" +
                            followOrder.getNetPositionStatus() + "、" + followOrder.getNetPositionHoldNumber());

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
        tradeRecord.setHandNumber(0);
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
     *
     *找到对应跟单得交易记录
     * */

    public List<FollowOrderTradeRecord> findListFollowOrderTradeRecord(Long followOrderId, String endTime, String startTime) {
        return followOrderTradeRecordDao.findListFollowOrderTradeRecord(followOrderId, endTime, startTime);
    }


    @Override
    public List<FollowOrderTradeRecord> getListOrderTradeByTicketAndTime(String ticket, String time) {
        return followOrderTradeRecordDao.getListOrderTradeByTicketAndTime(ticket, time);
    }

    /*
     *
     * 跟每单的客户数据展示列表
     * @param status:是否跟单（已跟单/未跟单）
     * @param followOrderId:策略Id
     * @parm openOrCloseStatus:是否平仓
     * */
    @Override
    public List<Map<String, Object>> getListClient(Long followOrderId, Integer status, Long followOrderClientId, Integer openOrCloseStatus) {
        List<Map<String, Object>> orderUserList = new ArrayList<>();
        FollowOrder followOrder = followOrderService.getFollowOrder(followOrderId);
        List<OrderUser> userList = findOrderUserByFollowOrder(followOrderClientId, followOrder, openOrCloseStatus);
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
                String userName = followOrderClientService.getUserName(orderUser.getUserCode(), orderUser.getPlatFormCode());
                orderUserMap.put("clientName", userName);

                //查找对应的明细
                FollowOrderDetail detail = followOrderDetailService.getFollowOrderDetailByTicket(orderUser.getTicket(), followOrderId);
                //查找对应的交易记录，如果没有就是跟每单的固定手数，客户拆手数进行交易
                if (detail != null) {
                    orderUserMap.put("detailId", detail.getId());
                    orderUserMap.put("status", FollowOrderEnum.FollowStatus.FOLLOW_ORDER_BY_CLIENT.getIndex());

                } else {
                    UselessTicket ticket = uselessTicketService.getUselessTicketByTicket(orderUser.getTicket());
                    if (ticket != null) {
                        orderUserMap.put("status", FollowOrderEnum.FollowStatus.FOLLOW_ORDER_BY_CLIENT_NO_NEED.getIndex());
                    } else {
                        orderUserMap.put("status", FollowOrderEnum.FollowStatus.NOT_FOLLOW_ORDER_BY_CLIENT.getIndex());
                    }
                }
                //条件查询
                if (status.equals(orderUserMap.get("status"))) {
                    orderUserList.add(orderUserMap);
                } else if (status.equals(-1)) {
                    //-1：代表查询全部
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
    public List<NetPositionDetailVo> getListClientNetPosition(Long followOrderId, Integer status, Long followOrderClientId, Integer openOrCloseStatus) {
//        List<String> userCode = followOrderClientService.getListUserCodeByFollowOrderId(followOrderId); //正式
        FollowOrder followOrder = followOrderService.getFollowOrder(followOrderId);
        //找到符合的客户数据
        List<OrderUser> userList = findOrderUserByFollowOrder(followOrderClientId, followOrder, openOrCloseStatus);
        List<NetPositionDetailVo> netPositionDetailVoList = new ArrayList<>();

        for (OrderUser orderUser : userList) {
            NetPositionDetailVo netPositionDetailVo = new NetPositionDetailVo();
            netPositionDetailVo.setVarietyName(orderUser.getProductCode());//设置品种名字
            netPositionDetailVo.setHandNumber(orderUser.getHandNumber() + "");//设置手数
            String userName = followOrderClientService.getUserName(orderUser.getUserCode(), orderUser.getPlatFormCode());
            netPositionDetailVo.setUserName(userName);//设置用户名

            netPositionDetailVo.setTradeDirection(orderUser.getLongShort());//设置多空方向
            netPositionDetailVo.setPoundage(orderUser.getCommission());//设置手续费
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

                    netPositionDetailVoClose.setUserName(netPositionDetailVo.getUserName());

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
                    netPositionDetailVoClose.setPoundage(orderUser.getCommission());//手续费
                    netPositionDetailVoClose.setProfit(orderUser.getProfit());//客户盈亏
                    netPositionDetailVoClose.setNetPositionSum(clientNetPositionClose.getNetPositionSum());//净头寸
                    netPositionDetailVoClose.setFollowOrderClientStatus(clientNetPositionClose.getStatus());//跟单是否成功
                    netPositionDetailVoClose.setOpenCloseType(FollowOrderEnum.FollowStatus.CLOSE.getIndex());//开平设置为平仓
                    //该方法是：当交易系统没有返回交易信息时，交易记录无法进行跟新，导致无法知悉是否跟单成功时，需要进行计算更改状态
                    checkFollowClientOrderStatus(netPositionDetailVoClose, orderUser, followOrder);

                    //重置
                    netPositionDetailVo.setNetPositionSum(clientNetPositionOpen.getNetPositionSum());//设置净头寸
                    netPositionDetailVo.setFollowOrderClientStatus(clientNetPositionOpen.getStatus());//设置状态

                    //该方法是：当交易系统没有返回交易信息时，交易记录无法进行跟新，导致无法知悉是否跟单成功时，需要进行计算更改状态
                    checkFollowClientOrderStatus(netPositionDetailVoClose, orderUser, followOrder);

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
                    netPositionDetailVo.setPoundage(orderUser.getCommission());//手续费
                    //该方法是：当交易系统没有返回交易信息时，交易记录无法进行跟新，导致无法知悉是否跟单成功时，需要进行计算更改状态
                    checkFollowClientOrderStatus(netPositionDetailVo, orderUser, followOrder);

                    findClientByStatus(status, netPositionDetailVo, netPositionDetailVoList);
                }
            }

        }
        return netPositionDetailVoList;
    }

    /*
     * 当交易系统没有返回交易信息时，交易记录无法进行跟新，导致无法知悉是否跟单成功时，需要进行计算更改状态
     * */
    private void checkFollowClientOrderStatus(NetPositionDetailVo netPositionDetailVo, OrderUser orderUser, FollowOrder followOrder) {
        //已经计算好得净头寸值
        Double netPositionSum = netPositionDetailVo.getNetPositionSum();
        //还没有进行计算得净头寸
        Double sum;

        if (orderUser.getLongShort().equals(FollowOrderEnum.FollowStatus.SELL.getIndex())) {
            //该数据交易方向为空是：sum = netPositionSum + 交易得手数
            sum = DoubleUtil.add(netPositionSum, orderUser.getHandNumber());//本来金头寸的值
        } else {
            //该数据交易方向为多是：sum = netPositionSum - 交易得手数
            sum = DoubleUtil.sub(netPositionSum, orderUser.getHandNumber());//本来金头寸的值
        }
        //oldNum 原始持仓手数
        int oldNum = (int) (sum / followOrder.getNetPositionChange());
        // 现有得持仓手数
        int newOldNum = (int) (netPositionSum / followOrder.getNetPositionChange());
        if (oldNum != newOldNum && netPositionDetailVo.getFollowOrderClientStatus() == null) {
            //oldNum!=newOldNum，并且该状态没有被记录时就更改为跟单失败
            netPositionDetailVo.setFollowOrderClientStatus(FollowOrderEnum.FollowStatus.NOT_FOLLOW_ORDER_BY_CLIENT.getIndex());
        }
    }

    /*
    * 查找客户数据
    * */
    private List<OrderUser> findOrderUserByFollowOrder(Long followOrderClientId,FollowOrder followOrder,Integer openOrCloseStatus){
        //todo 是哪个数据源不清楚
        ContractInfo info = contractInfoService.getInfoByVarietyIdAndPlatformId(followOrder.getVariety().getId(), 2L);
        List<OrderUser> userList = new ArrayList<>();

        if (followOrderClientId != -1) {
            FollowOrderClient followOrderClient = followOrderClientService.getFollowOrderClient(followOrderClientId);
            //同一个客户在不同时间段的客户交易数据
            sameUserDifferentTimePeriods(userList,followOrderClient,info.getContractCode(),openOrCloseStatus,followOrder.getId());
        } else {
            //-1:代表查询全部客户，状态为null:查找所有删除或者未删除的客户

            List<FollowOrderClient> FollowOrderClient = followOrderClientService.getListByFollowOrderId(followOrder.getId(),null); //查找该跟单下的客户编号
            for (FollowOrderClient orderClient : FollowOrderClient) {
                //同一个客户在不同时间段的客户交易数据
                sameUserDifferentTimePeriods(userList,orderClient,info.getContractCode(),openOrCloseStatus,followOrder.getId());
            }
        }

        return userList;
    }
    /*
    * 同一个客户，根据不同的开始时间和删除时间获取客户数据
    * */
     private void sameUserDifferentTimePeriods (List<OrderUser> userList,FollowOrderClient orderClient,String contractCode,Integer openOrCloseStatus,Long followOrderId){
         List<FollowOrderClient> followOrderClientList = followOrderClientService.getAllByUserCodeAndPlatformCode(orderClient.getUserCode(), orderClient.getPlatformCode(), followOrderId);
         List<String> userCode1 = new ArrayList<>();
         userCode1.add(orderClient.getUserCode());
         if(followOrderClientList.size()!=0){
             for (FollowOrderClient orderClient1 : followOrderClientList) {
                 if(orderClient1.getDeleteDate() != null){
                     //查找上一回开始-删除的时间段的客户数据
                     List<OrderUser> userIdList = orderUserService.findByUserIdList(userCode1, orderClient1.getCreateDate(),
                             orderClient1.getDeleteDate(), contractCode, openOrCloseStatus, orderClient1.getPlatformCode());
                     userList.addAll(userIdList);
                 }else {
                     //查找该客户，从添加时间-到现在的时间段的客户数据
                     List<OrderUser> userIdList = orderUserService.findByUserIdList(userCode1, orderClient1.getCreateDate(),
                             null, contractCode, openOrCloseStatus, orderClient1.getPlatformCode());
                     userList.addAll(userIdList);
                 }
             }
         }
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

        Map<Long, FollowOrderVo> map = tradeTotalCount(endTime,startTime,followOrderId,false);


        for (FollowOrderVo followOrderVo : map.values()) {
            List<FollowOrderDetail> detail = followOrderDetailService.getFollowOrderDetailByUserCode(followOrderId,
                    null, null, followOrderVo.getFollowOrderClient().getId());
            for (FollowOrderDetail followOrderDetail : detail) {
                //客户盈亏
                followOrderVo.setClientProfit(DoubleUtil.add(followOrderVo.getClientProfit(), followOrderDetail.getClientProfit()));
                //手续费
                followOrderVo.setPoundageTotal(DoubleUtil.add(followOrderVo.getPoundageTotal(), followOrderDetail.getPoundage()));
                if (followOrderDetail.getCloseTime() == null) {
                    //持仓盈亏
                    Map<String, Double> askAndBid = SweepTableSchedule.getAskAndBidByFollowOrderId(followOrderId);
                    if (askAndBid != null) {
                        Double ask = askAndBid.get("ask");
                        Double bid = askAndBid.get("bid");
                        if (followOrderDetail.getTradeDirection().equals(FollowOrderEnum.FollowStatus.BUY.getIndex())) {
                            //交易方向为多，查询：卖出价（ask）-->卖出价（逸富）-开仓价（detail）
                            followOrderVo.setPositionGainAndLoss(DoubleUtil.add(followOrderVo.getPositionGainAndLoss(),
                                    DoubleUtil.mul(DoubleUtil.sub(ask, followOrderDetail.getOpenPrice()), followOrderDetail.getHandNumber())));
                        } else {
                            //交易方向为空，查询：买入价（bid）-->开仓价（detail）-买入价（逸富）
                            followOrderVo.setPositionGainAndLoss(DoubleUtil.add(followOrderVo.getPositionGainAndLoss(),
                                    DoubleUtil.mul(DoubleUtil.sub(followOrderDetail.getOpenPrice(), bid), followOrderDetail.getHandNumber())));
                        }
                    }
                } else {
                    //平仓盈亏
                    followOrderVo.setOffsetGainAndLoss(DoubleUtil.add(followOrderVo.getOffsetGainAndLoss(), followOrderDetail.getProfitLoss()));
                }
            }
            Map<String, Object> orderUserMap = new HashMap<>();
            orderUserMap.put("winRate", followOrderVo.getSuccessTotal() + "/" + followOrderVo.getAllTotal());//成功率
            orderUserMap.put("followOrderVo", followOrderVo);//
            FollowOrderClient client = followOrderVo.getFollowOrderClient();
            String userName = followOrderClientService.getUserName(client.getUserCode(), client.getPlatformCode());
            orderUserMap.put("clientName", userName);//客户姓名
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

    /*
    *
    * 统计总跟单数 & 总跟单成功数
    * orderOrClient：为true:统计策略
    * 为false:统计每个客户得总跟单数 & 总跟单成功数
    *
    * */
    public Map<Long, FollowOrderVo> tradeTotalCount(String endTime, String startTime, Long followOrderId, boolean orderOrClient) {
        List<FollowOrderTradeRecord> tradeRecord = findListFollowOrderTradeRecord(followOrderId, endTime, startTime);
        Map<Long, FollowOrderVo> mapClient = new HashMap<>();
        FollowOrderVo orderVo1 = new FollowOrderVo();
        if(tradeRecord.size()!=0){
            for (FollowOrderTradeRecord record : tradeRecord) {
                if (!orderOrClient) {
                    orderVo1 = mapClient.get(record.getFollowOrderClient().getId());
                }
                if (orderVo1 == null) {
                    orderVo1 = new FollowOrderVo();
                }
                if (record.getHandNumber() != 0 && record.getTradeTime() != null) {
                    orderVo1.setAllTotal(orderVo1.getAllTotal() + 1);
                    orderVo1.setSuccessTotal(orderVo1.getSuccessTotal() + 1);
                } else if (record.getHandNumber() == 0 || record.getTradeTime() == null) {
                    orderVo1.setAllTotal(orderVo1.getAllTotal() + 1);
                }
                if (!orderOrClient) {
                    orderVo1.setFollowOrderClient(record.getFollowOrderClient());
                    mapClient.put(record.getFollowOrderClient().getId(), orderVo1);
                }

            }
        }
        if (orderOrClient) {
            mapClient.put(followOrderId, orderVo1);
        }

        return mapClient;

    }
}

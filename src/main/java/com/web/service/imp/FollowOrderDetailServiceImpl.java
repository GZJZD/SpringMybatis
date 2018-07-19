package com.web.service.imp;

import com.web.common.FollowOrderEnum;
import com.web.dao.FollowOrderDetailDao;
import com.web.database.OrderHongKongService;

import com.web.database.entity.PlatFromUsers;
import com.web.pojo.*;

import com.web.pojo.vo.followOrder.FollowOrderVo;
import com.web.pojo.vo.followOrder.NetPositionDetailVo;
import com.web.pojo.vo.OrderMsgResult;
import com.web.schedule.SweepTableSchedule;
import com.web.service.*;
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
 * Created by may on 2018/5/23.
 */
@Service
@Transactional
public class FollowOrderDetailServiceImpl implements FollowOrderDetailService {
    private static Logger log = LogManager.getLogger(FollowOrderDetailServiceImpl.class.getName());
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
    @Autowired
    private OrderHongKongService orderHongKongService;
    @Autowired
    private ContractInfoService contractInfoService;
    @Autowired
    private UselessTicketService uselessTicketService;

    @Override
    public void save(FollowOrderDetail followOrderDetail) {
        followOrderDetailDao.insert(followOrderDetail);
    }

    @Override
    public FollowOrderDetail getFollowOrderDetailByTicket(String ticket, Long followOrderId) {
        return followOrderDetailDao.getFollowOrderDetailByTicket(ticket, followOrderId);
    }

    @Override
    public FollowOrderDetail getFollowOrderDetail(Long id) {
        return followOrderDetailDao.selectByPrimaryKey(id);
    }

    public List<FollowOrderDetail> getDetailListByFollowOrderId(Long followOrderId, String endTime, String startTime, Integer status) {

        return followOrderDetailDao.getDetailListByFollowOrderId(followOrderId, endTime, startTime, status);
    }

    /*
     *
     * 找到客户Or 净头寸明细
     * @param endTime:结束时间
     * @param startTime:开始时间
     * @param status:是否平仓
     * */
    @Override
    public List<?> findDetailList(Long followOrderId, String endTime, String startTime, Integer status) {
        if (endTime != null && !"".equals(endTime)) {
            endTime = DateUtil.dateToStrLong(DateUtil.getEndDate(DateUtil.strToDate(endTime)));
        }
        List<FollowOrderDetail> orderDetailList = this.getDetailListByFollowOrderId(followOrderId, endTime, startTime, status);
        FollowOrder followOrder = followOrderService.getFollowOrder(followOrderId);
        if (orderDetailList != null) {
            if (followOrder.getFollowManner().equals(FollowOrderEnum.FollowStatus.FOLLOWMANNER_NET_POSITION.getIndex())) {
                //净头寸
                return findNetPositionDetail(orderDetailList, followOrder);
            } else {
                //客户
                List<Map<String,Object>> clientDetailList = new ArrayList<>();
                for (FollowOrderDetail followOrderDetail : orderDetailList) {
                    Map<String,Object> clientDetail = new HashMap<>();
                    clientDetail .put("id",followOrderDetail.getId());
                    clientDetail .put("contractCode",followOrderDetail.getContractCode());
                    clientDetail .put("tradeDirection",followOrderDetail.getTradeDirection());
                    clientDetail .put("handNumber",followOrderDetail.getHandNumber());
                    clientDetail .put("originalHandNumber",followOrderDetail.getOriginalHandNumber());

                    clientDetail .put("followOrderTradeRecordId",followOrderDetail.getFollowOrderTradeRecordId());
                    clientDetail .put("followOrderClientId",followOrderDetail.getFollowOrderClientId());
                    clientDetail .put("openPrice",followOrderDetail.getOpenPrice());
                    clientDetail .put("poundage",followOrderDetail.getPoundage());
                    clientDetail .put("clientProfit",followOrderDetail.getClientProfit());
                    clientDetail .put("followOrderId",followOrderDetail.getFollowOrderId());
                    if (followOrderDetail.getOpenTime() != null) {
                        clientDetail .put("openTime",DateUtil.strToStr(followOrderDetail.getOpenTime()));
                    }
                    if (followOrderDetail.getCloseTime() != null) {
                        clientDetail .put("closeTime",DateUtil.strToStr(followOrderDetail.getCloseTime()));
                        clientDetail .put("closePrice",followOrderDetail.getClosePrice());
                    }

                    PlatFromUsers users;
                    FollowOrderClient client = followOrderClientService.getFollowOrderClient(followOrderDetail.getFollowOrderClientId());

                    if(client.getPlatformCode().equals("orders75")){
                        users = orderHongKongService.getUser75(client.getUserCode());
                    }else {
                        users = orderHongKongService.getUser76(client.getUserCode());
                    }
                    if(users!=null){

                        clientDetail.put("clientName",users.getNAME());
                    }
                    if (followOrderDetail.getCloseTime() == null) {
                        Map<String, Double> askAndBid = SweepTableSchedule.getAskAndBidByFollowOrderId(followOrderId);
                        if (askAndBid != null) {
                            if (followOrderDetail.getTradeDirection().equals(FollowOrderEnum.FollowStatus.SELL.getIndex())) {
                                //交易方向为空：获取买入价：bid
                                //持仓盈亏
                                followOrderDetail.setProfitLoss(DoubleUtil.mul(DoubleUtil.sub(followOrderDetail.getOpenPrice(), askAndBid.get("bid")), followOrderDetail.getHandNumber()));

                            } else {
                                //交易方向为多：获取卖出价：ask
                                //持仓盈亏
                                followOrderDetail.setProfitLoss(DoubleUtil.mul(DoubleUtil.sub(askAndBid.get("ask"), followOrderDetail.getOpenPrice()), followOrderDetail.getHandNumber()));

                            }
                        } else {

                            followOrderDetail.setProfitLoss(0.0);
                        }
                    }
                    clientDetail .put("profitLoss",followOrderDetail.getProfitLoss());
                    clientDetailList.add(clientDetail);
                }
                return clientDetailList;
            }
        }
        return null;
    }

    /*
     *
     * 返回净头寸明细
     * */
    private List<NetPositionDetailVo> findNetPositionDetail(List<FollowOrderDetail> orderDetailList, FollowOrder followOrder) {
        List<NetPositionDetailVo> netPositionDetailVos = new ArrayList<>();
        for (FollowOrderDetail followOrderDetail : orderDetailList) {
            FollowOrderTradeRecord tradeRecord = followOrderTradeRecordService.getTradeRecord(followOrderDetail.
                    getFollowOrderTradeRecordId());
            ClientNetPosition clientNetPosition = clientNetPositionService.getClientNetPosition(tradeRecord.getClientNetPositionId());

            NetPositionDetailVo netPositionDetailVo = new NetPositionDetailVo();
            //设置净头寸
            netPositionDetailVo.setNetPositionSum(clientNetPosition == null ? followOrder.getNetPositionSum() : clientNetPosition.getNetPositionSum());
            //设置品种
            Variety variety = varietyService.getVariety(tradeRecord.getVarietyId());
            netPositionDetailVo.setVarietyName(variety.getVarietyName());
            //设置开平
            netPositionDetailVo.setOpenCloseType(tradeRecord.getOpenCloseType());
            //设置手数
            if (!followOrderDetail.getHandNumber().equals(followOrderDetail.getOriginalHandNumber()) &&
                    followOrderDetail.getOriginalHandNumber() != null) {
                netPositionDetailVo.setHandNumber(followOrderDetail.getHandNumber() +
                        "(" + followOrderDetail.getOriginalHandNumber() + ")");
            } else {
                netPositionDetailVo.setHandNumber((followOrderDetail.getHandNumber() == null ? 0 : followOrderDetail.getHandNumber()) + "");
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
            netPositionDetailVo.setRemainHandNumber(followOrderDetail.getRemainHandNumber());
            //设置明细的id
            netPositionDetailVo.setDetailId(followOrderDetail.getId());
            //设置盈亏
            netPositionDetailVo.setProfit(followOrderDetail.getProfitLoss());
            FollowOrderClient followOrderClient = followOrderClientService.getFollowOrderClient(followOrderDetail.getFollowOrderClientId());
            PlatFromUsers users;
            if(followOrderClient.getPlatformCode().equals("orders75")){
                users = orderHongKongService.getUser75(followOrderClient.getUserCode());
            }else {
                users = orderHongKongService.getUser76(followOrderClient.getUserCode());
            }
            if(users!=null){
                netPositionDetailVo.setUserName(users.getNAME());
            }
            //剩下得手数
            if (netPositionDetailVo.getRemainHandNumber()!=null&&netPositionDetailVo.getRemainHandNumber() != 0) {
                Map<String, Double> askAndBid = SweepTableSchedule.getAskAndBidByFollowOrderId(followOrder.getId());
                if (askAndBid != null) {
                    if (tradeRecord.getTradeDirection().equals(FollowOrderEnum.FollowStatus.BUY.getIndex())) {
                        //交易方向为多，查询：卖出价（ask）
                        netPositionDetailVo.setProfit(DoubleUtil.mul(DoubleUtil.sub(askAndBid.get("ask"), netPositionDetailVo.getMarketPrice()), netPositionDetailVo.getRemainHandNumber()));
//                        log.debug(netPositionDetailVo.getDetailId()+"L明细盈亏:"+netPositionDetailVo.getProfit()+",ask:"+askAndBid.get("ask"));

                    } else {
                        //交易方向为空，查询：买入价（bid）
                        netPositionDetailVo.setProfit(DoubleUtil.mul(DoubleUtil.sub(netPositionDetailVo.getMarketPrice(), askAndBid.get("bid")), netPositionDetailVo.getRemainHandNumber()));

                    }
                } else {
                    netPositionDetailVo.setProfit(0.0);
                }
            }
            netPositionDetailVos.add(netPositionDetailVo);
        }
        return netPositionDetailVos;
    }

    @Override
    public List<FollowOrderDetail> getDetailListByOrderIdAndDirection(Long followOrderId, Integer direction) {
        return followOrderDetailDao.getDetailListByOrderIdAndDirection(followOrderId, direction);
    }



    @Override
    public void updateDetail(FollowOrderDetail followOrderDetail) {
        int count = followOrderDetailDao.updateByPrimaryKey(followOrderDetail);
        if (count <= 0) {
            log.debug("");
            throw new RuntimeException("乐观锁异常：" + FollowOrderDetail.class);
        }
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

        FollowOrderClient followOrderClient  = followOrderTradeRecord.getFollowOrderClient();


        if (detail != null && detail.getCloseTime() == null) {
            log.debug("跟每单客户平仓：ticket{}," + detail.getTicket());
            //剩下手数
            int hand = detail.getHandNumber() - followOrderTradeRecord.getHandNumber();
            if (!followOrderTradeRecord.getTicket().equals(followOrderTradeRecord.getNewTicket()) &&
                    !followOrderClient.getHandNumberType().equals(FollowOrderEnum.FollowStatus.CLIENT_HAND_NUMBER_TYPE.getIndex())
                    && hand!=0) {
                //开仓单号和新开仓单号不一致 和不是固定手数
                //新建开仓单号

                Double poundage = DoubleUtil.div(detail.getPoundage(), detail.getHandNumber(), 2);//每一手的手续费
                FollowOrderDetail detailNew = new FollowOrderDetail();
                detailNew.setHandNumber(hand);
                detailNew.setOriginalHandNumber(detailNew.getHandNumber());
                detailNew.setTicket(followOrderTradeRecord.getNewTicket());
                detailNew.setPoundage(DoubleUtil.mul(poundage, detailNew.getHandNumber()));
                detailNew.setCreateDate(DateUtil.getStringDate());
                detailNew.setOpenPrice(detail.getOpenPrice());
                detailNew.setOpenTime(followOrderTradeRecord.getTradeTime());
                detailNew.setFollowOrderId(detail.getFollowOrderId());
                detailNew.setTradeDirection(detail.getTradeDirection());
                detailNew.setContractCode(detail.getContractCode());
                detailNew.setFollowOrderTradeRecordId(detail.getFollowOrderTradeRecordId());
                detailNew.setFollowOrderClientId(detail.getFollowOrderClientId());
                detailNew.setAccountId(detail.getAccountId());
                save(detailNew);
                log.debug("客户新开仓明细：newTicket{},handNumber{}," + detailNew.getTicket() + "," + detailNew.getHandNumber());
                detail.setHandNumber(followOrderTradeRecord.getHandNumber());
                detail.setOriginalHandNumber(followOrderTradeRecord.getHandNumber());
                detail.setPoundage(DoubleUtil.mul(poundage, detail.getHandNumber()));
            }
            if(!followOrderTradeRecord.getTicket().equals(followOrderTradeRecord.getNewTicket())&&
                    (followOrderClient.getHandNumberType().equals(FollowOrderEnum.FollowStatus.CLIENT_HAND_NUMBER_TYPE.getIndex()) || hand ==0 )){
                //当两个Ticket不一样时，而且是固定手数or 相减等于0时，代表newTicket是要保存在无需跟单的表中
                UselessTicket uselessTicket = new UselessTicket();
                uselessTicket.setTicket(followOrderTradeRecord.getNewTicket());
                uselessTicket.setCreateDate(DateUtil.getStringDate());
                uselessTicketService.save(uselessTicket);
            }
            //设置平仓时间
            detail.setCloseTime(orderMsgResult.getTradeDate() + orderMsgResult.getTradeTime());
            //设置平仓价格
            detail.setClosePrice(orderMsgResult.getTradePrice());
            //设置平仓盈亏
            detail.setProfitLoss(DoubleUtil.sub(DoubleUtil.mul(Double.valueOf(detail.getHandNumber()),
                    detail.getClosePrice()), DoubleUtil.mul(Double.valueOf(detail.getHandNumber()), detail.getOpenPrice())));
            //手续费
            detail.setPoundage(DoubleUtil.add(detail.getPoundage(), orderMsgResult.getTradeCommission()));
            //交易方向
            detail.setTradeDirection(followOrderTradeRecord.getTradeDirection());
            //设置客户盈亏
            OrderUser orderUser = orderUserService.findByTicket(detail.getTicket());
            detail.setClientProfit(orderUser.getProfit());
            updateDetail(detail);
            log.debug("客户平仓明细：ticket{},handNumber{},id{},closePrice{},followOrderId{}," + detail.getTicket() + "," + detail.getHandNumber() + "," + detail.getId() + "," + detail.getClosePrice()
                    + "," + detail.getFollowOrderId());

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
        Integer tradeHandNumber;
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
                Integer detailHandNumber = followOrderDetail.getRemainHandNumber();
                if (detailHandNumber > tradeHandNumber) {
                    tradePrice = DoubleUtil.mul(Double.valueOf(tradeHandNumber), followOrderTradeRecord.getMarketPrice());
                    detailPrice = DoubleUtil.mul(Double.valueOf(tradeHandNumber), followOrderDetail.getOpenPrice());
                    followOrderDetail.setRemainHandNumber(detailHandNumber - tradeHandNumber);
                } else {
                    followOrderDetail.setRemainHandNumber(0);
                    tradePrice = DoubleUtil.mul(Double.valueOf(detailHandNumber), followOrderTradeRecord.getMarketPrice());
                    detailPrice = DoubleUtil.mul(Double.valueOf(detailHandNumber), followOrderDetail.getOpenPrice());
                    tradeHandNumber = tradeHandNumber - detailHandNumber;
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
        if (followOrder.getFollowManner().equals(FollowOrderEnum.FollowStatus.FOLLOWMANNER_NET_POSITION.getIndex()) && followOrderTradeRecord.getOpenCloseType().equals(FollowOrderEnum.FollowStatus.OPEN.getIndex())) {
            //设置剩下手数
            orderDetail.setRemainHandNumber(orderDetail.getHandNumber());
        }
        orderDetail.setAccountId(followOrder.getAccount().getId());
        //设置原来手数
        orderDetail.setOriginalHandNumber(followOrderTradeRecord.getHandNumber());
        //设置交易方向
        orderDetail.setTradeDirection(followOrderTradeRecord.getTradeDirection());
        //设置品种的名称
        varietyService.getVariety(followOrderTradeRecord.getVarietyId());
        ContractInfo contractInfo = contractInfoService.getInfoByVarietyIdAndPlatformId(followOrderTradeRecord.getVarietyId(), followOrder.getAccount().getPlatform().getId());
        orderDetail.setContractCode(contractInfo.getContractCode());
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
        //客户姓名
        OrderUser orderUser = orderUserService.findByTicket(orderDetail.getTicket());
        orderDetail.setFollowOrderClientId(followOrderTradeRecord.getFollowOrderClient().getId());
        //客户的盈亏
        orderDetail.setClientProfit(orderUser.getProfit());
        save(orderDetail);
        log.debug("开/平仓明细：ticket{},handNumber{},id{},openPrice{},closePrice{},followOrderId{}," + orderDetail.getTicket() + "," + orderDetail.getHandNumber() + "," + orderDetail.getId() + "," + orderDetail.getOpenPrice()
                + "," + orderDetail.getClosePrice() + "," + orderDetail.getFollowOrderId());
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
        FollowOrder followOrder = followOrderService.getFollowOrder(followOrderTradeRecord.getFollowOrderId());
        //判断跟单方向
        if (followOrderTradeRecord != null) {
            if (followOrderTradeRecord.getOpenCloseType().equals(FollowOrderEnum.FollowStatus.CLOSE.getIndex())) {
                if (!followOrder.getFollowManner().equals(FollowOrderEnum.FollowStatus.FOLLOWMANNER_NET_POSITION.getIndex())) {
                    //客户平仓
                    createClientCloseDetail(followOrderTradeRecord, orderMsgResult);
                } else {
                    //净头寸
                    createNetPositionCloseDetail(orderDetail, followOrderTradeRecord, orderMsgResult);

                    createOpenDetail(orderDetail, followOrderTradeRecord, orderMsgResult);
                }
            } else {
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
    public List<FollowOrderDetail> getFollowOrderDetailByUserCode(Long followOrderId, String endTime, String startTime, Long followOrderClientId) {
        return followOrderDetailDao.getFollowOrderDetailByUserCode(followOrderId, endTime, startTime, followOrderClientId);
    }

    /*
     * 获取该账号的做单总数和总的平仓盈亏
     * */
    @Override
    public FollowOrderVo getAccountCountAndOffsetGainAndLossBYAccountId(Long accountId) {
        return followOrderDetailDao.getAccountCountAndOffsetGainAndLossBYAccountId(accountId);
    }
}

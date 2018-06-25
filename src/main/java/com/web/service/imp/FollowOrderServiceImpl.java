package com.web.service.imp;


import com.web.common.FollowOrderEnum;
import com.web.dao.FollowOrderDao;
import com.web.pojo.*;
import com.web.pojo.vo.FollowOrderPageVo;
import com.web.pojo.vo.FollowOrderVo;
import com.web.pojo.vo.OrderTrade;
import com.web.pojo.vo.UserLogin;
import com.web.service.*;
import com.web.util.FollowOrderGenerateUtil;
import com.web.util.common.DateUtil;
import com.web.util.common.DoubleUtil;
import com.web.util.json.WebJsion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 跟单模块
 * Created by may on 2zero18/5/8.
 */
@Service
@Transactional
@SuppressWarnings("All")
public class FollowOrderServiceImpl implements IFollowOrderService {

    //大于就是正数，小于就是负数
    private int zero = 0;
    private static Logger log = LogManager.getLogger(FollowOrderServiceImpl.class.getName());
    //映射页面的跟单总价算
    private FollowOrderPageVo followOrderPageVo;
    //跟单客户
    @Autowired
    private IFollowOrderClientService followOrderClientService;
    //跟单
    @Autowired
    private FollowOrderDao followOrderDao;
    //交易记录
    @Autowired
    private IFollowOrderTradeRecordService followOrderTradeRecordService;

    @Autowired
    private IFollowOrderDetailService followOrderDetailService;//明细
    //发送MQ
    @Autowired
    private IOrderTraderService orderTraderService;
    @Autowired
    private IVarietyService varietyService;//品种
    @Autowired
    private IClientNetPositionService clientNetPositionService;//客户净头寸关联


    @Override
    public void save(FollowOrder followOrder) {
        followOrderDao.insert(followOrder);
    }


    /**
     * 通过客户的名字找到对应的跟单集合
     *@Author: May
     *@Date: 14:30 2018/5/22
     * @param clientName
     * @param varietyCode
     */
    private List<FollowOrder> getListFollowOrderByClientName(String clientName, String varietyCode) {

        //通过客户的名字找到对应的客户id,
        List<Long> followOrderIds = followOrderClientService.getListByUserCode(clientName);
        List<FollowOrder> followOrderList;
        if(followOrderIds.size()!=0){
            //然后逐个找到符合的跟单,并且状态为启动
             followOrderList= followOrderDao.findFollowOrderStart(followOrderIds,varietyCode);
             return followOrderList;
        }

        return null;
    }

    /** 返回跟单
     *@Author: May
     *@param
     *@Date: 12:48 2018/5/8
     */
    @Override
    public FollowOrder getFollowOrder(Long id) {
        return followOrderDao.selectByPrimaryKey(id);
    }
    /*
    *
    * 修改跟单
    * */
    @Override
    public void updateFollowOrder(FollowOrder followOrder) {
        Variety varietyByCode = varietyService.getVarietyByCode(followOrder.getVariety().getVarietyCode());
        followOrder.setVariety(varietyByCode);
        if(!followOrder.getMaxProfit().equals(FollowOrderEnum.FollowStatus.SET_MAXPROFIT.getIndex())){
            followOrder.setMaxProfitNumber(null);
        }
        if(!followOrder.getMaxLoss().equals(FollowOrderEnum.FollowStatus.SET_MAXLOSS.getIndex())){
            followOrder.setMaxLossNumber(null);
        }
        if(!followOrder.getAccountLoss().equals(FollowOrderEnum.FollowStatus.SET_ACCOUNTLOSS.getIndex())){
            followOrder.setAccountLossNumber(null);
        }
        if(!followOrder.getOrderPoint().equals(FollowOrderEnum.FollowStatus.LIMIT_PRICE.getIndex())){
            followOrder.setClientPoint(null);
            followOrder.setClientPointNumber(null);
        }
        followOrder.setUpdateDate(DateUtil.getStringDate());
        followOrderDao.updateByPrimaryKey(followOrder);
    }
    @Override
    public List<FollowOrder> selectListFollowOrder(FollowOrderPageVo followOrderPageVo) {
        return followOrderDao.selectListFollowOrder(followOrderPageVo);
    }
    /*
    * 修改跟单的状态
    * */
    @Override
    public void updateFollowOrderStatus(Long followOrderId, Integer status) {
        String startTime = null;
        if (FollowOrderEnum.FollowStatus.FOLLOW_ORDER_START.getIndex().equals(status)) {
            startTime = DateUtil.getStringDate();
        }
        followOrderDao.updateFollowOrderStatus(followOrderId, status, startTime);
    }

    /*
     *
     *  返回页面的跟单映射
     * @author may
     * @date 2018/5/25 15:37
     * @param
     * @return
     */
    @Override
    public List<FollowOrderVo> getListFollowOrderVo(FollowOrderPageVo followOrderPageVo ) {
        List<FollowOrderVo> followOrderVos = new ArrayList<>();
        List<FollowOrder> followOrders = selectListFollowOrder(followOrderPageVo);
        followOrderPageVo = new FollowOrderPageVo();
        if (followOrders.size() != 0) {
            for (FollowOrder followOrder : followOrders) {
                FollowOrderVo followOrderVo = new FollowOrderVo();
                //设置总手续费
                followOrderVo.setPoundageTotal(followOrderDetailService.getCommissionTotal(followOrder.getId())==null?
                        0.0:followOrderDetailService.getCommissionTotal(followOrder.getId()));

                //设置手数
                followOrderVo.setHandNumberTotal(followOrderDetailService.getOpenHandNumber(followOrder.getId())==null?
                            0.0:followOrderDetailService.getOpenHandNumber(followOrder.getId()));

                BigDecimal commission = new BigDecimal(followOrderVo.getPoundageTotal());
                BigDecimal handNum = new BigDecimal(followOrderVo.getHandNumberTotal());

                followOrderVo.setPoundageTotal(commission.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
                followOrderVo.setHandNumberTotal(handNum.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());

                //todo demo过后删除
                followOrder.setAccount(FollowOrderGenerateUtil.getAccount());
                followOrderVo.setFollowOrder(followOrder);
                //设置持仓盈亏
                followOrderVo.setPositionGainAndLoss(0.0);
                //设置平仓盈亏
                FollowOrderVo offset = followOrderDetailService.getOffsetGainAndLossAndHandNumberByFollowOrderId(followOrder.getId());
                followOrderVo.setOffsetGainAndLoss(offset == null? 0.0 :offset.getOffsetGainAndLoss());
                //设置累计盈亏
                followOrderVo.setGainAndLossTotal(DoubleUtil.add(followOrderVo.getPositionGainAndLoss(),
                        followOrderVo.getOffsetGainAndLoss()) == null ? 0.0 :DoubleUtil.add(followOrderVo.getPositionGainAndLoss(),
                        followOrderVo.getOffsetGainAndLoss()));
                //客户盈亏
                followOrderVo.setClientProfit(0.0);
                //盈亏率
                followOrderVo.setProfitAndLossRate(DoubleUtil.div(followOrderVo.getOffsetGainAndLoss()==null? 0.0 :followOrderVo.getOffsetGainAndLoss(),
                        offset == null ?1.0 :offset.getOffsetHandNumber(), 2));
                //跟单成功
                followOrderVo.setSuccessTotal(followOrderTradeRecordService.getFollowOrderSuccessTotalAmount(followOrder.getId()));
                //跟单总数
                followOrderVo.setAllTotal(followOrderTradeRecordService.getFollowOrderTotalAmount(followOrder.getId()));

                followOrderVos.add(followOrderVo);

            }
        }
        return followOrderVos;
    }

    /**
     * 实现交易逻辑
     *@Author: May
     *@param data
     *@Date: 14:24 2018/4/23
     */
    @Override
    public synchronized void madeAnOrder(DataSource data) {
        //1.先获取符合的跟单
        List<FollowOrder> listFollowOrder = getListFollowOrderByClientName(data.getLogin(),data.getVarietyCode());
        if(listFollowOrder != null){

            for (FollowOrder followOrder : listFollowOrder) {
                followOrder.setAccount(FollowOrderGenerateUtil.getAccount());
                if (followOrder.getFollowManner().equals(FollowOrderEnum.FollowStatus.FOLLOWMANNER_NET_POSITION.getIndex())) {
                    //跟净头寸
                    netPositionOrder(followOrder, data);
                } else{
                    //客户
                    followUserOrder(followOrder,data);
                }

            }
        }
    }

    /*
    * 实现净头寸交易
    * */
    public void netPositionOrder(FollowOrder followOrder, DataSource data) {
        //获取累加的净头寸
        Double headNum = getNowNetPositionSum(data, followOrder);
        //创建客户与净头寸的关联
        ClientNetPosition clientNetPosition = new ClientNetPosition();
        //创建时间
        clientNetPosition.setCreateTime(DateUtil.getStringDate());
        //跟单ID
        clientNetPosition.setFollowOrderId(followOrder.getId());
        //设置净头寸
        clientNetPosition.setNetPositionSum(headNum);
        //设置Ticket
        clientNetPosition.setTicket(data.getTicket());
        if(data.getOpenClose().equals(FollowOrderEnum.FollowStatus.CLOSE.getIndex())){
            //设置平仓时间
            clientNetPosition.setCloseTime(data.getCreateTime());
        }else{
            //设置开仓时间
            clientNetPosition.setOpenTime(data.getCreateTime());
        }
        clientNetPositionService.save(clientNetPosition);
        //判断策略的状态是否在交易中,不在交易就进行判断
        if (followOrder.getNetPositionStatus().equals(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_PAUSE.getIndex())) {
            //获取原有的持仓数
            Double oldHoldNum = followOrder.getNetPositionHoldNumber();

            //现在的持仓数
            log.info("现在的净头寸的值："+headNum);
            //应持仓多少手
            int newHoldNum = (int) (headNum / followOrder.getNetPositionChange());
            //判断策略的正反方向跟单
            if (FollowOrderEnum.FollowStatus.DIRECTION_REVERSE.getIndex().equals(followOrder.getNetPositionDirection())) {
                //反向跟单,应有持仓数应该是相反的
                if (headNum > 0 && newHoldNum > 0) {
                    newHoldNum = -newHoldNum;
                }
                if (headNum < 0 && newHoldNum < 0) {
                    newHoldNum = -newHoldNum;
                }
            }
            //当数值不等于zero的时候才可以进行跟单操作
            if (newHoldNum != oldHoldNum) {
                //下单为空单，当净头寸的值为正的时候，持仓数应该为负
                //本次应该下单的数量
                double interim = Math.abs((int) newHoldNum - oldHoldNum);
                if (newHoldNum <= zero) {
                    //小于0就是空单
                    //并且应持仓数为负数的时候，如果大于原有的持仓数就是要平仓,并且应持仓数不为正数就不需要开仓
                    if (newHoldNum > oldHoldNum) {
                        //设置正在交易中的状态,普通交易状态
                        followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_START.getIndex());

                        //设置交易对象下单手数,发送交易请求,做多单
                        sendMsgByTrade(followOrder, FollowOrderEnum.FollowStatus.BUY.getIndex(), FollowOrderEnum.FollowStatus.CLOSE.getIndex(),
                                interim, data.getNewTicket(),data.getTicket(),data.getVarietyCode(),clientNetPosition.getId());
                    } else if (newHoldNum < oldHoldNum && oldHoldNum <= zero) {
                        //设置正在交易中的状态,普通交易状态
                        followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_START.getIndex());

                        //当newHoldNum为负数的时候小于原本的持仓，那就是开仓,发送交易请求,做空单

                        sendMsgByTrade(followOrder, FollowOrderEnum.FollowStatus.SELL.getIndex(), FollowOrderEnum.FollowStatus.OPEN.getIndex(),
                                interim, data.getNewTicket(),data.getTicket(),data.getVarietyCode(),clientNetPosition.getId());

                    } else if (oldHoldNum > zero) {

                        //oldHoldNum:做多单,同时平仓又开仓，平oldHoldNum，开newHoldNum
                        if (newHoldNum != 0) {
                            //设置正在交易中的状态,发送两条交易状态
                            followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_OPENCLOSE.getIndex());
                            //先做开仓,做空单
                            sendMsgByTrade(followOrder, FollowOrderEnum.FollowStatus.SELL.getIndex(), FollowOrderEnum.FollowStatus.OPEN.getIndex(),
                                    (double) Math.abs(newHoldNum), data.getNewTicket(),data.getTicket(),data.getVarietyCode(),clientNetPosition.getId());
                        }else{
                            followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_START.getIndex());
                        }
                        //再来做一单，平仓做空单
                        sendMsgByTrade(followOrder, FollowOrderEnum.FollowStatus.SELL.getIndex(), FollowOrderEnum.FollowStatus.CLOSE.getIndex(),
                                (double) Math.abs(oldHoldNum), data.getNewTicket(),data.getTicket(),data.getVarietyCode(),clientNetPosition.getId());
                    }
                } else if (newHoldNum >= zero) {
                    //大于0:就做多单
                    //1.当持仓数 > zero,newHoldNum < 持仓数，那么就平仓
                    if (oldHoldNum > zero && newHoldNum < oldHoldNum) {
                        //设置正在交易中的状态,普通交易状态
                        followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_START.getIndex());

                        sendMsgByTrade(followOrder, FollowOrderEnum.FollowStatus.SELL.getIndex(), FollowOrderEnum.FollowStatus.CLOSE.getIndex(),
                                interim, data.getNewTicket(),data.getTicket(),data.getVarietyCode(),clientNetPosition.getId());//发送下单请求,做空单，平仓

                    } else if (oldHoldNum >= zero && newHoldNum > oldHoldNum) {
                        //设置正在交易中的状态,普通交易状态
                        followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_START.getIndex());

                        //3.当持仓数 >=zero,newHoldNum > 持仓数，开仓
                        //当newHoldNum为正数的时候大于原本的持仓，那就是开仓
                        sendMsgByTrade(followOrder, FollowOrderEnum.FollowStatus.BUY.getIndex(), FollowOrderEnum.FollowStatus.OPEN.getIndex(),//发送交易请求
                                interim, data.getNewTicket(),data.getTicket(),data.getVarietyCode(),clientNetPosition.getId());
                    } else if (oldHoldNum < zero) {
                        //当持仓数 < zero ,平持仓数，开newHoldNum
                        //先做开仓,设置跟单明细的多空状态为空单,先做负的，设置手数
                        if (newHoldNum != zero) {
                            //设置正在交易中的状态,发送两条交易状态
                            followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_OPENCLOSE.getIndex());
                            sendMsgByTrade(followOrder, FollowOrderEnum.FollowStatus.BUY.getIndex(), FollowOrderEnum.FollowStatus.OPEN.getIndex(),
                                    (double) Math.abs(newHoldNum), data.getNewTicket(),data.getTicket(),data.getVarietyCode(),clientNetPosition.getId());
                        } else{
                            //设置成普通交易状态
                            followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_START.getIndex());
                        }
                        //再来做一单，平仓
                        sendMsgByTrade(followOrder, FollowOrderEnum.FollowStatus.BUY.getIndex(), FollowOrderEnum.FollowStatus.CLOSE.getIndex(),
                                (double) Math.abs(oldHoldNum), data.getNewTicket(),data.getTicket(),data.getVarietyCode(),clientNetPosition.getId());
                    }
                }
            }
            FollowOrder followOrder1 = getFollowOrder(followOrder.getId());
            followOrder.setVersion(followOrder1.getVersion());
            //修改策略
            updateFollowOrder(followOrder);
        }
    }

    /*
    * 实现跟客户的交易
    * */
    public void followUserOrder(FollowOrder followOrder, DataSource data) {
        //跟客户的策略判断
        FollowOrderClient followOrderClient = followOrderClientService.findClientByIdAndName(followOrder.getId(), data.getLogin());
        //判断手数类型：按比例=比例数*客户的手数
        Double handNumber = followOrderClient.getHandNumberType().equals(FollowOrderEnum.FollowStatus.CLIENT_HAND_NUMBER_TYPE.getIndex())?
                followOrderClient.getFollowHandNumber():DoubleUtil.mul(Double.valueOf(followOrderClient.getFollowHandNumber()),data.getHandNumber());
        //交易方向
        Integer direction ;
        //反向跟单
        if(followOrderClient.getFollowDirection().equals(FollowOrderEnum.FollowStatus.DIRECTION_REVERSE.getIndex())){
            direction = data.getCmd().equals(FollowOrderEnum.FollowStatus.BUY.getIndex())?
                    FollowOrderEnum.FollowStatus.SELL.getIndex():FollowOrderEnum.FollowStatus.BUY.getIndex();
        }else {
            direction = data.getCmd();
        }
        //平仓，找ticket,如果没有不跟，如果有就跟
        if(data.getOpenClose().equals(FollowOrderEnum.FollowStatus.CLOSE.getIndex())){
            FollowOrderDetail detail = followOrderDetailService.getFollowOrderDetailByTicket(data.getTicket(), followOrder.getId());
            if (detail!=null){
                sendMsgByTrade(followOrder,direction.equals(FollowOrderEnum.FollowStatus.BUY.getIndex())?
                        FollowOrderEnum.FollowStatus.SELL.getIndex():FollowOrderEnum.FollowStatus.BUY.getIndex(),
                        FollowOrderEnum.FollowStatus.CLOSE.getIndex(),handNumber,data.getNewTicket(),data.getTicket(),
                        data.getVarietyCode(),null);
            }
        }else {
            //开仓跟
            sendMsgByTrade(followOrder,direction,
                    FollowOrderEnum.FollowStatus.OPEN.getIndex(),handNumber,data.getNewTicket(),data.getTicket(),
                    data.getVarietyCode(),null);
        }
    }

    /*
    * 净头寸值计算
    * */
    private synchronized Double getNowNetPositionSum(DataSource data, FollowOrder followOrder) {
        //获取现有的净头寸
        Double netPositionSum = followOrder.getNetPositionSum();
        log.info("原本的净头寸的值："+followOrder.getNetPositionSum());
        if (data.getCmd().equals(FollowOrderEnum.FollowStatus.BUY.getIndex())) {
            //多，增加净头寸
            netPositionSum = DoubleUtil.add(netPositionSum, data.getHandNumber());
            //净头寸值
        } else {
            //空，减少净头寸
            netPositionSum = DoubleUtil.sub(netPositionSum, data.getHandNumber());
            //净头寸值
        }

        followOrder.setNetPositionSum(netPositionSum);
        updateFollowOrder(followOrder);
        return netPositionSum;
    }
    /*
    **判断账号是否登录了
     *@Author: May
     *@param
     *@Date: 12:12 2zero18/5/1zero
     */
    public void checkLogin(FollowOrder followOrder) {
        //todo 跟单的启动是一创建就启动,还是第一个客户过来再启动?
        if (followOrder.getFollowOrderStatus().equals(FollowOrderEnum.FollowStatus.FOLLOW_ORDER_STOP.getIndex())) {
            UserLogin login = new UserLogin();
            login.setTypeId("userLogin");
            login.setRequestId(followOrder.getId());
            login.setBrokerId(followOrder.getAccount().getPlatform().getName());
            login.setUserId(followOrder.getAccount().getUsername());
            login.setPassword(followOrder.getAccount().getPassword());
            Account account = followOrder.getAccount();
            log.info("发送一条登陆信息："+WebJsion.toJson(login));
            //发送MQ去登录
            orderTraderService.userLogin(login);


        }
    }
    /*
     *
     *   创建跟单
     * @author may
     * @date 2018/6/4 17:03
     * @param
     * @return
     */
    @Override
    public void createFollowOrder(FollowOrder followOrder, List<FollowOrderClient> followOrderClients) {
        FollowOrder order = followOrder;

        Variety variety = varietyService.getVarietyByCode(followOrder.getVariety().getVarietyCode());
        order.setVariety(variety);
        order.setFollowOrderStatus(FollowOrderEnum.FollowStatus.FOLLOW_ORDER_STOP.getIndex());
        //todo demo后删除
        order.setAccount(FollowOrderGenerateUtil.getAccount());
        //跟单方式
        if(order.getFollowManner().equals(FollowOrderEnum.FollowStatus.FOLLOWMANNER_NET_POSITION.getIndex())){
            //跟净头寸，设置不处于交易中的状态
            order.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_PAUSE.getIndex());
        }
        //最大盈利
        if(order.getMaxProfit().equals(FollowOrderEnum.FollowStatus.SET_MAXPROFIT.getIndex())){
            order.setMaxProfitNumber(followOrder.getMaxProfitNumber());
        }
        //最大止损
        if(order.getMaxLoss().equals(FollowOrderEnum.FollowStatus.SET_MAXLOSS.getIndex())){
            order.setMaxLossNumber(followOrder.getMaxLossNumber());
        }
        //账户止损
        if(order.getAccountLoss().equals(FollowOrderEnum.FollowStatus.SET_ACCOUNTLOSS.getIndex())){
            order.setAccountLossNumber(followOrder.getAccountLossNumber());
        }
        //客户点位
        order.setOrderPoint(followOrder.getOrderPoint());
        if(order.getOrderPoint().equals(FollowOrderEnum.FollowStatus.LIMIT_PRICE.getIndex())){
            order.setClientPoint(followOrder.getClientPoint());
            if(order.getOrderPoint().equals(FollowOrderEnum.FollowStatus.CLIENT_POINT_BAD.getIndex())){
                order.setClientPointNumber(-followOrder.getClientPointNumber());
            }else{
                order.setClientPointNumber(followOrder.getClientPointNumber());
            }
        }
        save(order);
        //保存客户与跟单关联
        followOrderClientService.saveListFollowOrderClient(followOrderClients,order);

        checkLogin(order);
    }

    @Override
    public synchronized void updateHoldNumByTradeAndFollowOrder(FollowOrder followOrder, FollowOrderTradeRecord followOrderTradeRecord) {
        //设置持仓值,获取原来的持仓值
        Double oldHoldNum = followOrder.getNetPositionHoldNumber();
        if (followOrderTradeRecord.getTradeDirection().equals(FollowOrderEnum.FollowStatus.SELL.getIndex())) {
            //做空就减
            followOrder.setNetPositionHoldNumber(DoubleUtil.sub(oldHoldNum, followOrderTradeRecord.getHandNumber()));
        } else {
            //做多就加
            followOrder.setNetPositionHoldNumber(DoubleUtil.add(oldHoldNum, followOrderTradeRecord.getHandNumber()));

        }
    }

    /**
     * 判断是市价还是限价，确定点位
     *
     * @param
     * @Author: May
     * @Date: 18:19 2zero18/4/25
     */
  /*  private OrderParameter checkPoint(FollowOrder followOrder, OrderParameter orderParameter) {

        //判断下单点位是否比客户好
        if (followOrder.getClientPoint() != null) {
            //设置市价or限价
            orderParameter.setOrderPoint(followOrder.getOrderPoint());
            if (followOrder.getClientPoint().equals(FollowOrderEnum.FollowStatus.CLIENT_POINT_GOOD.getIndex())) {
                //下单点位比客户好
                orderParameter.setCustomerPointNum(followOrder.getClientPointNumber());
            } else {
                //比客户差
                Double num = followOrder.getClientPointNumber();
                System.out.println(num);
                orderParameter.setCustomerPointNum(followOrder.getClientPointNumber());
            }
        }else {
            orderParameter.setCustomerPointNum(0.0);
        }

        return orderParameter;
    }
*/

    /**
     * 封装交易对象，新增交易记录，发送交易信息
     *
     * @param
     * @Author: May
     * @Date: 11:38 2zero18/5/1zero
     */
    public void sendMsgByTrade(FollowOrder followOrder, Integer orderDirection, Integer openClose,
                                Double handNumber,String newTicket,String ticket,String varietyCode,Long clientNetPositionId) {

        FollowOrderTradeRecord followOrderTradeRecord = new FollowOrderTradeRecord();
        //设置新开仓单号
        followOrderTradeRecord.setNewTicket(newTicket);
        //设置开仓单号
        followOrderTradeRecord.setTicket(ticket);
        //设置跟单id
        followOrderTradeRecord.setFollowOrderId(followOrder.getId());
        //设置品种的id
        followOrderTradeRecord.setVarietyId(followOrder.getVariety().getId());
        //设置品种代码 todo 通过数据源的品种代码,找到该交易账号所自身的品种代码
        followOrderTradeRecord.setVarietyCode(varietyCode);
        //设置开平状态
        followOrderTradeRecord.setOpenCloseType(openClose);
        //设置多空
        followOrderTradeRecord.setTradeDirection(orderDirection);
        //设置手数
        followOrderTradeRecord.setHandNumber(handNumber);
        //设置净头寸的值
        followOrderTradeRecord.setClientNetPositionId(clientNetPositionId);
        //添加创建时间
        followOrderTradeRecord.setCreateDate(DateUtil.getStringDate());
        //新增操作
        followOrderTradeRecordService.save(followOrderTradeRecord);

        //创建交易对象
        OrderTrade orderTrade = new OrderTrade();
        //设置交易记录的id
        orderTrade.setRequestId(followOrderTradeRecord.getId());
        //设置账号的交易平台
        orderTrade.setBrokerId(followOrder.getAccount().getPlatform().getName());
        //设置交易对象的交易账号
        orderTrade.setUserId(followOrder.getAccount().getUsername());
        //设置平台对应的品种合约代码 todo 实现该功能
        orderTrade.setInstrumentId("GC1808");
        //设置买卖方向
        orderTrade.setOrderDirection(orderDirection);
        if (openClose.equals(FollowOrderEnum.FollowStatus.CLOSE.getIndex())) {
            //平仓
            orderTrade.setTypeId("orderClose");
            orderTrade.setOrderVolume(handNumber);
            //发送平仓交易请求
            orderTraderService.orderClose(orderTrade);
            log.info("发送一条交易信息：" + WebJsion.toJson(orderTrade));


        } else {
            //开仓
            orderTrade.setTypeId("orderOpen");
            orderTrade.setVolumeTotalOriginal(handNumber);
            orderTraderService.orderOpen(orderTrade);
            log.info("发送一条交易信息：" + orderTrade.toString());
        }
    }

    /*
     *   平所有未平的跟单
     * @author may
     * @date 2018/6/5 19:08
     * @param
     * @return
     */
    @Override
    public void closeAllOrderByFollowOrderId(Long followOrderId) {
        List<FollowOrderDetail> orderDetails = followOrderDetailService.getNOCloseDetailListByFollowOrderId(followOrderId);
        FollowOrder followOrder = getFollowOrder(followOrderId);
        //todo 账号的设置去掉
        followOrder.setAccount(FollowOrderGenerateUtil.getAccount());
        for (FollowOrderDetail orderDetail : orderDetails) {
            Integer direction = orderDetail.getTradeDirection() == FollowOrderEnum.FollowStatus.BUY.getIndex() ?
                    FollowOrderEnum.FollowStatus.SELL.getIndex() :FollowOrderEnum.FollowStatus.BUY.getIndex();
            sendMsgByTrade(followOrder,direction,FollowOrderEnum.FollowStatus.CLOSE.getIndex(),
                    orderDetail.getRemainHandNumber(),orderDetail.getTicket(),
                    orderDetail.getTicket(),followOrder.getVariety().getVarietyCode(),null);
        }
    }

    /*
     *
     *   通过明细得id，进行手动平仓
     * @author may
     * @date 2018/6/6 17:13
     * @param
     * @return
     */

    @Override
    public void manuallyClosePosition(Long detailId) {
        FollowOrderDetail orderDetail = followOrderDetailService.getFollowOrderDetail(detailId);
        FollowOrder followOrder = getFollowOrder(orderDetail.getFollowOrderId());
        //todo 账号得设置去掉
        followOrder.setAccount(FollowOrderGenerateUtil.getAccount());
        //设置多空方向
        Integer direction = orderDetail.getTradeDirection() == FollowOrderEnum.FollowStatus.BUY.getIndex() ?
                FollowOrderEnum.FollowStatus.SELL.getIndex() :FollowOrderEnum.FollowStatus.BUY.getIndex();
        //发送交易信息
        sendMsgByTrade(followOrder,direction,FollowOrderEnum.FollowStatus.CLOSE.getIndex(),
                orderDetail.getRemainHandNumber(),orderDetail.getTicket(),
                orderDetail.getTicket(),followOrder.getVariety().getVarietyCode(),null);
    }


}

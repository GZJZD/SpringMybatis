package com.web.service.imp;


import com.qq.tars.client.Communicator;
import com.qq.tars.client.CommunicatorConfig;
import com.qq.tars.client.CommunicatorFactory;
import com.web.common.FollowOrderEnum;
import com.web.dao.FollowOrderDao;
import com.web.database.OrderHongKongService;
import com.web.database.entity.Prices;
import com.web.pojo.*;
import com.web.pojo.vo.FollowOrderPageVo;
import com.web.pojo.vo.FollowOrderVo;
import com.web.pojo.vo.OrderMsgResult;
import com.web.servant.center.*;
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
public class FollowOrderServiceImpl implements FollowOrderService {

    private static Logger log = LogManager.getLogger(FollowOrderServiceImpl.class.getName());
    //大于就是正数，小于就是负数
    private int zero = 0;
    //映射页面的跟单总价算
    private FollowOrderPageVo followOrderPageVo;
    //跟单客户
    @Autowired
    private FollowOrderClientService followOrderClientService;
    //跟单
    @Autowired
    private FollowOrderDao followOrderDao;
    //交易记录
    @Autowired
    private FollowOrderTradeRecordService followOrderTradeRecordService;
    @Autowired
    private FollowOrderDetailService followOrderDetailService;//明细
    //发送MQ
    @Autowired
    private OrderTraderService orderTraderService;
    @Autowired
    private VarietyService varietyService;//品种
    @Autowired
    private ClientNetPositionService clientNetPositionService;//客户净头寸关联
    @Autowired
    private OrderHongKongService orderHongKongService;//香港数据库
    @Autowired
    private ContractInfoService contractInfoService;//合约信息
    @Autowired
    private ContractInfoLinkService contractInfoLinkService;


    @Override
    public void save(FollowOrder followOrder) {
        followOrderDao.insert(followOrder);
    }


    /**
     * 通过客户的名字找到对应的跟单集合
     *
     * @param clientName
     * @param varietyCode
     * @Author: May
     * @Date: 14:30 2018/5/22
     */
    private List<FollowOrder> getListFollowOrderByClientName(String clientName, String varietyCode) {

        //通过客户的名字找到对应的客户id,
        List<Long> followOrderIds = followOrderClientService.getListByUserCode(clientName);
        List<FollowOrder> followOrderList;
        if (followOrderIds.size() != 0) {
            //然后逐个找到符合的跟单,并且状态为启动
            followOrderList = followOrderDao.findFollowOrderStart(followOrderIds, varietyCode);
            return followOrderList;
        }

        return null;
    }

    /**
     * 返回跟单
     *
     * @param
     * @Author: May
     * @Date: 12:48 2018/5/8
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
    public synchronized void updateFollowOrder(FollowOrder followOrder) {
        Variety varietyByCode = varietyService.getVarietyByCode(followOrder.getVariety().getVarietyCode());
        followOrder.setVariety(varietyByCode);
        if (!followOrder.getMaxProfit().equals(FollowOrderEnum.FollowStatus.SET_MAXPROFIT.getIndex())) {

            followOrder.setMaxProfitNumber(null);
        }
        if (!followOrder.getMaxLoss().equals(FollowOrderEnum.FollowStatus.SET_MAXLOSS.getIndex())) {

            followOrder.setMaxLossNumber(null);
        }
        if (!followOrder.getAccountLoss().equals(FollowOrderEnum.FollowStatus.SET_ACCOUNTLOSS.getIndex())) {

            followOrder.setAccountLossNumber(null);
        }
        if (!followOrder.getOrderPoint().equals(FollowOrderEnum.FollowStatus.LIMIT_PRICE.getIndex())) {

            followOrder.setClientPoint(null);
            followOrder.setClientPointNumber(null);
        }
        followOrder.setUpdateDate(DateUtil.getStringDate());

        int count = followOrderDao.updateByPrimaryKey(followOrder);
        if (count <= 0) {
            log.error("乐观锁异常：" + FollowOrderServiceImpl.class);
            throw new RuntimeException("乐观锁异常：" + FollowOrderServiceImpl.class);

        }
    }

    /*
     * 获取所有跟单
     *@param:FollowOrderPageVo 查询条件
     * */
    @Override
    public List<FollowOrder> selectListFollowOrder(FollowOrderPageVo followOrderPageVo) {
        return followOrderDao.selectListFollowOrder(followOrderPageVo);
    }

    /*
     * 修改跟单的状态
     * */
    @Override
    public synchronized void updateFollowOrderStatus(Long followOrderId, Integer status) {
        String startTime = null;
        if (FollowOrderEnum.FollowStatus.FOLLOW_ORDER_START.getIndex().equals(status)) {
            startTime = DateUtil.getStringDate();
        }
        log.debug("修改跟单策略转态：followOrderId=" + followOrderId + "状态值：" + status);
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
    public List<FollowOrderVo> getListFollowOrderVo(FollowOrderPageVo followOrderPageVo) {
        List<FollowOrderVo> followOrderVos = new ArrayList<>();
        List<FollowOrder> followOrders = selectListFollowOrder(followOrderPageVo);
        followOrderPageVo = new FollowOrderPageVo();
        if (followOrders.size() != 0) {
            for (FollowOrder followOrder : followOrders) {
                FollowOrderVo followOrderVo = new FollowOrderVo();
                //设置总手续费
                followOrderVo.setPoundageTotal(followOrderDetailService.getCommissionTotal(followOrder.getId()) == null ?
                        0.0 : followOrderDetailService.getCommissionTotal(followOrder.getId()));

                //设置手数
                followOrderVo.setHandNumberTotal(followOrderDetailService.getOpenHandNumber(followOrder.getId()) == null ?
                        0.0 : followOrderDetailService.getOpenHandNumber(followOrder.getId()));

                BigDecimal commission = new BigDecimal(followOrderVo.getPoundageTotal());
                BigDecimal handNum = new BigDecimal(followOrderVo.getHandNumberTotal());

                followOrderVo.setPoundageTotal(commission.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                followOrderVo.setHandNumberTotal(handNum.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

                followOrderVo.setFollowOrder(followOrder);
                //设置持仓盈亏

//                //买入持仓手数
//                Double buyHandNum=0.0;
//                List<FollowOrderDetail> buyDetail =
//                        followOrderDetailService.getDetailListByOrderIdAndDirection(followOrder.getId(),FollowOrderEnum.FollowStatus.BUY.getIndex());
//                for (FollowOrderDetail detail : buyDetail) {
//                    buyHandNum= DoubleUtil.add(buyHandNum,detail.getRemainHandNumber()==null?detail.getHandNumber():detail.getRemainHandNumber());
//                }
//                //卖出价持仓手数
//                Double sellHandNum=0.0;
//
//                List<FollowOrderDetail> sellDetail =
//                        followOrderDetailService.getDetailListByOrderIdAndDirection(followOrder.getId(),FollowOrderEnum.FollowStatus.SELL.getIndex());
//                for (FollowOrderDetail detail : sellDetail) {
//                    sellHandNum= DoubleUtil.add(buyHandNum,detail.getRemainHandNumber()==null?detail.getHandNumber():detail.getRemainHandNumber());
//                }
//                //盈亏
//                Double gainAndLoss = 0.0;
//                gainAndLoss = DoubleUtil.add(DoubleUtil.mul(sellHandNum))



                //获取价格
                Prices marketPrice = orderHongKongService.getMarketPrice(followOrder.getVariety().getVarietyCode());
                followOrderVo.setPositionGainAndLoss(0.0);
                //设置平仓盈亏,客户盈亏
                FollowOrderVo offset = followOrderDetailService.getOffsetGainAndLossAndHandNumberByFollowOrderId(followOrder.getId());
                followOrderVo.setOffsetGainAndLoss(offset == null ? 0.0 : offset.getOffsetGainAndLoss());
                //设置累计盈亏
                followOrderVo.setGainAndLossTotal(DoubleUtil.add(followOrderVo.getPositionGainAndLoss(),
                        followOrderVo.getOffsetGainAndLoss()) == null ? 0.0 : DoubleUtil.add(followOrderVo.getPositionGainAndLoss(),
                        followOrderVo.getOffsetGainAndLoss()));
                //客户盈亏
                followOrderVo.setClientProfit(offset == null ? 0.0 : offset.getClientProfit());
                //盈亏率
                followOrderVo.setProfitAndLossRate(DoubleUtil.div(followOrderVo.getOffsetGainAndLoss() == null ? 0.0 : followOrderVo.getOffsetGainAndLoss(),
                        offset == null ? 1.0 : offset.getOffsetHandNumber(), 2));
                //跟单成功:false:算总跟单的成功交易数,
                followOrderVo.setSuccessTotal(followOrderTradeRecordService.getFollowOrderSuccessTradeTotal(followOrder.getId(), false, null, null).get(0).getSuccessTotal());
                //跟单总数：false:算总跟单的交易数
                followOrderVo.setAllTotal(followOrderTradeRecordService.getFollowOrderTradeTotalCount(followOrder.getId(), false, null, null).get(0).getAllTotal());

                followOrderVos.add(followOrderVo);

            }
        }
        return followOrderVos;
    }

    /**
     * 实现交易逻辑
     *
     * @param data
     * @Author: May
     * @Date: 14:24 2018/4/23
     */
    @Override
    public void madeAnOrder(DataSource data) {
        //1.先获取符合的跟单
        List<FollowOrder> listFollowOrder = getListFollowOrderByClientName(data.getLogin(), data.getVarietyCode());
        if (listFollowOrder != null) {
            for (FollowOrder followOrder : listFollowOrder) {
                followOrder.setAccount(FollowOrderGenerateUtil.getAccount());
                if (followOrder.getFollowOrderStatus().equals(FollowOrderEnum.FollowStatus.FOLLOW_ORDER_START.getIndex())) {
                    //跟单为启动
                    if (followOrder.getFollowManner().equals(FollowOrderEnum.FollowStatus.FOLLOWMANNER_NET_POSITION.getIndex())) {
                        //跟净头寸
                        this.netPositionOrder(followOrder, data);
                    } else {
                        //客户
                        this.followUserOrder(followOrder, data);
                    }
                } else {
                    //跟单为暂停
                    this.followOrderTemporaryStop(followOrder, data);
                }

            }
        }
    }

    /*
     * 实现净头寸交易
     * */
    public void netPositionOrder(FollowOrder followOrder, DataSource data) {
        log.debug("净头寸跟单：followOrderName{}," + followOrder.getFollowOrderName());
        log.debug("净头寸跟单修改前：followOrderName{},netPositionChange{},netPositionDirection{},netPositionFollowNumber{},netPositionHoldNumber{},netPositionStatus{},netPositionSum{}," +
                followOrder.getFollowOrderName() + "," + followOrder.getNetPositionChange() + "," + followOrder.getNetPositionFollowNumber() + "," + followOrder.getNetPositionHoldNumber() + ","
                + followOrder.getNetPositionStatus() + "," + followOrder.getNetPositionSum());
        //获取累加的净头寸
        Double headNum = this.getNowNetPositionSum(data, followOrder);
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
        if (data.getOpenClose().equals(FollowOrderEnum.FollowStatus.CLOSE.getIndex())) {
            //设置平仓时间
            clientNetPosition.setCloseTime(data.getCreateTime());
        } else {
            //设置开仓时间
            clientNetPosition.setOpenTime(data.getCreateTime());
        }
        clientNetPositionService.save(clientNetPosition);
        //判断策略的状态是否在交易中,不在交易就进行判断
        if (followOrder.getNetPositionStatus().equals(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_PAUSE.getIndex())) {
            //获取原有的持仓数
            Integer oldHoldNum = followOrder.getNetPositionHoldNumber();
            log.debug(followOrder.getFollowOrderName() + "原有的持仓数:" + oldHoldNum);
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
            log.debug(followOrder.getFollowOrderName() + "应持仓:" + oldHoldNum);
            //当数值不等于zero的时候才可以进行跟单操作
            if (newHoldNum != oldHoldNum) {
                //下单为空单，当净头寸的值为正的时候，持仓数应该为负
                //本次应该下单的数量
                Integer interim = Math.abs((int) newHoldNum - oldHoldNum);
                if (newHoldNum <= zero) {
                    //小于0就是空单
                    //并且应持仓数为负数的时候，如果大于原有的持仓数就是要平仓,并且应持仓数不为正数就不需要开仓
                    if (newHoldNum > oldHoldNum) {
                        //设置正在交易中的状态,普通交易状态
                        followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_START.getIndex());

                        //设置交易对象下单手数,发送交易请求,做多单
                        this.sendMsgByTrade(followOrder, FollowOrderEnum.FollowStatus.BUY.getIndex(), FollowOrderEnum.FollowStatus.CLOSE.getIndex(),
                                interim, data.getNewTicket(), data.getTicket(), data.getVarietyCode(), clientNetPosition.getId(), data.getLogin());
                    } else if (newHoldNum < oldHoldNum && oldHoldNum <= zero) {
                        //设置正在交易中的状态,普通交易状态
                        followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_START.getIndex());
                        //当newHoldNum为负数的时候小于原本的持仓，那就是开仓,发送交易请求,做空单
                        this.sendMsgByTrade(followOrder, FollowOrderEnum.FollowStatus.SELL.getIndex(), FollowOrderEnum.FollowStatus.OPEN.getIndex(),
                                interim, data.getNewTicket(), data.getTicket(), data.getVarietyCode(), clientNetPosition.getId(), data.getLogin());

                    } else if (oldHoldNum > zero) {

                        //oldHoldNum:做多单,同时平仓又开仓，平oldHoldNum，开newHoldNum
                        if (newHoldNum != 0) {
                            //设置正在交易中的状态,发送两条交易状态
                            followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_OPENCLOSE.getIndex());
                            //先做开仓,做空单
                            this.sendMsgByTrade(followOrder, FollowOrderEnum.FollowStatus.SELL.getIndex(), FollowOrderEnum.FollowStatus.OPEN.getIndex(),
                                  Math.abs(newHoldNum), data.getNewTicket(), data.getTicket(), data.getVarietyCode(), clientNetPosition.getId(), data.getLogin());
                        } else {
                            followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_START.getIndex());
                        }
                        //再来做一单，平仓做空单
                        this.sendMsgByTrade(followOrder, FollowOrderEnum.FollowStatus.SELL.getIndex(), FollowOrderEnum.FollowStatus.CLOSE.getIndex(),
                                Math.abs(oldHoldNum), data.getNewTicket(), data.getTicket(), data.getVarietyCode(), clientNetPosition.getId(), data.getLogin());
                    }
                } else if (newHoldNum >= zero) {
                    //大于0:就做多单
                    //1.当持仓数 > zero,newHoldNum < 持仓数，那么就平仓
                    if (oldHoldNum > zero && newHoldNum < oldHoldNum) {
                        //设置正在交易中的状态,普通交易状态
                        followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_START.getIndex());

                        this.sendMsgByTrade(followOrder, FollowOrderEnum.FollowStatus.SELL.getIndex(), FollowOrderEnum.FollowStatus.CLOSE.getIndex(),
                                interim, data.getNewTicket(), data.getTicket(), data.getVarietyCode(), clientNetPosition.getId(), data.getLogin());//发送下单请求,做空单，平仓

                    } else if (oldHoldNum >= zero && newHoldNum > oldHoldNum) {
                        //设置正在交易中的状态,普通交易状态
                        followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_START.getIndex());

                        //3.当持仓数 >=zero,newHoldNum > 持仓数，开仓
                        //当newHoldNum为正数的时候大于原本的持仓，那就是开仓
                        this.sendMsgByTrade(followOrder, FollowOrderEnum.FollowStatus.BUY.getIndex(), FollowOrderEnum.FollowStatus.OPEN.getIndex(),//发送交易请求
                                interim, data.getNewTicket(), data.getTicket(), data.getVarietyCode(), clientNetPosition.getId(), data.getLogin());
                    } else if (oldHoldNum < zero) {
                        //当持仓数 < zero ,平持仓数，开newHoldNum
                        //先做开仓,设置跟单明细的多空状态为空单,先做负的，设置手数
                        if (newHoldNum != zero) {
                            //设置正在交易中的状态,发送两条交易状态
                            followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_OPENCLOSE.getIndex());
                            this.sendMsgByTrade(followOrder, FollowOrderEnum.FollowStatus.BUY.getIndex(), FollowOrderEnum.FollowStatus.OPEN.getIndex(),
                                     Math.abs(newHoldNum), data.getNewTicket(), data.getTicket(), data.getVarietyCode(), clientNetPosition.getId(), data.getLogin());
                        } else {
                            //设置成普通交易状态
                            followOrder.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_START.getIndex());
                        }
                        //再来做一单，平仓
                        this.sendMsgByTrade(followOrder, FollowOrderEnum.FollowStatus.BUY.getIndex(), FollowOrderEnum.FollowStatus.CLOSE.getIndex(),
                                 Math.abs(oldHoldNum), data.getNewTicket(), data.getTicket(), data.getVarietyCode(), clientNetPosition.getId(), data.getLogin());
                    }
                }
            }
            FollowOrder followOrder1 = this.getFollowOrder(followOrder.getId());
            followOrder.setVersion(followOrder1.getVersion());
            //修改策略
            this.updateFollowOrder(followOrder);
            log.debug("净头寸跟单修改后：followOrderName{},netPositionChange{},netPositionDirection{},netPositionFollowNumber{},netPositionHoldNumber{},netPositionStatus{},netPositionSum{}," +
                    followOrder.getFollowOrderName() + "," + followOrder.getNetPositionChange() + "," + followOrder.getNetPositionFollowNumber() + "," + followOrder.getNetPositionHoldNumber() + ","
                    + followOrder.getNetPositionStatus() + "," + followOrder.getNetPositionSum());
        }
    }

    /*
     * 实现跟客户的交易
     * */
    public void followUserOrder(FollowOrder followOrder, DataSource data) {
        log.debug("跟每单跟单：followOrderName{}," + followOrder.getFollowOrderName());
        //跟客户的策略判断
        FollowOrderClient followOrderClient = followOrderClientService.findClientByIdAndName(followOrder.getId(), data.getLogin());
        //判断手数类型：按比例=比例数*客户的手数
        Double handNumber = followOrderClient.getHandNumberType().equals(FollowOrderEnum.FollowStatus.CLIENT_HAND_NUMBER_TYPE.getIndex()) ?
                followOrderClient.getFollowHandNumber() : DoubleUtil.mul(Double.valueOf(followOrderClient.getFollowHandNumber()), data.getHandNumber());

        //手数不能为0
        int tradeHandNumber= (int) Math.floor(handNumber) == 0?1:(int) Math.floor(handNumber);
        //交易方向
        Integer direction;
        //反向跟单
        if (followOrderClient.getFollowDirection().equals(FollowOrderEnum.FollowStatus.DIRECTION_REVERSE.getIndex())) {
            direction = data.getCmd().equals(FollowOrderEnum.FollowStatus.BUY.getIndex()) ?
                    FollowOrderEnum.FollowStatus.SELL.getIndex() : FollowOrderEnum.FollowStatus.BUY.getIndex();
        } else {
            //跟单方向
            direction = data.getCmd();
        }
        //平仓，找ticket,如果没有不跟，如果有就跟
        if (data.getOpenClose().equals(FollowOrderEnum.FollowStatus.CLOSE.getIndex())) {
            FollowOrderDetail detail = followOrderDetailService.getFollowOrderDetailByTicket(data.getTicket(), followOrder.getId());
            if (detail != null) {
                log.debug("跟客户平仓：followOrderName{},ticket{}," + followOrder.getFollowOrderName() + "、" + data.getTicket());
                //手数向下取整
                this.sendMsgByTrade(followOrder, direction, FollowOrderEnum.FollowStatus.CLOSE.getIndex(), tradeHandNumber, data.getNewTicket(), data.getTicket(),
                        data.getVarietyCode(), null, data.getLogin());
            }
        } else {
            //开仓跟
            log.debug("跟客户开仓：followOrderName{},ticket{}," + followOrder.getFollowOrderName() + "、" + data.getTicket());
            this.sendMsgByTrade(followOrder, direction,
                    FollowOrderEnum.FollowStatus.OPEN.getIndex(), tradeHandNumber, data.getNewTicket(), data.getTicket(),
                    data.getVarietyCode(), null, data.getLogin());
        }
    }

    /*
     * 净头寸值计算
     * */
    private Double getNowNetPositionSum(DataSource data, FollowOrder followOrder) {
        //获取现有的净头寸
        Double netPositionSum = followOrder.getNetPositionSum();
        log.debug(followOrder.getFollowOrderName() + "策略原本的净头寸的值：" + followOrder.getNetPositionSum());
        if (data.getCmd().equals(FollowOrderEnum.FollowStatus.BUY.getIndex())) {
            //多，增加净头寸
            netPositionSum = DoubleUtil.add(netPositionSum, data.getHandNumber());
            //净头寸值
        } else {
            //空，减少净头寸
            netPositionSum = DoubleUtil.sub(netPositionSum, data.getHandNumber());
            //净头寸值
        }
        log.debug(followOrder.getFollowOrderName() + "策略现在的净头寸的值：" + netPositionSum);
        followOrder.setNetPositionSum(netPositionSum);
        this.updateFollowOrder(followOrder);
        return netPositionSum;
    }

    /*
     **判断账号是否登录了
     *@Author: May
     *@param
     *@Date: 12:12 2zero18/5/1zero
     */
    public void checkLogin(final FollowOrder followOrder) {

        if (followOrder.getFollowOrderStatus().equals(FollowOrderEnum.FollowStatus.FOLLOW_ORDER_STOP.getIndex())) {
            Account account = followOrder.getAccount();

            //tars框架代替MQ
            CommunicatorConfig cfg = new CommunicatorConfig();
            Communicator communicator = CommunicatorFactory.getInstance().getCommunicator(cfg);
            TraderServantPrx proxy = communicator.stringToProxy(TraderServantPrx.class, "TestApp.HelloServer.HelloTrade@tcp -h 192.168.3.189 -p 50506 -t 60000");
            userLoginRequest req = new userLoginRequest();
            req.setTypeId("userLogin");
            req.setRequestId(followOrder.getId().intValue());
            req.setBrokerId(account.getPlatform().getName());
            req.setUserId(account.getAccount());
            req.setPassword(account.getPassword());
            log.info(followOrder.getFollowOrderName() + "策略发送一条登陆信息：" + WebJsion.toJson(req));
            try {
                proxy.async_userLogin(new TraderServantPrxCallback() {
                    @Override
                    public void callback_expired() {
                    }

                    @Override
                    public void callback_exception(Throwable ex) {
                    }

                    @Override
                    public void callback_userLogout(int ret, userLogoutResponse rsp) {
                    }

                    @Override
                    public void callback_userLogin(int ret, userLoginResponse rsp) {
                        //设计启动
                        updateFollowOrderStatus(Long.valueOf(rsp.getRequestId()),
                                FollowOrderEnum.FollowStatus.FOLLOW_ORDER_START.getIndex());
                        log.debug(followOrder.getFollowOrderName() + "跟单策略启动");
                    }

                    @Override
                    public void callback_orderOpen(int ret, orderOpenResponse rsp) {
                    }

                    @Override
                    public void callback_orderClose(int ret, orderCloseResponse rsp) {
                    }

                    @Override
                    public void callback_instrumentQuery(int ret, instrumentQueryResponse rsp) {

                    }

                    @Override
                    public void callback_instrumentCommissionQuery(int ret, instrumentCommissionQueryResponse rsp) {

                    }

                    @Override
                    public void callback_marketDataQuery(int ret, marketDataQueryResponse rsp) {

                    }
                }, req);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
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

        //跟单方式
        if (order.getFollowManner().equals(FollowOrderEnum.FollowStatus.FOLLOWMANNER_NET_POSITION.getIndex())) {
            //跟净头寸，设置不处于交易中的状态
            order.setNetPositionStatus(FollowOrderEnum.FollowStatus.NET_POSITION_TRADING_PAUSE.getIndex());
        }
        //最大盈利
        if (order.getMaxProfit().equals(FollowOrderEnum.FollowStatus.SET_MAXPROFIT.getIndex())) {
            order.setMaxProfitNumber(followOrder.getMaxProfitNumber());
        }
        //最大止损
        if (order.getMaxLoss().equals(FollowOrderEnum.FollowStatus.SET_MAXLOSS.getIndex())) {
            order.setMaxLossNumber(followOrder.getMaxLossNumber());
        }
        //账户止损
        if (order.getAccountLoss().equals(FollowOrderEnum.FollowStatus.SET_ACCOUNTLOSS.getIndex())) {
            order.setAccountLossNumber(followOrder.getAccountLossNumber());
        }
        //客户点位
        order.setOrderPoint(followOrder.getOrderPoint());
        if (order.getOrderPoint().equals(FollowOrderEnum.FollowStatus.LIMIT_PRICE.getIndex())) {
            order.setClientPoint(followOrder.getClientPoint());
            if (order.getOrderPoint().equals(FollowOrderEnum.FollowStatus.CLIENT_POINT_BAD.getIndex())) {
                order.setClientPointNumber(-followOrder.getClientPointNumber());
            } else {
                order.setClientPointNumber(followOrder.getClientPointNumber());
            }
        }
        this.save(order);
        //保存客户与跟单关联
        followOrderClientService.saveListFollowOrderClient(followOrderClients, order);

        this.checkLogin(order);
        ContractInfo contractInfo = contractInfoService.getInfoByVarietyIdAndPlatformId(followOrder.getVariety().getId(), followOrder.getAccount().getPlatform().getId());
        //合约信息手续费查找
        contractInfoLinkService.instrumentCommissionQuery(contractInfo.getId(),followOrder.getAccount());
        //合约信息查找
        contractInfoLinkService.instrumentQuery(contractInfo.getId());
    }

    /*
     * 修改持仓数
     * */
    @Override
    public void updateHoldNumByTradeAndFollowOrder(FollowOrder followOrder, FollowOrderTradeRecord followOrderTradeRecord) {
        //设置持仓值,获取原来的持仓值
        Integer oldHoldNum = followOrder.getNetPositionHoldNumber();
        if (followOrderTradeRecord.getTradeDirection().equals(FollowOrderEnum.FollowStatus.SELL.getIndex())) {
            //做空就减
            followOrder.setNetPositionHoldNumber(oldHoldNum-followOrderTradeRecord.getHandNumber());
        } else {
            //做多就加
            followOrder.setNetPositionHoldNumber(oldHoldNum+followOrderTradeRecord.getHandNumber());

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
                               Integer handNumber, String newTicket, String ticket, String varietyCode, Long clientNetPositionId, String clientName) {

        FollowOrderTradeRecord followOrderTradeRecord = new FollowOrderTradeRecord();
        //设置新开仓单号
        followOrderTradeRecord.setNewTicket(newTicket);
        //设置开仓单号
        followOrderTradeRecord.setTicket(ticket);
        //设置客户姓名
        followOrderTradeRecord.setClientName(clientName);
        //设置跟单id
        followOrderTradeRecord.setFollowOrderId(followOrder.getId());
        //设置品种的id
        followOrderTradeRecord.setVarietyId(followOrder.getVariety().getId());
        //设置品种代码
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

        CommunicatorConfig cfg = new CommunicatorConfig();
        Communicator communicator = CommunicatorFactory.getInstance().getCommunicator(cfg);
        TraderServantPrx proxy = communicator.stringToProxy(TraderServantPrx.class, "TestApp.HelloServer.HelloTrade@tcp -h 192.168.3.189 -p 50506 -t 60000");

        if (openClose.equals(FollowOrderEnum.FollowStatus.CLOSE.getIndex())) {
            final orderCloseRequest close = new orderCloseRequest();
            //设置交易记录的id
            close.setRequestId(Math.toIntExact(followOrderTradeRecord.getId()));
            //设置账号的交易平台
            close.setBrokerId(followOrder.getAccount().getPlatform().getName());
            //设置交易对象的交易账号
            close.setUserId(followOrder.getAccount().getAccount());
            //设置平台对应的品种合约代码
            ContractInfo info = contractInfoService.getInfoByVarietyIdAndPlatformId(followOrder.getVariety().getId(), followOrder.getAccount().getPlatform().getId());
            close.setInstrumentId(info.getContractCode());
            //设置买卖方向
            close.setOrderDirection(orderDirection);
            //平仓
            close.setTypeId("orderClose");
            close.setOrderVolume(handNumber);

            log.debug(followOrder.getFollowOrderName() + "策略发送一条平仓交易信息：" + WebJsion.toJson(close));

            try {
                proxy.async_orderClose(new TraderServantPrxCallback() {
                    @Override
                    public void callback_expired() {
                    }

                    @Override
                    public void callback_exception(Throwable ex) {
                    }

                    @Override
                    public void callback_userLogout(int ret, userLogoutResponse rsp) {
                    }

                    @Override
                    public void callback_userLogin(int ret, userLoginResponse rsp) {

                    }

                    @Override
                    public void callback_orderOpen(int ret, orderOpenResponse rsp) {

                    }

                    @Override
                    public void callback_orderClose(int ret, orderCloseResponse rsp) {
                        if (ret == 0 && rsp.errcode == 0) {
                            OrderMsgResult closeResult = new OrderMsgResult();
                            closeResult.setRequestId(rsp.requestId);
                            closeResult.setErmsg(rsp.errmsg);
                            closeResult.setErrcode(rsp.errcode);
                            Double closePrice = 0.0;
                            int handNumber = 0;
                            for (tradeItem item : rsp.tradeArrayItems) {
                                closePrice = DoubleUtil.add(closePrice,item.price);
                                handNumber += item.volume;
                            }
                            log.debug("平仓交易数据详情"+WebJsion.toJson(rsp.tradeArrayItems));

                            closeResult.setTradePrice(DoubleUtil.div(closePrice, Double.valueOf(handNumber),2));
                            //todo 手续费：手数* （开仓/平仓手续费 + 成交价 * 开仓/平仓手续费率）
                            closeResult.setTradeCommission(22.2);
                            closeResult.setTradeDate(rsp.tradeArrayItems.get(rsp.tradeArrayItems.size()-1).tradeDate);
                            closeResult.setTradeTime(rsp.tradeArrayItems.get(rsp.tradeArrayItems.size()-1).tradeTime);
                            log.debug("接受一条平仓交易信息：" + WebJsion.toJson(closeResult));
                            followOrderTradeRecordService.updateRecordByComeBackTradeMsg(closeResult);
                        } else {
                            followOrderTradeRecordService.updateRecordByTradeFail((long) close.requestId);
                            if (ret == 0) {
                                log.error("平仓交易端错误：" + rsp.errmsg);

                            } else {
                                log.error("Tars框架异常：" + ret);
                            }
                        }
                    }

                    @Override
                    public void callback_instrumentQuery(int ret, instrumentQueryResponse rsp) {

                    }

                    @Override
                    public void callback_instrumentCommissionQuery(int ret, instrumentCommissionQueryResponse rsp) {

                    }

                    @Override
                    public void callback_marketDataQuery(int ret, marketDataQueryResponse rsp) {

                    }
                }, close);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("平仓交易失败" + e.getMessage());
            }


        } else {
            final orderOpenRequest open = new orderOpenRequest();
            //设置交易记录的id
            open.setRequestId(Math.toIntExact(followOrderTradeRecord.getId()));
            //设置账号的交易平台
            open.setBrokerId(followOrder.getAccount().getPlatform().getName());
            //设置交易对象的交易账号
            open.setUserId(followOrder.getAccount().getAccount());
            //设置平台对应的品种合约代码
            ContractInfo info = contractInfoService.getInfoByVarietyIdAndPlatformId(followOrder.getVariety().getId(), followOrder.getAccount().getPlatform().getId());

            open.setInstrumentId(info.getContractCode());
            //设置买卖方向
            open.setOrderDirection(orderDirection);
            //开仓
            open.setTypeId("orderOpen");
            open.setVolumeTotalOriginal(handNumber);

            log.debug(followOrder.getFollowOrderName() + "策略发送一条开仓交易信息：" + WebJsion.toJson(open));
            try {
                proxy.async_orderOpen(new TraderServantPrxCallback() {
                    @Override
                    public void callback_expired() {
                    }

                    @Override
                    public void callback_exception(Throwable ex) {
                    }

                    @Override
                    public void callback_userLogout(int ret, userLogoutResponse rsp) {
                    }

                    @Override
                    public void callback_userLogin(int ret, userLoginResponse rsp) {

                    }

                    @Override
                    public void callback_orderOpen(int ret, orderOpenResponse rsp) {
                        if (ret == 0 && rsp.errcode == 0) {
                            OrderMsgResult openResult = new OrderMsgResult();
                            openResult.setRequestId(rsp.requestId);//交易记录id
                            openResult.setErmsg(rsp.errmsg);//返回信息
                            openResult.setErrcode(rsp.errcode);//返回的错误码
                            Double closePrice = 0.0;
                            int handNumber = 0;
                            for (tradeItem item : rsp.tradeArrayItems) {
                                closePrice = DoubleUtil.add(closePrice,item.price);//累加价格
                                handNumber += item.volume;//累加手数
                            }
                            log.debug("开仓交易数据详情"+WebJsion.toJson(rsp));

                            openResult.setTradePrice(DoubleUtil.div(closePrice, Double.valueOf(handNumber),2));
                            //todo 手续费：手数* （开仓/平仓手续费 + 成交价 * 开仓/平仓手续费率）
                            openResult.setTradeCommission(22.2);
                            openResult.setTradeDate(rsp.tradeArrayItems.get(rsp.tradeArrayItems.size()-1).tradeDate);//交易日期：20180512
                            openResult.setTradeTime(rsp.tradeArrayItems.get(rsp.tradeArrayItems.size()-1).tradeTime);//交易时间：203024

                            openResult.setTradeVolume(handNumber);//成功交易手数
                            log.debug("接收一条开仓交易信息：" + WebJsion.toJson(openResult));
                            followOrderTradeRecordService.updateRecordByComeBackTradeMsg(openResult);
                        } else {
                            followOrderTradeRecordService.updateRecordByTradeFail((long) open.requestId);
                            if (ret == 0) {
                                log.error("开仓交易端错误：" + rsp.errmsg);

                            } else {
                                log.error("Tars框架异常：" + ret);
                            }
                        }
                    }

                    @Override
                    public void callback_orderClose(int ret, orderCloseResponse rsp) {
                    }

                    @Override
                    public void callback_instrumentQuery(int ret, instrumentQueryResponse rsp) {

                    }

                    @Override
                    public void callback_instrumentCommissionQuery(int ret, instrumentCommissionQueryResponse rsp) {

                    }

                    @Override
                    public void callback_marketDataQuery(int ret, marketDataQueryResponse rsp) {

                    }
                }, open);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("开仓失败" + e.getMessage());
            }

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

        for (FollowOrderDetail orderDetail : orderDetails) {
            Integer direction = orderDetail.getTradeDirection() == FollowOrderEnum.FollowStatus.BUY.getIndex() ?
                    FollowOrderEnum.FollowStatus.SELL.getIndex() : FollowOrderEnum.FollowStatus.BUY.getIndex();
            this.sendMsgByTrade(followOrder, direction, FollowOrderEnum.FollowStatus.CLOSE.getIndex(),
                    orderDetail.getRemainHandNumber(), orderDetail.getTicket(),
                    orderDetail.getTicket(), followOrder.getVariety().getVarietyCode(), null, orderDetail.getClientName());
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

        //设置多空方向
        Integer direction = orderDetail.getTradeDirection() == FollowOrderEnum.FollowStatus.BUY.getIndex() ?
                FollowOrderEnum.FollowStatus.SELL.getIndex() : FollowOrderEnum.FollowStatus.BUY.getIndex();
        //发送交易信息
        this.sendMsgByTrade(followOrder, direction, FollowOrderEnum.FollowStatus.CLOSE.getIndex(),
                orderDetail.getRemainHandNumber(), orderDetail.getTicket(),
                orderDetail.getTicket(), followOrder.getVariety().getVarietyCode(), null, orderDetail.getClientName());
    }

    /*
     * 策略跟单临时暂停：只做平仓单
     * */
    private void followOrderTemporaryStop(FollowOrder followOrder, DataSource data) {
        //跟净头寸
        if (followOrder.getFollowManner().equals(FollowOrderEnum.FollowStatus.FOLLOWMANNER_NET_POSITION.getIndex())) {
            //持仓数不为零
            if (followOrder.getNetPositionHoldNumber() != 0) {
                //净头寸的
                if ((followOrder.getNetPositionSum() > 0 && data.getCmd().equals(FollowOrderEnum.FollowStatus.SELL.getIndex()))
                        || (followOrder.getNetPositionSum() < 0 && data.getCmd().equals(FollowOrderEnum.FollowStatus.BUY.getIndex()))) {
                    //持仓数为正数，客户跟单方向如果做空，就跟 or 持仓数为负数，客户跟单方向如果做多，就跟
                    netPositionOrder(followOrder, data);
                }

            }
        } else {
            //如果还有没平仓的明细就做
            List<FollowOrderDetail> details = followOrderDetailService.getNOCloseDetailListByFollowOrderId(followOrder.getId());
            if (details.size() != 0 && data.getOpenClose().equals(FollowOrderEnum.FollowStatus.CLOSE.getIndex())) {
                //只做平仓的单
                followUserOrder(followOrder, data);
            }

        }
    }

    /*
    * 查找所有没有停止的跟单
    * */
    @Override
    public List<FollowOrder> getNOStopFollowOrder() {
        return followOrderDao.getNOStopFollowOrder();
    }

    /*
    *
    * 查找该账号跟单状态为启动
    * */
    @Override
    public int findAccountStatusByAccountId(Long accountId) {

        return followOrderDao.findAccountStatusByAccountId(accountId);
    }
}

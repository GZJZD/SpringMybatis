package com.web.service.imp;


import com.web.dao.FollowOrderDao;
import com.web.pojo.*;
import com.web.pojo.vo.FollowOrderVo;
import com.web.pojo.vo.OrderTrade;
import com.web.pojo.vo.UserLogin;
import com.web.service.*;

import com.web.util.common.DateUtil;
import com.web.util.common.DoubleUtil;

import com.web.util.FollowOrderGenerateUtil;
import com.web.util.StatusUtil;
import com.web.util.query.PageResult;
import com.web.util.query.QueryObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private static Logger log = Logger.getLogger(FollowOrderServiceImpl.class.getName());
    //中间表
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


    @Override
    public void save(FollowOrder followOrder) {
        followOrderDao.insert(followOrder);
    }

    @Override
    public PageResult getListFollowOrder(QueryObject queryObject) {
        int rows = followOrderDao.queryForCount(queryObject);
        if (rows == 0) {
            return new PageResult(queryObject.getPageSize());
        }
        return new PageResult(queryObject.getCurrentPage(), queryObject.getPageSize(), rows, followOrderDao.selectAll(queryObject));
    }

    @Override
    public List<FollowOrder> selectListFollowOrder() {
        return followOrderDao.selectListFollowOrder();
    }

    @Override
    public List<FollowOrder> getListFollowOrderByClientName(String clientName) {

        //通过客户的名字找到对应的客户id
        //followOrderClientService.getListByClientId()
        // 再通过客户的id找到中间表的跟单id，list<ids>
        //然后逐个找到对应的跟单,并且状态为启动
        // 对比数据源的品种是否为同一个
        return null;
    }


    @Override
    public FollowOrder getFollowOrder(Long id) {
        return followOrderDao.selectByPrimaryKey(id);
    }

    @Override
    public List<FollowOrderVo> getListFollowOrderVo() {
        List<FollowOrderVo> followOrderVos = new ArrayList<>();
        List<FollowOrder> followOrders = selectListFollowOrder();
        if (followOrders.size() != 0) {
            for (FollowOrder followOrder : followOrders) {
                FollowOrderVo followOrderVo = followOrderDetailService.getCommissionTotalAndHandNumTotal(followOrder.getId());
                if(followOrderVo == null){
                    followOrderVo = new FollowOrderVo();
                }
                //todo demo过后删除
                followOrder.setAccount(FollowOrderGenerateUtil.getAccount());
                followOrderVo.setFollowOrder(followOrder);
                //设置持仓盈亏
                followOrderVo.setPositionGainAndLoss(0.0);
                //设置平仓盈亏
                followOrderVo.setOffsetGainAndLoss(followOrderDetailService.
                        getOffsetGainAndLossByFollowOrderId(followOrder.getId()) == null? 0.0 :followOrderDetailService.
                        getOffsetGainAndLossByFollowOrderId(followOrder.getId()));
                //设置累计盈亏
                followOrderVo.setGainAndLossTotal(DoubleUtil.add(followOrderVo.getPositionGainAndLoss(),
                        followOrderVo.getOffsetGainAndLoss()) == null ? 0.0 :DoubleUtil.add(followOrderVo.getPositionGainAndLoss(),
                        followOrderVo.getOffsetGainAndLoss()));

                //客户盈亏
                followOrderVo.setClientProfit(0.0);
                //盈亏率
                followOrderVo.setProfitAndLossRate(DoubleUtil.div(followOrderVo.getOffsetGainAndLoss()==null? 0.0 :followOrderVo.getOffsetGainAndLoss(),
                        followOrderVo.getPoundageTotal() == 0.0 ?1.0 :followOrderVo.getPoundageTotal(), 2));
                //跟单成功
                followOrderVo.setSuccessTotal(followOrderTradeRecordService.getFollowOrderSuccessTotalAmount(followOrder.getId()));
                //跟单总数
                followOrderVo.setAllTotal(followOrderTradeRecordService.getFollowOrderTotalAmount(followOrder.getId()));

                followOrderVos.add(followOrderVo);
            }
        }


        return followOrderVos;
    }

    @Override
    public void updateFollowOrder(FollowOrder followOrder) {
        followOrderDao.updateByPrimaryKey(followOrder);
    }

    @Override
    public void updateFollowOrderStatus(Long followOrderId, Integer status) {
        String startTime = null;
        if (StatusUtil.FOLLOW_ORDER_START.getIndex().equals(status)) {
            startTime = DateUtil.getStringDate();
        }
        followOrderDao.updateFollowOrderStatus(followOrderId, status, startTime);
    }

    @Override
    public void madeAnOrder(DataSource data) {
        //1.先获取符合的跟单
        // List<FollowOrder> listFollowOrder = getListFollowOrderByClientName(data.login);
        // for (FollowOrder followOrder : listFollowOrder) {
        FollowOrder followOrder;
        //todo demo过后删除
        List<FollowOrder> followOrders = selectListFollowOrder();
        if (followOrders.size() == 0) {
            FollowOrderGenerateUtil followOrderGenerateUtil = new FollowOrderGenerateUtil();
            followOrder = followOrderGenerateUtil.getFollowOrder();
        } else {
            followOrder = followOrders.get(0);
        }
        followOrder.setAccount(FollowOrderGenerateUtil.getAccount());
        //todo 因为死策略是跟所有客户,所以需要判断是否符合策略的品种
        if (followOrder.getFollowOrderStatus().equals(StatusUtil.FOLLOW_ORDER_START.getIndex())) {
            if (data.getVarietyCode().equals(followOrder.getVariety().getVarietyCode())) {
                //判断是否登录了

                if (followOrder.getFollowManner().equals(StatusUtil.FOLLOWMANNER_NET_POSITION.getIndex())) {
                    //跟净头寸
                    netPositionOrder(followOrder, data);
                } else {
                    //todo 跟客户
                }
            }
        }
    }


    public void netPositionOrder(FollowOrder followOrder, DataSource data) {
        //判断策略的状态是否在交易中,不在交易就进行判断
        if (followOrder.getNetPositionStatus().equals(StatusUtil.TRADING_PAUSE.getIndex())) {


            //获取原有的持仓数
            Double oldHoldNum = followOrder.getNetPositionHoldNumber();
            //获取累加的净头寸
            Double headNum = getNowNetPositionSum(data, followOrder);
            //应持仓多少手
            int newHoldNum = (int) (headNum / followOrder.getNetPositionChange());
            //判断策略的正反方向跟单
            if (StatusUtil.DIRECTION_REVERSE.getIndex().equals(followOrder.getNetPositionDirection())) {
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
                        followOrder.setNetPositionStatus(StatusUtil.TRADING_START.getIndex());

                        //设置交易对象下单手数,发送交易请求,做多单
                        SendMsgByTrade(followOrder, StatusUtil.BUY.getIndex(), StatusUtil.CLOSE.getIndex(),
                                interim, data);
                    } else if (newHoldNum < oldHoldNum && oldHoldNum <= zero) {
                        //设置正在交易中的状态,普通交易状态
                        followOrder.setNetPositionStatus(StatusUtil.TRADING_START.getIndex());

                        //当newHoldNum为负数的时候小于原本的持仓，那就是开仓,发送交易请求,做空单

                        SendMsgByTrade(followOrder, StatusUtil.SELL.getIndex(), StatusUtil.OPEN.getIndex(),
                                interim, data);

                    } else if (oldHoldNum > zero) {
                        //设置正在交易中的状态,发送两条交易状态
                        followOrder.setNetPositionStatus(StatusUtil.TRADING_OPENCLOSE.getIndex());
                        //oldHoldNum:做多单,同时平仓又开仓，平oldHoldNum，开newHoldNum
                        if (newHoldNum != 0) {
                            //先做开仓,做空单
                            SendMsgByTrade(followOrder, StatusUtil.SELL.getIndex(), StatusUtil.OPEN.getIndex(),
                                    (double) Math.abs(newHoldNum), data);
                        }
                        //再来做一单，平仓做空单
                        SendMsgByTrade(followOrder, StatusUtil.SELL.getIndex(), StatusUtil.CLOSE.getIndex(),
                                (double) Math.abs(oldHoldNum), data);
                    }
                } else if (newHoldNum >= zero) {
                    //大于0:就做多单
                    //1.当持仓数 > zero,newHoldNum < 持仓数，那么就平仓
                    if (oldHoldNum > zero && newHoldNum < oldHoldNum) {
                        //设置正在交易中的状态,普通交易状态
                        followOrder.setNetPositionStatus(StatusUtil.TRADING_START.getIndex());

                        SendMsgByTrade(followOrder, StatusUtil.SELL.getIndex(), StatusUtil.CLOSE.getIndex(),
                                interim, data);//发送下单请求,做空单，平仓

                    } else if (oldHoldNum >= zero && newHoldNum > oldHoldNum) {
                        //设置正在交易中的状态,普通交易状态
                        followOrder.setNetPositionStatus(StatusUtil.TRADING_START.getIndex());

                        //3.当持仓数 >=zero,newHoldNum > 持仓数，开仓
                        //当newHoldNum为正数的时候大于原本的持仓，那就是开仓
                        SendMsgByTrade(followOrder, StatusUtil.BUY.getIndex(), StatusUtil.OPEN.getIndex(),//发送交易请求
                                interim, data);
                    } else if (oldHoldNum < zero) {
                        //设置正在交易中的状态,发送两条交易状态
                        followOrder.setNetPositionStatus(StatusUtil.TRADING_OPENCLOSE.getIndex());

                        //2.当持仓数 < zero ,平持仓数，开newHoldNum
                        //先做开仓,设置跟单明细的多空状态为空单,先做负的，设置手数
                        if (newHoldNum != zero) {
                            SendMsgByTrade(followOrder, StatusUtil.BUY.getIndex(), StatusUtil.OPEN.getIndex(),
                                    (double) Math.abs(newHoldNum), data);
                        }
                        //再来做一单，平仓
                        SendMsgByTrade(followOrder, StatusUtil.BUY.getIndex(), StatusUtil.CLOSE.getIndex(),
                                (double) Math.abs(oldHoldNum), data);
                    }
                }
            }
            FollowOrder followOrder1 = getFollowOrder(followOrder.getId());
            followOrder.setVersion(followOrder1.getVersion());
            //修改策略
            updateFollowOrder(followOrder);
        }
    }

    public void followUserOrder() {
        //跟客户的策略判断
    }


    private Double getNowNetPositionSum(DataSource data, FollowOrder followOrder) {
        //获取现有的净头寸
        Double netPositionSum = followOrder.getNetPositionSum();
        if (data.getCmd().equals(StatusUtil.BUY.getIndex())) {
            //多，增加净头寸
            netPositionSum = DoubleUtil.add(netPositionSum, data.getHandNumber());
        } else {
            //空，减少净头寸
            netPositionSum = DoubleUtil.sub(netPositionSum, data.getHandNumber());
        }
        followOrder.setNetPositionSum(netPositionSum);
        updateFollowOrder(followOrder);
        return netPositionSum;
    }

    public void checkLogin(FollowOrder followOrder) {
        //todo 跟单的启动是一创建就启动,还是第一个客户过来再启动?
        if (followOrder.getFollowOrderStatus().equals(StatusUtil.FOLLOW_ORDER_STOP.getIndex())) {
            UserLogin login = new UserLogin();
            login.setTypeId("userLogin");
            login.setRequestId(followOrder.getId());
            login.setBrokerId(followOrder.getAccount().getPlatform().getName());
            login.setUserId(followOrder.getAccount().getUsername());
            login.setPassword(followOrder.getAccount().getPassword());
            Account account = followOrder.getAccount();
            //发送MQ去登录
            orderTraderService.userLogin(login);
            //设计启动
            // followOrder.setFollowOrderStatus(StatusUtil.FOLLOW_ORDER_START.getIndex());
            //设计启动时间
            //followOrder.setStartTime(DateUtil.getStringDate());
            //更改表的状态

        }
    }

    @Override
    public void updateHoldNumByTradeAndFollowOrder(FollowOrder followOrder, FollowOrderTradeRecord followOrderTradeRecord) {
        //设置持仓值,获取原来的持仓值
        Double oldHoldNum = followOrder.getNetPositionHoldNumber();

        if (followOrderTradeRecord.getTradeDirection().equals(StatusUtil.SELL.getIndex())) {
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
            if (followOrder.getClientPoint().equals(StatusUtil.CLIENT_POINT_GOOD.getIndex())) {
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
     * 封装交易对象，新增交易记录，发送交易对象
     *
     * @param
     * @Author: May
     * @Date: 11:38 2zero18/5/1zero
     */
    private void SendMsgByTrade(FollowOrder followOrder, Integer orderDirection, Integer openClose,
                                Double handNumber, DataSource data) {


        FollowOrderTradeRecord followOrderTradeRecord = new FollowOrderTradeRecord();
        //设置新开仓单号
        followOrderTradeRecord.setNewTicket(data.getNewTicket());
        //设置开仓单号
        followOrderTradeRecord.setTicket(data.getTicket());
        //设置跟单id
        followOrderTradeRecord.setFollowOrderId(followOrder.getId());
        //设置品种的id
        followOrderTradeRecord.setVarietyId(followOrder.getVariety().getId());
        //设置品种代码 todo 通过数据源的品种代码,找到该交易账号所自身的品种代码
        followOrderTradeRecord.setVarietyCode(data.getVarietyCode());
        //设置开平状态
        followOrderTradeRecord.setOpenCloseType(openClose);
        //设置多空
        followOrderTradeRecord.setTradeDirection(orderDirection);
        //设置手数
        followOrderTradeRecord.setHandNumber(handNumber);
        //设置净头寸的值
        followOrderTradeRecord.setNetPositionSum(followOrder.getNetPositionSum());
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
        orderTrade.setInstrumentId(followOrder.getVariety().getVarietyCode());
        //设置买卖方向
        orderTrade.setOrderDirection(orderDirection);
        if (openClose.equals(StatusUtil.CLOSE.getIndex())) {
            //平仓
            orderTrade.setTypeId("orderClose");
            orderTrade.setOrderVolume(handNumber);
            //发送平仓交易请求
            orderTraderService.orderClose(orderTrade);
        } else {
            //开仓
            orderTrade.setTypeId("orderOpen");
            orderTrade.setVolumeTotalOriginal(handNumber);
            orderTraderService.orderOpen(orderTrade);
        }

        log.info("发送一条交易信息：" + orderTrade.toString());
    }


}

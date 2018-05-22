package com.web.service.imp;


import com.web.dao.FollowOrderDao;
import com.web.pojo.*;
import com.web.pojo.vo.OrderParameter;
import com.web.service.*;

import com.web.util.FollowOrderGenerateUtil;
import com.web.util.StatusUtil;
import com.web.util.common.DateUtil;
import com.web.util.common.DoubleUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    //发送MQ
    @Autowired
    private IOrderTraderService orderTraderService;


    @Override
    public void save(FollowOrder followOrder) {
        followOrderDao.insert(followOrder);
    }

    //todo demo 返回的是集合
    @Override
    public List<FollowOrder> getListFollowOrder() {

        //通过客户的名字找到对应的跟单id
        // 再通过获取对应策略找到对应的品种,
        // 对比数据源的品种是否为同一个
        //followOrderClientService.getListByClientId()

        return null;
    }

    @Override
    public FollowOrder getFollowOrder(Long id) {
        return followOrderDao.selectByPrimaryKey(id);
    }

    @Override
    public void updateFollowOrder(FollowOrder followOrder) {
        followOrderDao.updateByPrimaryKey(followOrder);
    }

    @Override
    public void madeAnOrder(DataSource data) {
        //1.先获取符合的跟单
       // List<FollowOrder> listFollowOrder = getListFollowOrder();
       // for (FollowOrder followOrder : listFollowOrder) {
        // List<FollowOrder> listFollowOrder = getListFollowOrder();
        FollowOrder followOrder = FollowOrderGenerateUtil.getFollowOrder();

        if(getFollowOrder(1L) ==null){
            save(followOrder);
        }else{
            followOrder = getFollowOrder(1L);
        }

        followOrder.setAccount(FollowOrderGenerateUtil.getAccount());
        //todo 因为死策略是跟所有客户,所以需要判断是否符合策略的品种
            if (data.getVarietyCode().equals(followOrder.getVariety().getVarietyCode())) {
                //判断是否登录了
                checkLogin(followOrder);
                if(followOrder.getFollowManner().equals(StatusUtil.FOLLOWMANNER_NET_POSITION.getIndex())){
                    //跟净头寸
                    netPositionOrder(followOrder,data);
                }else{
                    //todo 跟客户
                }
            }
        }



    public void netPositionOrder(FollowOrder followOrder, DataSource data) {
        //判断策略的状态是否在交易中,不在交易就进行判断
        if (followOrder.getNetPositionStatus().equals(StatusUtil.TRADING_PAUSE.getIndex())) {
            //设置正在交易中的状态
            // tactics.setNetPositionStatus(StatusUtil.TRADING_START.getIndex());

            //获取原有的持仓数
            Integer oldHoldNum = followOrder.getNetPositionHoldNumber();
            //todo
            //获取累加的净头寸
            Double headNum = getNowNetPositionSum(data, followOrder);
            //应持仓多少手
            int newHoldNum = (int) (headNum / followOrder.getNetPositionChange());
            //判断策略的正反方向跟单
            if (followOrder.getNetPositionDirection().equals(StatusUtil.DIRECTION_REVERSE.getIndex())) {
                //反向跟单,应有持仓数应该是相反的
                if(headNum > 0 && newHoldNum > 0){
                    newHoldNum = -newHoldNum;
                }
                if(headNum < 0 && newHoldNum < 0){
                    newHoldNum = -newHoldNum;
                }
            }
            //当数值不等于zero的时候才可以进行跟单操作
            if (newHoldNum != oldHoldNum) {
                //下单为空单，当净头寸的值为正的时候，持仓数应该为负
                //应有的持仓数为负
                //原有的持仓数为负数
                //todo 老大的交易数据过来的时候再进行修改
                followOrder.setNetPositionHoldNumber(newHoldNum);
                //本次应该下单的数量
                double interim = Math.abs((int) newHoldNum - oldHoldNum);
                if (newHoldNum <= zero) {
                    //小于0就是空单
                    //并且应持仓数为负数的时候，如果大于原有的持仓数就是要平仓,并且应持仓数不为正数就不需要开仓
                    if (newHoldNum > oldHoldNum) {
                        //doColseData(interim);
                        //设置交易对象下单手数
                        //发送交易请求,做多单
                        SendMsgByTrade(followOrder,StatusUtil.BUY.getIndex(), StatusUtil.CLOSE.getIndex(),
                                interim, data);
                    }else if (newHoldNum < oldHoldNum && oldHoldNum <= zero) {
                        //当newHoldNum为负数的时候小于原本的持仓，那就是开仓
                        //发送交易请求,做空单
                        SendMsgByTrade(followOrder, StatusUtil.SELL.getIndex(), StatusUtil.OPEN.getIndex(),
                                interim, data);

                    }else if (oldHoldNum > zero ) {
                        //oldHoldNum:做多单
                        //同时平仓又开仓，平oldHoldNum，开newHoldNum
                            if(newHoldNum != 0){
                                //先做开仓,做空单
                                SendMsgByTrade(followOrder,StatusUtil.SELL.getIndex(), StatusUtil.OPEN.getIndex(),
                                        (double) Math.abs(newHoldNum), data);
                            }
                        //再来做一单，平仓做空单
                        SendMsgByTrade(followOrder,StatusUtil.SELL.getIndex(), StatusUtil.CLOSE.getIndex(),
                                (double) Math.abs(oldHoldNum), data);
                    }
                }else if (newHoldNum >= zero) {
                    //大于0:就做多单
                    //1.当持仓数 > zero,newHoldNum < 持仓数，那么就平仓
                    if (oldHoldNum > zero && newHoldNum < oldHoldNum) {

                        //发送下单请求,做空单，平仓
                        SendMsgByTrade(followOrder,StatusUtil.SELL.getIndex(), StatusUtil.CLOSE.getIndex(),
                                interim, data);
                    }else if (oldHoldNum >= zero && newHoldNum > oldHoldNum) {
                        //3.当持仓数 >=zero,newHoldNum > 持仓数，开仓
                        //当newHoldNum为正数的时候大于原本的持仓，那就是开仓

                        //发送交易请求
                        SendMsgByTrade(followOrder,StatusUtil.BUY.getIndex(), StatusUtil.OPEN.getIndex(),
                                interim, data);
                    }else if (oldHoldNum < zero ) {
                        //2.当持仓数 < zero ,平持仓数，开newHoldNum
                        //先做开仓
                        //设置跟单明细的多空状态为空单,先做负的，设置手数
                            if(newHoldNum != zero){
                                SendMsgByTrade(followOrder,StatusUtil.BUY.getIndex(), StatusUtil.OPEN.getIndex(),
                                        (double) Math.abs(newHoldNum), data);
                            }
                        //再来做一单，平仓
                        SendMsgByTrade(followOrder,StatusUtil.BUY.getIndex(), StatusUtil.CLOSE.getIndex(),
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

    public void followUserOrder(){
        //跟客户的策略判断
    }


    private Double getNowNetPositionSum(DataSource data, FollowOrder followOrder) {
        //获取现有的净头寸
        Double netPositionSum = followOrder.getNetPositionSum();
        if (data.getCmd().equals(StatusUtil.BUY.getIndex())) {
            //多，增加净头寸
            netPositionSum = DoubleUtil.add(netPositionSum, data.getCounts());
        } else {
            //空，减少净头寸
            netPositionSum = DoubleUtil.sub(netPositionSum, data.getCounts());

        }
        followOrder.setNetPositionSum(netPositionSum);
        updateFollowOrder(followOrder);
        return netPositionSum;
    }

    public void checkLogin( FollowOrder followOrder) {
        if (followOrder.getFollowOrderStatus().equals(StatusUtil.FOLLOW_ORDER_STOP.getIndex())) {

            Account account = followOrder.getAccount();
            //发送MQ去登录
            orderTraderService.login(account);
            //设计启动
            followOrder.setFollowOrderStatus(StatusUtil.FOLLOW_ORDER_START.getIndex());
            //设计启动时间
            followOrder.setStartTime(DateUtil.getStringDate());
            //更改表的状态

        }
    }

    /**
     * 判断是市价还是限价，确定点位
     *
     * @param
     * @Author: May
     * @Date: 18:19 2zero18/4/25
     */
    private OrderParameter checkPoint(FollowOrder followOrder, OrderParameter orderParameter) {

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

    /**
     * 封装交易对象，新增交易记录，发送交易对象
     *
     * @param
     * @Author: May
     * @Date: 11:38 2zero18/5/1zero
     */
    private void SendMsgByTrade(FollowOrder followOrder,Integer cmd, Integer openClose,
                                Double counts, DataSource data) {


        FollowOrderTradeRecord followOrderTradeRecord = new FollowOrderTradeRecord();
        //设置新开仓单号
        followOrderTradeRecord.setNewOpenOrderNumber(data.getNewOpenOrderNum());
        //设置开仓单号
        followOrderTradeRecord.setOpenOrderNumber(data.getOpenOrderNum());
        //设置跟单id
        followOrderTradeRecord.setFollowOrderId(followOrder.getId());
        //设置品种的id
        followOrderTradeRecord.setVarietyId(followOrder.getVariety().getId());
        //设置品种代码 todo 通过数据源的品种代码,找到该交易账号所自身的品种代码
        followOrderTradeRecord.setVarietyCode(data.getVarietyCode());
        //设置开平状态
        followOrderTradeRecord.setOpenCloseType(openClose);
        //设置多空
        followOrderTradeRecord.setTradeDirection(cmd);
        //设置手数
        followOrderTradeRecord.setHandNumber(counts);
        //设置净头寸的值
        followOrderTradeRecord.setNetPositionSum(followOrder.getNetPositionSum());
        //新增操作
        followOrderTradeRecordService.save(followOrderTradeRecord);

        //设置对接号
        followOrderTradeRecord.setSignalNumber(data.getOpenOrderNum()+followOrder.getId()+followOrderTradeRecord.getId());
        //再update
        followOrderTradeRecordService.updateTradeRecord(followOrderTradeRecord);

        //创建交易对象
        OrderParameter orderParameter = new OrderParameter();
        //设置交易对象的交易账号
        orderParameter.setDealAccount(followOrder.getAccount().getUsername());
        //设置交易对象的新开仓单号
        orderParameter.setSignalNumber(followOrderTradeRecord.getSignalNumber());
        //设置交易对象下单手数
        orderParameter.setHandNum(counts);
        //设置多空，为空单
        orderParameter.setCmd(cmd);
        //开仓
        orderParameter.setOpenClose(openClose);
        //设置时间
        orderParameter.setCreateTime(data.getCreateTime());
        //设置交易对象的点位
        checkPoint(followOrder, orderParameter);
        //发送交易请求
        orderTraderService.addOrder(orderParameter);
        log.info("发送一条交易信息：" + orderParameter.toString());
    }

    /**
     * 保存跟单明细
     *
     * @param
     * @Author: May
     * @Date: 11:51 2zero18/5/1zero
     */
  /*  private void saveDocumentaryDetail(DocumentaryDetail documentaryDetail, Integer cmd, Double counts) {
        documentaryDetail.setCmd(cmd);
        //设置跟单明细的手数
        documentaryDetail.setCounts(counts);
        //设置跟单明细剩下的手数
        documentaryDetail.setLeftoverCounts(counts);
        //设置tcp的新开仓单号
        // documentaryDetail.setNewOpenOrderNum();
        //新增一个跟单明细
        //documentaryDetailService.insert(documentaryDetail);
    }
*/

    /**
     * 从跟单明细中找最早的时间进行平仓
     *
     * @param
     * @Author: May
     * @Date: 12:zero8 2zero18/5/1zero
     */
    /*private void doColseData(Double interim) {
        List<DocumentaryDetail> dataList = documentaryDetailService.selectDataByASC();
        for (DocumentaryDetail documentaryDetail : dataList) {
            if (interim != zero) {
                Double counts = documentaryDetail.getLeftoverCounts();
                if (counts > interim) {
                    documentaryDetail.setLeftoverCounts(counts - interim);
                    documentaryDetailService.updateByPrimaryKey(documentaryDetail);
                    break;
                }
                if (counts <= interim) {
                    documentaryDetail.setLeftoverCounts(0.0);
                    documentaryDetail.setClosePrice(88.8);
                    documentaryDetail.setCloseTime(new Date());
                    documentaryDetailService.updateByPrimaryKey(documentaryDetail);
                    interim = DoubleUtil.sub(interim, counts);
                }
            }
        }
    }*/


}

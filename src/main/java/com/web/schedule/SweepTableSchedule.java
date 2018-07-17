package com.web.schedule;

import com.qq.tars.client.Communicator;
import com.qq.tars.client.CommunicatorConfig;
import com.qq.tars.client.CommunicatorFactory;
import com.web.common.FollowOrderEnum;
import com.web.pojo.FollowOrder;
import com.web.pojo.FollowOrderDetail;
import com.web.pojo.vo.OrderMsgResult;
import com.web.servant.center.*;
import com.web.service.FollowOrderDetailService;
import com.web.service.FollowOrderService;
import com.web.util.common.DoubleUtil;
import com.web.util.json.WebJsion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
public class SweepTableSchedule {

    private static Map<Long,Map<String,Double>> detailPositionGainAndLoss = new HashMap<>();
    @Autowired
    private FollowOrderDetailService followOrderDetailService;
    @Autowired
    private FollowOrderService followOrderService;

    private static Logger log = LogManager.getLogger(SweepTableSchedule.class.getName());

    public static Map<String,Double> getAskAndBidByFollowOrderId(Long followOrderId){

        return detailPositionGainAndLoss.get(followOrderId);
    }

    //每3秒： */3 * * * * ?
//    @Scheduled(cron = "*/3 * * * * ?")
    public void doSweepTable(){
//        log.debug("定时器执行");
        List<FollowOrder> followOrder = followOrderService.getNOStopFollowOrder();
        CommunicatorConfig cfg = new CommunicatorConfig();
        Communicator communicator = CommunicatorFactory.getInstance().getCommunicator(cfg);
        TraderServantPrx proxy = communicator.stringToProxy(TraderServantPrx.class, "TestApp.HelloServer.HelloTrade@tcp -h 192.168.3.189 -p 50506 -t 60000");

        for (FollowOrder order : followOrder) {
            List<FollowOrderDetail> detailList = followOrderDetailService.getNOCloseDetailListByFollowOrderId(order.getId());
            if(detailList.size()!=0){
                try {
                    marketDataQueryRequest req = new marketDataQueryRequest();
                    req.setTypeId("marketDataQueryRequest");
                    req.setRequestId(order.getId().intValue());//跟单id
                    req.setInstrumentId(detailList.get(0).getContractCode());
                    proxy.async_marketDataQuery(new TraderServantPrxCallback() {
                        @Override
                        public void callback_expired() {
                        }

                        @Override
                        public void callback_exception(Throwable ex) {
                            log.debug("定时器异常："+ex.getMessage());
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

                        }

                        @Override
                        public void callback_instrumentQuery(int ret, instrumentQueryResponse rsp) {

                        }

                        @Override
                        public void callback_instrumentCommissionQuery(int ret, instrumentCommissionQueryResponse rsp) {

                        }

                        @Override
                        public void callback_marketDataQuery(int ret, marketDataQueryResponse rsp) {
                            if (ret == 0 && rsp.errcode == 0) {
                                setAskAndPid(rsp, (long) rsp.getRequestId());
                            } else {

                                if (ret == 0) {
                                    log.error("交易端市价查询异常：" + rsp.errmsg);

                                } else {
                                    log.error("Tars框架异常：" + ret);
                                }
                            }

                        }
                    }, req);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("市价查询失败" + e.getMessage());
                }
            }
        }

    }

    public void setAskAndPid(marketDataQueryResponse resp,Long orderId){
        //BUY:多；
        List<FollowOrderDetail> buyList = followOrderDetailService.getDetailListByOrderIdAndDirection(orderId, FollowOrderEnum.FollowStatus.BUY.getIndex());
        //SELL:空；
        List<FollowOrderDetail> sellList = followOrderDetailService.getDetailListByOrderIdAndDirection(orderId, FollowOrderEnum.FollowStatus.SELL.getIndex());
        Double ask = 0.0;//卖出价
        Double bid = 0.0;//买入价
        Integer buyHandNumber = 0;
        Integer sellHandNumber = 0;
        for (FollowOrderDetail orderDetail : buyList) {
            if (orderDetail.getRemainHandNumber() == null) {
                buyHandNumber += orderDetail.getHandNumber();
            } else {
                buyHandNumber += orderDetail.getRemainHandNumber();
            }
        }
        for (FollowOrderDetail orderDetail : sellList) {
            if (orderDetail.getRemainHandNumber() == null) {
                sellHandNumber += orderDetail.getHandNumber();
            } else {
                sellHandNumber += orderDetail.getRemainHandNumber();
            }
        }

        //用于计算
        Integer buyHandNum = buyHandNumber;
        Integer sellHandNum = sellHandNumber;
        for (askArrayItem item : resp.askArrayItems) {
            if(buyHandNum==0){
                break;
            }
            //卖出价格位计算
            if(buyHandNum >= item.askVolume){

                ask = DoubleUtil.add(ask,DoubleUtil.mul(item.askPrice,item.askVolume));
                buyHandNum -= item.askVolume;
            }else {
                ask = DoubleUtil.add(ask,DoubleUtil.mul(item.askPrice,buyHandNum));
                buyHandNum = 0;
            }
        }
        for (bidArrayItem item : resp.bidArrayItems) {
            if(sellHandNum==0){
                break;
            }
            //卖出价格位计算
            if(sellHandNum >= item.bidVolume){

                bid = DoubleUtil.add(bid,DoubleUtil.mul(item.bidPrice,item.bidVolume));
                sellHandNum -= item.bidVolume;
            }else {
                bid = DoubleUtil.add(bid,DoubleUtil.mul(item.bidPrice,sellHandNum));
                sellHandNum = 0;
            }
        }
        //算平均值
        ask =DoubleUtil.div(ask,buyHandNumber==0?1:buyHandNumber,2);
        bid = DoubleUtil.div(bid,sellHandNumber==0?1:sellHandNumber,2);
        Map<String,Double> holdPrice = new HashMap<>();
        holdPrice.put("ask",ask);
        holdPrice.put("bid",bid);
//        log.debug(holdPrice);
        detailPositionGainAndLoss.put((long) resp.getRequestId(),holdPrice);

    }
}

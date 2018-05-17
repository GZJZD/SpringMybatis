package com.web.imp;


import com.web.pojo.*;
import com.web.pojo.vo.OrderParameter;
import com.web.service.*;
import com.web.util.DoubleUtil;
import com.web.util.StatusUtil;
import com.web.util.TacticsGenerateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 跟单模块
 * Created by may on 2zero18/5/8.
 */
@Service
@Transactional
@SuppressWarnings("All")
public class MoinitorServiceImpl implements IMoinitorService {
    //死策略
    private Tactics Tactics = TacticsGenerateUtil.getTactics();
    //大于就是正数，小于就是负数
    private  int zero = 0 ;
    private static Logger log = Logger.getLogger(MoinitorServiceImpl.class.getName());
    @Autowired
    private IDataSourceService dataSourceService ;

    //发送MQ
  //  @Autowired
    private ITradeMsgService tradeMsgService;

    @Autowired
    private IDocumentaryDetailService documentaryDetailService;//跟单明细的service
    @Autowired
    private ITradeHistoryService tradeHistoryService;//交易历史对象

    @Override
    public Tactics getTactics() {

        return Tactics;
    }

    @Override
    public void madeAnOrder(DataSource data) {
        if (data.getVarietyCode().equals(Tactics.getVariety().getVarietyCode())) {
            //判断是否登录了
            checkLogin(Tactics);
            //获取策略的跟单参数
            DocumentaryParameters documentaryParameters = Tactics.getDocumentaryParameters();
            //判断策略的状态是否在交易中,不在交易就进行判断
            if (documentaryParameters.getStatus().equals(StatusUtil.TRADING_PAUSE.getIndex())) {
                //创建交易对象
                OrderParameter orderParameter = new OrderParameter();
                //设置交易对象的交易账号
                orderParameter.setDealAccount(Tactics.getAccount().getUsername());

                //获取现有的净头寸
                Double headNum = documentaryParameters.getHeadNum();

                //获取原有的持仓数
                Integer holdNum = documentaryParameters.getHoldNum();
                //判断策略的正反方向跟单
                if (documentaryParameters.getDirection().equals(StatusUtil.DIRECTION_REVERSE.getIndex())) {
                    //反向跟单
                    //判断TCP数据的多空方向
                    if (data.getCmd().equals(StatusUtil.BUY)) {
                        //多，增加净头寸
                        headNum = DoubleUtil.add(headNum, data.getCounts());
                    } else {
                        //空，减少净头寸
                        headNum = DoubleUtil.sub(headNum, data.getCounts());

                    }
                    System.out.println("净头寸的值为："+headNum);
                    //修改净头寸的值
                    documentaryParameters.setHeadNum(headNum);
                    //应持仓多少手
                    int sum = (int) (headNum / documentaryParameters.getChangeNum());
                    sum = -sum;
                    //当数值不等于zero的时候才可以进行跟单操作
                    if(sum== holdNum){
                        documentaryParameters.setHoldNum(sum);
                        System.out.println("现在的持仓数是：" +sum);
                    }
                    if (sum != holdNum) {
                        //下单为空单，当净头寸的值为正的时候，持仓数应该为负
                        //应有的持仓数为负
                        //原有的持仓数为负数

                        //todo 老大的交易数据过来的时候再进行修改
                        documentaryParameters.setHoldNum(sum);
                        System.out.println("现在的持仓数是：" +sum);
                        //创建跟单明细
                        DocumentaryDetail documentaryDetail = new DocumentaryDetail();
                        //跟单明细的品种
                        documentaryDetail.setVarietyCode(Tactics.getVariety().getVarietyCode());
                        //设置跟单明细的净头寸
                        documentaryDetail.setLogin(headNum + "");
                        //本次应该下单的数量
                        double interim =   Math.abs((int)sum - holdNum);
                        if (sum < zero) {
                            //并且应持仓数为负数的时候，如果大于原有的持仓数就是要平仓,并且应持仓数不为正数就不需要开仓
                            if (sum > holdNum  && holdNum < zero) {

                                doColseData(interim);

                                //设置交易对象下单手数
                                //发送交易请求
                                SendMsgByTrade(orderParameter,StatusUtil.SELL.getIndex(),StatusUtil.CLOSE.getIndex(),
                                        interim,data,documentaryDetail.getId(),documentaryParameters);
                            } else if (sum < holdNum && holdNum <= zero) {
                                //当sum为负数的时候小于原本的持仓，那就是开仓
                                //设置跟单明细的多空状态为空单,设置手数
                                saveDocumentaryDetail(documentaryDetail,StatusUtil.SELL.getIndex(),interim);

                                //发送交易请求
                                SendMsgByTrade(orderParameter,StatusUtil.SELL.getIndex(),StatusUtil.OPEN.getIndex(),
                                        interim,data,documentaryDetail.getId(),documentaryParameters);

                            }else if( holdNum > zero){
                                //同时平仓又开仓，平holdNum，开sum
                                //设置跟单明细的多空状态为空单,先做负的，设置手数
                                saveDocumentaryDetail(documentaryDetail,StatusUtil.SELL.getIndex(),(double) Math.abs(sum));

                                //先做开仓
                                SendMsgByTrade(orderParameter,StatusUtil.SELL.getIndex(),StatusUtil.OPEN.getIndex(),
                                        (double) Math.abs(sum),data,documentaryDetail.getId(),documentaryParameters);
                                //再来做一单，平仓
                                //平仓跟单明细
                                doColseData((double) Math.abs(holdNum));
                                //创建交易对象
                                OrderParameter orderParameterClose = new OrderParameter();
                                //设置交易对象的交易账号
                                orderParameterClose.setDealAccount(Tactics.getAccount().getUsername());
                                SendMsgByTrade(orderParameterClose,StatusUtil.SELL.getIndex(),StatusUtil.CLOSE.getIndex(),
                                        (double) Math.abs(holdNum),data,null,documentaryParameters);
                            }

                        } else if (sum > zero) {
                                //1.当持仓数 > zero,sum < 持仓数，那么就平仓
                            if(holdNum > zero && sum < holdNum){
                                //平仓
                                doColseData(interim);
                                //发送下单请求,做多单，平仓
                                SendMsgByTrade(orderParameter,StatusUtil.BUY.getIndex(),StatusUtil.CLOSE.getIndex(),
                                        interim,data,null,documentaryParameters);
                            }else if(holdNum < zero){
                                //2.当持仓数 < zero ,平持仓数，开sum

                                //先做开仓
                                //设置跟单明细的多空状态为空单,先做负的，设置手数
                                saveDocumentaryDetail(documentaryDetail,StatusUtil.BUY.getIndex(),(double) Math.abs(sum));

                                SendMsgByTrade(orderParameter,StatusUtil.BUY.getIndex(),StatusUtil.OPEN.getIndex(),
                                        (double) Math.abs(sum),data,documentaryDetail.getId(),documentaryParameters);
                                //再来做一单，平仓
                                //平仓跟单明细
                                doColseData((double) Math.abs(holdNum));
                                //创建交易对象
                                OrderParameter orderParameterClose = new OrderParameter();
                                //设置交易对象的交易账号
                                orderParameterClose.setDealAccount(Tactics.getAccount().getUsername());
                                SendMsgByTrade(orderParameterClose,StatusUtil.BUY.getIndex(),StatusUtil.CLOSE.getIndex(),
                                        (double) Math.abs(holdNum),data,null,documentaryParameters);

                            }else if(holdNum >= zero && sum > holdNum ){
                                //3.当持仓数 >=zero,sum > 持仓数，开仓
                                //当sum为正数的时候大于原本的持仓，那就是开仓
                                //设置跟单明细的多空状态为多单,设置手数
                                saveDocumentaryDetail(documentaryDetail,StatusUtil.BUY.getIndex(),interim);
                                //发送交易请求
                                SendMsgByTrade(orderParameter,StatusUtil.BUY.getIndex(),StatusUtil.OPEN.getIndex(),
                                        interim,data,documentaryDetail.getId(),documentaryParameters);
                            }
                        }else if(sum == zero){
                            //平仓操作，
                            doColseData((double) Math.abs(holdNum));
                            if(holdNum < zero){
                                //设置交易对象下单手数
                                //发送交易请求
                                SendMsgByTrade(orderParameter,StatusUtil.BUY.getIndex(),StatusUtil.CLOSE.getIndex(),
                                        (double) Math.abs(holdNum),data,documentaryDetail.getId(),documentaryParameters);
                            }else {
                                //设置交易对象下单手数
                                //发送交易请求
                                SendMsgByTrade(orderParameter,StatusUtil.SELL.getIndex(),StatusUtil.CLOSE.getIndex(),
                                        (double) Math.abs(holdNum),data,documentaryDetail.getId(),documentaryParameters);
                            }
                        }

                    }
                } else {
                    //todo 正向跟单
                }
            }
        }
        //新增一条tcp数据
        dataSourceService.insert(data);
    }



    /**判断该账号是否登录了
     *@Author: May
     *@param
     *@Date: 12:12 2zero18/5/1zero
     */
    private void checkLogin(Tactics Tactics){
        if (Tactics.getStatus().equals(StatusUtil.Tactics_STOP.getIndex())) {
            Account account = Tactics.getAccount();
            //发送MQ去登录
            tradeMsgService.login(account);
            //设计启动
            Tactics.setStatus(StatusUtil.Tactics_START.getIndex());
        }
    }
    /**
     * 判断是市价还是限价，确定点位
     *
     * @param
     * @Author: May
     * @Date: 18:19 2zero18/4/25
     */
    private OrderParameter checkPoint(DocumentaryParameters documentaryParameters, OrderParameter orderParameter) {
        //设置市价or限价
        orderParameter.setOrderPoint(documentaryParameters.getOrderPoint());
        //判断下单点位是否比客户好
        if (documentaryParameters.getCustomerPoint() != null) {

            if (documentaryParameters.getCustomerPoint()) {
                //下单点位比客户好
                orderParameter.setCustomerPointNum(documentaryParameters.getCustomerPointNum());
            } else {
                //比客户差
                Double num = -documentaryParameters.getCustomerPointNum();
                System.out.println(num);
                orderParameter.setCustomerPointNum(documentaryParameters.getCustomerPointNum());
            }
        }

        return orderParameter;
    }

    /**
     * 封装交易对象，新增交易历史，发送交易对象
     *@Author: May
     *@param
     *@Date: 11:38 2zero18/5/1zero
     */
    private void SendMsgByTrade(OrderParameter orderParameter,Integer cmd,Integer OpenClose,
                                Double counts,DataSource data,Long documentaryDetailId,DocumentaryParameters documentaryParameters){
        TradeHistory tradeHistory = new TradeHistory();
        tradeHistory.setVarietyCode(Tactics.getVariety().getVarietyCode());//商品
        tradeHistory.setOpenClose(OpenClose);//开仓
        tradeHistory.setCmd(cmd);//多空
        tradeHistory.setCounts(counts);//手数
        tradeHistory.setNewOpenOrderNum(data.getNewOpenOrderNum());//新开仓单号
        tradeHistoryService.insert(tradeHistory);

        //设置交易对象的新开仓单号
        orderParameter.setNewOpenOrderNum(data.getNewOpenOrderNum());
        //设置交易对象下单手数
        orderParameter.setHandNum(counts);
        //设置多空，为空单
        orderParameter.setCmd(cmd);
        //开仓
        orderParameter.setOpenClose(OpenClose);
        //设置时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        orderParameter.setCreateTime(formatter.format(data.getCreateTime()));
        //设置交易对象的点位
        checkPoint(documentaryParameters, orderParameter);
        orderParameter.setDetailedId(documentaryDetailId);
        //发送交易请求
        tradeMsgService.addOrder(orderParameter);
        log.info("发送一条交易信息："+orderParameter.toString());
    }
    /**
     * 保存跟单明细
     *@Author: May
     *@param
     *@Date: 11:51 2zero18/5/1zero
     */
    private void saveDocumentaryDetail(DocumentaryDetail documentaryDetail,Integer cmd,Double counts){
        documentaryDetail.setCmd(cmd);
        //设置跟单明细的手数
        documentaryDetail.setCounts(counts);
        //设置跟单明细剩下的手数
        documentaryDetail.setLeftoverCounts(counts);
        //设置tcp的新开仓单号
       // documentaryDetail.setNewOpenOrderNum();
        //新增一个跟单明细
        documentaryDetailService.insert(documentaryDetail);
    }


    /**
     * 从跟单明细中找最早的时间进行平仓
     *@Author: May
     *@param
     *@Date: 12:zero8 2zero18/5/1zero
     */
    private void doColseData(Double interim){
        List<DocumentaryDetail> dataList = documentaryDetailService.selectDataByASC();
        for (DocumentaryDetail documentaryDetail : dataList) {
            if(interim!=zero){
                Double counts = documentaryDetail.getLeftoverCounts();
                if(counts>interim){
                    documentaryDetail.setLeftoverCounts(counts-interim);
                    documentaryDetailService.updateByPrimaryKey(documentaryDetail);
                    break;
                }
                if(counts <= interim){
                    documentaryDetail.setLeftoverCounts(0.0);
                    documentaryDetail.setClosePrice(88.8);
                    documentaryDetail.setCloseTime(new Date());
                    documentaryDetailService.updateByPrimaryKey(documentaryDetail);
                    interim = DoubleUtil.sub(interim,counts);
                }
            }
        }
    }


}

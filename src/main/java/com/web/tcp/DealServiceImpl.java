package com.web.tcp;

import com.alibaba.fastjson.JSON;
import com.web.pojo.DealData;
import com.web.service.ICheckStrategyService;
import com.web.service.IDealDataService;
import com.web.tcp.service.DealService;
import com.web.util.ApplicationContextHolder;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 处理交易的业务逻辑
 * @author riseSun
 *
 * 2018年1月5日下午8:30:59
 */
public class DealServiceImpl implements DealService,Runnable{

    private String socketData;
    private String platformName;

    private ICheckStrategyService
            checkStrategyService = (ICheckStrategyService) ApplicationContextHolder
            .getBeanByName("checkStrategyServiceImpl");

    private IDealDataService
            dealDataService = (IDealDataService) ApplicationContextHolder
            .getBeanByName("dealDataServiceImpl");
    private static Logger log = Logger.getLogger(DealServiceImpl.class.getName());

    public DealServiceImpl(String socketData, String platformName) {
        this.socketData = socketData;
        this.platformName = platformName;
    }

    @Override
    public DealData constructor(String dealMsg) {
        String[] splitArr = dealMsg.split(";");
        int index = 0;
        System.out.println(splitArr);
        DealData dealData = new DealData();
        try {
            dealData.setHead(splitArr[index++]);//头
            dealData.setLogin(splitArr[index++]);//账号
            dealData.setOpenOrderNum(splitArr[index++]);//开仓单号
            dealData.setNewOpenOrderNum(splitArr[index++]);//新开仓单号
            dealData.setVarietyCode(splitArr[index++]);//商品
            dealData.setCounts(Double.parseDouble(splitArr[index++]));//手数
            dealData.setPrice(Double.parseDouble(splitArr[index++]));//价位
            String date = splitArr[index++];
            Date createTime = new SimpleDateFormat("yyyyMMddHHmmss").parse(date);//创建时间
            dealData.setCreateTime(createTime);//时间
            dealData.setCmd(Integer.parseInt(splitArr[index++]));//多空
            dealData.setOpenClose(Integer.parseInt(splitArr[index++]));//开平
            dealData.setProfit(Double.parseDouble(splitArr[index++]));//平仓盈亏
            dealDataService.insert(dealData);
            log.info("接收到一条来自TCP的数据："+ JSON.toJSONString(dealData));

        }catch (Exception e) {
            //数据的构造失败，毁灭当前信息
            e.printStackTrace();
            Logger.getLogger(this.getClass()).error("数据的构造失败，毁灭当前信息");
        }
        return dealData;
    }

    @Override
    public String madeAnOrder(DealData dealData) {
        return null;
    }

    @Override
    public Boolean needAnOrder(DealData dealData) {
        //获取平台当前净头寸
        //	Double netPosition = orderService.getNetPosition(platformName, dealData.getVarietyCode());
        //获取
        //平台自动下单
        //1.
        return null;
    }

    @Override
    public void run() {
        //构造下单数据
        DealData dealData = constructor(socketData);
        checkStrategyService.judgingOrderConditions(dealData);
    }

//    @Override
//    public void run() {
//        //构造下单数据
//        DealData dealData = constructor(socketData);
//        //是否需要下单 TODO 判断卖出的是否还要数量
//        //Boolean needAnOrder = needAnOrder(dealData);
//        //下单
//		/*if(needAnOrder) {
//			madeAnOrder(dealData);
//		}*/
//        OrderDealData orderDealData = judgementStrategyServiceImpl.getOrderDealData(dealData);
//        System.out.println(orderDealData);
//        log.info(orderDealData.toString());
//        // log.info(judgementStrategyServiceImpl.getCustomerDataVOByKey("xiaoXAUUSD.eDZ").toString());
//        //List<DocumentaryDetailedData> detailedDataByKey = judgementStrategyServiceImpl.getDocumentaryDetailedDataByKey("xiaoXAUUSD.eDZ");
//	/*	for (DocumentaryDetailedData detailedData : detailedDataByKey) {
//			System.out.println(detailedData.toString());
//		}
//		for (CustomerDataVO customerDataVO : judgementStrategyServiceImpl.getCustomerDataVOByKey("xiaoXAUUSD.eDZ")) {
//			System.out.println(customerDataVO.toString());
//		}*/
//
//    }

}

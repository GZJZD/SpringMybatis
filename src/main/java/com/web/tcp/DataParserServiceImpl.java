package com.web.tcp;

import com.alibaba.fastjson.JSON;
import com.web.pojo.DataSource;
import com.web.service.IFollowOrderService;
import com.web.service.OrderUserService;
import com.web.tcp.service.DataParserService;
import com.web.util.ApplicationContextHolder;

import com.web.util.common.DateUtil;
import org.apache.log4j.Logger;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 处理交易的业务逻辑
 * @author riseSun
 *
 * 2018年1月5日下午8:30:59
 */
public class DataParserServiceImpl implements DataParserService,Runnable{

    private String socketData;
    private String platformName;
    private IFollowOrderService
            followOrderService = (IFollowOrderService) ApplicationContextHolder
            .getBeanByName("followOrderServiceImpl");
    private OrderUserService
            orderUserService = (OrderUserService) ApplicationContextHolder
            .getBeanByName("orderUserService");
    private static String path = "/tcpData/";


    private static Logger log = Logger.getLogger(DataParserServiceImpl.class.getName());

    public DataParserServiceImpl(String socketData, String platformName) {
        this.socketData = socketData;
        this.platformName = platformName;
    }

    @Override
    public synchronized DataSource constructor(String dealMsg) {
        String[] splitArr = dealMsg.split(";");
        int index = 0;
        System.out.println(splitArr);
        DataSource dataSource = new DataSource();
        try {
            dataSource.setHead(splitArr[index++]);//头
            dataSource.setLogin(splitArr[index++]);//账号
            dataSource.setTicket(splitArr[index++]);//开仓单号
            dataSource.setNewTicket(splitArr[index++]);//新开仓单号
            dataSource.setVarietyCode(splitArr[index++]);//商品
            dataSource.setHandNumber(Double.parseDouble(splitArr[index++]));//手数
            dataSource.setPrice(Double.parseDouble(splitArr[index++]));//价位
            dataSource.setCreateTime(splitArr[index++]);//时间
            dataSource.setCmd(Integer.parseInt(splitArr[index++]));//多空
            dataSource.setOpenClose(Integer.parseInt(splitArr[index++]));//开平
            dataSource.setProfit(Double.parseDouble(splitArr[index++]));//平仓盈亏
            dataSource.setPlatformName(this.platformName);
            dataSource.setAgencyName("JZT");
            Logger.getLogger(this.getClass()).info("接收到一条来自TCP的数据："+ JSON.toJSONString(dataSource));
            createFile(dealMsg);
            orderUserService.addOrderUser(dataSource);

        }catch (Exception e) {
            //数据的构造失败，毁灭当前信息
            e.printStackTrace();
            Logger.getLogger(this.getClass()).error("数据的构造失败，毁灭当前信息");
        }
        return dataSource;
    }


    @Override
    public void run() {
        //构造下单数据
        DataSource dataSource = constructor(socketData);
        followOrderService.madeAnOrder(dataSource);
    }
    /**
     * 按天创建文件
     *@Author: May
     *@param
     *@Date: 14:10 2018/5/18
     */
    public  void createFile(String tcpData)throws Exception{
        Date date = new Date();
        String fileName=path+new SimpleDateFormat("yyyyMMdd").format(date);
        //如果不存在,创建文件夹
        File f = new File(fileName);
        if(!f.exists()){
            f.createNewFile();
        }
        try {
            FileWriter fw = new FileWriter(f, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(DateUtil.getStringDate()+"- - - - - - - - > "+tcpData);

          //  bw.append(tcpData);
            bw.newLine();
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

package com.web.tcp.service;

import com.web.pojo.DealData;

/**
 * 下单相关业务处理(目前只用于Socket数据监听后的处理过程)
 * @author riseSun
 *
 * 2018年1月4日上午12:10:52
 */
public interface DataParserService {
    /**
     * 构造交易数据实体对象
     * @param dealMsg
     * @return
     * @author riseSun

     * 2018年1月4日上午12:42:03
     */
    public DealData constructor(String dealMsg);
    /**
     * 实现交易逻辑
     * @return
     * @author riseSun

     * 2018年1月4日上午12:42:06
     */
    public String madeAnOrder(DealData dealData);
    /**
     * 是否需要进行下单
     * @param dealData
     * @return
     * @author riseSun

     * 2018年1月4日上午12:42:16
     */
    public Boolean needAnOrder(DealData dealData);
}

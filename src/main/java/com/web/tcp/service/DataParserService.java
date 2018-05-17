package com.web.tcp.service;

import com.web.pojo.DataSource;

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
    public DataSource constructor(String dealMsg);

}

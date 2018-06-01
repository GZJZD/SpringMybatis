package com.web.service.imp;

import com.alibaba.fastjson.JSON;

import com.web.pojo.vo.OrderTrade;
import com.web.pojo.vo.UserLogin;
import com.web.service.IOrderTraderService;
import com.web.service.IProducerMsgService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by may on 2018/4/25.
 */

@Service
@Transactional
public class OrderTraderServiceImpl implements IOrderTraderService {

    @Autowired
    private IProducerMsgService producerMsgService ;



    @Override
    public void userLogin(UserLogin userLogin) {

        producerMsgService.sendMessage(JSON.toJSONString(userLogin));
    }

    @Override
    public void userLogout(UserLogin userLogin) {
        producerMsgService.sendMessage(JSON.toJSONString(userLogin));
    }

    @Override
    public void orderOpen(OrderTrade orderTrade) {
        producerMsgService.sendMessage(JSON.toJSONString(orderTrade));
    }

    @Override
    public void orderClose(OrderTrade orderTrade) {
        producerMsgService.sendMessage(JSON.toJSONString(orderTrade));
    }
}

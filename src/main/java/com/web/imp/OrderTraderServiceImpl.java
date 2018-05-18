package com.web.imp;

import com.alibaba.fastjson.JSON;

import com.web.pojo.Account;
import com.web.pojo.vo.OrderParameter;

import com.web.service.IOrderTraderService;
import com.web.service.IProducerMsgService;
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
    private IProducerMsgService producerMsgService;


    @Override
    public void login(Account account) {
        producerMsgService.sendMessage(JSON.toJSONString(account));
    }

    @Override
    public void loginOut(Account account) {
        producerMsgService.sendMessage(JSON.toJSONString(account));
    }

    @Override
    public void addOrder(OrderParameter orderParameter) {
        producerMsgService.sendMessage(JSON.toJSONString(orderParameter));

    }

    @Override
    public void cancelOrder() {

    }
}

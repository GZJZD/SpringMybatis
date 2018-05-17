package com.web.imp;

import com.alibaba.fastjson.JSON;

import com.web.pojo.Account;
import com.web.pojo.vo.OrderParameter;

import com.web.service.IProducerService;
import com.web.service.ITradeMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by may on 2018/4/25.
 */

@Service
@Transactional
public class TradeMsgServiceImpl implements ITradeMsgService {

    @Autowired
    private IProducerService producerService;


    @Override
    public void login(Account account) {
        producerService.sendMessage(JSON.toJSONString(account));
    }

    @Override
    public void loginOut(Account account) {
        producerService.sendMessage(JSON.toJSONString(account));
    }

    @Override
    public void addOrder(OrderParameter orderParameter) {
        producerService.sendMessage(JSON.toJSONString(orderParameter));

    }

    @Override
    public void cancelOrder() {

    }
}

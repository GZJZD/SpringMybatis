package com.web.service.imp;

import com.alibaba.fastjson.JSON;

import com.web.pojo.Account;
import com.web.pojo.vo.OrderParameter;

import com.web.service.IOrderTraderService;
import com.web.service.IProducerMsgService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by may on 2018/4/25.
 */

@Service
@Transactional
public class OrderTraderServiceImpl implements IOrderTraderService {

    @Resource(name = "loginProducerMsgServiceImpl")
    private IProducerMsgService loginProducerMsgServiceImpl ;
    @Resource(name = "orderProducerMsgServiceImpl")
    private IProducerMsgService orderProducerMsgServiceImpl ;


    @Override
    public void login(Account account) {
        loginProducerMsgServiceImpl.sendMessage(JSON.toJSONString(account));
    }

    @Override
    public void loginOut(Account account) {
        loginProducerMsgServiceImpl.sendMessage(JSON.toJSONString(account));
    }

    @Override
    public void addOrder(OrderParameter orderParameter) {
        orderProducerMsgServiceImpl.sendMessage(JSON.toJSONString(orderParameter));

    }

    @Override
    public void cancelOrder() {

    }
}

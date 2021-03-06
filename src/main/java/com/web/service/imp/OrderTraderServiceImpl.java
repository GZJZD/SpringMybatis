package com.web.service.imp;



import com.web.pojo.vo.OrderTrade;
import com.web.pojo.vo.UserLogin;
import com.web.service.OrderTraderService;
import com.web.service.ProducerMsgService;


import com.web.util.json.WebJsion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by may on 2018/4/25.
 */

@Service
@Transactional
public class OrderTraderServiceImpl implements OrderTraderService {

    @Autowired
    private ProducerMsgService producerMsgService ;



    @Override
    public void userLogin(UserLogin userLogin) {

        producerMsgService.sendMessage(WebJsion.toJson(userLogin));
    }

    @Override
    public void userLogout(UserLogin userLogin) {
        producerMsgService.sendMessage(WebJsion.toJson(userLogin));
    }

    @Override
    public void orderOpen(OrderTrade orderTrade) {
        producerMsgService.sendMessage(WebJsion.toJson(orderTrade));
    }

    @Override
    public void orderClose(OrderTrade orderTrade) {
        producerMsgService.sendMessage(WebJsion.toJson(orderTrade));
    }
}

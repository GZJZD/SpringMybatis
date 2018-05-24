package com.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.web.pojo.OrderUser;
import com.web.service.OrderUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * 用户管理控制层
 */
@Controller
@RequestMapping("/orderUser")
public class OrderUserController {
    @Autowired
    @Qualifier("orderUserService")
    private OrderUserService orderUserService;



    @RequestMapping(value = "/get.Action")
    @ResponseBody
    public  List<OrderUser> findAll(){
        String str = JSONArray.toJSONString(orderUserService.findAll());
       List<OrderUser> orderUserList =  orderUserService.findAll();
        return orderUserList;

    }
}

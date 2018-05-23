package com.web.controller;

import com.web.pojo.FollowOrder;
import com.web.service.IFollowOrderService;
import com.web.util.query.PageResult;
import com.web.util.query.QueryObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by may on 2018/5/23.
 */
@Controller
public class FollowOrderController {
    @Autowired
    private IFollowOrderService followOrderService;
    @RequestMapping("pageTest")
    @ResponseBody
    public PageResult getOrderTest(@ModelAttribute("qo") QueryObject queryObject){
        PageResult result = followOrderService.getListFollowOrder(queryObject);
        return result;
    }
}

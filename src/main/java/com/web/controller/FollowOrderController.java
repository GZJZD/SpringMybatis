package com.web.controller;

import com.web.pojo.FollowOrder;
import com.web.pojo.FollowOrderClient;
import com.web.pojo.vo.FollowOrderVo;
import com.web.pojo.vo.NetPositionDetailVo;
import com.web.service.IFollowOrderDetailService;
import com.web.service.IFollowOrderService;
import com.web.util.JSONResult;
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
    @Autowired
    private IFollowOrderDetailService followOrderDetailService;
    @RequestMapping("pageTest")
    @ResponseBody
    public PageResult getOrderTest(@ModelAttribute("qo") QueryObject queryObject){
        PageResult result = followOrderService.getListFollowOrder(queryObject);
        return result;
    }

    @RequestMapping(value = "getPositionDetails.Action")
    @ResponseBody
    public  List<NetPositionDetailVo> getPositionDetails( Long followOrderId){
        List<NetPositionDetailVo> netPositionDetailVos = followOrderDetailService.
                getDetailListByFollowOrderId(followOrderId);
        return netPositionDetailVos;
    }

    @RequestMapping("getListFollowOrder.Action")
    @ResponseBody
    public List<FollowOrderVo> getListFollowOrder(){
        List<FollowOrderVo> listFollowOrderVo = followOrderService.getListFollowOrderVo();
        return listFollowOrderVo;
    }


    public JSONResult createFollowOrder(FollowOrder followOrder, List<FollowOrderClient> followOrderClients){
        try {
            FollowOrder order = new FollowOrder();
            order.setFollowOrderName(followOrder.getFollowOrderName());
           // order.setAccount();

        }catch (Exception e){
            e.printStackTrace();
            return new JSONResult(false,"创建跟单失败");
        }
        return new JSONResult("创建跟单成功");
    }



}

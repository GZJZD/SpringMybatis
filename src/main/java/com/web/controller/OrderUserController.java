package com.web.controller;

import com.web.pojo.FollowOrder;
import com.web.pojo.OrderUser;
import com.web.pojo.vo.orderuser.OrderUserDetailsVo;
import com.web.pojo.vo.orderuser.OrderUserListVo;
import com.web.pojo.vo.orderuser.OrderUserVo;
import com.web.service.FollowOrderService;
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
    @Autowired
    private FollowOrderService followOrderService;


    @RequestMapping(value = "/findAll.Action")
    @ResponseBody
    public  List<OrderUser> findAll(){
//        String str = JSONArray.toJSONString(orderUserService.findAll());
       List<OrderUser> orderUserList =  orderUserService.findAll();
        return orderUserList;

    }

    /**
     * 用戶列表的计算 & 列表展示
     * @param endTime 结束时间
     * @param startTime 开始时间
     * @param platFormCode  平台
     * @param productCode 商品代码
     * @param agencyName 代理人
     * @param contract 合约
     * @param userCode 用户代码
     * @return
     */
    @RequestMapping(value = "/countOrderUser.Action")
    @ResponseBody
    public OrderUserListVo countOrderUser(String endTime ,String startTime,String platFormCode,String productCode,String agencyName,String contract,String userCode ){
        OrderUserVo orderUserVo = new OrderUserVo();
        orderUserVo.setStartTime(startTime);
        orderUserVo.setEndTime(endTime);
        orderUserVo.setProductCode(productCode);
        orderUserVo.setAgencyName(agencyName);
        orderUserVo.setPlatFormCode(platFormCode);
        orderUserVo.setContract(contract);
        orderUserVo.setUserCode(userCode);
        OrderUserListVo orderUserVos = orderUserService.countOrderUser(orderUserVo);
        return orderUserVos;
    }

    /**
     * 查看明细查询
     * @param productCode 产品代码
     * @param userCode 用户代码
     * @return
     */
    @RequestMapping(value = "/getOrderUser.Action")
    @ResponseBody
    public OrderUserDetailsVo getOrderUser (String productCode,String userCode,String platFormCode){
        OrderUserDetailsVo orderUserDetailsVos =  orderUserService.getUserDetails(userCode,productCode,platFormCode);
        return orderUserDetailsVos;
    }

    /**
     * 获取所有启动中的策略
     */
    @RequestMapping(value = "getfollowOrderList.Action")
    @ResponseBody
    public List<FollowOrder>  getfollowOrderList(){
        List <FollowOrder> list = followOrderService.getNOStopFollowOrder();
            return list;
    }

    /**
     * 通过id 获取策略明细
     */
    @RequestMapping(value = "getFollowOrder.Action")
    @ResponseBody
    public FollowOrder getFollowOrder(Long id){
        FollowOrder followOrder = followOrderService.getFollowOrder(id);
        return followOrder;

    }
}

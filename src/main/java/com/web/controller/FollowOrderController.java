package com.web.controller;

import com.alibaba.fastjson.JSON;
import com.web.common.FollowOrderEnum;
import com.web.pojo.Account;
import com.web.pojo.FollowOrder;
import com.web.pojo.FollowOrderClient;
import com.web.pojo.Variety;
import com.web.pojo.vo.FollowOrderPageVo;
import com.web.pojo.vo.FollowOrderVo;
import com.web.pojo.vo.NetPositionDetailVo;
import com.web.service.IFollowOrderDetailService;
import com.web.service.IFollowOrderService;
import com.web.service.IVarietyService;
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
 * created by may on 2018/5/23.
 */
@Controller
@RequestMapping("/followOrder")
public class FollowOrderController {


    @Autowired
    private IFollowOrderService followOrderService;
    @Autowired
    private IFollowOrderDetailService followOrderDetailService;
    @Autowired
    private IVarietyService varietyService;

    @RequestMapping("/pageTest")
    @ResponseBody
    public PageResult getOrderTest(@ModelAttribute("qo") QueryObject queryObject){
        PageResult result = followOrderService.getListFollowOrder(queryObject);
        return result;
    }

    @RequestMapping(value = "/getPositionDetails.Action")
    @ResponseBody
    public  List<NetPositionDetailVo> getPositionDetails( Long followOrderId){
        List<NetPositionDetailVo> netPositionDetailVos = followOrderDetailService.
                getDetailListByFollowOrderId(followOrderId);
        return netPositionDetailVos;
    }

    @RequestMapping("/getListFollowOrder.Action")
    @ResponseBody
    public List<FollowOrderVo> getListFollowOrder(Long varietyId,Long accountId){
        List<FollowOrderVo> listFollowOrderVo = followOrderService.getListFollowOrderVo(varietyId,accountId);
        return listFollowOrderVo;
    }

    @RequestMapping(value = "/createFollowOrder.Action")
    @ResponseBody
    public JSONResult createFollowOrder(String followOrderName,Long accountId,Long varietyId,Integer maxProfit,Double maxProfitNumber,
                                        Integer maxLoss,Double maxLossNumber,Integer accountLoss,Double accountLossNumber,Integer orderPoint,
                                        Integer clientPoint,Double clientPointNumber,Integer followManner,Integer netPositionDirection,
                                        Integer netPositionChange,Integer netPositionFollowNumber,
                                        List<FollowOrderClient> followOrderClients){
        try {
            FollowOrder followOrder = new FollowOrder();
            followOrder.setFollowOrderName(followOrderName);
            Account account = new Account();
            account.setId(accountId);
            followOrder.setAccount(account);
            Variety variety = new Variety();
            variety.setId(varietyId);
            followOrder.setVariety(variety);
            followOrder.setMaxProfit(maxProfit);
            followOrder.setMaxProfitNumber(maxProfitNumber);
            followOrder.setMaxLoss(maxLoss);
            followOrder.setMaxLossNumber(maxLossNumber);
            followOrder.setAccountLoss(accountLoss);
            followOrder.setAccountLossNumber(accountLossNumber);
            followOrder.setOrderPoint(orderPoint);
            followOrder.setClientPoint(clientPoint);
            followOrder.setClientPointNumber(clientPointNumber);
            followOrder.setFollowManner(followManner);
            followOrder.setNetPositionDirection(netPositionDirection);
            followOrder.setNetPositionChange(netPositionChange);
            followOrder.setNetPositionFollowNumber(netPositionFollowNumber);
            followOrderService.createFollowOrder(followOrder,followOrderClients);

        }catch (Exception e){
            e.printStackTrace();
            return new JSONResult(false,"创建跟单失败");
        }
        return new JSONResult("创建跟单成功");
    }
    @RequestMapping(value = "/updateFollowOrderStatus.Action")
    @ResponseBody
    public JSONResult updateFollowOrderStatus(Long id,Integer status){
        try {
            followOrderService.updateFollowOrderStatus(id,status);
            if(status.equals(FollowOrderEnum.FollowStatus.FOLLOW_ORDER_STOP.getIndex())){
                followOrderService.closeAllOrderByFollowOrderId(id);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new JSONResult(false,"操作失败");
        }
        return new JSONResult("操作成功");
    }
    /*
     *
     *   手动平仓
     * @author may
     * @date 2018/6/6 16:46
     * @param
     * @return
     */
    @RequestMapping("/manuallyClosePosition.Action")
    @ResponseBody
    public JSONResult manuallyClosePosition(Long detailId){
        try {
            followOrderService.manuallyClosePosition(detailId);
        }catch (Exception e){
            e.printStackTrace();
            return new JSONResult(false,"手动平仓失败");
        }
        return new JSONResult("手动平仓成功");
    }

    @RequestMapping("/getFollowOrderPageVo.Action")
    @ResponseBody
    public FollowOrderPageVo getFollowOrderPageVo(){
        return followOrderService.getFollowOrderPageVo();
    }

    @RequestMapping("/getListVariety.Action")
    @ResponseBody
    public String getListVariety(){
        return JSON.toJSONString(varietyService.getVarietyList());
    }

}

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
import com.web.service.IFollowOrderClientService;
import com.web.service.IFollowOrderDetailService;
import com.web.service.IFollowOrderService;
import com.web.service.IFollowOrderTradeRecordService;
import com.web.util.JSONResult;
import com.web.util.common.DateUtil;
import com.web.util.json.WebJsion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    private IFollowOrderClientService followOrderClientService;
    @Autowired
    private IFollowOrderTradeRecordService followOrderTradeRecordService;


    /*
     * 跟单明细列表展示
     * @param followOrderId 跟单id
     * @return
     */
    @RequestMapping(value = "/getListDetails.Action")
    @ResponseBody
    public  List<?> getListDetails( Long followOrderId,String endTime ,String startTime,Integer status ){
        FollowOrder followOrder = followOrderService.getFollowOrder(followOrderId);
        if(followOrder!=null){
            if(followOrder.getFollowManner().equals(FollowOrderEnum.FollowStatus.FOLLOWMANNER_NET_POSITION.getIndex())){
               //净头寸

                List<NetPositionDetailVo> netPositionDetailVos = followOrderDetailService.
                        getDetailListByFollowOrderId(followOrderId,endTime,startTime,status);
                return netPositionDetailVos;
            }else{
                //客户
                return null;
            }
        }
        return null;
    }

    /*
     * 跟单列表展示
     * @param varietyId 品种id
     * @param accountId 账号id
     * @param endTime 结束时间
     * @param startTime 开始时间
     * @param status 跟单状态
     * @return
     */
    @RequestMapping("/getListFollowOrder.Action")
    @ResponseBody
    public List<FollowOrderVo> getListFollowOrder(Long varietyId,Long accountId,String endTime ,String startTime,Integer status ){
        FollowOrderPageVo followOrderPageVo = new FollowOrderPageVo();
        followOrderPageVo.setVarietyId(varietyId);
        followOrderPageVo.setStatus(status);
        followOrderPageVo.setAccountId(accountId);
        if(endTime != null && !"".equals(endTime)){
            String date = DateUtil.dateToStrLong(DateUtil.getEndDate(DateUtil.strToDate(endTime)));
            followOrderPageVo.setEndTime(date);
        }
        if(startTime != null && !"".equals(startTime)){
            followOrderPageVo.setStartTime(startTime);
        }
        List<FollowOrderVo> listFollowOrderVo = followOrderService.getListFollowOrderVo(followOrderPageVo);
        return listFollowOrderVo;
    }

    /*
     * 创建跟单
     *
     * @param followOrderName：跟单名称
     * @param accountId：账号id
     * @param varietyCode：品种名称
     * @param maxProfit：最大止盈
     * @param maxProfitNumber：最大止盈数
     * @param maxLoss：最大止损
     * @param maxLossNumber：最大止损数
     * @param accountLoss：账户止损
     * @param accountLossNumber：账户止损数
     * @param orderPoint：下单点位
     * @param clientPoint：客户点位
     * @param clientPointNumber：客户点位数
     * @param followManner：跟单方式
     * @param netPositionDirection：净头寸方向
     * @param netPositionChange：净头寸变化值
     * @param netPositionFollowNumber：跟几首
     * @param followOrderClients：跟单与客户的关联表
     * @return
     */
    @RequestMapping(value = "/createFollowOrder.Action")
    @ResponseBody
    public JSONResult createFollowOrder(String followOrderName,Long accountId,String varietyCode,Integer maxProfit,Double maxProfitNumber,
                                        Integer maxLoss,Double maxLossNumber,Integer accountLoss,Double accountLossNumber,Integer orderPoint,
                                        Integer clientPoint,Double clientPointNumber,Integer followManner,Integer netPositionDirection,
                                        Integer netPositionChange,Integer netPositionFollowNumber,
                                       String followOrderClients){
        try {
            List<FollowOrderClient> followOrderClients1 = WebJsion.parseArray(followOrderClients,FollowOrderClient.class);
            FollowOrder followOrder = new FollowOrder();
            followOrder.setFollowOrderName(followOrderName);
            Account account = new Account();
            account.setId(accountId);
            followOrder.setAccount(account);
            Variety variety = new Variety();
            variety.setVarietyCode(varietyCode);
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
            if(followOrder.getFollowManner().equals(FollowOrderEnum.FollowStatus.FOLLOWMANNER_NET_POSITION.getIndex())){
                followOrder.setNetPositionDirection(netPositionDirection);
                followOrder.setNetPositionChange(netPositionChange);
                followOrder.setNetPositionFollowNumber(netPositionFollowNumber);
            }

           followOrderService.createFollowOrder(followOrder,followOrderClients1);


        }catch (Exception e){
            e.printStackTrace();
            return new JSONResult(false,"创建跟单失败");
        }
        return new JSONResult("创建跟单成功");
    }
    /*
     *  跟单状态修改
     * @param   id  跟单id
     * @param   status  跟单状态
     * @return
     */
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
     * @param detailId 明细id
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

    /*
    * 跟单列表头展示
    * */
    @RequestMapping("/getFollowOrderPageVo.Action")
    @ResponseBody
    public FollowOrderPageVo getFollowOrderPageVo(){
        return followOrderDetailService.getFollowOrderPageVo();
    }



    /*
    * 修改跟单
    * */
    @RequestMapping("/updateFollowOrder.Action")
    @ResponseBody
    public JSONResult updateFollowOrder(Long id,String followOrderName,Long accountId,String varietyCode,Integer maxProfit,Double maxProfitNumber,
                                        Integer maxLoss,Double maxLossNumber,Integer accountLoss,Double accountLossNumber,Integer orderPoint,
                                        Integer clientPoint,Double clientPointNumber,Integer followManner,Integer netPositionDirection,
                                        Integer netPositionChange,Integer netPositionFollowNumber,
                                        String followOrderClients){
        try {
            FollowOrder followOrder = followOrderService.getFollowOrder(id);
            if(followOrder !=null){
                List<FollowOrderClient> followOrderClients1 = WebJsion.parseArray(followOrderClients,FollowOrderClient.class);
                followOrder.setFollowOrderName(followOrderName);
                Account account = new Account();
                account.setId(accountId);
                followOrder.setAccount(account);
                Variety variety = new Variety();
                variety.setVarietyCode(varietyCode);
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
                if(followOrder.getFollowManner().equals(FollowOrderEnum.FollowStatus.FOLLOWMANNER_NET_POSITION.getIndex())){
                    followOrder.setNetPositionDirection(netPositionDirection);
                    followOrder.setNetPositionChange(netPositionChange);
                    followOrder.setNetPositionFollowNumber(netPositionFollowNumber);
                }
                followOrderService.updateFollowOrder(followOrder);
                //删除
                followOrderClientService.deleteByFollowOrderId(id);
                if(!followOrder.getFollowManner().equals(FollowOrderEnum.FollowStatus.FOLLOWMANNER_NET_POSITION.getIndex())){
                    //保存关联
                    followOrderClientService.saveListFollowOrderClient(followOrderClients1,followOrder);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            return new JSONResult(false,"修改跟单失败");
        }
        return new JSONResult("修改成功");
    }

    /*
    *
    * 客户数据展示
    * */
    @RequestMapping("/getListClientNetPosition.Action")
    @ResponseBody
    public List<?> getListClientNetPosition(Long followOrderId,Integer status,String clientName,Integer openOrCloseStatus){
        FollowOrder followOrder = followOrderService.getFollowOrder(followOrderId);
        if (followOrder!=null){
            if(followOrder.getFollowManner().equals(FollowOrderEnum.FollowStatus.FOLLOWMANNER_NET_POSITION.getIndex())){

                return followOrderTradeRecordService.getListClientNetPosition(followOrderId,status,clientName,openOrCloseStatus);
            }else{
                //todo 跟每一单
                return null;
            }
        }
        return null;
    }


}

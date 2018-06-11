package com.web.service;


import com.web.pojo.DataSource;
import com.web.pojo.FollowOrder;
import com.web.pojo.FollowOrderClient;
import com.web.pojo.FollowOrderTradeRecord;
import com.web.pojo.vo.FollowOrderPageVo;
import com.web.pojo.vo.FollowOrderVo;
import com.web.util.query.PageResult;
import com.web.util.query.QueryObject;


import java.util.List;


/**
 * 跟单模块
 * Created by may on 2018/5/8.
 */
public interface IFollowOrderService {


    void save(FollowOrder followOrder);
    void updateFollowOrder(FollowOrder followOrder);

    //修改跟单的状态
    void updateFollowOrderStatus(Long followOrderId,Integer status);

   // PageResult getListFollowOrder(QueryObject queryObject);




    List<FollowOrder> getListFollowOrderByClientName(String clientName);

    FollowOrder getFollowOrder(Long id);

    List<FollowOrderVo> getListFollowOrderVo(Long varietyId, Long accountId);

    void madeAnOrder(DataSource data);



    void checkLogin( FollowOrder followOrder);

    void createFollowOrder(FollowOrder followOrder, List<FollowOrderClient> followOrderClients);

    //设置持仓值
    void updateHoldNumByTradeAndFollowOrder(FollowOrder followOrder, FollowOrderTradeRecord followOrderTradeRecord);


    void closeAllOrderByFollowOrderId(Long followOrderId);

    void sendMsgByTrade(FollowOrder followOrder, Integer orderDirection, Integer openClose,
                               Double handNumber,String newTicket,String ticket,String varietyCode);

    void manuallyClosePosition(Long detailId);

    FollowOrderPageVo getFollowOrderPageVo();

    List<FollowOrder> selectListFollowOrder(Long varietyId, Long accountId);
}

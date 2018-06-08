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
    /** 返回跟单集合,分页
     *@Author: May
     *@Date: 12:48 2018/5/8
     * @param
     * @param queryObject
     */
    PageResult getListFollowOrder(QueryObject queryObject);



    /**
     * 通过客户的名字找到对应的跟单集合
     *@Author: May
     *@param clientName
     *@Date: 14:30 2018/5/22
     */
    List<FollowOrder> getListFollowOrderByClientName(String clientName);
    /** 返回跟单
     *@Author: May
     *@param
     *@Date: 12:48 2018/5/8
     */
    FollowOrder getFollowOrder(Long id);

    /*
     *
     *  返回页面的跟单映射
     * @author may
     * @date 2018/5/25 15:37
     * @param
     * @return
     */
    List<FollowOrderVo> getListFollowOrderVo(Long varietyId, Long accountId);

    /**
     * 实现交易逻辑
     *@Author: May
     *@param data
     *@Date: 14:24 2018/4/23
     */
    void madeAnOrder(DataSource data);


    /**判断该账号是否登录了
     *@Author: May
     *@param
     *@Date: 12:12 2zero18/5/1zero
     */
    void checkLogin( FollowOrder followOrder);
    /*
     *
     *   创建跟单
     * @author may
     * @date 2018/6/4 17:03
     * @param
     * @return
     */
    void createFollowOrder(FollowOrder followOrder, List<FollowOrderClient> followOrderClients);

    //设置持仓值
    void updateHoldNumByTradeAndFollowOrder(FollowOrder followOrder, FollowOrderTradeRecord followOrderTradeRecord);

    /*
     *
     *   平所有未平的跟单
     * @author may
     * @date 2018/6/5 19:08
     * @param
     * @return
     */
    void closeAllOrderByFollowOrderId(Long followOrderId);
    /**
     * 封装交易对象，新增交易记录，发送交易信息
     *
     * @param
     * @Author: May
     * @Date: 11:38 2zero18/5/1zero
     */
    void sendMsgByTrade(FollowOrder followOrder, Integer orderDirection, Integer openClose,
                               Double handNumber,String newTicket,String ticket,String varietyCode);
    /*
     *
     *   通过明细得id，进行手动平仓
     * @author may
     * @date 2018/6/6 17:13
     * @param
     * @return
     */
    void manuallyClosePosition(Long detailId);
    /*
     *
     *   返回页面总计算
     * @author may
     * @date 2018/6/7 14:33
     * @param
     * @return
     */
    FollowOrderPageVo getFollowOrderPageVo();

    List<FollowOrder> selectListFollowOrder(Long varietyId, Long accountId);
}

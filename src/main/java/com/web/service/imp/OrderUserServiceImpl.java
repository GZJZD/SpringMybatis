package com.web.service.imp;

import com.web.common.OrderUserEnum;
import com.web.dao.OrderUserDao;
import com.web.database.OrderHongKongService;
import com.web.database.entity.Agent;
import com.web.database.entity.Prices;
import com.web.pojo.DataSource;
import com.web.pojo.OrderUser;
import com.web.database.entity.PlatFromUsers;
import com.web.pojo.vo.OrderUserDetailsVo;
import com.web.pojo.vo.OrderUserListVo;
import com.web.pojo.vo.OrderUserVo;
import com.web.service.OrderUserService;
import com.web.util.common.DateUtil;
import com.web.util.common.DoubleUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

@Service("orderUserService")
@Transactional
public class OrderUserServiceImpl implements OrderUserService {
    @Autowired(required = false)
    private OrderUserDao orderUserDao;
    @Autowired
    private OrderHongKongService orderHongKongService;

    private static Logger log = LogManager.getLogger(OrderUserServiceImpl.class.getName());
    public static final    String [] platFromList={"orders75","orders76"};


    @Override
    public List<OrderUser> findAll() {
        List<OrderUser> orderUserList = orderUserDao.findAll();
        return orderUserList;
    }


    public  String addOrderUser (DataSource dataSource){
                 String message = null;
                if ( dataSource == null){
                    message ="数据为空";
                }else{
                //开仓
                if (dataSource.getOpenClose() == OrderUserEnum.openOrClose.open.getCode()){
                    message = save(dataSource);
                }
                //平仓
                if( dataSource.getOpenClose() == OrderUserEnum.openOrClose.close.getCode()){
                    message = updateOrAdd(dataSource);
                }
            }
                    log.info(message);
                    return message;
    }

    public String updateOrAdd(DataSource dataSource){
            String message = null;
            OrderUser orderUser = findByTicket(dataSource.getTicket());
            Integer longShor ; //历史多空
            Double openPrice =0.0;// 历史开仓价格
            Double difference;
        if(orderUser == null){
            message = saveClose(dataSource);
            return message;
        }else{
          longShor = orderUser.getLongShort(); //历史多空
          openPrice = orderUser.getOpenPrice();// 历史开仓价格
        }
        //全平
        if(dataSource.getTicket().equals(dataSource.getNewTicket())){

                orderUser.setCloseTime(dataSource.getCreateTime()); //平仓时间
                orderUser.setCommission(dataSource.getCommission());//手续费
                orderUser.setStopLoss(dataSource.getStopLoss());//止损
                orderUser.setStopProfit(dataSource.getStopProfit());//止盈
    //          orderUser.setHandNumber(dataSource.getHandNumber());
                orderUser.setCommission(dataSource.getCommission());//手续费
                orderUser.setStopLoss(dataSource.getStopLoss());//止损
                orderUser.setStopProfit(dataSource.getStopProfit());//止盈
                orderUser.setProfit(dataSource.getProfit()); //平仓盈亏
                orderUser.setClosePrice(dataSource.getPrice()); //平仓价格
                orderUser.setLongShort(dataSource.getCmd());//开多  ，平 空
                message = update(orderUser);

        }
        //平半
        if(!dataSource.getTicket().equals(dataSource.getNewTicket())){
            //平开
            if(orderUser.getHandNumber() > dataSource.getHandNumber()){
                double handNumber = orderUser.getHandNumber();
                double everyHandPrice = DoubleUtil.div(orderUser.getCommission(),orderUser.getHandNumber());//每手价格
                //先平仓
                orderUser.setHandNumber(dataSource.getHandNumber()); // 手数
                orderUser.setCloseTime(dataSource.getCreateTime()); //平仓时间
                orderUser.setLongShort(dataSource.getCmd());//多空
                orderUser.setProfit(dataSource.getProfit()); //平仓盈亏
                orderUser.setClosePrice(dataSource.getPrice());//平仓价格
                orderUser.setUpdateDate(DateUtil.getStringDate());//修改时间
                orderUser.setStopLoss(dataSource.getStopLoss());//止损
                orderUser.setStopProfit(dataSource.getStopProfit());//止盈
                orderUser.setCommission(DoubleUtil.add(DoubleUtil.mul(everyHandPrice,dataSource.getHandNumber()),dataSource.getCommission()));//手续费
                update(orderUser);
                //后开仓
                DataSource newDataSource = new DataSource();
                newDataSource = dataSource;
                difference = DoubleUtil.sub(handNumber,dataSource.getHandNumber()); //计算差值
                newDataSource.setHandNumber(difference);
                newDataSource.setCmd(longShor);
                newDataSource.setPrice(openPrice);
                newDataSource.setCommission(DoubleUtil.mul(difference,everyHandPrice));//手续费

                save(newDataSource);
                message = "平仓成功";
            }
        }
        return message;
    }


    /**
     * 新增
     * @param dataSource tcp 数据包
     * @return String  状态
     */
    public String save(DataSource dataSource){
            OrderUser orderUser = new OrderUser();
//            orderUser.setCloseTime(dataSource.getCreateTime());//平仓时间&
            orderUser.setTicket(dataSource.getNewTicket());//新开仓单号
            orderUser.setOpenTime(dataSource.getCreateTime());//开仓时间
            orderUser.setUserCode(dataSource.getLogin()); //账号
            orderUser.setAgencyName(dataSource.getAgencyName());//代理人
            orderUser.setPlatFormCode(dataSource.getPlatformName());//平台名称
            orderUser.setProductCode(dataSource.getVarietyCode());//商品
            orderUser.setHandNumber( dataSource.getHandNumber());//手数);
            orderUser.setLongShort(dataSource.getCmd());//多空
            orderUser.setPrice(dataSource.getPrice());//价位
            orderUser.setProfit(dataSource.getProfit());//平仓盈亏
            orderUser.setOpenPrice(dataSource.getPrice());//开仓价
            orderUser.setCommission(dataSource.getCommission());//手续费
            orderUser.setStopLoss(dataSource.getStopLoss());//止损
            orderUser.setStopProfit(dataSource.getStopProfit());//止盈
            orderUser.setCreateDate(DateUtil.getStringDate());//创建时间
            orderUserDao.addOrderUser(orderUser);
            log.info("新增orderUser数据成功");
            return  "添加成功";
    }

    /**
     *
     * @param dataSource tcp 数据包
     * @return
     */
    public String saveClose(DataSource dataSource){
        OrderUser orderUser = new OrderUser();
        orderUser.setCloseTime(dataSource.getCreateTime());//平仓时间&
        orderUser.setTicket(dataSource.getNewTicket());//新开仓单号
        orderUser.setUserCode(dataSource.getLogin()); //账号
        orderUser.setAgencyName(dataSource.getAgencyName());//代理人
        orderUser.setPlatFormCode(dataSource.getPlatformName());//平台名称
        orderUser.setProductCode(dataSource.getVarietyCode());//商品
        orderUser.setHandNumber( dataSource.getHandNumber());//手数);
        orderUser.setLongShort(dataSource.getCmd());//多空
        orderUser.setPrice(dataSource.getPrice());//价位
        orderUser.setProfit(dataSource.getProfit());//平仓盈亏
        orderUser.setCommission(dataSource.getCommission());//手续费
        orderUser.setStopLoss(dataSource.getStopLoss());//止损
        orderUser.setStopProfit(dataSource.getStopProfit());//止盈
        orderUser.setClosePrice(dataSource.getPrice());//平仓价
        orderUser.setCreateDate(DateUtil.getStringDate());//创建时间
        orderUserDao.addOrderUser(orderUser);
        log.info("平仓成功 新开仓单号为："+orderUser.getTicket());
        return  "添加成功";
    }





    /**
     * //通过开仓单号 查询交易信息
     * @param ticket 开仓单号
     * @return
     */
    public OrderUser findByTicket(String  ticket){
        OrderUser orderUser = new OrderUser();
        if(ticket != null && !StringUtils.isEmpty(ticket)){
            orderUser = orderUserDao.findByTicket(ticket);
        }
        return orderUser;
    }

    /**
     * 平仓
     * @param orderUser 平仓数据对象
     * @return
     */
    public String update(OrderUser orderUser){
         String message =null;
        if(orderUser != null){
            orderUser.setUpdateDate(DateUtil.getStringDate());//修改时间
            orderUserDao.update(orderUser);
            message = "平仓成功" ;
        }else{ message = "平仓修改失败";}

        return  message;
    }




    /**
     * 用户列表显示 & 数据统计查询
     * @param orderUserVo
     * @return
     */
    public OrderUserListVo countOrderUser( OrderUserVo orderUserVo){
        List<OrderUserVo> list =  new ArrayList<>();
        List<OrderUser> orderUserlist =  orderUserDao.countOrderUser(orderUserVo);
        OrderUserListVo orderUserListVo = new OrderUserListVo();
        double total_Position_gain_and_loss = DoubleUtil.Double_val; //总持仓盈亏
        double  total_gain_and_loss = DoubleUtil.Double_val;//客户总盈亏
        double total_commission = DoubleUtil.Double_val; //总手续费

        for (OrderUser orderUser : orderUserlist){
            OrderUserVo orderUserVo1 = new OrderUserVo();
            Prices prices = orderHongKongService.getMarketPrice(orderUser.getProductCode()); //价格
            PlatFromUsers platFromUsers =  getPlatFromUser(orderUser);
            Agent agent = new Agent();
            if (platFromUsers == null){
                agent = null;
            }else {
                agent    = orderHongKongService.getAgent(platFromUsers.getAGENT());//代理人
            }


            //平仓盈亏
            double profit = DoubleUtil.Double_val;
            //累计盈亏
            double totalGainAndLoss = DoubleUtil.Double_val;
            //盈利值（计算胜率）
            double winRate = DoubleUtil.Double_val; //盈利值
            //持仓盈亏
            double position_gain_and_loss = DoubleUtil.Double_val;
            //盈亏效率
            double profit_loss_than = DoubleUtil.Double_val;

            int doOrderNumber = 0; //做单数
            double handNumber = DoubleUtil.Double_val; //手数
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            Date recentlyTime = null;//最近下单时间
             for (OrderUser orderUser1 : orderUserlist){
                 if(orderUser.getUserCode().equals(orderUser1.getUserCode())){
                     total_commission = DoubleUtil.add(total_commission,(orderUser1.getCommission() == null ? DoubleUtil.Double_val : orderUser1.getCommission()));
                     //持仓总数
                     if(orderUser1.getCloseTime() == null && StringUtils.isEmpty(orderUser1.getCloseTime())){
                         //多
                         if (orderUser1.getLongShort() == 0){
                                //（卖出价（Hk）  - 买入（orderUser）） * 持仓数（orderUser）
                             position_gain_and_loss = DoubleUtil.add(position_gain_and_loss,DoubleUtil.mul(DoubleUtil.sub(prices.getAsk(),orderUser1.getOpenPrice()),orderUser1.getHandNumber()));
                         }
                         //空
                         //（开仓价（orderUser）— 买入（HK））* 持仓数 （orderUser）
                         if (orderUser1.getLongShort() == 1){
                             position_gain_and_loss =
                                     DoubleUtil.add(position_gain_and_loss,DoubleUtil.mul(DoubleUtil.sub(orderUser1.getOpenPrice(),prices.getBid()),orderUser1.getHandNumber()));
                         }
                     }
                     //平仓盈亏
                    if(orderUser1.getCloseTime() != null && !StringUtils.isEmpty(orderUser1.getCloseTime()) && orderUser1.getProfit() != null){
                        profit = DoubleUtil.add(profit,( orderUser1.getProfit() ==null ? DoubleUtil.Double_val : orderUser1.getProfit() ));
                        doOrderNumber++; //做单数
                        handNumber = DoubleUtil.add(handNumber,(orderUser1.getHandNumber() == null ? DoubleUtil.Double_val : orderUser1.getHandNumber())); //手数计算
                        if(orderUser1.getProfit() > 0){
                            winRate= DoubleUtil.add(winRate,(orderUser1.getProfit() == null ? DoubleUtil.Double_val : orderUser1.getProfit())); //盈利
                        }
                    }
                    //计算最近下单时间
                     if (recentlyTime == null){
                         try {
                             recentlyTime = sdf.parse(orderUser1.getCreateDate());
                         } catch (ParseException e) {
                             e.printStackTrace();
                         }
                     }else{
                         try {
                             if (recentlyTime.getTime() < sdf.parse(orderUser1.getCreateDate()).getTime()){
                                 recentlyTime = sdf.parse(orderUser1.getCreateDate());
                             }
                         } catch (ParseException e) {
                             e.printStackTrace();
                         }
                     }
                 }
             }
            if(winRate != DoubleUtil.Double_val){
                System.out.println("winRate:"+winRate);
                System.out.println("doOrderNumber:"+doOrderNumber);
                orderUserVo1.setWinRate(DoubleUtil.div(winRate,doOrderNumber,1));//胜率
            }else{
                orderUserVo1.setWinRate(DoubleUtil.Double_val);//胜率
            }

            if(agent == null){
                orderUserVo1.setAgencyName("-");//代理人
            }else {
                orderUserVo1.setAgencyName(agent.getAgentname());// 代理人
            }
             orderUserVo1.setPlatformName(orderUser.getPlatFormCode()); //平台名称
            if (platFromUsers == null) {
                orderUserVo1.setUserName("-");//用户名称
            } else {
                orderUserVo1.setUserName(platFromUsers.getNAME());//用户名称
            }

             orderUserVo1.setRecentlyTime(DateUtil.longToStrDate(recentlyTime.getTime()));//最近下单时间
             orderUserVo1.setUserCode(orderUser.getUserCode()); //客户
             orderUserVo1.setHandNumber(handNumber);//持仓手数
             orderUserVo1.setPosition_gain_and_loss(position_gain_and_loss);//持仓盈亏
             orderUserVo1.setOffset_gain_and_loss(profit);//平仓盈亏

            if(profit != DoubleUtil.Double_val){
                profit_loss_than = DoubleUtil.div(profit,handNumber,1);
            }else {
                profit_loss_than = DoubleUtil.Double_val;
            }

            if (Double.isNaN(profit_loss_than)){
                profit_loss_than = 0.0;
            }
             orderUserVo1.setProfit_loss_than(profit_loss_than);  //盈亏效率
             orderUserVo1.setDoOrderNumber(doOrderNumber);//做单数
             orderUserVo1.setTotalGainAndLoss(DoubleUtil.add(position_gain_and_loss,profit));//累计盈亏
             total_Position_gain_and_loss = DoubleUtil.add(total_Position_gain_and_loss,position_gain_and_loss); //总持仓盈亏
             total_gain_and_loss = DoubleUtil.add(DoubleUtil.add(profit,position_gain_and_loss),total_gain_and_loss); //客户总盈亏
             list.add(orderUserVo1);

        }
        orderUserListVo.setTotal_gain_and_loss(total_gain_and_loss);
        orderUserListVo.setTotal_Position_gain_and_loss(total_Position_gain_and_loss);
        orderUserListVo.setTotal_commission(total_commission);
        orderUserListVo.setListVo(list);
        return orderUserListVo;
    }

    /**
     * 条件查询
     * @param list  用户id集合
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param productCode 产品代码
     * @param openOrCloseStatus
     * @return
     */
    @Override
    public List<OrderUser> findByUserIdList(List<String> list, String startTime, String endTime, String productCode, Integer openOrCloseStatus) {

        List<OrderUser> orderUserList = new ArrayList<>();
        if(list.size() >0 && startTime != null && !StringUtils.isEmpty(startTime) && productCode != null && !StringUtils.isEmpty(productCode)){
            orderUserList = orderUserDao.findByUserIdList(list,startTime,endTime,productCode,openOrCloseStatus);
        }
        return orderUserList;
    }

    /**
     * 用户明细
     * @param userCode   用户代码
     * @param productCode  产品代码
     * @return
     */
    @Override
    public OrderUserDetailsVo getUserDetails(String userCode, String productCode) {
        OrderUserDetailsVo detailsVo = new OrderUserDetailsVo();
        if(userCode != null && !StringUtils.isEmpty(userCode) && productCode != null && !StringUtils.isEmpty(productCode) ){
                int index = 0;
                int endIndex =10;
                List<OrderUser> orderUserList = orderUserDao.getUserDetails(userCode,productCode);
                LinkedHashSet<String> set = new LinkedHashSet<String>(orderUserList.size());

                List<OrderUser> handList = new ArrayList<>();
                List<OrderUser> profitList = new ArrayList<>();
                double remainMoney = DoubleUtil.Double_val;//余额
                double totalHandNumber = DoubleUtil.Double_val;//持仓数
                double profit = DoubleUtil.Double_val; //平仓盈亏
                long countNumber = orderUserList.size(); //做单数
                for(OrderUser orderUser : orderUserList){
                    totalHandNumber = DoubleUtil.add(totalHandNumber,orderUser.getHandNumber());//累计持仓数
                    if(orderUser.getCloseTime() != null && !StringUtils.isEmpty(orderUser.getCloseTime())){
                        profit = DoubleUtil.add(profit,orderUser.getProfit()) ; //平仓盈亏
                        profitList.add(orderUser); //构造平仓List
                    }

                    if(orderUser.getCloseTime() == null || StringUtils.isEmpty(orderUser.getCloseTime())){
                        handList.add(orderUser);//构造持仓list
                    }
                    if(orderUser.getCreateDate() != null && !StringUtils.isEmpty(orderUser.getCreateDate())){
                        set.add(orderUser.getCreateDate().substring(index,endIndex).trim());//计算做单天数
                    }


                }
                //构造vo类
                detailsVo.setDoOrderDays(set.size()); //做单天数
                detailsVo.setCountNumber(countNumber);//做单数
                detailsVo.setRemainMoney(remainMoney);//余额
                detailsVo.setHoldList(handList);//持仓数据
                detailsVo.setProfitList(profitList);//平仓数据
                detailsVo.setPosition_gain_and_loss(profit);//平仓盈亏
                detailsVo.setOffset_gain_and_loss(totalHandNumber);//持仓盈亏   ****还需要计算
                detailsVo.setPlatformName("支付宝");//平台
                detailsVo.setLoginTime("2017-08-21 18:50:12");//注册时间
                detailsVo.setAgencyName("马云");//代理人


        }

        return detailsVo;
    }

    /**
     * 获取不同用户信息
     * @param orderUser
     * @return
     */

    public PlatFromUsers getPlatFromUser(OrderUser orderUser ){
           PlatFromUsers platFromUsers  = new PlatFromUsers();
        if (orderUser.getPlatFormCode().equals(platFromList[0])){
            platFromUsers =    orderHongKongService.getUser75(orderUser.getUserCode());
        }
        if (orderUser.getPlatFormCode().equals(platFromList[1])){
            platFromUsers =    orderHongKongService.getUser76(orderUser.getUserCode());
        }
        return  platFromUsers;
    }

}

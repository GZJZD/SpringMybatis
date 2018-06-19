package com.web.service.imp;

import com.web.common.OrderUserEnum;
import com.web.dao.OrderUserDao;
import com.web.pojo.DataSource;
import com.web.pojo.OrderUser;
import com.web.pojo.vo.OrderUserDetailsVo;
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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Service("orderUserService")
@Transactional
public class OrderUserServiceImpl implements OrderUserService {
    @Autowired(required = false)
    private OrderUserDao orderUserDao;

    private static Logger log = LogManager.getLogger(OrderUserServiceImpl.class.getName());

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
    //          orderUser.setHandNumber(dataSource.getHandNumber());
                orderUser.setProfit(dataSource.getProfit()); //平仓盈亏
                orderUser.setClosePrice(dataSource.getPrice()); //平仓价格
                orderUser.setLongShort(dataSource.getCmd());//开多  ，平 空
                message = update(orderUser);

        }

        if(!dataSource.getTicket().equals(dataSource.getNewTicket())){
            //平开
            if(orderUser.getHandNumber() > dataSource.getHandNumber()){
                double handNumber = orderUser.getHandNumber();
                //先平仓
                orderUser.setHandNumber(dataSource.getHandNumber()); // 手数
                orderUser.setCloseTime(dataSource.getCreateTime()); //平仓时间
                orderUser.setLongShort(dataSource.getCmd());//多空
                orderUser.setProfit(dataSource.getProfit()); //平仓盈亏
                orderUser.setClosePrice(dataSource.getPrice());//平仓价格
                orderUser.setUpdateDate(DateUtil.getStringDate());//修改时间
                update(orderUser);
                //后开仓
                DataSource newDataSource = new DataSource();
                newDataSource = dataSource;
                difference = DoubleUtil.sub(handNumber,dataSource.getHandNumber()); //计算差值
                newDataSource.setHandNumber(difference);
                newDataSource.setCmd(longShor);
                newDataSource.setPrice(openPrice);
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
    public List<OrderUserVo> countOrderUser( OrderUserVo orderUserVo){
        List<OrderUserVo> list =  new ArrayList<>();
        List<OrderUser> orderUserlist =  orderUserDao.countOrderUser(orderUserVo);

        for (OrderUser orderUser : orderUserlist){
            OrderUserVo orderUserVo1 = new OrderUserVo();
            //持仓数
             double totalHandNumber = 0.00;
             //价位
             double price = 0.00;
            //平仓盈亏
            double profit = 0.00;
            //累计盈亏
            //胜率
            //回报率
            //盈亏效率
             double profit_loss_than = 0.00;
             double winRate = 0.00; //胜率
            //客户类型
             for (OrderUser orderUser1 : orderUserlist){
                 if(orderUser1.getUserCode().equals(orderUser.getUserCode())){
                     //持仓总数
                     if(orderUser1.getCloseTime() == null && StringUtils.isEmpty(orderUser1.getCloseTime())){
                         totalHandNumber = DoubleUtil.add( orderUser1.getHandNumber(),totalHandNumber);//持仓总数
                     }
                     //平仓盈亏
                    if(orderUser1.getCloseTime() != null && !StringUtils.isEmpty(orderUser1.getCloseTime()) && orderUser1.getProfit() != null){
                        profit = DoubleUtil.add(profit,orderUser1.getProfit()) ;
                    }

                 }
             }

             orderUserVo1.setWinRate(winRate);//胜率
             orderUserVo1.setAgencyName("阿里巴巴集团");// 代理人
             orderUserVo1.setPlatformName(orderUser.getPlatFormCode()); //平台名称
             orderUserVo1.setUserName("马云");//用户名称
             orderUserVo1.setUserCode(orderUser.getUserCode()); //客户
             orderUserVo1.setPosition_gain_and_loss(totalHandNumber);//持仓盈亏
             orderUserVo1.setOffset_gain_and_loss(profit);//平仓盈亏
             orderUserVo1.setProfit_loss_than(profit_loss_than);  //盈亏效率
             orderUserVo1.setTotalGainAndLoss(DoubleUtil.add(totalHandNumber,profit));//累计盈亏
             list.add(orderUserVo1);

        }

        return list;
    }

    /**
     * 条件查询
     * @param list  用户id集合
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param productCode 产品代码
     * @return
     */
    @Override
    public List<OrderUser> findByUserIdList(List<String>list ,String startTime,String endTime,String productCode) {

        List<OrderUser> orderUserList = new ArrayList<>();
        if(list.size() >0 && startTime != null && !StringUtils.isEmpty(startTime) && productCode != null && !StringUtils.isEmpty(productCode)){
            orderUserList = orderUserDao.findByUserIdList(list,startTime,endTime,productCode);
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
                    double remainMoney = 0.00;//余额
                    double totalHandNumber = 0.00;//持仓数
                    double profit = 0.00; //平仓盈亏
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



}

package com.web.service.imp;

import com.web.common.OrderUserEnum;
import com.web.dao.OrderUserDao;
import com.web.pojo.DataSource;
import com.web.pojo.OrderUser;
import com.web.pojo.vo.OrderUserVo;
import com.web.service.OrderUserService;
import com.web.util.common.DateUtil;
import com.web.util.common.DoubleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service("orderUserService")
public class OrderUserServiceImpl implements OrderUserService {
    @Autowired(required = false)
    private OrderUserDao orderUserDao;



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

                //平仓
                if( dataSource.getOpenClose() == OrderUserEnum.openOrClose.close.getCode()){
                    message = updateOrAdd(dataSource);
                }

                //开仓
                if (dataSource.getOpenClose() == OrderUserEnum.openOrClose.open.getCode()){
                    message = save(dataSource);
                }

            }
                    return message;
    }


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
            return  "添加成功";
    }
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
        return  "添加成功";
    }

    public String updateOrAdd(DataSource dataSource){
            String message = null;
            OrderUser orderUser = findByTicket(dataSource.getTicket());
            Double difference;
        if(orderUser == null){
           message = saveClose(dataSource);
            return message;
        }
        if(dataSource.getTicket().equals(dataSource.getNewTicket())){
            orderUser.setCloseTime(dataSource.getCreateTime());
            orderUser.setHandNumber(dataSource.getHandNumber());
            message = update(orderUser);
        }

        if(!dataSource.getTicket().equals(dataSource.getNewTicket())){
            //平开
                if(orderUser.getHandNumber() > dataSource.getHandNumber()){
                    double handNumber=orderUser.getHandNumber();
                    //先平仓
                    orderUser.setHandNumber(dataSource.getHandNumber());
                    orderUser.setCloseTime(dataSource.getCreateTime());
                    orderUser.setLongShort(dataSource.getCmd());
                    update(orderUser);
                    //后开仓
                    DataSource newDataSource = new DataSource();
                    newDataSource = dataSource;
                    difference = DoubleUtil.sub(handNumber,dataSource.getHandNumber()); //计算差值
                    newDataSource.setHandNumber(difference);
                    save(newDataSource);
                    message = "平仓成功";
                }
        }
        return message;
    }

    //通过开仓单号 查询交易信息
    public OrderUser findByTicket(String  ticket){
        OrderUser orderUser = new OrderUser();
        if(!StringUtils.isEmpty(ticket)){
            ticket.trim();
            orderUser = orderUserDao.findByTicket(ticket);
        }
        return orderUser;
    }


    public String update(OrderUser orderUser){
         String message =null;
        if(orderUser != null){
            orderUser.setUpdateDate(DateUtil.getStringDate());
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
             double totalHandNumber=0;
             //价位
             double price =0;
            //平仓盈亏
            double profit = 0;
            //累计盈亏
            //胜率
            //回报率
            //盈亏效率
             double profit_loss_than = 0;
             double winRate=0; //胜率
            //客户类型
             for (OrderUser orderUser1 : orderUserlist){
                 if(orderUser.getUserCode().equals(orderUser.getUserCode())){
                     //持仓总数
                     if(StringUtils.isEmpty(orderUser1.getCloseTime())){
                         totalHandNumber = DoubleUtil.add( orderUser1.getHandNumber(),totalHandNumber);//持仓总数
                     }
                     //平仓盈亏
                    if(orderUser1.getCloseTime() != null && !StringUtils.isEmpty(orderUser1.getCloseTime()) && orderUser1.getProfit() != null){
                        profit = DoubleUtil.add(profit,orderUser1.getProfit()) ;
                    }

                 }
             }
             orderUserVo1.setWinRate(winRate);//胜率
             orderUserVo1.setAgencyName("代理人");// 代理人
             orderUserVo1.setPlatformName(orderUser.getPlatFormCode()); //平台名称
             orderUserVo1.setUserName("wangxing");//用户名称
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
     * 根据用户id & 时间 & 商品
     */
    @Override
    public List<OrderUser> findByUserIdList(List<String>list ,String startTime,String endTime,String productCode) {

        List<OrderUser> orderUserList = new ArrayList<>();
        if(list.size() >0 && startTime != null && !StringUtils.isEmpty(startTime) && productCode != null && !StringUtils.isEmpty(productCode)){
            orderUserList = orderUserDao.findByUserIdList(list,startTime,endTime,productCode);
        }
        return orderUserList;
    }









}

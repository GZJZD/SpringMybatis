package com.web.service.imp;

import com.web.common.OrderUserEnum;
import com.web.dao.OrderUserDao;
import com.web.pojo.DataSource;
import com.web.pojo.OrderUser;
import com.web.service.OrderUserService;
import com.web.util.common.DoubleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
            orderUser.setAgencyName("test");//跟单人
            orderUser.setPlatFormCode(dataSource.getPlatformName());//平台名称
            orderUser.setProduceCode(dataSource.getVarietyCode());//商品
            orderUser.setHandNumber( dataSource.getHandNumber());//手数);
            orderUser.setLongShort(dataSource.getCmd());//多空
            orderUser.setPrice(dataSource.getPrice());//价位
            orderUser.setProfit(dataSource.getProfit());//平仓盈亏
            orderUserDao.addOrderUser(orderUser);
            return  "添加成功";
    }

    public String updateOrAdd(DataSource dataSource){
            String message = null;
            OrderUser orderUser = findByTicket(dataSource.getTicket());
            Double difference;
        if(orderUser == null){
           message = save(dataSource);
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
                    //先平仓
                    orderUser.setHandNumber(dataSource.getHandNumber());
                    orderUser.setCloseTime(dataSource.getCreateTime());
                    orderUser.setLongShort(dataSource.getCmd());
                    update(orderUser);
                    //后开仓
                    DataSource newDataSource = new DataSource();
                    newDataSource = dataSource;
                    difference = DoubleUtil.sub(orderUser.getHandNumber(),dataSource.getHandNumber()); //计算差值
                    newDataSource.setHandNumber(difference);
                    save(newDataSource);
                    message = "平仓成功";
                }
        }
        return message;
    }


    public OrderUser findByTicket(String  ticket){
        OrderUser orderUser = new OrderUser();
        if(!StringUtils.isEmpty(ticket)){
            orderUser = orderUserDao.findByTicket(ticket);
        }
        return orderUser;
    }


    public String update(OrderUser orderUser){
         String message = "修改成功";
        if(orderUser != null){
            orderUserDao.update(orderUser);
        }
        return  message;
    }

}

package com.web.service.imp;

import com.web.common.OrderUserEnum;
import com.web.dao.OrderUserDao;
import com.web.pojo.DataSource;
import com.web.pojo.OrderUser;
import com.web.service.OrderUserService;
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
        String Message = null;
        if ( dataSource == null){
            Message ="数据不能为空";
        }else{
            OrderUser orderUser = new OrderUser();
            dataSource.getCmd();//多空
            dataSource.getOpenClose();//开平（0=开，1=平）
            //平仓
            if( dataSource.getOpenClose()== OrderUserEnum.openOrClose.close.getCode()){
                orderUser.setCloseTime(dataSource.getCreateTime());//平仓时间
                orderUser.setTicket(dataSource.getNewTicket());//新开仓单号
            //开仓
            }else{
                orderUser.setOpenTime(dataSource.getCreateTime());//开仓时间
                orderUser.setTicket(dataSource.getTicket());//开仓单号
            }
            orderUser.setUserCode(dataSource.getLogin()); //账号
            orderUser.setAgencyName("test");//跟单人
            orderUser.setPlatformCode(dataSource.getPlatformName());//平台名称
            orderUser.setProduceCode(dataSource.getVarietyCode());//商品
            orderUser.setHandNumber( dataSource.getHandNumber());//手数);
            orderUser.setPrice(dataSource.getPrice());//价位
            orderUser.setProfit(dataSource.getProfit());//平仓盈亏
            orderUserDao.addOrderUser(orderUser);
            Message ="数据接收成功";
        }
             return Message;
    }

}

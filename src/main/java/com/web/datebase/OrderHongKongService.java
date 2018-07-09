package com.web.datebase;

import com.web.dao.OrderHongKongDao;
import com.web.datebase.config.TransFormDataSource;
import com.web.datebase.entity.Agent;
import com.web.datebase.entity.PlatFromUsers;
import com.web.datebase.entity.Prices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderHongKongService {
    @Autowired
    protected OrderHongKongDao orderHongKongDao;


    @TransFormDataSource(name="dataSourceHongKong")
    public Prices getMarketPrice(String varietyId){
      Prices prices =  orderHongKongDao.getMarketPrice(varietyId);
      return  prices;
    }

    @TransFormDataSource(name="dataSourceHongKong")
    public PlatFromUsers getUser75(String LOGIN){
        PlatFromUsers platFromUsers =  orderHongKongDao.getUser75(LOGIN);
        return  platFromUsers;
    }

    @TransFormDataSource(name="dataSourceHongKong")
    public PlatFromUsers getUser76(String LOGIN){

        PlatFromUsers platFromUsers =  orderHongKongDao.getUser76(LOGIN);
        return  platFromUsers;
    }

    @TransFormDataSource(name="dataSourceHongKong")
    public Agent getAgent(String agentId){
        Agent agent =  orderHongKongDao.getAgent(agentId);
        return  agent;
    }

}

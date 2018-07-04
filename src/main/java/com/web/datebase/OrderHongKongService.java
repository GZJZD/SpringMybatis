package com.web.datebase;

import com.web.dao.OrderHongKongDao;
import com.web.datebase.config.TransFormDataSource;
import com.web.datebase.entity.Prices;
import com.web.datebase.entity.Users75;
import com.web.datebase.entity.Users76;
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
    public Users75 getUser75(String LOGIN){
        Users75 users75 =  orderHongKongDao.getUser75(LOGIN);
        return  users75;
    }

    @TransFormDataSource(name="dataSourceHongKong")
    public Users76 getUser76(String LOGIN){
        Users76 users76 =  orderHongKongDao.getUser76(LOGIN);
        return  users76;
    }

}

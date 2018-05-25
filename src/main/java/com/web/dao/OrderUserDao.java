package com.web.dao;


import com.web.pojo.OrderUser;
import java.util.List;



public interface OrderUserDao {
    List<OrderUser> findAll();
    void addOrderUser(OrderUser orderUser);
    OrderUser findByTicket(String  ticket);
    void update(OrderUser orderUser);

}

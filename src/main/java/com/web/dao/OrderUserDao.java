package com.web.dao;

import com.web.pojo.DataSource;
import com.web.pojo.OrderUser;

import java.util.List;

public interface OrderUserDao {
    List<OrderUser> findAll();
    void addOrderUser(OrderUser orderUser);
}

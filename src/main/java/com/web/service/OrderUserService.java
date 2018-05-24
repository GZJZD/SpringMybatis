package com.web.service;

import com.web.pojo.DataSource;
import com.web.pojo.OrderUser;

import java.util.List;

public interface OrderUserService {
    List<OrderUser> findAll();

    String  addOrderUser(DataSource dataSource);
}

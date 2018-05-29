package com.web.service;

import com.web.pojo.DataSource;
import com.web.pojo.OrderUser;
import com.web.pojo.vo.OrderUserVo;

import javax.persistence.criteria.Order;
import java.util.List;

public interface OrderUserService {
    List<OrderUser> findAll();
    String  addOrderUser(DataSource dataSource);
    OrderUser findByTicket(String ticket);
    String update(OrderUser orderUser);
    List<OrderUserVo> countOrderUser(OrderUserVo orderUserVo);
}

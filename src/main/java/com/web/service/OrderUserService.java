package com.web.service;

import com.web.pojo.DataSource;
import com.web.pojo.OrderUser;
import com.web.pojo.vo.OrderUserDetailsVo;
import com.web.pojo.vo.OrderUserListVo;
import com.web.pojo.vo.OrderUserVo;

import java.util.List;

public interface OrderUserService {
    List<OrderUser> findAll();
    String  addOrderUser(DataSource dataSource);
    OrderUser findByTicket(String ticket);
    String update(OrderUser orderUser);
    OrderUserListVo countOrderUser(OrderUserVo orderUserVo);
    List<OrderUser>findByUserIdList(List<String> list, String startTime, String endTime, String productCode, Integer openOrCloseStatus);
    OrderUserDetailsVo getUserDetails(String userCode, String productCode);
    OrderUserDetailsVo  getOrderUserCount(String userCode, String productCode);

}

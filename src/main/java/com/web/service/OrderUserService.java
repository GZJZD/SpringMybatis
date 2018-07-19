package com.web.service;

import com.web.pojo.DataSource;
import com.web.pojo.OrderUser;
import com.web.pojo.vo.orderuser.OrderUserDetailsVo;
import com.web.pojo.vo.orderuser.OrderUserListVo;
import com.web.pojo.vo.orderuser.OrderUserVo;

import java.util.List;
import java.util.Map;

public interface OrderUserService {
    List<OrderUser> findAll();
    String  addOrderUser(DataSource dataSource);
    OrderUser findByTicket(String ticket);
    String update(OrderUser orderUser);
    OrderUserListVo countOrderUser(OrderUserVo orderUserVo);
    List<OrderUser>findByUserIdList(Map<String, String> map, String startTime, String endTime, String productCode, Integer openOrCloseStatus);
    OrderUserDetailsVo getUserDetails(String userCode, String productCode,String platFormCode);
    OrderUserDetailsVo  getOrderUserCount(String userCode, String productCode,String platFormCode);

}

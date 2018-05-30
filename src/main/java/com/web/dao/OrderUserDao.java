package com.web.dao;


import com.web.pojo.OrderUser;
import com.web.pojo.vo.OrderUserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;



public interface OrderUserDao {
    List<OrderUser> findAll();
    void addOrderUser(OrderUser orderUser);
    OrderUser findByTicket(String  ticket);
    void update(OrderUser orderUser);
    List<OrderUser> countOrderUser(OrderUserVo orderUserVo);

    List<OrderUser> findByUserIdList(@Param("list") List<String> list ,@Param("endTime")  String endTime ,@Param("startTime") String startTime ,@Param("productCode") String productCode);

}

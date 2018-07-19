package com.web.dao;


import com.web.pojo.OrderUser;
import com.web.pojo.vo.orderuser.OrderUserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;



public interface OrderUserDao {
    List<OrderUser> findAll();
    void addOrderUser(OrderUser orderUser);
    OrderUser findByTicket(String  ticket);
    void update(OrderUser orderUser);
    List<OrderUser> countOrderUser(OrderUserVo orderUserVo);
    List<OrderUser> findByUserIdList(@Param("list") List<String> list , @Param("startTime")  String startTime , @Param("endTime") String endTime , @Param("productCode") String productCode, @Param("status") Integer status);
    List<OrderUser> getUserDetails(@Param("userCode")  String userCode,@Param("productCode")  String productCode , @Param("platFormCode")  String platFormCode);


}

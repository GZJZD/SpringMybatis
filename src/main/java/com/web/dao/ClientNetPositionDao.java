package com.web.dao;


import com.web.pojo.ClientNetPosition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClientNetPositionDao {
    int insert(ClientNetPosition record);

    ClientNetPosition selectByPrimaryKey(Long id);

    ClientNetPosition selectByTicketAndTime(@Param("ticket") String ticket, @Param("openTime") String openTime, @Param("closeTime") String closeTime, @Param("followOrderId") Long followOrderId);

    List<ClientNetPosition> selectAll();

    int update(ClientNetPosition record);
}
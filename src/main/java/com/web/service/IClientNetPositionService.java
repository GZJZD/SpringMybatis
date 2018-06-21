package com.web.service;

import com.web.pojo.ClientNetPosition;

public interface IClientNetPositionService {
    void save(ClientNetPosition record);

    ClientNetPosition getClientNetPosition(Long id);

    ClientNetPosition selectByTicketAndTime(String ticket,String openTime,String closeTime,Long followOrderId);

    void update(ClientNetPosition record);
}

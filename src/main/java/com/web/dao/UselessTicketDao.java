package com.web.dao;

import com.web.pojo.UselessTicket;

public interface UselessTicketDao {

    UselessTicket getUselessTicketByTicket(String ticket);
    void save(UselessTicket uselessTicket);
}

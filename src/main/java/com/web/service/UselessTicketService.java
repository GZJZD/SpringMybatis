package com.web.service;

import com.web.pojo.UselessTicket;

public interface UselessTicketService {
    UselessTicket getUselessTicketByTicket(String ticket);
    void save(UselessTicket uselessTicket);
}

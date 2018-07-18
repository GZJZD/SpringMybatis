package com.web.service.imp;

import com.web.dao.UselessTicketDao;
import com.web.pojo.UselessTicket;
import com.web.service.UselessTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service@Transactional
public class UselessTicketServiceImpl implements UselessTicketService {
    @Autowired
    private UselessTicketDao uselessTicketDao;
    @Override
    public UselessTicket getUselessTicketByTicket(String ticket) {
        return uselessTicketDao.getUselessTicketByTicket(ticket);
    }

    @Override
    public void save(UselessTicket uselessTicket) {
        uselessTicketDao.save(uselessTicket);
    }
}

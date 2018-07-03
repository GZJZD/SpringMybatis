package com.web.service.imp;

import com.web.dao.ClientNetPositionDao;
import com.web.pojo.ClientNetPosition;
import com.web.service.IClientNetPositionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
* 客户数据的净头寸值
* */
@Service
@Transactional
public class ClientNetPositionServiceImpl implements IClientNetPositionService {
    private static Logger log = LogManager.getLogger(FollowOrderServiceImpl.class.getName());
    @Autowired
    private ClientNetPositionDao clientNetPositionDao;
    @Override
    public void save(ClientNetPosition record) {
        clientNetPositionDao.insert(record);
        log.debug("新增客户净头寸：ticket{},netPositionSum{},followOrderId{},"+record.getTicket()+"、"+record.getNetPositionSum()+"、"+record.getFollowOrderId());
    }

    @Override
    public ClientNetPosition getClientNetPosition(Long id) {
        return clientNetPositionDao.selectByPrimaryKey(id);
    }


    /*
    * 查找符合条件的数据
    *
    * */
    @Override
    public ClientNetPosition selectByTicketAndTime(String ticket, String openTime, String closeTime,Long followOrderId) {
        return clientNetPositionDao.selectByTicketAndTime(ticket,openTime,closeTime,followOrderId);
    }

    @Override
    public void update(ClientNetPosition record) {

        clientNetPositionDao.update(record);
    }
}

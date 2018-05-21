package com.web.service.imp;

import com.web.dao.FollowOrderClientDao;
import com.web.pojo.FollowOrderClient;
import com.web.service.IFollowOrderClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by may on 2018/5/19.
 */
@Service@Transactional
public class FollowOrderClientServiceImpl implements IFollowOrderClientService {
    @Autowired
    private FollowOrderClientDao followOrderClientDao;
    @Override
    public FollowOrderClient get(Long id) {
        return followOrderClientDao.selectByPrimaryKey(id);
    }

    @Override
    public List<FollowOrderClient> getListByClientId(Long clientId) {
        return followOrderClientDao.selectByClientId(clientId);
    }
}

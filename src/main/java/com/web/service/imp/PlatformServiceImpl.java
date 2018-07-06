package com.web.service.imp;

import com.web.dao.PlatformDao;
import com.web.pojo.Platform;
import com.web.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service@Transactional
public class PlatformServiceImpl implements PlatformService {
    @Autowired
    private PlatformDao platformDao;
    @Override
    public Platform getPlatformById(Long platformId) {
        return platformDao.getPlatformById(platformId);
    }

    @Override
    public List<Platform> getListPlatform() {
        return platformDao.getListPlatform();
    }
}

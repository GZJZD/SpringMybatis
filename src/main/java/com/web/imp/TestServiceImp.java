package com.web.imp;

import com.web.dao.TestMapper;
import com.web.pojo.Test;
import com.web.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Macx on 17/6/16.
 */
/**
 * 测试服务
 */
@Service("testService")
public class TestServiceImp implements TestService {
    @Autowired(required = false)
    private TestMapper testMapper = null;

    public Test getById(int id) {
        return testMapper.selectByPrimaryKey(id);
    }
}

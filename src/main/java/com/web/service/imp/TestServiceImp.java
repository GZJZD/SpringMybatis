package com.web.service.imp;

import com.web.dao.TestDao;
import com.web.pojo.Test;
import com.web.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Macx on 17/6/16.
 */
/**
 * 测试服务
 */
@Service("testService")
public class TestServiceImp implements TestService {
    @Autowired(required = false)
    private TestDao testMapper = null;

    public Test getById(int id) {
        return testMapper.selectByPrimaryKey(id);
    }

    public List<Test> findAll() {
        return testMapper.findAll();
    }
}

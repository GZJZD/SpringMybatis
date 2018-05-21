package com.web.dao;

import com.web.pojo.Test;

import java.util.List;

/**
 * Created by Macx on 17/6/16.
 */
public interface TestDao {
    Test selectByPrimaryKey(Integer id);
    List<Test> findAll();
}

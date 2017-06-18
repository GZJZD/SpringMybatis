package com.web.dao;

import com.web.pojo.Test;

/**
 * Created by Macx on 17/6/16.
 */
public interface TestMapper {
    Test selectByPrimaryKey(Integer id);
}

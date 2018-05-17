package com.web.service;


import com.web.pojo.DataSource;

import java.util.List;

/**
 * TCP数据接口
 * Created by may on 2018/5/9.
 */
public interface IDataSourceService {
    void insert(DataSource record);

    List<DataSource> selectAll();

}

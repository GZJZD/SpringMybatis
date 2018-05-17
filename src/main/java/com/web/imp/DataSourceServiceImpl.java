package com.web.imp;

import com.web.dao.DataSourceMapper;
import com.web.pojo.DataSource;
import com.web.service.IDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by may on 2018/5/9.
 */
@Service
public class DataSourceServiceImpl implements IDataSourceService {
    @Autowired
    private DataSourceMapper dataSourceMapper;
    @Override
    public void insert(DataSource record) {
        dataSourceMapper.insert(record);
    }


    @Override
    public List<DataSource> selectAll() {
        return null;
    }
}

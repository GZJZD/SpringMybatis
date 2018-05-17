package com.web.dao;



import com.web.pojo.DataSource;

import java.util.List;

public interface DataSourceMapper {


    int insert(DataSource record);

    List<DataSource> selectAll();


}
package com.web.service;


import com.web.pojo.DealData;

import java.util.List;

/**
 * TCP数据接口
 * Created by may on 2018/5/9.
 */
public interface IDealDataService {
    void insert(DealData record);

    List<DealData> selectAll();

}

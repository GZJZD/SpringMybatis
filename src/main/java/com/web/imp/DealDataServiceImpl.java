package com.web.imp;

import com.web.dao.DealDataMapper;
import com.web.pojo.DealData;
import com.web.service.IDealDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by may on 2018/5/9.
 */
@Service
public class DealDataServiceImpl implements IDealDataService {
    @Autowired
    private DealDataMapper dealDataMapper;
    @Override
    public void insert(DealData record) {
        dealDataMapper.insert(record);
    }


    @Override
    public List<DealData> selectAll() {
        return null;
    }
}

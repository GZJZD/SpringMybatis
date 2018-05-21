package com.web.service.imp;

import com.web.dao.VarietyDao;
import com.web.pojo.Variety;
import com.web.service.IVarietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by may on 2018/5/21.
 */
@Service@Transactional
public class VarietyServiceImpl implements IVarietyService{
    @Autowired
    private VarietyDao varietyDao;

    @Override
    public Variety getVariety(Long id) {
        return varietyDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Variety> getVarietyList() {
        return varietyDao.selectAll();
    }

    @Override
    public Variety getVarietyByCode(String varietyCode) {
        return varietyDao.selectByVarietyCode(varietyCode);
    }
}

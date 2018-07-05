package com.web.service.imp;

import com.web.dao.ContractInfoDao;
import com.web.pojo.ContractInfo;
import com.web.service.ContractInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by may on 2018/5/21.
 */
@Service@Transactional
public class ContractInfoServiceImpl implements ContractInfoService {
    @Autowired
    private ContractInfoDao contractInfoDao;
    @Override
    public ContractInfo getInfoByVarietyIdAndTradeName(Long varietyId, String tradeName) {
        return contractInfoDao.getInfoByVarietyIdAndTradeName(varietyId,tradeName);
    }

    @Override
    public List<ContractInfo> getContractInfoList() {
        return contractInfoDao.getContractInfoList();
    }
}

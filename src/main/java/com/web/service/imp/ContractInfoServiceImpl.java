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
    public ContractInfo getInfoByVarietyIdAndPlatformId(Long varietyId, Long platformId) {
        return contractInfoDao.getInfoByVarietyIdAndPlatformId(varietyId,platformId);
    }

    @Override
    public List<ContractInfo> getContractInfoList() {
        return contractInfoDao.getContractInfoList();
    }
    
    @Override
    public ContractInfo getContractInfoById(Long id) {
        return contractInfoDao.getContractInfoById(id);
    }

	@Override
	public void save(ContractInfo contractInfo) {
		contractInfoDao.save(contractInfo);
	}

	@Override
	public void updateContractInfo(ContractInfo contractInfo) {
		contractInfoDao.updateContractInfo(contractInfo);
	}
}

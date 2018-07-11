package com.web.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.web.pojo.ContractInfo;

/**
 * Created by may on 2018/5/21.
 */
public interface ContractInfoDao {
    ContractInfo getInfoByVarietyIdAndPlatformId(@Param("varietyId") Long varietyId, @Param("platformId") Long platformId);
    List<ContractInfo> getContractInfoList();
    ContractInfo getContractInfoById(Long id);
    void save (ContractInfo contractInfo);
    void updateContractInfo(ContractInfo contractInfo);
}

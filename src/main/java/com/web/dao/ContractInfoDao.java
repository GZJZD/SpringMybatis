package com.web.dao;

import com.web.pojo.ContractInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by may on 2018/5/21.
 */
public interface ContractInfoDao {
    ContractInfo getInfoByVarietyIdAndPlatformId(@Param("varietyId") Long varietyId, @Param("platformId") Long platformId);
    List<ContractInfo> getContractInfoList();
    ContractInfo getContractInfoById(Long id);
}

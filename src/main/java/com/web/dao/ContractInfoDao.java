package com.web.dao;

import com.web.pojo.ContractInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by may on 2018/5/21.
 */
public interface ContractInfoDao {
    ContractInfo getInfoByVarietyIdAndTradeName(@Param("varietyId") Long varietyId, @Param("tradeName") String tradeName);
    List<ContractInfo> getContractInfoList();
}

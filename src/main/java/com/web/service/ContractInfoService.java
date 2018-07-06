package com.web.service;

import java.util.List;

import com.web.pojo.ContractInfo;

/**
 * 合约信息
 * Created by may on 2018/5/21.
 */
public interface ContractInfoService {
    /**
     * 通过品种的id，和平台id找到对应的合约信息
     *@Author: May
     *@param
     *@Date: 14:47 2018/5/21
     */
    ContractInfo getInfoByVarietyIdAndPlatformId( Long varietyId,Long platformId);
    List<ContractInfo> getContractInfoList();
}

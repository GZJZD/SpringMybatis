package com.web.service;

import com.web.pojo.ContractInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 合约信息
 * Created by may on 2018/5/21.
 */
public interface ContractInfoService {
    /**
     * 通过品种的id，和交易账号的交易所名称找到对应的合约信息
     *@Author: May
     *@param
     *@Date: 14:47 2018/5/21
     */
    ContractInfo getInfoByVarietyIdAndTradeName( Long varietyId,String tradeName);
    List<ContractInfo> getContractInfoList();
}

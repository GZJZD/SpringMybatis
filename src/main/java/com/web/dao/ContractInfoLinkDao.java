package com.web.dao;

import com.web.pojo.ContractInfo;
import com.web.pojo.ContractInfoLink;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by may on 2018/5/21.
 */
public interface ContractInfoLinkDao {
    void save(ContractInfoLink contractInfoLink);
    void updateContractInfoLink(ContractInfoLink contractInfoLink);
    ContractInfoLink getContractInfoLink(Long contractInfoLinkId);
    ContractInfoLink getContractInfoLinkByInfoId(Long contractInfoId);
}

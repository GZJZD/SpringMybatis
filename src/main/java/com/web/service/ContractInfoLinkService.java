package com.web.service;

import com.web.pojo.Account;
import com.web.pojo.ContractInfoLink;

public interface ContractInfoLinkService {
    void save(ContractInfoLink contractInfoLink);
    void updateContractInfoLink(ContractInfoLink contractInfoLink);
    ContractInfoLink getContractInfoLink(Long contractInfoLinkId);
    ContractInfoLink getContractInfoLinkByInfoId(Long contractInfoId);
    void instrumentCommissionQuery(Long contractInfoId,Account account);
    void instrumentQuery(Long contractInfoId);
}

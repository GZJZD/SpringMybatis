package com.web.pojo;

import java.util.Date;

/**
 * 合约信息
 *@Author: May
 *@param
 *@Date: 14:11 2018/5/21
 */
public class ContractInfo {
    private Long id;

    private Variety variety;
    //合约代码
    private String contractCode;
    //交易所名字：Sp/大有/信管家
    private String tradePlaceName;
    //合约名称
    private String contractName;
    //修改人id
    private Long updateByUser;
    //修改时间
    private String updateDate;
    //创建时间
    private String createDate;

    private Long createUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Variety getVariety() {
        return variety;
    }

    public void setVariety(Variety variety) {
        this.variety = variety;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getTradePlaceName() {
        return tradePlaceName;
    }

    public void setTradePlaceName(String tradePlaceName) {
        this.tradePlaceName = tradePlaceName;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

}
package com.web.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 平台策略
 * 2017年12月23日上午1:11:29
 */

public class Tactics implements Serializable {

    private Long id;
    //品种
    private Variety variety;
    //交易账号
    private Account account;
    //平台跟单参数
    private DocumentaryParameters documentaryParameters;
    //客户
   //  private List<Customer> customers;
    //是否启用
    private Integer status;
    //开始时间
    @JsonFormat(pattern = "yyyy-MM-dd" , timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date openTime;



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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public DocumentaryParameters getDocumentaryParameters() {
        return documentaryParameters;
    }

    public void setDocumentaryParameters(DocumentaryParameters documentaryParameters) {
        this.documentaryParameters = documentaryParameters;
    }



    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }


}

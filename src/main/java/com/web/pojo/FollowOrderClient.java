package com.web.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 记录客户与跟单之间的联系
 *@Author: May
 *@param
 *@Date: 10:38 2018/5/21
 */
public class FollowOrderClient implements Serializable{
    private Long id;
    //跟单id
    private Long followOrderId;
    //客户id
    private Long clientId;
    //跟单方式:反向/正向
    private Integer followManner;
    //手数类型:按比例/固定手数
    private Integer handNumberType;
    //手数
    private Integer followHandNumber;
    //修改人id
    private Long updateByUser;
    //修改时间
    private String updateDate;
    //创建时间
    private String createDate;

    private Long createUser;
    //版本
    private Integer version=0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUpdateByUser() {
        return updateByUser;
    }

    public void setUpdateByUser(Long updateByUser) {
        this.updateByUser = updateByUser;
    }

    public Long getFollowOrderId() {
        return followOrderId;
    }

    public void setFollowOrderId(Long followOrderId) {
        this.followOrderId = followOrderId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Integer getFollowManner() {
        return followManner;
    }

    public void setFollowManner(Integer followManner) {
        this.followManner = followManner;
    }

    public Integer getHandNumberType() {
        return handNumberType;
    }

    public void setHandNumberType(Integer handNumberType) {
        this.handNumberType = handNumberType;
    }

    public Integer getFollowHandNumber() {
        return followHandNumber;
    }

    public void setFollowHandNumber(Integer followHandNumber) {
        this.followHandNumber = followHandNumber;
    }



    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
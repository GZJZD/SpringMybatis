package com.web.pojo;

import com.web.util.common.BaseUtil;

/**
 * 记录客户与跟单之间的联系
 *@Author: May
 *@param
 *@Date: 10:38 2018/5/21
 */
public class FollowOrderClient extends BaseUtil{
    private Long id;
    //跟单id
    private Long followOrderId;
    //客户的编号
    private String userCode;
    //跟单方式:反向/正向
    private Integer followDirection;
    //手数类型:按比例/固定手数
    private Integer handNumberType;
    //手数
    private Integer followHandNumber;

    //版本
    private Integer version=0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFollowOrderId() {
        return followOrderId;
    }

    public void setFollowOrderId(Long followOrderId) {
        this.followOrderId = followOrderId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Integer getFollowDirection() {
        return followDirection;
    }

    public void setFollowDirection(Integer followDirection) {
        this.followDirection = followDirection;
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
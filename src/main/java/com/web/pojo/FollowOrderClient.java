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
    //平台
    private String platformCode;
    //跟单方式:反向/正向
    private Integer followDirection;
    //手数类型:按比例/固定手数
    private Integer handNumberType;
    //手数
    private Integer followHandNumber;

    //删除时间
    private String deleteDate;

    //操作人
    private Long deleteByUser;

    //是否被删除
    private Integer status;
    //版本
    private Integer version=0;

    public Long getDeleteByUser() {
        return deleteByUser;
    }

    public void setDeleteByUser(Long deleteByUser) {
        this.deleteByUser = deleteByUser;
    }

    public String getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(String deleteDate) {
        this.deleteDate = deleteDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

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

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }
}
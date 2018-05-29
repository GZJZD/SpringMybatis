package com.web.pojo.vo;

import java.io.Serializable;

//此类用于与交易系统进行登录登出的实体类
public class UserLogin implements Serializable {
    //标识是登录还是订单
    private String typeId;
    //跟单id
    private Long requestId;
    //经纪公司代码
    private String brokerId;
    //交易账号名称
    private String userId;
    //交易账号密码
    private String password;
    //用户端产品信息：可选
    private String userProductInfo;
    //账号是否登录，0：否，1:是，不传默认为否
    private Integer forcedreset;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserProductInfo() {
        return userProductInfo;
    }

    public void setUserProductInfo(String userProductInfo) {
        this.userProductInfo = userProductInfo;
    }

    public Integer getForcedreset() {
        return forcedreset;
    }

    public void setForcedreset(Integer forcedreset) {
        this.forcedreset = forcedreset;
    }
}

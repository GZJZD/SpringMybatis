package com.web.pojo;

public class ClientNetPosition {
    private Long id;

    private Long followOrderId;//跟单id

    private Double netPositionSum;//净头寸值

    private String ticket;//唯一索引

    private String openTime;//开仓时间

    private String closeTime;//平仓时间

    private String createTime;//创建时间

    private Integer status;//是否跟单成功

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

    public Double getNetPositionSum() {
        return netPositionSum;
    }

    public void setNetPositionSum(Double netPositionSum) {
        this.netPositionSum = netPositionSum;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
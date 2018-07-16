package com.web.pojo.vo.followOrder;

/*
* 跟单编辑中客户参数的映射列表
* */
public class FollowOrderClientParamVo {
    /*
     * 用户编号
     * */
    private String userCode;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 平仓盈亏
     */
    private Double offset_gain_and_loss;
    /**
     * 盈亏效率
     */
    private Double profit_loss_than;

    /*
     * 跟单方式:反向/正向
     * */
    private Integer followDirection;

    /*
     * 手数类型:按比例/固定手数
     * */
    private Integer handNumberType;

    /*
     * 手数
     * */
    private Integer handNumber;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getOffset_gain_and_loss() {
        return offset_gain_and_loss;
    }

    public void setOffset_gain_and_loss(Double offset_gain_and_loss) {
        this.offset_gain_and_loss = offset_gain_and_loss;
    }

    public Double getProfit_loss_than() {
        return profit_loss_than;
    }

    public void setProfit_loss_than(Double profit_loss_than) {
        this.profit_loss_than = profit_loss_than;
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

    public Integer getHandNumber() {
        return handNumber;
    }

    public void setHandNumber(Integer handNumber) {
        this.handNumber = handNumber;
    }
}
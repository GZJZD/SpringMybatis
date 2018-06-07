package com.web.pojo.vo;


/**
 * 用户列表映射（此类没表，只是映射）
 */
public class OrderUserVo {


    /**********************************************     用户列表所用字段     *************************************************************************/


    /**
     * 用户Code
     */
    private String userCode;
    /**
     * 用户名称
     */
     private String userName;
    /**
     * 平台名称
     */
     private String platformName;
    /**
     * 代理人
     */
    private String agencyName;

    /**
     * 持仓盈亏
     */
    private Double position_gain_and_loss;
    /**
     * 平仓盈亏
     */
    private Double offset_gain_and_loss;
    /**
     * 累计盈亏
     */
    private Double totalGainAndLoss;
    /**
     * 胜率
     */
    private Double winRate;
    /**
     * 回报率
     */
    private Double rateOfReturn;
    /**
     *盈亏效率
     */
    private Double profit_loss_than;
    /**
     * 用户类型
     */
    private String userType;



/**********************************************     用户列表所用字段  END   *************************************************************************/


/********************************
 * 过滤条件所用字段               *
 * ******************************/
    /**
     * 启始时间
     */
    private  String startTime;

    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 合约
     */
    private String contract;
    /**
     * 商品
     */
    private String productCode;



/***************** 查询条件end ***********************/




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

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public Double getPosition_gain_and_loss() {
        return position_gain_and_loss;
    }

    public void setPosition_gain_and_loss(Double position_gain_and_loss) {
        this.position_gain_and_loss = position_gain_and_loss;
    }

    public Double getOffset_gain_and_loss() {
        return offset_gain_and_loss;
    }

    public void setOffset_gain_and_loss(Double offset_gain_and_loss) {
        this.offset_gain_and_loss = offset_gain_and_loss;
    }

    public Double getTotalGainAndLoss() {
        return totalGainAndLoss;
    }

    public void setTotalGainAndLoss(Double totalGainAndLoss) {
        this.totalGainAndLoss = totalGainAndLoss;
    }

    public Double getWinRate() {
        return winRate;
    }

    public void setWinRate(Double winRate) {
        this.winRate = winRate;
    }

    public Double getRateOfReturn() {
        return rateOfReturn;
    }

    public void setRateOfReturn(Double rateOfReturn) {
        this.rateOfReturn = rateOfReturn;
    }

    public Double getProfit_loss_than() {
        return profit_loss_than;
    }

    public void setProfit_loss_than(Double profit_loss_than) {
        this.profit_loss_than = profit_loss_than;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }


}

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
    private String produceCode;



/***************** 查询条件end ***********************/








    /**
     * 最近下单时间
     */
    private String latestOrderTime;

    /**
     * 入金
     */
    private Double inMoney;
    /**
     * 出金
     */
    private Double outMoney;

    /**
     * 手续费总额
     */
    private Double serviceChargeTotal;







    /**
     * 做单数
     */
    private Double do_singular;

    /**
     * 做单天数
     */
    private Integer total_Day;


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

    public String getProduceCode() {
        return produceCode;
    }

    public void setProduceCode(String produceCode) {
        this.produceCode = produceCode;
    }

    public String getLatestOrderTime() {
        return latestOrderTime;
    }

    public void setLatestOrderTime(String latestOrderTime) {
        this.latestOrderTime = latestOrderTime;
    }

    public Double getInMoney() {
        return inMoney;
    }

    public void setInMoney(Double inMoney) {
        this.inMoney = inMoney;
    }

    public Double getOutMoney() {
        return outMoney;
    }

    public void setOutMoney(Double outMoney) {
        this.outMoney = outMoney;
    }

    public Double getServiceChargeTotal() {
        return serviceChargeTotal;
    }

    public void setServiceChargeTotal(Double serviceChargeTotal) {
        this.serviceChargeTotal = serviceChargeTotal;
    }

    public Double getDo_singular() {
        return do_singular;
    }

    public void setDo_singular(Double do_singular) {
        this.do_singular = do_singular;
    }

    public Integer getTotal_Day() {
        return total_Day;
    }

    public void setTotal_Day(Integer total_Day) {
        this.total_Day = total_Day;
    }
}

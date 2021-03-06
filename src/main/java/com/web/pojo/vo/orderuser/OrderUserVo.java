package com.web.pojo.vo.orderuser;


import com.web.pojo.vo.FiltrateParameter;

/**
 * 用户列表映射（此类没表，只是映射）
 *  用户首页面列表数据展示
 */
public class OrderUserVo extends FiltrateParameter {


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
     private String platFormCode;
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
    /**
     * 做单数
     */
    private int doOrderNumber;
    /**
     * 手数
     */
    private Double handNumber;

    /**
     * 最近下单时间
     */
    private String recentlyTime;

    public OrderUserVo() {

    }




    public OrderUserVo(String startTime, String endTime, String contract, String productCode, String userCode, String userName, String platFormCode, String agencyName, Double position_gain_and_loss, Double offset_gain_and_loss, Double totalGainAndLoss, Double winRate, Double rateOfReturn, Double profit_loss_than, String userType, int doOrderNumber, Double handNumber, String recentlyTime) {
        super(startTime, endTime, contract, productCode);
        this.userCode = userCode;
        this.userName = userName;
        this.platFormCode = platFormCode;
        this.agencyName = agencyName;
        this.position_gain_and_loss = position_gain_and_loss;
        this.offset_gain_and_loss = offset_gain_and_loss;
        this.totalGainAndLoss = totalGainAndLoss;
        this.winRate = winRate;
        this.rateOfReturn = rateOfReturn;
        this.profit_loss_than = profit_loss_than;
        this.userType = userType;
        this.doOrderNumber = doOrderNumber;
        this.handNumber = handNumber;
        this.recentlyTime = recentlyTime;
    }

    public String getPlatFormCode() {
        return platFormCode;
    }

    public void setPlatFormCode(String platFormCode) {
        this.platFormCode = platFormCode;
    }

    /**********************************************     用户列表所用字段  END   *************************************************************************/






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



    public int getDoOrderNumber() {
        return doOrderNumber;
    }

    public void setDoOrderNumber(int doOrderNumber) {
        this.doOrderNumber = doOrderNumber;
    }

    public Double getHandNumber() {
        return handNumber;
    }

    public void setHandNumber(Double handNumber) {
        this.handNumber = handNumber;
    }

    public String getRecentlyTime() {
        return recentlyTime;
    }

    public void setRecentlyTime(String recentlyTime) {
        this.recentlyTime = recentlyTime;
    }
}

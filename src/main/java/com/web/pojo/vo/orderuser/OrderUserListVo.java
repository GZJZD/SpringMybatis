package com.web.pojo.vo.orderuser;

import java.util.List;

public class OrderUserListVo {

    /**
     *  用户列表 首页数据 展示
     * orderUserVo 列表集合数据
     */
    List<OrderUserVo> listVo;

    /**
     * 列表用户总 持仓盈亏
     */
     private double total_Position_gain_and_loss;

    /**
     * 客户列表总盈亏
     */
    private double total_gain_and_loss;

    /**
     *客户列表总手续费
     */
    private double total_commission;

    /**
     * 客户列表总盈亏效率
     */
    private double total_profit_loss_than;

    public OrderUserListVo() {

    }

    public OrderUserListVo(List<OrderUserVo> listVo, double total_Position_gain_and_loss, double total_gain_and_loss, double total_commission, double total_profit_loss_than) {
        this.listVo = listVo;
        this.total_Position_gain_and_loss = total_Position_gain_and_loss;
        this.total_gain_and_loss = total_gain_and_loss;
        this.total_commission = total_commission;
        this.total_profit_loss_than = total_profit_loss_than;
    }

    public List<OrderUserVo> getListVo() {
        return listVo;
    }

    public void setListVo(List<OrderUserVo> listVo) {
        this.listVo = listVo;
    }

    public double getTotal_Position_gain_and_loss() {
        return total_Position_gain_and_loss;
    }

    public void setTotal_Position_gain_and_loss(double total_Position_gain_and_loss) {
        this.total_Position_gain_and_loss = total_Position_gain_and_loss;
    }

    public double getTotal_gain_and_loss() {
        return total_gain_and_loss;
    }

    public void setTotal_gain_and_loss(double total_gain_and_loss) {
        this.total_gain_and_loss = total_gain_and_loss;
    }

    public double getTotal_commission() {
        return total_commission;
    }

    public void setTotal_commission(double total_commission) {
        this.total_commission = total_commission;
    }

    public double getTotal_profit_loss_than() {
        return total_profit_loss_than;
    }

    public void setTotal_profit_loss_than(double total_profit_loss_than) {
        this.total_profit_loss_than = total_profit_loss_than;
    }
}

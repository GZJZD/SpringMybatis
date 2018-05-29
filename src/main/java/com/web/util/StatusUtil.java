package com.web.util;

/**
 * 各种状态的表示
 * Created by may on 2018/5/15.
 */
public enum StatusUtil {
    BUY("多",0),
    SELL("空",1),
    OPEN("开",0),
    CLOSE("平",1),
    TRADING_START("净头寸交易中",0),
    TRADING_PAUSE("净头寸交易暂停",1),
    //发出两条交易信息
    TRADING_OPENCLOSE("净头寸开平",2),
    //一条交易信息返回
    TRADING_OPENCLOSE_ONE("净头寸一条交易信息返回状态",3),

    TRADING_SUCCESS("交易成功",1),
    TRADING_FAILURE("交易失败",0),
    DIRECTION_REVERSE("反向跟单",0),
    DIRECTION_POSITIVE("正向跟单",1),
    MARKET_PRICE("市价",1),
    LIMIT_PRICE("限价",0),
    FOLLOW_ORDER_START("启动",1),
    FOLLOW_ORDER_TEMPORARY_STOP("暂停",2),
    FOLLOW_ORDER_STOP("停止",0),
    FOLLOW_MANNER_USER("跟用户",0),
    FOLLOWMANNER_NET_POSITION("净头寸",1),
    CLIENT_POINT_GOOD("好",1),
    CLIENT_POINT_BAD("差",0);

    private String name;
    private Integer index;

    StatusUtil(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}

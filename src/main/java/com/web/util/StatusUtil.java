package com.web.util;

/**
 * 各种状态的表示
 * Created by may on 2018/5/15.
 */
public enum StatusUtil {
    BUY("多",0),SELL("空",1),OPEN("开",0),CLOSE("平",1),TRADING_START("交易中",0),TRADING_PAUSE("交易暂停",1),
    DIRECTION_REVERSE("反向跟单",0),DIRECTION_POSITIVE("正向跟单",1),MARKET_PRICE("市价",1),LIMIT_PRICE("限价",0),
    STRATEGY_START("启动",1),STRATEGY_STOP("停止",0);

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

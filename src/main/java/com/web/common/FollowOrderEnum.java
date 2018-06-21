package com.web.common;

public  class FollowOrderEnum {
    public  enum FollowStatus{
        //跟单
        BUY("多",0),
        SELL("空",1),
        OPEN("开仓",0),
        CLOSE("平仓",1),
        NET_POSITION_TRADING_START("净头寸交易中",0),
        NET_POSITION_TRADING_PAUSE("净头寸交易暂停",1),
        //发出两条交易信息
        NET_POSITION_TRADING_OPENCLOSE("净头寸开平",2),
        //一条交易信息返回
        NET_POSITION_TRADING_OPENCLOSE_ONE("净头寸一条交易信息返回状态",3),

        //跟单状态
        FOLLOW_ORDER_STOP("停止",0),
        FOLLOW_ORDER_START("启动",1),
        FOLLOW_ORDER_TEMPORARY_STOP("暂停",2),
        FOLLOW_ORDER_LOGOUT_STOP("账号停止",3),

        //策略
        DIRECTION_REVERSE("反向跟单",0),

        //下单点位
        LIMIT_PRICE("限价",0),
        CLIENT_POINT_BAD("差",0),

        //最大止盈
        SET_MAXPROFIT("设置最大止盈",0),

        //最大止损
        SET_MAXLOSS("设置最大止损",0),

        //账户止损
        SET_ACCOUNTLOSS("设置账户止损",0),

        //跟单方式
        FOLLOWMANNER_NET_POSITION("净头寸",1),

        //客户:手数类型
        CLIENT_HAND_NUMBER_TYPE("固定手数",0),

        //是否跟单
        NOT_FOLLOW_ORDER_BY_CLIENT("跟单失败",0),
        //是否跟单
        FOLLOW_ORDER_BY_CLIENT("已跟单",1)

        ;






        private String name;
        private Integer index;
        FollowStatus(String name, Integer index) {
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
}

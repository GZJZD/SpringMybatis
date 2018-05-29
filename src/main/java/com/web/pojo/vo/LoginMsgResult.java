package com.web.pojo.vo;

import java.io.Serializable;

/*
 *
 * 此类用于交易信息返回结果
 * @author may
 * @date 2018/5/25 16:50
 */
public class LoginMsgResult implements Serializable {
    private String typeId;
    //对应的跟单id
    private Integer requestId;
    //返回码
    private Integer errcode;
    //对返回码的文本描述
    private String ermsg;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErmsg() {
        return ermsg;
    }

    public void setErmsg(String ermsg) {
        this.ermsg = ermsg;
    }
}

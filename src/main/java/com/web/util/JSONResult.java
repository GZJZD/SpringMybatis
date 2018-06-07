package com.web.util;

public class JSONResult {

    private boolean success = true;
    private String msg;
    public JSONResult(String msg){
        this.msg = msg;
    }
    public JSONResult(boolean success, String msg){
        this.success = success;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

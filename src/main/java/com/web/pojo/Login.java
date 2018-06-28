package com.web.pojo;

/**
 * 登录用户实体类
 */
public class Login {
    private Long id; //id
    private String loginName; // 登录用户名
    private String phoneNumber;//手机号码
    private String verifyTime;//验证时间
    private String  code; //验证码
    private String createDate;//用户创建时间
    private String updateDate;//用户修改时间
    private String createBy;//创建人
    private String token; // phoneNumber + 验证时间 + UUid  + Base64 = token
    private String UUid; //登录成功的独立id


    public Login() {

    }


    public Login(Long id, String loginName, String phoneNumber, String verifyTime, String code, String createDate, String updateDate, String createBy, String token, String UUid) {
        this.id = id;
        this.loginName = loginName;
        this.phoneNumber = phoneNumber;
        this.verifyTime = verifyTime;
        this.code = code;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.createBy = createBy;
        this.token = token;
        this.UUid = UUid;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUUid() {
        return UUid;
    }

    public void setUUid(String UUid) {
        this.UUid = UUid;
    }

    @Override
    public String toString() {
        return "Login{" +
                "id=" + id +
                ", loginName='" + loginName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", verifyTime='" + verifyTime + '\'' +
                ", code='" + code + '\'' +
                ", createDate='" + createDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", createBy='" + createBy + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(String verifyTime) {
        this.verifyTime = verifyTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}

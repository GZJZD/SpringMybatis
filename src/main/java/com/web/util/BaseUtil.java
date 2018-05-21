package com.web.util;



public class BaseUtil {
    /**
     * id
     */
    private Long id;
    /**
     * 创建时间
     */
    private String createDate;
    /**
     * 修改时间
     */
    private String updateDate;
    /**
     * 创建人
     */
    private Long createUser;
    /**
     * 修改人
     */
    private Long updateByUser;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreateDate() {
        return createDate;

    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
        if (createDate == null && createDate.isEmpty()) {
            createDate = DateUtil.getHour();
        }
    }

    public String getUpdateDate() {

        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
        updateDate = DateUtil.getHour();

    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getUpdateByUser() {
        return updateByUser;
    }

    public void setUpdateByUser(Long updateByUser) {
        this.updateByUser = updateByUser;
    }
}
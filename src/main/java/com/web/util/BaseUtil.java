package com.web.util;

import com.sun.deploy.util.StringUtils;

import java.util.Date;

public class BaseUtil {
    /**
     * id
     */
    private Integer id;
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
    private Integer createUser;
    /**
     * 修改人
     */
    private Integer updateByUser;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreateDate() {
        return createDate;

    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
        if (createDate == null && createDate.isEmpty() ){
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

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Integer getUpdateByUser() {
        return updateByUser;
    }

    public void setUpdateByUser(Integer updateByUser) {
        this.updateByUser = updateByUser;
    }
}

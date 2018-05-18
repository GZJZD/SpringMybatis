package com.web.service;


import com.web.pojo.DocumentaryDetail;

import java.util.List;

/**
 * Created by may on 2018/5/9.
 */
public interface IDocumentaryDetailService {

    void insert(DocumentaryDetail record);


    List<DocumentaryDetail> selectAll();
    void updateByPrimaryKey(DocumentaryDetail record);

    /**
     * 查找还没有平仓按时间顺序排序
     *@Author: May
     *@param
     *@Date: 15:48 2018/5/9
     */
    List<DocumentaryDetail> selectDataByASC();

}

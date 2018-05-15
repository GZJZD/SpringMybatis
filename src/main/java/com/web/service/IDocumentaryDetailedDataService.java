package com.web.service;


import com.web.pojo.DocumentaryDetailedData;

import java.util.List;

/**
 * Created by may on 2018/5/9.
 */
public interface IDocumentaryDetailedDataService {

    void insert(DocumentaryDetailedData record);


    List<DocumentaryDetailedData> selectAll();
    void updateByPrimaryKey(DocumentaryDetailedData record);

    /**
     * 查找还没有平仓按时间顺序排序
     *@Author: May
     *@param
     *@Date: 15:48 2018/5/9
     */
    List<DocumentaryDetailedData> selectDataByASC();

}

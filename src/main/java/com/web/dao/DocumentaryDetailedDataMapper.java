package com.web.dao;



import com.web.pojo.DocumentaryDetailedData;

import java.util.List;

public interface DocumentaryDetailedDataMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DocumentaryDetailedData record);


    List<DocumentaryDetailedData> selectAll();
    /**
     * 按时间顺序，把还没有平仓的数据查找出来
     *@Author: May
     *@param
     *@Date: 18:30 2018/5/11
     */
    List<DocumentaryDetailedData> selectDataByASC();

    int updateByPrimaryKey(DocumentaryDetailedData record);

}
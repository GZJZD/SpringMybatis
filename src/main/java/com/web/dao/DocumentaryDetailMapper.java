package com.web.dao;



import com.web.pojo.DocumentaryDetail;

import java.util.List;

public interface DocumentaryDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DocumentaryDetail record);


    List<DocumentaryDetail> selectAll();
    /**
     * 按时间顺序，把还没有平仓的数据查找出来
     *@Author: May
     *@Date: 18:30 2018/5/11
     * @param
     *
     */
    List<DocumentaryDetail> selectDataByASC();

    int updateByPrimaryKey(DocumentaryDetail record);

}
package com.web.imp;



import com.web.pojo.DealData;

import java.util.List;

public interface DealDataMapper {


    int insert(DealData record);

    List<DealData> selectAll();


}
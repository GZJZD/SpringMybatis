package com.web.dao;



import com.web.pojo.Variety;

import java.util.List;

public interface VarietyDao {


    Variety selectByPrimaryKey(Long id);

    List<Variety> selectAll();


    Variety selectByVarietyCode(String varietyCode);
}
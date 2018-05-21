package com.web.dao;


import com.web.pojo.Tactics;

import java.util.List;

public interface TacticsDao {
    int deleteByPrimaryKey(Long id);

    int insert(Tactics record);

    Tactics selectByPrimaryKey(Long id);

    List<Tactics> selectAll();

    int updateByPrimaryKey(Tactics record);
}
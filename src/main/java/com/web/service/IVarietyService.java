package com.web.service;

import com.web.pojo.Variety;

import java.util.List;

/**
 * 品种类
 * Created by may on 2018/5/21.
 */
public interface IVarietyService {
    Variety getVariety(Long id);
    List<Variety> getVarietyList();
    /**
     * 通过品种的代码找对应的id
     *@Author: May
     *@param
     *@Date: 12:07 2018/5/21
     */
    Variety getVarietyByCode(String varietyCode);
}

package com.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.pojo.Variety;
import com.web.service.VarietyService;

@Controller
@RequestMapping("/variety")
public class VarietyController {
    @Autowired
    private VarietyService varietyService;
    /*
     *   品种展示
     */
    @RequestMapping("/getListVariety.Action")
    @ResponseBody
    public List<Variety> getListVariety(){
        return varietyService.getVarietyList();
    }
}

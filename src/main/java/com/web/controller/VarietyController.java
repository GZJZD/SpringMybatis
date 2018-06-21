package com.web.controller;

import com.alibaba.fastjson.JSON;
import com.web.service.IVarietyService;
import com.web.util.json.WebJsion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/variety")
public class VarietyController {
    @Autowired
    private IVarietyService varietyService;
    /*
     *   品种展示
     */
    @RequestMapping("/getListVariety.Action")
    @ResponseBody
    public String getListVariety(){
        return WebJsion.toJson(varietyService.getVarietyList());
    }
}

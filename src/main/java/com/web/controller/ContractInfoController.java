package com.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.service.ContractInfoService;
import com.web.util.json.WebJsion;

@Controller
@RequestMapping("/contractInfo")
public class ContractInfoController {
    @Autowired
    private ContractInfoService contractInfoService;
    /*
     *   合约信息展示
     */
    @RequestMapping("/getContractInfoList.Action")
    @ResponseBody
    public String getContractInfoList(){
    	System.out.println("sssssss");
        return WebJsion.toJson(contractInfoService.getContractInfoList());
    }
}

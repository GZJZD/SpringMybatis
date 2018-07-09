package com.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.pojo.ContractInfo;
import com.web.service.ContractInfoService;

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
    public List<ContractInfo> getContractInfoList(){
        return contractInfoService.getContractInfoList();
    }
}

package com.web.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.pojo.ContractInfo;
import com.web.pojo.Platform;
import com.web.pojo.Variety;
import com.web.service.ContractInfoService;
import com.web.util.json.JSONResult;

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
	
	@RequestMapping("/saveContractInfo.Action")
    @ResponseBody
    public JSONResult saveContractInfo(Long contractInfoId,Long platformId,Long varietyId,String contractName,String contractCode){
		try {
			//构造合约信息实体
			ContractInfo contractInfo = new ContractInfo();
			contractInfo.setContractCode(contractCode);
			contractInfo.setContractName(contractName);
			contractInfo.setCreateTime(new Date());
			contractInfo.setCreateUser("sa");
			Platform platform = new Platform();
			platform.setId(platformId);
			contractInfo.setPlatform(platform);
			Variety variety = new Variety();
			variety.setId(varietyId);
			contractInfo.setVariety(variety);
			//通过是否有contractInfoId来判断保存或者更新
			if(contractInfoId!=null) {
				contractInfo.setId(contractInfoId);
				contractInfoService.updateContractInfo(contractInfo);
			}else {
				contractInfoService.save(contractInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new JSONResult(false, "合约信息保存失败");
		}
		return new JSONResult("合约信息保存成功");
    }
    
}

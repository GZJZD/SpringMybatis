package com.web.controller;

import com.web.pojo.vo.followOrder.FollowOrderClientParamVo;
import com.web.service.FollowOrderClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/followOrderClient")
public class FollowOrderClientController {
    @Autowired
    private FollowOrderClientService followOrderClientService;

    /*
     * 通过跟单id，返回跟单编辑中的客户列表
     * param 跟单id
     * */
    @RequestMapping("/getListFollowOrderClientParamVo.Action")
    @ResponseBody
    public List<Map<String,Object>> getListFollowOrderClientParamVo(Long followOrderId){
        return followOrderClientService.getListFollowOrderClientParamVo(followOrderId);
    }

    /*
    * 返回该跟单下的客户编号
    * */
    @RequestMapping("/findListUserName.Action")
    @ResponseBody
    public List<Map<String,Object>> findListUserName(Long followOrderId){
        return followOrderClientService.getListUserNameByFollowOrderId(followOrderId);
    }


}

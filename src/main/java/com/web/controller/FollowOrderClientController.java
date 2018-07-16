package com.web.controller;

import com.web.pojo.vo.followOrder.FollowOrderClientParamVo;
import com.web.service.FollowOrderClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/followOrderClient")
public class FollowOrderClientController {
    @Autowired
    private FollowOrderClientService followOrderClientService;

    /*
     * 通过跟单id，客户列表数据展示
     * param 跟单id
     * */
    @RequestMapping("/getListFollowOrderClientParamVo.Action")
    @ResponseBody
    public List<FollowOrderClientParamVo> getListFollowOrderClientParamVo(Long followOrderId){
        return followOrderClientService.getListFollowOrderClientParamVo(followOrderId);
    }

    /*
    * 返回该跟单下的客户编号
    * */
    @RequestMapping("/findListUserName.Action")
    @ResponseBody
    public List<String> findListUserName(Long followOrderId){
        return followOrderClientService.getListUserNameByFollowOrderId(followOrderId);
    }


}

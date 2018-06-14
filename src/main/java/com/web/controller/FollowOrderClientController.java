package com.web.controller;

import com.web.common.FollowOrderEnum;
import com.web.pojo.vo.FollowOrderClientParamVo;
import com.web.service.IFollowOrderClientService;
import com.web.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/followOrderClient")
public class FollowOrderClientController {
    @Autowired
    private IFollowOrderClientService followOrderClientService;

    /*
     * 通过跟单id，查找客户列表数据
     * param 跟单id
     * */
    @RequestMapping("/getListFollowOrderClientParamVo.Action")
    @ResponseBody
    public List<FollowOrderClientParamVo> getListFollowOrderClientParamVo(Long followOrderId){
        return followOrderClientService.getListFollowOrderClientParamVo(followOrderId);
    }


}

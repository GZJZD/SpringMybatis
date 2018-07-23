package com.web.controller;

import com.web.pojo.FollowOrderClient;
import com.web.service.FollowOrderClientService;
import com.web.util.json.JSONResult;
import com.web.util.json.WebJsion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static Logger log = LogManager.getLogger(FollowOrderClientController.class.getName());

    /*
     * 通过跟单id，返回跟单编辑中的客户列表
     * param 跟单id
     * */
    @RequestMapping("/getListFollowOrderClientParamVo.Action")
    @ResponseBody
    public List<Map<String, Object>> getListFollowOrderClientParamVo(Long followOrderId) {
        return followOrderClientService.getListFollowOrderClientParamVo(followOrderId);
    }

    /*
     * 返回该跟单下的客户编号
     * */
    @RequestMapping("/findListUserName.Action")
    @ResponseBody
    public List<Map<String, Object>> findListUserName(Long followOrderId) {
        return followOrderClientService.getListUserNameByFollowOrderId(followOrderId);
    }


    /*
     * 策略添加新客户
     * */
    @RequestMapping("/addClientByFollowOrder.Action")
    @ResponseBody
    public JSONResult addClientByFollowOrder(Long followOrderId, String followOrderClients) {
        try {
            List<FollowOrderClient> followOrderClients1 = WebJsion.parseArray(followOrderClients, FollowOrderClient.class);
            //检查客户是否已经存在
            List<String> userName = followOrderClientService.checkAddClient(followOrderClients1, followOrderId);
            StringBuilder sBuilder = new StringBuilder();
            if (userName.size()!=0){
                for (String name : userName) {
                    sBuilder.append(name+"、");
                }
                sBuilder.deleteCharAt(sBuilder.length() - 1);
                return new JSONResult(sBuilder+"客户已在跟单中");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new JSONResult(false, "添加客户失败");
        }
        return new JSONResult("添加成功");
    }

    /*
     * 删除客户
     * */
    @RequestMapping("/deleteClient.Action")
    @ResponseBody
    public JSONResult deleteClient(Long followOrderClientId){
        try {

            followOrderClientService.deleteClient(followOrderClientId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new JSONResult(false, "删除客户失败");
        }
        return new JSONResult("删除成功");
    }

}

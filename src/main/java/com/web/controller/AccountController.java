package com.web.controller;

import com.web.common.FollowOrderEnum;
import com.web.pojo.Account;
import com.web.pojo.Agent;
import com.web.pojo.Platform;
import com.web.service.AccountService;
import com.web.util.common.DateUtil;
import com.web.util.json.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;


    @RequestMapping("/createAccount.Action")
    @ResponseBody
    public JSONResult createAccount(Long platformId,Long agentId,String name,String password){
        try {
            Account account = new Account();
            account.setAccount(name);
            Agent agent = new Agent();
            agent.setId(agentId);
            account.setAgent(agent);
            Platform platform = new Platform();
            platform.setId(platformId);
            account.setPlatform(platform);
            account.setPassword(password);
            account.setCreateTime(DateUtil.getStringDate());
            account.setCreateUser("猜猜");
            //正常状态
            account.setStatus(FollowOrderEnum.FollowStatus.ACCOUNT_START.getIndex());
            accountService.save(account);
        }catch (Exception e){
            e.printStackTrace();
            return new JSONResult(false, "创建账号失败");
        }
        return new JSONResult( "创建账号成功");
    }
}

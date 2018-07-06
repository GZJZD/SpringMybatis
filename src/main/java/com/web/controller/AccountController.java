package com.web.controller;

import com.web.common.FollowOrderEnum;
import com.web.pojo.Account;
import com.web.pojo.Agent;
import com.web.pojo.FollowOrderDetail;
import com.web.pojo.Platform;
import com.web.pojo.vo.FollowOrderVo;
import com.web.service.AccountService;
import com.web.service.FollowOrderDetailService;
import com.web.service.FollowOrderService;
import com.web.util.common.DateUtil;
import com.web.util.json.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private FollowOrderService followOrderService;
    @Autowired
    private FollowOrderDetailService followOrderDetailService;

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

    @RequestMapping("getListAccount.Action")
    @ResponseBody
    public List<Map<String,Object>> getListAccount(){
        List<Account> accountList = accountService.getListAccount();
        List<Map<String,Object> >  listMap = new ArrayList<>();
        for (Account account : accountList) {
            Map<String,Object> accountMap = new HashMap<>();
            accountMap.put("id",account.getId());
            accountMap.put("platformName",account.getPlatform().getName());
            accountMap.put("account",account.getAccount());
            accountMap.put("agentName",account.getAgent().getName());
            int count = followOrderService.findAccountStatusByAccountId(account.getId());
            if (count > 0) {
                accountMap.put("status", "跟单中");
            } else {
                accountMap.put("status", "未跟单");
            }

            FollowOrderVo followOrderVo = followOrderDetailService.getAccountCountAndOffsetGainAndLossBYAccountId(account.getId());

            accountMap.put("allTotal",followOrderVo.getAllTotal());
            accountMap.put("profitAndLoss",followOrderVo.getOffsetGainAndLoss());
            listMap.add(accountMap);
        }
        return listMap;
    }
    @RequestMapping(value = "/goToFollowOrderPage.Action")
    public String goToFollowOrderPage(String gotoPage){

        //forward
//        return "redirect:/login.thml";
//        return new ModelAndView("redirect:/login.html");
        return  "redirect:/login.html";
    }

}

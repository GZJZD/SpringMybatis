package com.web.controller;

import com.web.pojo.Account;
import com.web.service.AccountService;
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


    @RequestMapping("/createAccount")
    @ResponseBody
    public JSONResult createAccount(Account account){
        try {
            accountService.save(account);
        }catch (Exception e){
            e.printStackTrace();
            return new JSONResult(false, "创建账号失败");
        }
        return new JSONResult( "创建账号成功");
    }
}

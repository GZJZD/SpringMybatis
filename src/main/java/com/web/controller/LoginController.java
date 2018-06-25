package com.web.controller;


import com.alibaba.fastjson.JSONObject;
import com.web.pojo.Login;
import com.web.service.LoginService;
import com.web.util.common.DateUtil;
import com.web.util.json.WebJsion;
import com.web.util.sms.AliSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登录
 */
@Controller
@RequestMapping("/Login")
public class LoginController {

    @Autowired
    @Qualifier("loginService")
    private LoginService loginService;
    /**
     * 获取验证码
      * @param phoneNumber 手机号码
     * @return
     */
    @RequestMapping(value = "/getCode.Action")
    @ResponseBody
    public String  getCode(String phoneNumber){
        int code = (int)(Math.random()*8999)+1000; //生成随机4位验证码
        Login login = loginService.findByPhoneNumber(phoneNumber.trim());

        try {
//            AliSms.SendSms(phoneNumber,code);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(login != null){
            login.setCode(Integer.toString(code));
            login.setUpdateDate(DateUtil.getStringDate());
            login.setVerifyTime(DateUtil.getStringDate());
            loginService.updateCode(login);
        }
        return WebJsion.toJson(login);
    }


    /**
     * 登录校验
     * @param phoneNumber 手机号码
     * @param code 验证码
     * @return
     */
    @RequestMapping(value = "/login.Action")
    @ResponseBody
    public String login(String phoneNumber,String code){
        String SMS_Status=null;
        Login login =  loginService.findByPhoneNumber(phoneNumber.trim());
        SMS_Status = loginService.checkLogin(login,code);
        return SMS_Status;
    }

}

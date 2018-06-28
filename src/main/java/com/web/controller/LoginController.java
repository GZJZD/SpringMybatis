package com.web.controller;


import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.web.pojo.Login;
import com.web.service.LoginService;
import com.web.util.common.DateUtil;
import com.web.util.cookie.CookieConstantTable;
import com.web.util.cookie.CookieUtils;
import com.web.util.json.WebJsion;
import com.web.util.sms.AliSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        String msg = null;
        if(phoneNumber == null && StringUtils.isEmpty(phoneNumber)){
            return msg = "手机号码不能为空";
        }
        Login login = loginService.findByPhoneNumber(phoneNumber.trim());
        if (login == null){
            return  msg = "用户不存在，请与管理员联系！";
        }
         msg = loginService.sendSms(phoneNumber,login);
            return msg;
    }


    /**
     * 登录校验
     * @param phoneNumber 手机号码
     * @param code 验证码
     * @return
     */
    @RequestMapping(value = "/login.Action")
    @ResponseBody
    public String login(String phoneNumber,String code,boolean rememberme, HttpServletResponse response, HttpServletRequest request){
        String SMS_Status=null;
        if (phoneNumber == null &&StringUtils.isEmpty(phoneNumber)){
            return SMS_Status="手机号码不能为空！";
        }
        if(code == null && StringUtils.isEmpty(code)){
            return SMS_Status="验证码不能为空！";
        }
        Login login =  loginService.findByPhoneNumber(phoneNumber.trim());
        if (login == null){
           return   SMS_Status = "用户不存在，请与管理员联系!";
        }
        SMS_Status = loginService.checkLogin(login,code, rememberme, response,request);
        return SMS_Status;
    }

    /**
     * 退出登录
      * @return
     */
    @RequestMapping(value = "/logout.Action")
    @ResponseBody
    public void logout(HttpServletRequest request, HttpServletResponse response){

        Login login = (Login) request.getSession().getAttribute("login");
        Login l = loginService.findByPhoneNumber(login.getPhoneNumber());
        request.getSession().removeAttribute("login");
        CookieUtils.delCookie(request, response, CookieConstantTable.RememberMe);
    }

}

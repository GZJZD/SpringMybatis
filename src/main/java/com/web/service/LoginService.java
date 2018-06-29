package com.web.service;

import com.web.pojo.Login;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginService {
    Login findByPhoneNumber(String phoneNumber);
    void updateCode(Login login);
    String checkLogin(Login login,String code,boolean rememberme, HttpServletResponse response, HttpServletRequest request);
    String sendSms(String phoneNumber,Login login);
    boolean checkCookie(String token);
}

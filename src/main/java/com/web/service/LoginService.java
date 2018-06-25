package com.web.service;

import com.web.pojo.Login;

public interface LoginService {
    Login findByPhoneNumber(String phoneNumber);
    void updateCode(Login login);
    String checkLogin(Login login,String code);

}

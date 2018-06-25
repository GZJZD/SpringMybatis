package com.web.dao;

import com.web.pojo.Login;

public interface LoginDao {
    Login findByPhoneNumber(String phoneNumber);
    void updateCode(Login login);
}

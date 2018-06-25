package com.web.service.imp;

import com.web.dao.LoginDao;
import com.web.pojo.Login;
import com.web.service.LoginService;
import com.web.util.common.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service("loginService")
@Transactional
public class loginServiceImpl implements LoginService {
    private static Logger log = LogManager.getLogger(loginServiceImpl.class.getName());
    @Autowired(required = false)
    private LoginDao loginDao;
    @Override
    public Login findByPhoneNumber(String phoneNumber) {
    Login login = loginDao.findByPhoneNumber(phoneNumber);
    return  login;
    }

    @Override
    public void updateCode(Login login) {
        loginDao.updateCode(login);

    }

    @Override
    public String checkLogin(Login login,String code) {
        long outTime = 28800000; //8小时
        String message = null;
        if(login ==null){
            return  message="用户不存在，请与管理员联系";
        }
        if(null != login.getCode() && login.getCode().equals(code)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(login.getVerifyTime()!= null && !StringUtils.isEmpty(login.getVerifyTime())){
                try {
                    Date begin_Time = sdf.parse(login.getVerifyTime());
                    Date end_Time = sdf.parse(DateUtil.getStringDate());
                    long days=(end_Time.getTime()-begin_Time.getTime());//今天时间 减去最后验证时间
                    System.out.println(DateUtil.getStringDate());
                    System.out.println(end_Time.getTime()+"beginTime:"+begin_Time);
                    System.out.println(days);
                    if(days<outTime){
                        message="true";
                    }else {
                      message="超出有效时间，请重新验证";
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    log.info("日期转换错误");

                }
            }else {
                message="验证日期为空，请联系管理人员";
            }

        }else{
            message="验证码错误，请输入有效验证码！";
        }
        return message;
    }
}

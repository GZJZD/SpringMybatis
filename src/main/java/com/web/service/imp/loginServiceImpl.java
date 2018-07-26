package com.web.service.imp;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.web.dao.LoginDao;
import com.web.pojo.Login;
import com.web.service.LoginService;
import com.web.util.common.DateUtil;
import com.web.util.cookie.CookieConstantTable;
import com.web.util.cookie.CookieUtils;
import com.web.util.encryption.EncryptionUtil;
import com.web.util.sms.AliSms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

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
    public String checkLogin(Login login,String code,boolean rememberme, HttpServletResponse response, HttpServletRequest request) {
        long outTime = CookieConstantTable.outTime; //8小时
        String message = null;
        if(login == null){
            return  message="用户不存在，请与管理员联系！";
        }
        if(null != login.getCode() && login.getCode().equals(code)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(login.getVerifyTime() != null && !StringUtils.isEmpty(login.getVerifyTime())){
                try {
                    login.setUUid(UUID.randomUUID().toString());
                    login.setToken(EncryptionUtil.base64Encode(login.getPhoneNumber()+ "_"+login.getVerifyTime()+"_" +login.getUUid()));
                    Date begin_Time = sdf.parse(login.getVerifyTime());
                    Date end_Time = sdf.parse(DateUtil.getStringDate());
                    long days = (end_Time.getTime()-begin_Time.getTime());//当前时间 减去最后验证时间
                    if(days < outTime){
                        loginDao.updateCode(login);
                        HttpSession session = request.getSession();
                        session.setAttribute("employee", login.getToken());//将登录信息设置到会话
                        Cookie cookie = new Cookie("token", login.getToken());//将登录信息加入cookie中
//                        cookie.setMaxAge(CookieConstantTable.COOKIE_MAX_AGE); //设置cookie最大失效时间<br>　
                        cookie.setMaxAge(8*60*60); //设置cookie最大失效时间<br>　
                        // 设置domain
                        cookie.setDomain("127.0.0.1");
                        // 设置path
                        cookie.setPath("/");
                        response.addCookie(cookie);//将cookie返回加入
                        message="true";
                    }else {
                      message="超出有效时间，请重新验证！";
                    }
                } catch (ParseException e) {
                    log.info("日期转换错误！");
                    e.printStackTrace();
                }
            }else {
                message="验证日期为空，请联系管理人员！";
            }
        }else{
            message="验证码错误，请输入有效验证码！";
        }
        return message;
    }

    @Override
    public String sendSms(String phoneNumber,Login login) {
        String msg =null;
        int code = (int)(Math.random()*8999)+1000; //生成随机4位验证码
        try {
            SendSmsResponse sendSmsResponse  = AliSms.SendSms(phoneNumber,code);
            if(sendSmsResponse != null){
                if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                    login.setCode(Integer.toString(code));
                    login.setUpdateDate(DateUtil.getStringDate());
                    login.setVerifyTime(DateUtil.getStringDate());
                    loginDao.updateCode(login);
                    msg="OK";
                }
                if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("isv.AMOUNT_NOT_ENOUGH")){
                    msg="阿里短信账号余额不足，请联系管理员充值";
                }
                if(sendSmsResponse.getMessage() != null && !sendSmsResponse.getMessage().equals("OK")){
                    msg= sendSmsResponse.getMessage();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            msg = "短信发送失败";
        }

        return msg;
    }

    @Override
    public boolean checkCookie(String token) {
        boolean cookie_status=true;
        try {
            String string = EncryptionUtil.base64Decode(token);
            String[] strList = string.split("_");
            String phoneNumber = strList[0];
            String verifyTime = strList[1];
            Login login = loginDao.findByPhoneNumber(phoneNumber);
            cookie_status =  checkOutTime(login,verifyTime,cookie_status);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return cookie_status;
    }

    public boolean checkOutTime(Login login,String verifyTime,boolean cookie_status){
        long outTime = CookieConstantTable.outTime;
        Date begin_Time = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (login != null) {
            try {
                begin_Time = sdf.parse(verifyTime);
                Date end_Time = sdf.parse(DateUtil.getStringDate());
                long days = (end_Time.getTime() - begin_Time.getTime());//当前时间 减去最后验证时间
                if (days > outTime) {
                    //有效期已经超时，需要重新登录
                    cookie_status = false;
                } else {
                    cookie_status = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                log.info("登录日期转换出错！");
            }

        }else {
            cookie_status = false;
        }
        return cookie_status;
    }

}

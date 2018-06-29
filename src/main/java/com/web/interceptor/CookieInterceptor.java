package com.web.interceptor;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.web.pojo.Login;
import com.web.service.LoginService;
import com.web.util.common.DateUtil;
import com.web.util.cookie.CookieConstantTable;
import com.web.util.cookie.CookieUtils;
import com.web.util.encryption.EncryptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class CookieInterceptor extends HandlerInterceptorAdapter {

    private final Logger log = LoggerFactory.getLogger(CookieInterceptor.class);
    @Autowired
    @Qualifier("loginService")
    private LoginService loginService;
    /**
     * 用于处理自动登录
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("=================test=================="+request.getContextPath());
                HttpSession session = request.getSession();
                Object o  =   session.getAttribute("employee");
                long outTime = CookieConstantTable.outTime;
                System.out.println("*****************************************:"+o);
                Cookie rememberme = CookieUtils.getCookie(request, "token");

        // 已登录
        if (o != null) {

                    String str =EncryptionUtil.base64Decode(String.valueOf(o));
                    String[] strList = str.split("_");
                    String phoneNumber = strList[0];
                    System.out.println(phoneNumber);
                    String verifyTime = strList[1];
                    Login login = loginService.findByPhoneNumber(phoneNumber);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date begin_Time = sdf.parse(login.getVerifyTime());
                    Date end_Time = sdf.parse(DateUtil.getStringDate());
                    long days = (end_Time.getTime()-begin_Time.getTime());//当前时间 减去最后验证时间
                    if(days > outTime){
                        //有效期已经超时，需要重新登录 重定向login 页面
                        CookieUtils.delCookie(response,rememberme);
                    }

                return true;
        } else {

            Cookie cookie = CookieUtils.getCookie(request, "token");
            if( cookie != null){
                //判断是否超时
                System.out.println("not null");
                System.out.println(cookie.getName());
                System.out.println(cookie.getValue());
                return true;
            }else {
                System.out.println("is null");
                session.removeAttribute("employee");
                session.removeAttribute("JSESSIONID");
                session.invalidate();
                CookieUtils.delCookie(response,rememberme);
                String toLoginPath=request.getContextPath() + "/Login/goto.Action?gotoPage="+"gotoPage";

                //response.sendRedirect("https://www.baidu.com/");

                //如果request.getHeader("X-Requested-With") 返回的是"XMLHttpRequest"说明就是ajax请求，需要特殊处理
                if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
                    response.setHeader("REDIRECT", "REDIRECT");//标识
                    response.setHeader("CONTENTPATH",toLoginPath);//跳转地址
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }else{
                    //重定向跳轉
                    TopPageOpenUtils.sendRedirect(toLoginPath,response);
                }
                return false;
            }
        }

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

}

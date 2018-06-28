package com.web.interceptor;


import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor extends HandlerInterceptorAdapter {
	/**
	 * 用于在登录前验证 _csrf 参数
	 * */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		String _csrfByForm = request.getParameter("_csrf");  //表单中的值
		String _csrfBySession = String.valueOf(session.getAttribute("_csrf"));  //session中的值
		session.removeAttribute("_csrf");  //使用之后从session中删掉

		//验证是否存在CSRF攻击
		if(StringUtils.isEmpty(_csrfByForm) && StringUtils.isEmpty(_csrfBySession) && _csrfByForm.equals(_csrfBySession)){
			return true;
		}else{
			return false;
		}		
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

}

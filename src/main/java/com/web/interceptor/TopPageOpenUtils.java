package com.web.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * @Title: 在iframe最父级上打开页面
 * @Description: 防止页面嵌套在iframe框架内-打开
 * @author sunwuzhao
 * @date 2017.09.08
 * @version V1.0
 */
public class TopPageOpenUtils {

	/**
	 * 在最父级上打开页面,防止页面嵌套在iframe框架内-打开
	 * @param url
	 * @param response
	 */
	public static void sendRedirect(String url, HttpServletResponse response) {
		try {
			// 有可页面能嵌套在iframe框架内-打开<br/>
			// response.sendRedirect(request.getContextPath()+"/global/fail.html");

			/*
			 * 因为response.sendRedirect()没有target属性<br/> 
			 * 但html页面和js中有<br/>
			 * 于是，当判断出用户没有访问权限时 或者 程序出现错误时，我们可以在jsp中使用js来转向到真正的相关页面。<br/>
			 */
			// 跳转到父页面-打开
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<script>");
			out.println("window.open ('" + url + "','_top')");
			out.println("</script>");
			out.println("</html>");
			out.flush();
			out.close();
		} catch (IOException e) {
			//
		}
	}
}

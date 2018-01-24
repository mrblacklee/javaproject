package com.tedu.webserver.servlet;

import java.io.File;

import com.tedu.webserver.context.HttpContext;
import com.tedu.webserver.dao.UserDAO;
import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;
import com.tedu.webserver.vo.UserInfo;

/**
 * 处理用户注册逻辑
 * @author adminitartor
 *
 */
public class RegServlet {
	public void service(HttpRequest request,HttpResponse response){
		System.out.println("开始处理注册业务!");
		
		UserDAO dao = new UserDAO();
		//首先获取用户的注册信息
		
		//获取用户名
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		System.out.println(username+":"+password+":"+nickname);
		
		//检查该用户是否已经存在
		//根据检查结果确定后续工作
		if(dao.findUserByUserName(username)!=null){
			//跳转错误页面
			forward("webapps/myweb/reg_fail.html",request,response);
		}else{
			UserInfo userinfo = new UserInfo(username, password, nickname);
			dao.save(userinfo);
			forward("webapps/myweb/reg_success.html",request,response);
		}
		System.out.println("注册业务处理完毕");
	}
	/**
	 * 跳转指定路径
	 * @param url
	 * @param request
	 * @param response
	 */
	private void forward(String url,HttpRequest request,HttpResponse response){
		File file = new File(url);
		response.setContentType(
				HttpContext.getMimeType("html"));
		response.setContentLength(file.length());
		response.setEntity(file);
		response.flush();	
	}
	
	
}






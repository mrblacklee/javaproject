package com.tedu.webserver;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import com.tedu.webserver.context.HttpContext;
import com.tedu.webserver.http.EmptyRequestException;
import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;
import com.tedu.webserver.servlet.RegServlet;

/**
 * 处理客户端请求的处理类
 * @author adminitartor
 *
 */
public class ClientHandler implements Runnable{
	private Socket socket;
	public ClientHandler(Socket socket){
		this.socket = socket;
	}
	public void run(){
		try {
			//解析请求
			HttpRequest request 
				= new HttpRequest(socket);
			//生成响应对象
			HttpResponse response
				= new HttpResponse(socket);
			//获取请求路径
			String requestURI = request.getRequestURI();
			//判断是否请求业务
			if("/myweb/reg".equals(requestURI)){
				//请求注册业务
				RegServlet regServlet = new RegServlet();
				regServlet.service(request, response);
				
			}else{
				File file = new File("webapps"+requestURI);
				if(file.exists()){
					System.out.println("webapps"+requestURI+":该文件存在!");
					//回复客户端(发送HTTP响应)
					//设置响应头
					//先截取资源文件后缀名 
					String fileName = file.getName();
					System.out.println("请求的资源文件名:"+fileName);
					String ex = fileName.substring(fileName.lastIndexOf(".")+1);
					System.out.println("该资源后缀:"+ex);
					//根据资源后缀的名字获取对应的ContentType的值并设置响应头
					response.setContentType(HttpContext.getMimeType(ex));
					response.setContentLength((int)file.length());
					response.setEntity(file);
					response.flush();
				}else{
					System.out.println("webapps"+requestURI+":该文件不存在!");
				}	
			}
		} catch(EmptyRequestException e){
			//空请求不做任何处理
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				/*
				 * Socket关闭后，通过它获取的输入流与
				 * 输出流就关闭了。
				 */
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
}





package com.tedu.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.tedu.webserver.context.HttpContext;
import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;

/**
 * 处理用户注册逻辑
 * @author adminitartor
 *
 */
public class RegServlet {
	public void service(HttpRequest request,HttpResponse response){
		System.out.println("开始处理注册业务!");
		//首先获取用户的注册信息
		
		//获取用户名
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		System.out.println(username+":"+password+":"+nickname);
		
		//将该用户信息保存到文件中
		/*
		 * 数据保存在user.dat文件中
		 * 该文件每条记录定为90个字节。
		 * 其中用户名，密码，昵称各占30个字节
		 */
		try (RandomAccessFile raf
				= new RandomAccessFile("user.dat","rw");
			){
			/*
			 * 首先要判断该用户名是否已经存在
			 * 存在则直接跳转错误页面:reg_fail.html
			 * 告知注册失败。
			 * 否则才进行注册工作。
			 * 
			 * 首先读取user.dat文件中每条记录中的用户名
			 * 然后查看是否有重复。每条记录中前30个字节
			 * 就是该用户的名字，读取出来时要去除空白字符。
			 */
			//读取user.dat文件，查看该用户是否存在
			boolean have = false;
			for(int i =0;i<raf.length()/90;i++){
				raf.seek(i*90);
				byte[] data = new byte[30];
				raf.read(data);
				String name = new String(data,"UTF-8").trim();
				if(name.equals(username)){
					have = true;
					break;
				}
			}
			//根据检查结果确定后续工作
			if(have){
				//跳转错误页面
				File file = new File(
						"webapps/myweb/reg_fail.html");
				response.setContentType(
						HttpContext.getMimeType("html"));
				response.setContentLength(file.length());
				response.setEntity(file);
				response.flush();
				
			}else{
				//将指针移动到文件末尾
				raf.seek(raf.length());
				
				//写入用户名
				byte[] nameArr = username.getBytes("UTF-8");
				nameArr = Arrays.copyOf(nameArr, 30);
				raf.write(nameArr);
				
				byte[] pwdArr = password.getBytes("UTF-8");
				pwdArr = Arrays.copyOf(pwdArr, 30);
				raf.write(pwdArr);
				
				byte[] nickArr = nickname.getBytes("UTF-8");
				nickArr = Arrays.copyOf(nickArr, 30);
				raf.write(nickArr);
				
				//跳转注册成功的页面
				File file = new File("webapps/myweb/reg_success.html");
				response.setContentType(HttpContext.getMimeType("html"));
				response.setContentLength(file.length());
				response.setEntity(file);
				response.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("注册业务处理完毕");
	}
}






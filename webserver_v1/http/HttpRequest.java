package com.tedu.webserver.http;

import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.tedu.webserver.context.HttpContext;

/**
 * 该类的每个实例用于表示一个HTTP请求内容
 * @author adminitartor
 *
 */
public class HttpRequest {
	//通过Socket获取输入流从而读取客户端的请求内容
	private Socket socket;
	
	//请求行相关信息
	//请求方式
	private String method;
	//请求资源路径
	private String url;
	//url中的请求部分
	private String requestURI;
	//url中的参数部分
	private String queryString;
	//参数对应关系
	private Map<String,String> parameters = new HashMap<String,String>();
	//请求使用的协议
	private String protocol;
	
	
	//消息头相关信息
	//所有消息头对应的Map
	private Map<String,String> headers = new HashMap<String,String>();
	/**
	 * 实例化HttpRequest并通过给定的Socket解析请求
	 * @param socket
	 */
	public HttpRequest(Socket socket)throws Exception{
		//System.out.println("初始化HttpRequest...");
		this.socket = socket;
		InputStream in = socket.getInputStream();
		//读请求行
		parseRequestLine(in);
		//读消息头
		parseHeaders(in);
		//读消息正文	
		//System.out.println("初始化HttpRequest完毕!");
	}
	/**
	 * 解析请求行
	 * @param in
	 */
	private void parseRequestLine(InputStream in){
		//System.out.println("开始解析请求行...");
		String line = readLine(in);
		//判断是否为空请求
		if(line.length()==0){
			throw new EmptyRequestException("空请求");
		}
		//System.out.println("请求行内容:"+line);
		
		/*
		 * GET /index.html HTTP/1.1
		 * GET /reg?username=fanchuanqi&password=123456&nickname=fanfan HTTP/1.1
		 * 将请求行中的三部分内容分别设置到
		 * method,url,protocol属性上。
		 * 设置完毕后打桩查看,例如:
		 * method:GET
		 * url:/index.html
		 * protocol:HTTP/1.1
		 */
		//按照空格拆分
		String[] data = line.split("\\s");
		//System.out.println("len:"+data.length);
		
		this.method = data[0];
		this.url = data[1];
		this.protocol = data[2];
		//System.out.println("method:"+method);
		//System.out.println("url:"+url);
		//System.out.println("protocol:"+protocol);
		
		//进一步解析URL
		parseURL();
		//System.out.println("解析请求行完毕!");
	}
	/**
	 * 解析请求行中的URL部分
	 */
	private void parseURL(){
		System.out.println("开始解析URL");
		/*
		 * 请求行中URL部分可能的两种情况:
		 * 1:/myweb/index.html
		 *   不含传参的形式
		 * 
		 * 2:/myweb/reg?name=zhangsan&pwd=123...
		 *   含有传递的参数
		 *   HTTP协议规定地址栏传参的格式以"?"分割请求
		 *   与参数。参数部分则为:参数名=参数值的形式
		 *   并且每个参数之间以"&"分割
		 * 
		 * 处理URL的方式:
		 * 如果url属性不含有参数，则直接将url属性值内容
		 * 赋值给属性requestURI.
		 * 若含有参数则应当先按照?拆分url,将请求部分赋值
		 * 给requestURI,将参数部分赋值给queryString
		 * 然后再将参数部分进行拆分，将每个参数都存入到
		 * 属性parameters这个Map中.其中:
		 * key为参数的名字,value为参数的值
		 * 
		 */
		//判断url是否含有?
		int index = url.indexOf("?");
		if(index!=-1){
			//按照?拆分出两部分		
			this.requestURI = url.substring(0,index);
			//?是否为url的最后一个字符
			if(index+1<url.length()){
				this.queryString = url.substring(index+1);
				//解析参数部分
				String[] paras = queryString.split("&");
				//解析每一个参数
				for(String para : paras){
					String[] parainfo = para.split("=");
					if(parainfo.length==1){
						//没有参数值
						parameters.put(parainfo[0], "");
					}else{
						parameters.put(
							parainfo[0], parainfo[1]);
					}
				}
				System.out.println(parameters);
			}
		}else{
			this.requestURI = url;
		}
		
		
		System.out.println("解析URL完毕");
	}
	/**
	 * 解析消息头
	 */
	private void parseHeaders(InputStream in){
		//System.out.println("开始解析消息头...");
		/*
		 * 解析思路:
		 * 循环读取一行内容(readLine方法)，然后
		 * 按照":"截取出两项，左边一项应当是消息头
		 * 的名字，右边应当是消息头的值。然后将他们
		 * 存入属性headers这个Map中保存。当读取一行
		 * 内容返回的字符串是空字符串时说明单独读取
		 * 到了一个CRLF,这就说明消息头都读取完毕了。
		 * 那么应当停止循环读取工作。
		 */
		while(true){
			String line = readLine(in);
			if(line.equals("")){
				break;
			}
			//System.out.println("消息头:"+line);
			String[] data = line.split(":\\s");
			//System.out.println("len:"+data.length);
			//System.out.println("key:"+data[0]);
			//System.out.println("value:"+data[1]);
			headers.put(data[0], data[1]);
		}		
		//System.out.println("headers:"+headers);
		//System.out.println("消息头解析完毕!");
	}
	
	
	
	/**
	 * 读取一行字符串(以CRLF结尾)
	 * 从给定的流中读取若干字符，直到连续读取CRLF
	 * 然后将这些字符以字符串形式返回。
	 * @param in
	 * @return
	 */
	private String readLine(InputStream in){
		StringBuilder builder = new StringBuilder();
		try {
			char c1='a';//上次读取到的字符
			char c2='a';//本次读取到的字符
			int d = -1;
			while((d = in.read())!=-1){
				c2 = (char)d;
				//若上次读取CR本次读取LF则停止读取
				if(c1==HttpContext.CR&&c2==HttpContext.LF){
					break;
				}
				builder.append(c2);
				c1 = c2;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//trim的目的是将字符串最后的CR去除
		return builder.toString().trim();
	}
	public String getMethod() {
		return method;
	}
	public String getUrl() {
		return url;
	}
	public String getProtocol() {
		return protocol;
	}
	public String getRequestURI() {
		return requestURI;
	}
	public String getQueryString() {
		return queryString;
	}
	/**
	 * 根据给定的参数名获取对应的参数值
	 * @param name
	 * @return
	 */
	public String getParameter(String name){
		return parameters.get(name);
	}
	
	
	
}












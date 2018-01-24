package com.tedu.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.tedu.webserver.context.HttpContext;

/**
 * 该类的每一个实例表示一个具体的HTTP响应
 * @author adminitartor
 *
 */
public class HttpResponse {
	private Socket socket;
	private OutputStream out;
	//响应要发送的文件实体
	private File entity;
	/*
	 * 响应头信息
	 * key:头的名字
	 * value:头对应的值
	 */
	private Map<String,String> headers = new HashMap<String,String>();
	
	
	
	public HttpResponse(Socket socket) throws Exception{
		this.socket = socket;
		this.out = socket.getOutputStream();
	}
	/**
	 * 响应客户端
	 * 将当前对象表示的响应内容发送给客户端
	 */
	public void flush(){
		/*
		 * 响应客户端分为三步:
		 * 1:发送状态行
		 * 2:发送响应头
		 * 3:发送响应正文
		 */
		sendStautsLine();
		sendHeaders();
		sendContent();
	}
	/**
	 * 发送状态行
	 */
	private void sendStautsLine(){
		try {
			System.out.println("开始发送状态行...");
			String line = "HTTP/1.1 200 OK";
			println(line);
			System.out.println("状态行:HTTP/1.1 200 OK");
			System.out.println("状态行发送完毕!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 发送响应头
	 */
	private void sendHeaders(){
		try {
			System.out.println("开发发送响应头:");
			
//			String line = "Content-Type:text/html";
//			println(line);
//			System.out.println("响应头:"+line);
//			
//			line = "Content-Length:"+entity.length();
//			println(line);
//			System.out.println("响应头:"+line);
			
			//遍历headers发送所有头信息
			Set<Entry<String,String>> entrySet
						= headers.entrySet();
			String line="";
			for(Entry<String,String> header: entrySet){
				line = header.getKey()+
					   HttpContext.HEADER_SEPARATOR+
					   header.getValue();
				println(line);
				System.out.println("响应头:"+line);
			}
			
			//单独发送CRLF表示所有响应头发送完毕
			println("");
			System.out.println("响应头发送完毕!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 发送响应正文
	 */
	private void sendContent(){
		try(FileInputStream fis
				= new FileInputStream(entity);){
			System.out.println("开始发送正文");	
			byte[] data = new byte[1024*10];
			int len  = -1;
			while((len = fis.read(data))!=-1){
				out.write(data, 0, len);
			}
			System.out.println("正文发送完毕!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 发送一行字符串(以CRLF结尾)
	 * @param line
	 */
	private void println(String line){
		try {
			out.write(line.getBytes("ISO8859-1"));
			out.write(HttpContext.CR);//written CR
			out.write(HttpContext.LF);//written LF
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public File getEntity() {
		return entity;
	}
	public void setEntity(File entity) {
		this.entity = entity;
	}
	/**
	 * 设置响应头信息Content-Type的值
	 * @param contentType
	 */
	public void setContentType(String contentType){
		this.headers.put(
			HttpContext.HEADER_CONTENT_TYPE, 
			contentType
		);
	}
	/**
	 * 设置头信息:Content-Length
	 * @param contentLength
	 */
	public void setContentLength(long contentLength){
		this.headers.put(
			HttpContext.HEADER_CONTENT_LENGTH, 
			contentLength+""
		);
	}
	
}











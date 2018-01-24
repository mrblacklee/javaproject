package com.tedu.webserver;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * WebServer主类
 * @author adminitartor
 *
 */
public class WebServer {
	private ServerSocket server;
	
	public WebServer(){
		try {
			server = new ServerSocket(8080);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start(){
		try {
			while(true){
//				System.out.println("等待客户端连接...");
				Socket socket = server.accept();
//				System.out.println("一个客户端连接了!");
				
				ClientHandler handler 
					= new ClientHandler(socket);
				Thread t = new Thread(handler);
				t.start();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		WebServer server = new WebServer();
		server.start();
	}
}











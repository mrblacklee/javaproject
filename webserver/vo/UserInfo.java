package com.tedu.webserver.vo;
/**
 * vo  value object  值对象
 * vo的每个实例用于表示一条具体的记录，不含有逻辑
 * 功能。
 * 
 * UserInfo的每一个实例用于表示一个用户信息
 * @author adminitartor
 *
 */
public class UserInfo {
	private String username;
	private String password;
	private String nickname;
	
	public UserInfo(){
		
	}

	public UserInfo(String username, String password, String nickname) {
		super();
		this.username = username;
		this.password = password;
		this.nickname = nickname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String toString() {
		return username+","+password+","+nickname;
	}
}










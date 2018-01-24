package com.tedu.webserver.dao;

import java.io.RandomAccessFile;
import java.util.Arrays;

import com.tedu.webserver.vo.UserInfo;

/**
 * DAO  Data Access Object 数据连接对象
 * 
 * UserDAO的职责是对用户数据进行存取工作，配合业务
 * 逻辑类完成数据操作。
 * 增删改查
 * @author adminitartor
 *
 */
public class UserDAO {
	/**
	 * 根据给定的用户名查找指定用户
	 * @param username
	 * @return 没有此用户时返回null
	 */
	public UserInfo findUserByUserName(String username){
		try (RandomAccessFile raf 
				= new RandomAccessFile("user.dat","rw")
		){
			for(int i=0;i<raf.length()/90;i++){
				raf.seek(i*90);
				String name = readString(raf,30);
				if(name.equals(username)){
					//找到了
					//读取密码
					String password = readString(raf,30);
					String nickname = readString(raf,30);
					UserInfo userInfo = new UserInfo(name,password,nickname);
					return userInfo;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 将给定的UserInfo对象表示的用户信息保存
	 * @param userinfo
	 * @return 保存成功返回true
	 */
	public boolean save(UserInfo userinfo){
		try (RandomAccessFile raf 
				= new RandomAccessFile("user.dat","rw");
		){
			//将指针移动到文件末尾
			raf.seek(raf.length());	
			writeString(raf,userinfo.getUsername(),30);
			writeString(raf,userinfo.getPassword(),30);
			writeString(raf,userinfo.getNickname(),30);		
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 将给定的字符串通过raf写入文件，写入的长度
	 * 有len决定
	 * @param raf
	 * @param str
	 * @param len
	 */
	private void writeString(
		RandomAccessFile raf,String str,int len){
		try {
			byte[] data = str.getBytes();
			data = Arrays.copyOf(data, len);
			raf.write(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 从给定的RandomAccessFile中当前指针位置连续
	 * 读取给定的字节长度并转换为字符串返回。
	 * @param raf
	 * @param len
	 * @return
	 */
	private String readString(
				RandomAccessFile raf,int len){
		try {
			byte[] data = new byte[len];
			raf.read(data);
			String str = new String(data,"UTF-8").trim();
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		UserDAO dao = new UserDAO();
		UserInfo user = dao.findUserByUserName("fancq");
		System.out.println(user);
	}
}













package com.tedu.webserver.context;

import java.awt.datatransfer.MimeTypeParseException;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * HTTP协议相关定义
 * @author adminitartor
 *
 */
public class HttpContext {
	/**
	 * 回车符
	 */
	public static final int CR = 13;
	/**
	 * 换行符
	 */
	public static final int LF = 10;
	
	
	/*
	 * 头信息相关定义
	 */
	/**
	 * header:分隔符":"
	 */
	public static final String HEADER_SEPARATOR = ":";
	/**
	 * header:Content-Type
	 */
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	/**
	 * header:Content-Length
	 */
	public static final String HEADER_CONTENT_LENGTH = "Content-Length";
	
	
	/**
	 * 介质类型映射
	 * 资源后缀与Content-Type值的对应关系
	 */
	private static final Map<String,String> mimeMapping = new HashMap<String,String>();
	
	static{
		//初始化介质类型映射
		initMimeMapping();
	}
	/**
	 * 初始化介质映射
	 */
	private static void initMimeMapping(){
//		mimeMapping.put("html", "text/html");
//		mimeMapping.put("png", "image/png");
//		mimeMapping.put("jpg", "image/jpg");
//		mimeMapping.put("gif", "image/gif");
//		mimeMapping.put("css", "text/css");
//		mimeMapping.put("js", "application/javascript");
		/*
		 * 读取conf/web.xml文件。
		 * 将根标签<web-app>下所有的<mime-mapping>标签
		 * 解析出来，将其中的子标签<extension>中间的文本
		 * 作为key，将子标签<mime-type>中间的文本作为value
		 * 存入到mimeMapping这个Map中完成初始化操作。
		 * 
		 */
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(
				new File("conf/web.xml"));
			Element root = doc.getRootElement();
			List<Element> mimeList 
				= root.elements("mime-mapping");
			for(Element mime : mimeList){
				String key = mime.elementText("extension");
				String value = mime.elementText("mime-type");
				mimeMapping.put(key, value);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据资源后缀获取对应ContentType的值
	 * @param ex
	 */
	public static String getMimeType(String ex){
		return mimeMapping.get(ex);
	}
	
	public static void main(String[] args) {
		String type = getMimeType("css");
		System.out.println(type);
	}
}









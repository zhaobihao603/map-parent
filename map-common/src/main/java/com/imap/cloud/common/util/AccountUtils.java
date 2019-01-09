package com.imap.cloud.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

/**
 * Security 工具类
 * @author 冯林
 */
public class AccountUtils {
	
	private static final PasswordEncoder encoder = new StandardPasswordEncoder("my-secret-key");//秘钥值  
//	private static final Md5PasswordEncoder md5 = new Md5PasswordEncoder();
	public static final String TEXT_TYPE = "text/html;charset=utf-8";
	public static final String JSON_TYPE = "application/json;charset=utf-8";
	public static final String ADMIN = "6be900f1f1864de3a71886d539009413";		//数据库admin(超级管理员)主键ID
	public static final String ROLEADMIN = "caec20f05f0043d4926dd4e3490816ae";	//数据库admin(超级管理员)主键ID
	public static final String PERMISSIONADMIN = "fb8a2afe9b9c41e29bf7d008cd3c3e29";//数据库admin(超级管理员)主键ID
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 
	 * @param reponse
	 * @param setContentType	返回数据类型
	 * @param content			返回数据内容
	 * @throws IOException
	 */
	public static void getWriter(HttpServletResponse reponse,String setContentType,String content) throws IOException {
		reponse.setContentType(setContentType);
		PrintWriter writer = reponse.getWriter();
		writer = reponse.getWriter();
		if (content.endsWith("}")) {
			JSONObject jsonObject = JSON.parseObject(content);
			jsonObject.put("code", "0000");
			writer.write(jsonObject.toJSONString());
		} else {
			JSONObject jsonObject = new JSONObject();
			JSONArray jsonArray = JSON.parseArray(content);
			jsonObject.put("code", "0000");
			jsonObject.put("data", jsonArray);
			writer.write(jsonObject.toJSONString());
		}
		writer.flush();
		writer.close();
	}
	
	/**
	 * @return	返回UUID32位
	 */
	public static String getUUID(){
		return java.util.UUID.randomUUID().toString().replace("-", "");
	}
	
	public static void main(String[] args){
		String password = "111111";
    	String rawPassword = "5b6e116b0d69266ac9d3ac198a9419508abe42d11158855c9bbbee1183e638c5c69e4a469b38b7fe";
    	boolean b = AccountUtils.SecurityMatch(rawPassword,password);
    	System.out.println(b);
	}
	
	/**
	 * MD5 + 盐  
	 * @param rawPass 原密码
	 * @param salt	     盐
	 * @return  返回加密后的密码 32位
	 */
	public static String SecurityMD5(String rawPass,String salt){
//		return md5.encodePassword(rawPass, salt);
		return null;
	}
	
	/**
	 * 判断加密后的密码是否正确
	 * @param encPass  加密后的密码
	 * @param rawPass  原密码
	 * @param salt     盐
	 * @return  密码通过返回 true
	 */
	public static boolean SecurityMD5IsVaild(String encPass, String rawPass, Object salt){
//		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
//		return md5.isPasswordValid(encPass, rawPass, salt);
		return false;
	}
	
	/**
	 * 升级版加密方法(推荐使用)
	 * 迭代SHA算法+密钥+随机盐来对密码加密
	 * @param rawPassword 原密码
	 * @return  返回加密后的密码 80位
	 */
	public static String SecurityEncrypt(String rawPassword) {  
         return encoder.encode(rawPassword);  
    }  
    
    /**
	 * 判断加密后的密码是否正确
	 * @param password     原密码
	 * @param rawPassword  加密后的密码
	 * @return  密码通过返回 true
	 */
    public static boolean SecurityMatch(String password,String rawPassword) {  
         return encoder.matches(password, rawPassword);  
    }  
    
    /**
     * 返回spring ioc 中的 Bean,不能返回springmvc ioc中的bean
     * @param beanNae			Bean名称
     * @param servletContext	Servlet上下文
     * @return	
     */
    public static Object getBean(String beanNae,ServletContext servletContext){
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		return webApplicationContext.getBean(beanNae);
	}	
}

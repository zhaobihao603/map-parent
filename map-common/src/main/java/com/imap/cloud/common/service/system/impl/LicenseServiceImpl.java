package com.imap.cloud.common.service.system.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.alibaba.druid.util.StringUtils;
import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dto.License;
import com.imap.cloud.common.service.system.LicenseService;
import com.imap.cloud.common.util.DesUtils;

@LogDescription(name="软件证书的服务")
@Service(value="licenseService")
public class LicenseServiceImpl implements LicenseService {
	
	private final static String filePath = "/WEB-INF/license.xml"; // 配置文件相对路径
	
	@Value("${product.name}")
	public String name;
	
	@Override
	public String getProductName(){
		return name;
	}

	@LogDescription(name="校验证书")
	@Override
	public boolean verifyLicense(String rootPath) throws Exception{
		boolean flag = false;
		String key = this.readLicenseKey(rootPath);
		if(StringUtils.isEmpty(key)) return flag;
		
		DesUtils des = new DesUtils("imap"); 
		try{
			String keyDec = des.decrypt(key); 
	
			String keys[] = keyDec.split("---"); 
			if(keys==null || keys.length!=5)return false;
			String mac = keys[0];
			String startTime = keys[1];
			String deadline = keys[2];
			String product = keys[3];
			String valid = keys[4];
			License license = new License(mac,product, valid, startTime, deadline);
			String localMac = this.getMac();
			Date now = new Date();
		
			if (license.isVaild()) {
				if (!license.isInScopeTime(now)) { //如果License中，valid为1，时间过期时，讲valid重写为0。
					StringBuffer sb = new StringBuffer();
					sb.append(mac).append(startTime).append("---").append(deadline).append("---").append(product).append("---0---");
					keyDec = sb.toString();
					key = des.encrypt(keyDec);
					this.writeLicenseKey(key, rootPath); //重写License
					
					return false;
				}
				
				if (license.isEqualMac(localMac) && license.isEqualProductName(this.name)) {
					return true;
				}
			}
		}catch (Exception e) {
			return false;
		}
		return false;
	}
	
	@LogDescription(name="更新证书")
	@Override
	public boolean updateLicense(String key, String rootPath) throws Exception{
		DesUtils des = new DesUtils("imap");// 自定义密钥
		
		try{
			String keyDec = des.decrypt(key);
			if (!keyDec.matches("([0-9A-Fa-f]{2}-){5}[0-9A-Fa-f]{2}---\\d{4}/\\d{2}/\\d{2}---\\d{4}/\\d{2}/\\d{2}---[\\s\\S]*---1")) {
				return false;
			} 
			String keys[] = keyDec.split("---");
			String mac = keys[0];
			String startTime = keys[1];
			String deadline = keys[2];
			String productName = keys[3];
			String valid = keys[4];
			String localMac = this.getMac();
			Date now = new Date();
			
			License license = new License(mac, productName,valid, startTime, deadline);
			if (!license.isVaild()) {
				return false;
			}
			if (!license.isEqualProductName(productName)) {
				return false;
			}
			if (!license.isEqualMac(localMac)) {
				return false;
			}
			if (!license.isInScopeTime(now)) { //验证是否有效
				return false;
			}
			this.writeLicenseKey(key, rootPath); // 重写配置文件
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	@Override
	public String getMac(){
		String os = this.getOSName();
		if(os.toLowerCase().contains("windows"))
			return this.getMacId();
		else return this.getUnixMACAddress();
	}
	
	public static void main(String[] args) throws Exception {
		/*String os = LicenseServiceImpl.getOSName();
		System.out.println(os);*/
//		String ip = getLocalIP();
//		System.out.println(ip);
		/*String mac =getMacId();
		System.out.println(mac);*/
//		
		//LicenseServiceImpl s = new LicenseServiceImpl();
		//String key = s.generatorKey(mac, "imap2.0.1", "2016/12/20", "2019/12/20");
		//System.out.println(key);
		//String key = "a31c17ad6e06696a6bb2bd2519e72380f2b9c251428ffd554acb6c9fb16797f21027e2b5ced4c582516e78d3d204c760a0de70dc37d0f9a8";
		/*DesUtils des = new DesUtils("imap");// 自定义密钥
		mac = "F4-8E-38-A2-00-B1";//"0E:EF:9B:45:EF:48";
		String key = des.encrypt(mac+"---imap2.0.1---1---2016/12/20---2019/12/20");*/
		
//		System.out.println(key);
//		String keyDec = des.decrypt(key);  //用户输入的key解密后格式：开始日期-结束日期
//		System.out.println(keyDec);
	}
	
	public String generatorKey(String mac,String productName,String start,String end)throws Exception{
		if(StringUtils.isEmpty(productName)) productName = this.name;
		if(StringUtils.isEmpty(mac)) mac = this.getMac();
		License license = new License(mac, productName, "1", start, end);
		if (!license.isInScopeTime(new Date())) { //验证是否有效
			throw new Exception("证书时间无效！");
		}	
		
		DesUtils des = new DesUtils("imap");// 自定义密钥
		String key = license.generatorKey(des);
		//updateLicense(key,"");
		return key;
	}
	
	/**
	 * 读取配置文件xml
	 * @param rootPath
	 * @return key
	 * @throws Exception
	 * 
	 */
	public String readLicenseKey(String rootPath) throws Exception {
		File file = new File(rootPath + filePath);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		
		String key = doc.getElementsByTagName("key").item(0).getFirstChild().getNodeValue(); // 在配置文件中读取key字段
		
		return key;
	}
	
	/**
	 * 更行配置文件
	 * @param key
	 * @param rootPath
	 * @throws Exception
	 * 
	 */
	public void writeLicenseKey(String key, String rootPath) throws Exception {
		File file = new File(rootPath + filePath);
		if(!file.getParentFile().exists())file.getParentFile().mkdirs();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getElementsByTagName("key").item(0).setTextContent(key); // 更新配置文件内容
		
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer former = factory.newTransformer();
		former.transform(new DOMSource(doc), new StreamResult(file)); // 保存修改后的内容
	}
	
	/**
	 * 此方法描述的是：获得服务器的os
	 * 
	 * @author: Hxin
	 * @version: 2017年1月4日 下午4:55:35
	 */
	private static String getOSName() {
		return System.getProperty("os.name").toLowerCase();
	}
	
	/**
	  * 此方法描述的是：获得服务器的IP地址
	  * @author: Hxin
	  * @version: 2017年1月4日 下午4:57:15
	  */
	public static String getLocalIP() {
		String sIP = "";
		InetAddress ip = null;
		try {
			boolean bFindIP = false;
			Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
					.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				if (bFindIP) {
					break;
				}
				NetworkInterface ni = (NetworkInterface) netInterfaces
						.nextElement();
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					ip = (InetAddress) ips.nextElement();
					if (!ip.isLoopbackAddress()
							&& ip.getHostAddress().matches(
									"(\\d{1,3}\\.){3}\\d{1,3}")) {
						bFindIP = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != ip) {
			sIP = ip.getHostAddress();
		}
		return sIP;
	}
	
	/**
	 * 此方法描述的是：获得服务器的IP地址(多网卡)
	 * 
	 * @author: Hxin
	 * @version: 2017年1月4日 下午4:59:22
	 */
	public static List<String> getLocalIPS() {
		InetAddress ip = null;
		List<String> ipList = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
					.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) netInterfaces
						.nextElement();
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					ip = (InetAddress) ips.nextElement();
					if (!ip.isLoopbackAddress() // 非127.0.0.1
							&& ip.getHostAddress().matches(
									"(\\d{1,3}\\.){3}\\d{1,3}")) {
						ipList.add(ip.getHostAddress());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipList;
	}
	
	/**
	 * 此方法描述的是：Windows获得服务器的MAC地址
	 * 
	 * @author: Hxin
	 * @version: 2017年1月4日 下午5:03:55
	 */
	public static String getMacId() {
		String macId = "";
		InetAddress ip = null;
		NetworkInterface ni = null;
		try {
			boolean bFindIP = false;
			Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
					.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				if (bFindIP) {
					break;
				}
				ni = (NetworkInterface) netInterfaces.nextElement();
				// ----------特定情况，可以考虑用ni.getName判断
				// 遍历所有ip
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					ip = (InetAddress) ips.nextElement();
					if (!ip.isLoopbackAddress() // 非127.0.0.1
							&& ip.getHostAddress().matches(
									"(\\d{1,3}\\.){3}\\d{1,3}")) {
						bFindIP = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != ip) {
			try {
				macId = getMacFromBytes(ni.getHardwareAddress());
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
		return macId;
	}
	
	/**
	 * 此方法描述的是：Windows获得服务器的MAC地址(多网卡)
	 * 
	 * @author: Hxin
	 * @version: 2017年1月4日 下午5:10:22
	 */
	public static List<String> getMacIds() {
		InetAddress ip = null;
		NetworkInterface ni = null;
		List<String> macList = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
					.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				ni = (NetworkInterface) netInterfaces.nextElement();
				// ----------特定情况，可以考虑用ni.getName判断
				// 遍历所有ip
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					ip = (InetAddress) ips.nextElement();
					if (!ip.isLoopbackAddress() // 非127.0.0.1
							&& ip.getHostAddress().matches(
									"(\\d{1,3}\\.){3}\\d{1,3}")) {
						macList.add(getMacFromBytes(ni.getHardwareAddress()));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return macList;
	}

	private static String getMacFromBytes(byte[] bytes) {
		StringBuffer mac = new StringBuffer();
		byte currentByte;
		boolean first = false;
		for (byte b : bytes) {
			if (first) {
				mac.append("-");
			}
			currentByte = (byte) ((b & 240) >> 4);
			mac.append(Integer.toHexString(currentByte));
			currentByte = (byte) (b & 15);
			mac.append(Integer.toHexString(currentByte));
			first = true;
		}
		return mac.toString().toUpperCase();
	}
	
	/**
	 * 此方法描述的是：Linux获得服务器的MAC地址
	 * 
	 * @author: Hxin
	 * @version: 2017年1月4日 下午5:03:55
	 */
	private static String getUnixMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			//"ifconfig eth0"、"ifconfig eth2"
			process = Runtime.getRuntime().exec("ifconfig");// linux下的命令，一般取eth0作为本地主网卡
																	// 显示信息中包含有mac地址信息
			bufferedReader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line = null;
//			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				/*index = line.toLowerCase().indexOf("hwaddr");// 寻找标示字符串[hwaddr]
				System.out.println(line);
				if (index >= 0) {// 找到了
					mac = line.substring(index + "hwaddr".length() + 1).trim();// 取出mac地址并去除2边空格
					break;
				}*/
				Pattern pat = Pattern.compile("\\b\\w+:\\w+:\\w+:\\w+:\\w+:\\w+\\b");
				Matcher mat= pat.matcher(line);
				if (mat.find()) {
					mac=mat.group(0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			bufferedReader = null;
			process = null;
		}
		return mac.replace(":", "-").toUpperCase();
	}

}

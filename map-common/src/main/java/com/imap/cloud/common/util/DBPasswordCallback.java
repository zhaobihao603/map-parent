package com.imap.cloud.common.util;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.util.DruidPasswordCallback;

public class DBPasswordCallback extends DruidPasswordCallback {
	//私钥key
    //MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIKXSHX4XswFFDGfu5jkv/RZEgH0bjcdmVy37L/JW9Z+ClHSYFmny+bVUWsqSBhxV9OdtXqop/ObTwy7sSAvyQQwhDseEUhcLbf/kjFhls5/fujd/kd27w5MPjaP8XPb5eENbgUDk3zc4ULncgdZ4RuJVjamHkYhP4mpjCk3qqTBAgMBAAECgYABlAJ0KoQ6wvTzhJKnzlvjgBAPpfyO/fSUgkSCLNTHSs6lHn27l6OHLpzFRsWIRhZhoE5JJTCiaQYTiVEiqnErb1yn87QzpkVBy71/tzxa0DSjE4zVD7Ey6HzpvcjUH1b1VDAC84vmai0leN3qlp76GG9N8gOhBZ2TgsmIUcBwYQJBAMPYLsi8bAeynM4sDfZsN4s7Q7GJpkqlbs4iunaj8GB13dB3x/3mf7Se+RZRrZziXHrGRWejSbGka/6ogRJBbv8CQQCqtAcLlbdUMBhI5lBvBbmun6ze6MurjpnGkbazn6ULP/VKwDazTK2eDq9OvPiYSYEvudb1kUcGCfGXKHiKA6w/AkEAovniZPY4SGehCgmrTOhdiKY2/SV5lyD4ht5roZNn1D4B6xvi+C27FIo+6w3plmbhYuJgm5BNIWGc5X1Ae/qr7QJBAIAl3DAfOdySqnh3piRiiO2F43IIE3X7/IcsNpsMGwuh/RQbzjS2OJQyc/rJNFqmu7vHaI87e7UV2+7dK/h4H2MCQBTN/fVa3fu/FO0lwKB+YhlqVzJbiIP44H9GKda8RKCY+zS2WCWOh4IXULtVQ9N0wVUWmjH5nydD8DXSthegjuE=
    //公钥
    //MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCl0h1+F7MBRQxn7uY5L/0WRIB9G43HZlct+y/yVvWfgpR0mBZp8vm1VFrKkgYcVfTnbV6qKfzm08Mu7EgL8kEMIQ7HhFIXC23/5IxYZbOf37o3f5Hdu8OTD42j/Fz2+XhDW4FA5N83OFC53IHWeEbiVY2ph5GIT+JqYwpN6qkwQIDAQAB
	//上述生成的公钥
	public static final String PUBLIC_KEY_STRING = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCl0h1+F7MBRQxn7uY5L/0WRIB9G43HZlct+y/yVvWfgpR0mBZp8vm1VFrKkgYcVfTnbV6qKfzm08Mu7EgL8kEMIQ7HhFIXC23/5IxYZbOf37o3f5Hdu8OTD42j/Fz2+XhDW4FA5N83OFC53IHWeEbiVY2ph5GIT+JqYwpN6qkwQIDAQAB";

    public void setProperties(Properties properties) {
        super.setProperties(properties);
        String prefix = properties.getProperty("usedDatabase");
        String pwd = properties.getProperty(prefix.concat("jdbc.password"));
        if (StringUtils.isNotBlank(pwd)) {
            try {
                //这里的password是将jdbc.properties配置得到的密码进行解密之后的值
                String password = ConfigTools.decrypt(PUBLIC_KEY_STRING, pwd); 
                setPassword(password.toCharArray());
            } catch (Exception e) {
                setPassword(pwd.toCharArray());
            }
        }
    }

    // 请使用该方法加密后，把密文写入classpath:/config/jdbc.properties
    public static void main(String[] args) {
        //System.out.println(SecurityUtil.encryptDes("", key));
    }
}

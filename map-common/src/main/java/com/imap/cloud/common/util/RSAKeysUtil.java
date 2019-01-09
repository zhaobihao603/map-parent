package com.imap.cloud.common.util;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.alibaba.druid.filter.config.ConfigTools;


public class RSAKeysUtil {
	public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    //私钥key
    //MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIKXSHX4XswFFDGfu5jkv/RZEgH0bjcdmVy37L/JW9Z+ClHSYFmny+bVUWsqSBhxV9OdtXqop/ObTwy7sSAvyQQwhDseEUhcLbf/kjFhls5/fujd/kd27w5MPjaP8XPb5eENbgUDk3zc4ULncgdZ4RuJVjamHkYhP4mpjCk3qqTBAgMBAAECgYABlAJ0KoQ6wvTzhJKnzlvjgBAPpfyO/fSUgkSCLNTHSs6lHn27l6OHLpzFRsWIRhZhoE5JJTCiaQYTiVEiqnErb1yn87QzpkVBy71/tzxa0DSjE4zVD7Ey6HzpvcjUH1b1VDAC84vmai0leN3qlp76GG9N8gOhBZ2TgsmIUcBwYQJBAMPYLsi8bAeynM4sDfZsN4s7Q7GJpkqlbs4iunaj8GB13dB3x/3mf7Se+RZRrZziXHrGRWejSbGka/6ogRJBbv8CQQCqtAcLlbdUMBhI5lBvBbmun6ze6MurjpnGkbazn6ULP/VKwDazTK2eDq9OvPiYSYEvudb1kUcGCfGXKHiKA6w/AkEAovniZPY4SGehCgmrTOhdiKY2/SV5lyD4ht5roZNn1D4B6xvi+C27FIo+6w3plmbhYuJgm5BNIWGc5X1Ae/qr7QJBAIAl3DAfOdySqnh3piRiiO2F43IIE3X7/IcsNpsMGwuh/RQbzjS2OJQyc/rJNFqmu7vHaI87e7UV2+7dK/h4H2MCQBTN/fVa3fu/FO0lwKB+YhlqVzJbiIP44H9GKda8RKCY+zS2WCWOh4IXULtVQ9N0wVUWmjH5nydD8DXSthegjuE=
    //公钥
    //MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCl0h1+F7MBRQxn7uY5L/0WRIB9G43HZlct+y/yVvWfgpR0mBZp8vm1VFrKkgYcVfTnbV6qKfzm08Mu7EgL8kEMIQ7HhFIXC23/5IxYZbOf37o3f5Hdu8OTD42j/Fz2+XhDW4FA5N83OFC53IHWeEbiVY2ph5GIT+JqYwpN6qkwQIDAQAB
    public static void main(String[] args) {
        Map<String, Object> keyMap;
        try {
            /*keyMap = initKey();
            String publicKey = getPublicKey(keyMap);
            System.out.println(publicKey);
            String privateKey = getPrivateKey(keyMap);
            System.out.println(privateKey);*/
        	String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIKXSHX4XswFFDGfu5jkv/RZEgH0bjcdmVy37L/JW9Z+ClHSYFmny+bVUWsqSBhxV9OdtXqop/ObTwy7sSAvyQQwhDseEUhcLbf/kjFhls5/fujd/kd27w5MPjaP8XPb5eENbgUDk3zc4ULncgdZ4RuJVjamHkYhP4mpjCk3qqTBAgMBAAECgYABlAJ0KoQ6wvTzhJKnzlvjgBAPpfyO/fSUgkSCLNTHSs6lHn27l6OHLpzFRsWIRhZhoE5JJTCiaQYTiVEiqnErb1yn87QzpkVBy71/tzxa0DSjE4zVD7Ey6HzpvcjUH1b1VDAC84vmai0leN3qlp76GG9N8gOhBZ2TgsmIUcBwYQJBAMPYLsi8bAeynM4sDfZsN4s7Q7GJpkqlbs4iunaj8GB13dB3x/3mf7Se+RZRrZziXHrGRWejSbGka/6ogRJBbv8CQQCqtAcLlbdUMBhI5lBvBbmun6ze6MurjpnGkbazn6ULP/VKwDazTK2eDq9OvPiYSYEvudb1kUcGCfGXKHiKA6w/AkEAovniZPY4SGehCgmrTOhdiKY2/SV5lyD4ht5roZNn1D4B6xvi+C27FIo+6w3plmbhYuJgm5BNIWGc5X1Ae/qr7QJBAIAl3DAfOdySqnh3piRiiO2F43IIE3X7/IcsNpsMGwuh/RQbzjS2OJQyc/rJNFqmu7vHaI87e7UV2+7dK/h4H2MCQBTN/fVa3fu/FO0lwKB+YhlqVzJbiIP44H9GKda8RKCY+zS2WCWOh4IXULtVQ9N0wVUWmjH5nydD8DXSthegjuE=";
            String plainText = "root";
            System.out.println(ConfigTools.encrypt(privateKey,plainText));
            String plainText123456 = "1";
            System.out.println(ConfigTools.encrypt(privateKey,plainText123456));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        byte[] publicKey = key.getEncoded();
        return encryptBASE64(key.getEncoded());
    }

    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        byte[] privateKey = key.getEncoded();
        return encryptBASE64(key.getEncoded());
    }

    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    public static Map<String, Object> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

}

package com.imap.cloud.common.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.alibaba.druid.filter.config.ConfigTools;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
		public static final String PUBLIC_KEY_STRING = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCl0h1+F7MBRQxn7uY5L/0WRIB9G43HZlct+y/yVvWfgpR0mBZp8vm1VFrKkgYcVfTnbV6qKfzm08Mu7EgL8kEMIQ7HhFIXC23/5IxYZbOf37o3f5Hdu8OTD42j/Fz2+XhDW4FA5N83OFC53IHWeEbiVY2ph5GIT+JqYwpN6qkwQIDAQAB";
	
		private String[] propertyNames = {
			"mysql.jdbc.password", "oracle.jdbc.password","redis.pass"
		};

		/**
		 * 解密指定propertyName的加密属性值
		 * @param propertyName
		 * @param propertyValue
		 * @return
		 */
		@Override
		protected String convertProperty(String propertyName, String propertyValue) {
			for (String p : propertyNames) {
				if (p.equalsIgnoreCase(propertyName)) {
					//return AESUtil.AESDecode(propertyValue);
					try {
						return ConfigTools.decrypt(PUBLIC_KEY_STRING, propertyValue);
					} catch (Exception e) {
						
					}
				}
			}
			return super.convertProperty(propertyName, propertyValue);
		}
}

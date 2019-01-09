package com.imap.cloud.common.service.system;

public interface LicenseService {
	public String getMac();
	public String getProductName();
	public boolean verifyLicense(String rootPath) throws Exception;
	public boolean updateLicense(String key, String parentPath) throws Exception;
	public String generatorKey(String mac,String productName,String start,String end)throws Exception;
}

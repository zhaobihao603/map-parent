package com.imap.cloud.common.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import com.imap.cloud.common.util.DesUtils;

public class License {
	private String productName; // 系统名
	private String mac; // 服务器硬件地址
	private String valid;  // 用于验证License本身的有效性
	private Date startTime; // 开始时间
	private Date deadline; // 结束时间
	
	public License(String startTime, String deadline) {
		String pat = "yyyy/MM/dd";
		SimpleDateFormat sdf = new SimpleDateFormat(pat);
		try {
			this.startTime = sdf.parse(startTime);
			this.deadline = sdf.parse(deadline);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public License(String mac,String productName, String valid, String startTime, String deadline) {
		this(startTime, deadline);
		this.productName = productName;
		this.mac = mac;
		this.valid = valid;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	/**
	 * 验证License中的MAC与本地的MAC是否一致
	 * @param u
	 * @return boolean
	 * 
	 */
	public boolean isEqualMac(String m) {
		return this.mac.equals(m);
	}
	
	/**
	 * 验证License中的MAC与本地的MAC是否一致
	 * @param u
	 * @return boolean
	 * 
	 */
	public boolean isEqualProductName(String n) {
		return this.productName.equals(n);
	}
	
	/**
	 * 验证License本事是否有效，“1”有效，“0”无效
	 * @return boolean
	 * 
	 */
	public boolean isVaild() {
		return this.valid.equals("1");
	}

	/**
	 * 验证目前时间是否在License的时间范围内
	 * @param d
	 * @return boolean
	 * 
	 */
	public boolean isInScopeTime(Date d) {
		return (d.getTime() >= this.startTime.getTime())
				&& (d.getTime() <= this.deadline.getTime() + 86400000);
	}

	public String generatorKey(DesUtils des) throws Exception {
		StringBuffer sb = new StringBuffer(this.mac);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		sb.append("---").append(sdf.format(this.startTime)).append("---").append(sdf.format(this.deadline))
			.append("---").append(this.productName).append("---1");
		String key = des.encrypt(sb.toString());
		return key;
	}
}

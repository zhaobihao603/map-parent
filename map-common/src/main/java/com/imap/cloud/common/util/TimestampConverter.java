package com.imap.cloud.common.util;

import java.sql.Timestamp;

import org.springframework.core.convert.converter.Converter;

import com.alibaba.druid.util.StringUtils;

public class TimestampConverter implements Converter<String, Timestamp>{
	 
    @Override
	public Timestamp convert(String timeStr) {
	    Timestamp t=null;
	    if(!StringUtils.isEmpty(timeStr)){
	        long time=Long.valueOf(timeStr);
	        t=new Timestamp(time);
	    }
	    return t;
	}
}
package com.imap.cloud.common.service.map;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import com.imap.cloud.common.aop.annotation.LogDescription;

public interface SearchingService {

	/**
	 * 查询实体的WFS服务生成索引
	 * @param request
	 */
	public void buildEntityIndex(HttpServletRequest request);
	
	/**
	 * 查询实体数据表生成索引（弃用）
	 * @param request
	 */
	@Deprecated
	public void buildEntityIndex2(HttpServletRequest request);
	
	/**
	 * 查询单位数据表生成索引
	 * @param request
	 */
	public void buildUnitIndex(HttpServletRequest request);
	
	/**
	 * 从索引文件里查询
	 * @param request
	 */
	public Map<String,Object> search(String keyWord, Integer areaId, Integer pageSize,Integer currentPage,HttpServletRequest request) 
			throws IOException, ParseException, InvalidTokenOffsetsException;
	/**
	 * 根据关键字从索引文件里查询,areaId是指定校区排前
	 * @param request
	 */
	public Map<String,Object> search(String keyWord,String areaId, Integer pageSize,Integer currentPage,HttpServletRequest request) 
			throws IOException, ParseException, InvalidTokenOffsetsException;
	
	/**
	 * 查询实体单位数据表生成索引
	 * @param request
	 * **/
	public void buildEntityUnitIndexByRedis(HttpServletRequest request);
	
	/**
	 * 根据关键字从索引文件里查询,areaId是指定校区排前
	 * **/
	public Map<String,Object> searchToRedis(String keyWord,String areaId, Integer pageSize,Integer currentPage,HttpServletRequest request) 
			throws IOException, ParseException, InvalidTokenOffsetsException;
}

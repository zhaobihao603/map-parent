package com.imap.cloud.common.service.lushu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.formula.functions.T;

import com.imap.cloud.common.dto.BaseResult;
import com.imap.cloud.common.entity.lushu.Lushu;
import com.imap.cloud.common.service.base.BaseService;

public interface LushuService extends BaseService<Lushu,String> {
	
	public int insert(Lushu record,HttpServletRequest request);

	public BaseResult<T> update(Lushu record,HttpServletRequest request) throws Exception;
	
	int deleteByPrimaryKey(String id);

	Lushu selectByPrimaryKey(String id);

	//发布
	int updateByfabu(String id,boolean fabu);

	/**
	 * 根据条件查询所有的路书
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public List<Lushu> selectList(Lushu entity) throws Exception;
}

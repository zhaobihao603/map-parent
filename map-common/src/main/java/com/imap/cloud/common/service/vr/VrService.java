package com.imap.cloud.common.service.vr;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.formula.functions.T;

import com.imap.cloud.common.dto.BaseResult;
import com.imap.cloud.common.entity.vr.Vr;
import com.imap.cloud.common.service.base.BaseService;

/**
 * vr管理service层接口
 * @author lcy
 */
public interface VrService  extends BaseService<Vr,String>{
	
	int deleteByPrimaryKey(String id);

    Vr selectByPrimaryKey(String id);

    /**
     * 根据条件查询所有的vr
     * @param vr
     * @return
     * @throws Exception
     */
	public List<Vr> selectList(Vr vr) throws Exception;
	
	/**
	 * 新增VR
	 * @param vr
	 * @param title
	 * @param request
	 * @return
	 */
	public int insert(Vr vr,String title,HttpServletRequest request);
	/**
	 * 修改VR
	 * @param vr
	 * @param title
	 * @param request
	 * @return
	 */
	public BaseResult<T> update(Vr vr,String upid,String title,HttpServletRequest request);
}

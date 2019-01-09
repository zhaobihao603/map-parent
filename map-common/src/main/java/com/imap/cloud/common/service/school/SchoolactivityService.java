package com.imap.cloud.common.service.school;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import com.imap.cloud.common.dto.BaseResult;
import com.imap.cloud.common.entity.school.Schoolactivity;
import com.imap.cloud.common.service.base.BaseService;

public interface SchoolactivityService extends BaseService<Schoolactivity,String>{
	
		int deleteByPrimaryKey(String id);

	    Schoolactivity selectByPrimaryKey(String id);

	    //发布
	    int updateByfabu(@Param(value="id") String id,@Param(value="fabu") boolean fabu,@Param(value="fabuState") Integer fabuState);
	    
	    /**
	     * 查询所有的活动
	     * @param entity
	     * @return
	     * @throws Exception
	     */
	    public List<Schoolactivity> selectList(Schoolactivity entity) throws Exception;
	    
	    /**
	     * 新增
	     * @param entity
	     * @return
	     * @throws Exception
	     */
	    public int insert(Schoolactivity record,String title,HttpServletRequest request);
	    
	    /**
	     * 修改
	     * @param entity
	     * @return
	     * @throws Exception
	     */
	    public BaseResult<T> update(Schoolactivity record,String upid,String title,HttpServletRequest request);
}

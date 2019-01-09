package com.imap.cloud.common.service.teachers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.formula.functions.T;

import com.imap.cloud.common.dto.BaseResult;
import com.imap.cloud.common.entity.teachers.Teachers;
import com.imap.cloud.common.service.base.BaseService;

public interface TeachersService extends BaseService<Teachers,String>{
	//删除一条记录
    int deleteByPrimaryKey(String id);
  
    //查询一个记录
    Teachers selectByPrimaryKey(String id);
    //建立上传连接关联
    int linkTeachersUpload(String teachersId ,String uploadId);
    //删除上传连接关联
    int deleteTeachersUpload( String teachersId);
    
    //根据校区查询所有实验室
    List<Teachers> selectLaboratory(String area);

    /**
     * 新增
     * @param teachers
     * @param title
     * @param request
     */
    public int insert(Teachers teachers, String title, HttpServletRequest request);
    /**
     * 修改
     * @param teachers
     * @param title
     * @param request
     */
    public BaseResult<T> update(Teachers teachers,String id, String title,HttpServletRequest request);
}

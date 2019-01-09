package com.imap.cloud.common.service.lushu.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.lushu.LushuMapper;
import com.imap.cloud.common.dto.BaseResult;
import com.imap.cloud.common.entity.lushu.Lushu;
import com.imap.cloud.common.enums.Module;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.lushu.LushuService;
import com.imap.cloud.common.service.system.FileService;
import com.imap.cloud.common.util.AccountUtils;

@LogDescription(name="路书路径服务")
@Service
public class LushuServiceImpl extends BaseServiceImpl< Lushu, String> implements LushuService {

	@Autowired
	private LushuMapper lushumapper;
	@Autowired
	private FileService fileService;
	
	@Autowired
	LushuServiceImpl(LushuMapper lushumapper){
		this.lushumapper =  lushumapper;
		super.setBaseDao(lushumapper);
	}
	
	@Override
	@LogDescription(name="路书删除记录")
	@Transactional
	public int deleteByPrimaryKey(String id) {
		
		return lushumapper.deleteByPrimaryKey(id);
	}

	@Override
	public Lushu selectByPrimaryKey(String id) {
		
		return lushumapper.selectByPrimaryKey(id);
	}

	@Override
	@LogDescription(name="路书修改发布记录")
	@Transactional
	public int updateByfabu(String id, boolean fabu) {
		return lushumapper.updateByfabu(id, fabu);
	}
	
	public List<Lushu> selectList(Lushu entity) throws Exception {
        List<Lushu> lushuList = lushumapper.selectAll(entity);
		return lushuList;
	}
	
	@LogDescription(name="路书插入记录")
	@Transactional
	public int insert(Lushu record,HttpServletRequest request){
		CommonsMultipartResolver commonsMultipartResolver = new   
        		CommonsMultipartResolver(request.getSession().getServletContext());  
        //设置编码  
        commonsMultipartResolver.setDefaultEncoding("utf-8");  
        //判断 request 是否有文件上传,即多部分请求...  
        if (commonsMultipartResolver.isMultipart(request)) { 
        	//转换成多部分request  
        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; 
        	List<MultipartFile> fileList = multipartRequest.getFiles("files");
        	MultipartFile[] o=new MultipartFile[fileList.size()];
        	
        	String[] ids = fileService.upload(fileList.toArray(o), Module.ROADBOOK.getCode(), request);
        	record.setId(AccountUtils.getUUID());
        	record.setFabu(false);
			record.setCreatedTime(new Date());
			record.setUpdatedTime(new Date());
			int num=lushumapper.insert(record);
			for (int i = 0; ids!=null && i < ids.length; i++) {
				lushumapper.linkLushuUpload(record.getId(), ids[i]);
			}
			return num;
        }		
		return 0;
	}
	
	@Override
	@LogDescription(name="路书修改记录")
	@Transactional
	public BaseResult<T> update(Lushu record,HttpServletRequest request) throws Exception{
		CommonsMultipartResolver commonsMultipartResolver = new   
        		CommonsMultipartResolver(request.getSession().getServletContext());  
        //设置编码  
        commonsMultipartResolver.setDefaultEncoding("utf-8");
        Lushu lushu = lushumapper.selectByPrimaryKey(record.getId());//旧对象
        record.setFabu(false);
		record.setUpdatedTime(new Date());
		int num=lushumapper.updateByPrimaryKeySelective(record);//修改
    	if(num>0 && !lushu.getArea().equals(record.getArea())){//如果变换了校区，删除路线
			lushumapper.deleteLushuPathsByLushuId(record.getId());
		}
        //判断 request 是否有文件上传,即多部分请求...  
        if (commonsMultipartResolver.isMultipart(request)) {
        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; 
        	List<MultipartFile> fileList = multipartRequest.getFiles("files");
        	if(fileList!=null && fileList.get(0)!=null){
        		String ids = fileService.upload(fileList.get(0), Module.ROADBOOK.getCode(), request);
            	lushumapper.updatelushuUpload(record.getId(), ids);
        	}        	
        }
        return new BaseResult<T>(true, num, "修改成功");
    }

}

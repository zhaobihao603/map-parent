package com.imap.cloud.common.service.arboretum.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.github.pagehelper.PageHelper;
import com.imap.cloud.common.dao.arboretum.ArboretumShuMapper;
import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.entity.arboretum.ArboretumShu;
import com.imap.cloud.common.entity.arboretum.ArboretumTree;
import com.imap.cloud.common.enums.Module;
import com.imap.cloud.common.service.arboretum.ArboretumShuService;
import com.imap.cloud.common.service.arboretum.ArboretumTreeService;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.system.FileService;
import com.imap.cloud.common.util.AccountUtils;

@Service("arboretumShuServiceIpml")
public class ArboretumShuServiceImpl extends BaseServiceImpl<ArboretumShu, String> implements ArboretumShuService{
	
	private ArboretumShuMapper arboretumShuMapper;
	
	@Resource
	private ArboretumTreeService arboretumTreeService;
	@Resource
	private FileService fileService;
	
	@Autowired
	ArboretumShuServiceImpl(ArboretumShuMapper arboretumShuMapper){
		super.setBaseDao(arboretumShuMapper);
		this.arboretumShuMapper = arboretumShuMapper;
	}

	public void deleteByPkZbgl(String id) throws Exception {
		super.deleteByPk(id);
		arboretumShuMapper.deleteByPkZbgl(id);
	}

	public Pager<ArboretumShu> selectPageTreeNode(String treeNode,
			String area, int pageNum, int pageSize) throws Exception {
		
		List<String> array = new ArrayList<String>();
		this.getTreeNodeData(treeNode, array);
		Map<String,Object> map = new HashMap<>();
		map.put("tid", array);
		map.put("pageSize", pageSize);
		map.put("pageNum",  ((pageNum-1)*pageSize));
		
		PageHelper.startPage(pageNum, pageSize);
		List<ArboretumShu> node = arboretumShuMapper.selectPkTreeNode(map);
		int count = arboretumShuMapper.selectPkTreeNodeCount(array);
		return this.packagePager(count, pageNum, pageSize, node);
		
		/*List<List<ArboretumShu>> listShu = new ArrayList<>();
		this.getTreeNodeData(treeNode, listShu);
		List<ArboretumShu> returnList = new ArrayList<ArboretumShu>();
		for (List<ArboretumShu> list2 : listShu) {
			for (ArboretumShu arboretumTrres : list2) {
				returnList.add(arboretumTrres);
			}
		}
		int count = returnList.size();
		int tem = (int)count/10;
		int totalPage = (count%10==0)?tem:(tem+1);
		Pager<ArboretumShu> page = new Pager<ArboretumShu>(returnList, pageNum, pageSize, count, totalPage, returnList.size());
	    return page;*/
	}
	
	/**
	 * 递归查询Tree的子节点数据
	 * @param id				父节点ID
	 * @param listTemporary		listShu 封装容器
	 * @throws Exception 
	 */
	/*private void getTreeNodeData(String treeNode,List<List<ArboretumShu>> listShu) throws Exception{
		//判断节点中是否有子节点
		List<ArboretumTree> list = arboretumTreeService.selectByPkList(treeNode);
		for (ArboretumTree arboretumTree : list) {
			List<ArboretumShu> treeList = arboretumShuMapper.selectPkTreeNode(arboretumTree.getId());
			if(treeList.size() > 0){
				listShu.add(treeList);
			}
		}
		//查询当前的子节点，是否还有子节点
		for(ArboretumTree arboretumTree : list){
			this.getTreeNodeData(arboretumTree.getId(),listShu);
		}
	}*/
	
	private void getTreeNodeData(String treeNode,List<String> array) throws Exception{
		array.add(treeNode);
		//判断节点中是否有子节点
		List<ArboretumTree> list = arboretumTreeService.selectByPkList(treeNode);
		//查询当前的子节点，是否还有子节点
		for(ArboretumTree arboretumTree : list){
			this.getTreeNodeData(arboretumTree.getId(),array);
		}
	}
	
	public List<ArboretumShu> findShuData(String xmin, String ymin,
			String xmax, String ymax)throws Exception {
		Map<String,String> map = new HashMap<>();
		map.put("xmin", xmin);
		map.put("ymin", ymin);
		map.put("xmax", xmax);
		map.put("ymax", ymax);
		return arboretumShuMapper.findShuData(map);
	}

	public Pager<ArboretumShu> selectPageIndex(ArboretumShu shu, int pageNum,
			int pageSize) {
		Map<String,Object> map = new HashMap<>();
		map.put("shu", shu);
		map.put("pageSize", pageSize);
		map.put("pageNum", (pageNum-1)*pageSize);
		int count = arboretumShuMapper.selectPageIndexCount(shu);
		List<ArboretumShu> list = null;
		if(count > 0){
			list = arboretumShuMapper.selectPageIndex(map);	
		}
		return this.packagePager(count, pageNum, pageSize, list);
	}
	
	public Pager<ArboretumShu> selectPageTreeNodeIndex(String treeNode,
			String area, int pageNum, int pageSize) throws Exception {
		List<String> array = new ArrayList<>();
		this.getTreeNodeData(treeNode, array);
		Map<String,Object> map = new HashMap<>();
		map.put("treeNodeArray", array);
		map.put("area", area);
		map.put("pageSize", pageSize);
		map.put("pageNum", (pageNum-1)*pageSize);
		int count = arboretumShuMapper.selectPkTreeNodeCount(array);
		List<ArboretumShu> list = null;
		if(count > 0){
			list = arboretumShuMapper.selectPageTreeNodeIndex(map);	
		}
		return this.packagePager(count, pageNum, pageSize, list);
	}
	
	
	
	private Pager<ArboretumShu>  packagePager(int count,int pageNum,int pageSize,List<ArboretumShu> list){
		int tem = (int)count/pageSize;
		int totalPage = (count%pageSize==0)?tem:(tem+1);
		Pager<ArboretumShu> page = new Pager<ArboretumShu>(list, pageNum, pageSize, count, totalPage, list != null ? list.size() : 0);
		return page;
	}
	
	
	@Transactional
	public int save(ArboretumShu shu, HttpServletRequest request) throws Exception{
		//创建一个通用的多部分解析器.  
        CommonsMultipartResolver cmr = new CommonsMultipartResolver(request.getSession().getServletContext());  
        //设置编码  
        cmr.setDefaultEncoding("utf-8");
        int i = 0;
        if (cmr.isMultipart(request)) {  
        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;    
            List<MultipartFile> fileList = multipartRequest.getFiles("files");
            if(fileList!=null && fileList.size()>0){
            	String path = fileService.uploadFile(fileList.get(0), Module.ARBORETUM.getCode(), request);
            	String fileId = fileService.save(fileList.get(0),path,Module.ARBORETUM.getCode(), shu.getChinesename(), null,request);
            	shu.setImgurl(path);
            }
        }
        if(!StringUtils.isNotBlank(shu.getId())){
        	shu.setId(AccountUtils.getUUID());
			i = arboretumShuMapper.insert(shu);
		}else{
			i = arboretumShuMapper.updateByPk(shu);
		}
        return i;
	}
	
}

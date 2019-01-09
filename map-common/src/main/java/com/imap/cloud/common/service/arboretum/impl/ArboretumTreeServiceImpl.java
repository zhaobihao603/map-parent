package com.imap.cloud.common.service.arboretum.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imap.cloud.common.dao.arboretum.ArboretumTreeMapper;
import com.imap.cloud.common.entity.arboretum.ArboretumTree;
import com.imap.cloud.common.service.arboretum.ArboretumTreeService;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;


/**
 * 虚拟植物园  林学分类 serviceImpl
 * @author 冯林
 *
 */
@Service("arboretumTreeServiceImpl")
public class ArboretumTreeServiceImpl extends BaseServiceImpl<ArboretumTree, String> implements ArboretumTreeService {
	
	private ArboretumTreeMapper arboretumTreeMapper;
	
	@Autowired
	ArboretumTreeServiceImpl(ArboretumTreeMapper arboretumTreeMapper){
		this.arboretumTreeMapper = arboretumTreeMapper;
		super.setBaseDao(arboretumTreeMapper);
	}
	
	public ArboretumTree selectByPkTwo(String targetId) {
		return arboretumTreeMapper.selectByPkTwo(targetId);
	}

	public void insertChangeLevel(ArboretumTree tree) throws Exception {
		if(StringUtils.isNotBlank(tree.getParentId())){
			arboretumTreeMapper.updateByPKParentNode(tree.getParentId());
		}
		ArboretumTree selectByPkTwo = arboretumTreeMapper.selectByPkTwo(tree.getId());
		arboretumTreeMapper.updateByPkSelective(tree);
		List<ArboretumTree> list = arboretumTreeMapper.selectByPkList(selectByPkTwo.getParentId());
		if(list.size() <= 0){	//说明该父节点下没有子节点了
			ArboretumTree entity = new ArboretumTree();
			entity.setState("open");
			entity.setId(selectByPkTwo.getParentId());
			arboretumTreeMapper.updateByPk(entity);
		}
	}

	public void insertPKParentId(ArboretumTree entity) throws Exception {
		super.insert(entity);
		arboretumTreeMapper.updateByPKParentNode(entity.getParentId());
	}

	public void deleteByPkNew(String id) throws Exception {
		super.deleteByPk(id);
		List<ArboretumTree> selectByPkList = arboretumTreeMapper.selectByPkList(id);
		if(selectByPkList.size() > 0){	//说明该节点下还有子节点
			List<String> list = new ArrayList<>();
			for (ArboretumTree arboretumTree : selectByPkList) {
				list.add(arboretumTree.getId());
			}
			arboretumTreeMapper.deleteByPkForEachSubNode(list);
		}
	}

	public List<ArboretumTree> findAllTree() {
		return arboretumTreeMapper.findAllTree();
	}
	
}

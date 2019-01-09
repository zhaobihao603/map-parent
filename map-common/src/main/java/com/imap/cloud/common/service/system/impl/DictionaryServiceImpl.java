package com.imap.cloud.common.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.cache.RedisCache;
import com.imap.cloud.common.dao.system.DictionaryMapper;
import com.imap.cloud.common.dto.Node;
import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.entity.system.DictionaryItem;
import com.imap.cloud.common.entity.system.DictionarySubItem;
import com.imap.cloud.common.exception.DictionaryCodeRepeatException;
import com.imap.cloud.common.service.system.DictionaryService;

@LogDescription(name="数据字典的服务")
@Service
public class DictionaryServiceImpl implements DictionaryService {

	
	@Autowired
	private DictionaryMapper dictionaryMapper;
	
	@Autowired
	private RedisCache cache;

	@Override
	public List<Node> findChildNodesById(String parentid) {
		List<DictionaryItem> tnLst = dictionaryMapper.selectChildNodeById(parentid);
		List<Node> treeNodeLst = new ArrayList<Node>();
		for (DictionaryItem map : tnLst) {
			Node treeNode = new Node();
			treeNode.setId(String.valueOf(map.getId()));//map.get("id").toString());
			treeNode.setText(map.getValue());//(String)map.get("text"));//text-->value
			treeNode.setState(map.getState());//(String)map.get("state"));
			Map<String, Object> attr = new HashMap<String,Object>();
			attr.put("code", map.getCode());//map.get("code"));
			attr.put("datatype", map.getDatatype());//map.get("datatype"));
			attr.put("parentid", map.getParentid());//map.get("parentid"));
			attr.put("shortvalue", map.getShortvalue());//map.get("shortvalue"));
			attr.put("isLeaf", map.getIsLeaf());//map.get("isLeaf"));
			treeNode.setAttributes(attr);
			treeNodeLst.add(treeNode);
		}
		return treeNodeLst;
	}
	
	@Override
	public DictionaryItem getDictionaryItemById(String id) {
		return dictionaryMapper.selectById(id);
	}
	
	@Override
	public List<DictionaryItem> getChildItemsById(String id) {
		return dictionaryMapper.selectChildItemsById(id);
	}
	
	public List<DictionaryItem> selectItemByValue(String value){
		return dictionaryMapper.selectItemByValue(value);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<DictionaryItem> pageChildItemsById(String id, Integer page,
			Integer rows,String sort,String order) {
		PageHelper.startPage(page, rows);
		if(!StringUtils.isEmpty(sort)){
			PageHelper.orderBy(sort+" "+order);
		}
		List list = dictionaryMapper.selectPageChildItemsbyId(id);
		return new Pager(list).getList();
	}
	
	@LogDescription(name="数据字典保存")
	@Override
	public void save(DictionaryItem dictionaryItem) throws DictionaryCodeRepeatException{
		int count = dictionaryMapper.countItemsByCode(dictionaryItem.getCode());
		if(count>=1){
			throw new DictionaryCodeRepeatException("数据字典项编码不能重复！！！");
		}else{
			dictionaryMapper.addDictionaryItem(dictionaryItem);
		}
	}
	
	@LogDescription(name="数据字典修改")
	@Override
	@Transactional
	public void update(DictionaryItem dictionaryItem) throws DictionaryCodeRepeatException {
		DictionaryItem dic = dictionaryMapper.selectById(dictionaryItem.getId());
		if(!dic.getCode().equals(dictionaryItem.getCode())){
			int count = dictionaryMapper.countItemsByCode(dictionaryItem.getCode());
			if(count>=1)
				throw new DictionaryCodeRepeatException("数据字典项编码不能重复！！！");
		}
		/*int count = dictionaryMapper.countItemsByCode(dictionaryItem.getCode());
		if(count>=1){
			throw new DictionaryCodeRepeatException("数据字典项编码不能重复！！！");*/
		dictionaryMapper.update(dictionaryItem);
	}
	
	@LogDescription(name="数据字典删除")
	@Override
	@Transactional
	public void deleteDictionaryItem(DictionaryItem dictionaryItem) {
		//1.若删除的为子节点,将其子节点的父节点ID变为当前节点的父节点
		List<DictionaryItem> lstChildItem = 
				dictionaryMapper.selectChildItemsById(dictionaryItem.getId());
		for (DictionaryItem item : lstChildItem) {
			dictionaryMapper.updateParentIdById(item.getId(), dictionaryItem.getParentid());
		}
		//2.若删除的为叶子节点,先删除叶子节点的属性
		List<DictionarySubItem> lstSubItem = 
				dictionaryMapper.selectSubItemByItemId(dictionaryItem.getId());
		for (DictionarySubItem dictionarySubItem : lstSubItem) {
			dictionaryMapper.deleteSubItemById(dictionarySubItem.getId());
		}
		//3.删除节点
		dictionaryMapper.deleteById(dictionaryItem.getId());
	}
	
	@LogDescription(name="数据字典拖动保存")
	@Override
	@Transactional
	public void dragAndSave(DictionaryItem dictionaryItem) {
		//1.更新父元素的信息（叶子节点和子节点状态能变时）
//		dictionaryMapper.updateStateById(dictionaryItem.getParentid(), "closed");
		//2.获取源数据字典项的属性
//		DictionaryItem sourceItem = dictionaryMapper.selectById(dictionaryItem.getId());
		//3.更新源数据字典项的父节点id（叶子节点和子节点状态能变时,需要注释部分，否则只需要这一步）
		dictionaryMapper.updateParentIdById(dictionaryItem.getId(),dictionaryItem.getParentid());
		//4.拖拽之后，判断之前的父节点是否还存在子节点，如果没有，则更新父元素状态为open（叶子节点和子节点状态能变时）
//		List<Map<String,Object>> map_itemLst = 
//				treeNodeMapper.selectChildNodeById(sourceItem.getParentid());
//		if(map_itemLst.size()<=0){
//			dictionaryMapper.updateStateById(sourceItem.getParentid(), "open");
//		}  
	}

	@Override
	public List<DictionarySubItem> getSubItemByItemid(String itemid) {
		return dictionaryMapper.selectSubItemByItemId(itemid);
	}

	@LogDescription(name="删除数据字典子项")
	@Override
	@Transactional
	public void deleteDictionarySubItemById(String id) {
		dictionaryMapper.deleteSubItemById(id);
	}

	@LogDescription(name="保存数据字典子项")
	@Override
	@Transactional
	public void saveSubItem(DictionarySubItem dictionarySubItem) throws DictionaryCodeRepeatException {
		int count = dictionaryMapper.countItemsByCode(dictionarySubItem.getCode());
		if(count>=1){
			throw new DictionaryCodeRepeatException("数据字典项编码不能重复！！！");
		}else{
			dictionaryMapper.addSubItem(dictionarySubItem);
		}
	}

	@LogDescription(name="更新数据字典子项")
	@Override
	@Transactional
	public void updateSubItem(DictionarySubItem dictionarySubItem) throws DictionaryCodeRepeatException {
		DictionarySubItem dic = dictionaryMapper.selectSubItemById(dictionarySubItem.getId());
		if(!dic.getCode().equals(dictionarySubItem.getCode())){
			int count = dictionaryMapper.countItemsByCode(dictionarySubItem.getCode());
			if(count>=1)
				throw new DictionaryCodeRepeatException("数据字典项编码不能重复！！！");
		}
		/*int count = dictionaryMapper.countItemsByCode(dictionarySubItem.getCode());
		if(count>=1){
			throw new DictionaryCodeRepeatException("数据字典项编码不能重复！！！");
		}else{*/
		dictionaryMapper.updateSubItem(dictionarySubItem);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<DictionarySubItem> pageSubItemsByItemId(String id,
			Integer page, Integer rows,String sort,String order) {
		PageHelper.startPage(page, rows);
		if(!StringUtils.isEmpty(sort)){
			PageHelper.orderBy("subitem."+sort+" "+order);
		}
		List<DictionarySubItem> list = dictionaryMapper.selectSubItemByItemId(id);
		return new Pager(list).getList();
	}
	
	@Override
	public Integer countSubItemsByItemId(String id) {
		return dictionaryMapper.selectSubItemByItemId(id).size();
	}

	@Override
	public Integer countChildItemsById(String id) {
		return dictionaryMapper.countAllChildItemsById(id);
	}

	@Override
	public List<DictionaryItem> getChildItemsByCode(String code) {
		DictionaryItem item = dictionaryMapper.selectByCode(code);
		return dictionaryMapper.selectChildItemsById(item.getId());
	}
	
	@Override
	public DictionaryItem getByCode(String code) {
		DictionaryItem item = dictionaryMapper.selectByCode(code);
		return item;
	}
	
	public List<DictionaryItem> getByCodes(List<String> codes){
		return dictionaryMapper.selectItemsByCodes(codes);
	}

	@Override
	public List<DictionaryItem> getChildItemByCodeForSec(String id) {
		if(id!=null && id.equals("DWFL"))id = null;
		return dictionaryMapper.selectChildItemsByIdForSec(id);
	}

}

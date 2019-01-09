package com.imap.cloud.common.service.security.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.security.OrganizationalMapper;
import com.imap.cloud.common.dao.security.UserMapper;
import com.imap.cloud.common.entity.security.Organizational;
import com.imap.cloud.common.entity.security.User;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.security.OrganizationalService;
import com.imap.cloud.common.service.security.UrlService;

/**
 * 机构Service实现类
 * @author 冯林
 *
 */
@LogDescription(name="机构管理")
@Service("organizationalServiceImpl")
public class OrganizationalServiceImpl extends BaseServiceImpl<Organizational, String> implements OrganizationalService {
	
	private OrganizationalMapper organizationalMapper;
	@Resource
	private UserMapper userMapper;
	@Resource
	private UrlService urlService;
	
	@Autowired
	OrganizationalServiceImpl(OrganizationalMapper organizationalMapper){
		super.setBaseDao(organizationalMapper);
		this.organizationalMapper = organizationalMapper;
	}
	
	public Organizational selectByPkTwo(String id) {
		return organizationalMapper.selectByPkTwo(id);
	}
	
	@Transactional
	@LogDescription(name="拖动保存")
	public void insertChangeLevel(Organizational tree) throws Exception {
		if(StringUtils.isNotBlank(tree.getParentId())){
			organizationalMapper.updateByPKParentNode(tree.getParentId());
		}
		Organizational selectByPkTwo = organizationalMapper.selectByPkTwo(tree.getId());
		organizationalMapper.updateByPkSelective(tree);
		List<Organizational> list = organizationalMapper.selectByPkList(selectByPkTwo.getParentId());
		if(list.size() <= 0){	//说明该父节点下没有子节点了
			Organizational entity = new Organizational();
			entity.setState("open");
			entity.setId(selectByPkTwo.getParentId());
			organizationalMapper.updateByPk(entity);
		}
		
	}

	@Transactional
	@LogDescription(name="删除机构")
	public void deleteByPkUser(String id) throws Exception {
		super.deleteByPk(id);
		organizationalMapper.deleteByPkUser(id);
		List<Organizational> selectByPkList = organizationalMapper.selectByPkList(id);
		if(selectByPkList.size() > 0){	//说明该节点下还有子节点
			List<String> list = new ArrayList<>();
			for (Organizational Organizational : selectByPkList) {
				list.add(Organizational.getId());
			}
			organizationalMapper.deleteByPkForEachSubNode(list);
		}
	}

	@Transactional
	@LogDescription(name="添加机构")
	public void insertPKParentId(Organizational entity) throws Exception {
		super.insert(entity);
		organizationalMapper.updateByPKParentNode(entity.getParentId());
	}

	public List<User> selectPKLoadUser(String id, String type, Integer page,
			Integer rows, String search) throws Exception {
		List<String> array = new ArrayList<>(); 
		this.getTreeNodeData(id, array);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", array);
		map.put("type", type);
		map.put("page", ((page-1)*rows));
		map.put("rows", rows);
		map.put("search", search);
		PageHelper.startPage(page, rows);
		List<User> list = organizationalMapper.selectPKLoadUser(map);
		return list;
	}
	
	private void getTreeNodeData(String treeNode,List<String> array) throws Exception{
		array.add(treeNode);
		//判断节点中是否有子节点
		List<Organizational> list = organizationalMapper.selectByPkList(treeNode);
		//查询当前的子节点，是否还有子节点
		for(Organizational organizational : list){
			this.getTreeNodeData(organizational.getId(),array);
		}
	}
	
	public int selectPKLoadUserCount(String id, String type, String search) throws Exception {
		List<String> array = new ArrayList<>();
		this.getTreeNodeData(id, array);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", array);
		map.put("type", type);
		map.put("search", search);
		return organizationalMapper.selectPKLoadUserCount(map);
	}
	
	@Transactional
	@LogDescription(name="禁用机构下的用户")
	public void disabledPKUser(JSONObject disabled, JSONArray array) {
		this.UpdateDisabledPKStart(disabled, array);
	}

	@Transactional
	@LogDescription(name="开启机构下的用户")
	public void startPKJSONUser(JSONObject start_, JSONArray array) {
		this.UpdateDisabledPKStart(start_, array);
	}
	
	private void UpdateDisabledPKStart(JSONObject userType, JSONArray array){
		userType.put("array", array);
		organizationalMapper.UpdateDisabledPKStart(userType);
	}
	
	@Transactional
	@LogDescription(name="角色选取该机构下的用户 添加角色权限")
	public void InsertRoleUser(JSONArray roleJSONArray_, JSONObject user) {
		user.put("rid", roleJSONArray_);
		userMapper.insertPkRole(user);
	}
	
	@Transactional
	@LogDescription(name="角色修改该机构下的用户 修改角色权限")
	public void UpdateRoleUser(JSONArray roleJSONArray_, JSONObject user) {
		userMapper.deletepkRole((String)user.get("aid"));
		if(roleJSONArray_.size() > 0){
			user.put("rid", roleJSONArray_);
			userMapper.insertPkRole(user);
		}
	}
	
	@Transactional
	@LogDescription(name="用户个人权限添加")
	@Override
	public void insertPermissionUser(JSONArray permissionJSONArray_,
			JSONObject user) {
		user.put("pid", permissionJSONArray_);
		userMapper.insertPersonalPermission(user);
		try {
			urlService.getAllConfigAttributesCallPermission();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Transactional
	@LogDescription(name="用户个人权限修改")
	@Override
	public void unsertPermissionUser(JSONArray permissionJSONArray_,
			JSONObject user) {
		userMapper.deletepkPersonalPermission((String)user.get("uid"));
		if(permissionJSONArray_.size() > 0){
			user.put("pid", permissionJSONArray_);
			userMapper.insertPersonalPermission(user);
			try {
				urlService.getAllConfigAttributesCallPermission();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public List<Organizational> findAllSonNode() {
		return organizationalMapper.findAllSonNode();
	}
	
	@Transactional
	@LogDescription(name="添加机构用户")
	public void insertUser(String uid, String oid) {
		Map<String,Object> map = new HashMap<>();
		JSONArray uid_ = JSONArray.parseArray(uid);
		map.put("uid", uid_);
		map.put("oid", oid);
		organizationalMapper.insertUser(map);
	}

	@Override
	public JSONObject findSonNodeName(String name) {
		int count = organizationalMapper.findSonNodeName(name);
		JSONObject json = new JSONObject();
		if(count > 0){
			json.put("data", "error");
		}else{
			json.put("data", "success");
		}
		return json;
	}
}

package com.imap.cloud.common.dao.security;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.security.Url;
import org.springframework.stereotype.Repository;

/**
 * 资源管理 URL 接口
 * @author 冯林
 *
 */
@Repository
public interface UrlMapper extends BaseDao<Url,String>{
	
	/**
	 * @return 返回权限对应的用户
	 * @throws Exception
	 */
	List<Url> findAllTwoUser() throws Exception;
	
	/**
	 * @return		返回权限对应的角色
	 * @throws Exception
	 */
	List<Url> findAllTwoRole() throws Exception;
	
	/**
	 * 
	 * @return 返回Url菜单中所以的父节点（后台）
	 */ 
	List<Map<String, Object>> findAllParentNode();
	
	/**
	 * 
	 * @param list 当前用户的所有权限集合
	 * @return	返回该用户的菜单Url
	 */
	List<Map<String,Object>> selectPkPermissionUrl(List<String> list);
	
	/**
	 * 删除和权限的中间表数据
	 * @param id
	 */
	void deletePKPermission(String id);
	
	/**
	 * 
	 * @param url
	 * @return	父节点分页信息
	 */
	List<Map<String, Object>> selectPageFather(Url url);
	
	/**
	 * 
	 * @param id
	 * @return 返回子级菜点(和对应的父级菜单)
	 */
	Map<String, Object> selectUrlAndParentByPk(String id);
	
	/**
	 * 
	 * @return 返回所以菜单列表（子级菜单）只查询显示的。
	 */
	List<Url> findAllNoDispaly();
	
	/**
	 * @return 返回前端所有菜单列表（父级菜单），返回格式JSON数组
	 */
	List<Url> findAllFrontParentNode();
	//List<Map<String,Object>> findAllFrontParentNode();
	
	/**
	 * @return 返回前端所有菜单列表（子级菜单），返回格式JSON数组
	 */
	List<Url> findAllFrontSonNode();
	
	/**
	 * 
	 * @return 返回当前排序中的最大值
	 */
	int findMaxOrder();
	
	/**s
	 * 
	 * @return 返回Url菜单中所有的父节点（包括前后台）
	 */
	List<Map<String, Object>> findAllParentNodeAround();
	
	/**
	 * (前台)查询子级或父级菜单中的code值，是否以存在，不存在返回 true, 存在返回false
	 * @param codes
	 * @return
	 */
	int selectByPkCode(@Param("id")String id,@Param("code")String code);
	
	/**
	 * 查询当前菜单是否以被权限使用
	 * @param id	菜单ID
	 * @return 
	 */
	int selectByPkPermission(String id);
	
	/**
	 * 根据父节点id查询可显示菜单
	 * @param id	菜单ID
	 * @return 
	 */
	List<Url> getByParentCode(String parentCode);
}

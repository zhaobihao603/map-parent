package com.imap.cloud.common.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.entity.system.DictionaryItem;
import com.imap.cloud.common.entity.system.DictionarySubItem;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryMapper {
	
	/**
	 * 根据id查询数据字典该项对应的子节点
	 * @param parentid
	 * @return List<Map<String,Object>>
	 */
	List<DictionaryItem> selectChildNodeById(String parentid);
	
	/**
	 * 根据id查询数据字典项
	 * @param id
	 * @return DictionaryItem
	 */
	DictionaryItem selectById(String id);
	
	/**
	 * 根据编码查询数据字典项
	 * @param code
	 * @return DictionaryItem
	 */
	DictionaryItem selectByCode(String code);
	
	/**
	 * 根据parentid查询其所有的子项及子项的属性
	 * @param id
	 * @return List<DictionaryItem>
	 */
	List<DictionaryItem> selectChildItemsById(String id);
	
	/**
	 * 分页查询数据字典子项
	 * @param id 父节点id
	 * @param start 开始的index
	 * @param pageSize 每页的个数
 	 * @return List<DictionaryItem>
	 */
	List<DictionaryItem> selectPageChildItemsbyId(@Param("parentid")String id);
	
	/**
	 * 根据value查询数据字典子项
	 * @param value
	 * @return
	 */
	List<DictionaryItem> selectItemByValue(@Param("value")String value);
	
	/**
	 * 添加数据字典项
	 * @param dictionaryItem
	 */
	void addDictionaryItem(DictionaryItem dictionaryItem);

	/**
	 * 更新数据字典项
	 * @param dictionaryItem
	 */
	void update(DictionaryItem dictionaryItem);
	
	/**
	 * 根据id更新数据字典项的父元素（parentid）
	 * @param string
	 * @param string2
	 */
	void updateParentIdById(@Param("id") String string,@Param("parentid") String string2);

	/**
	 * 删除数据字典项
	 * @param id
	 */
	void deleteById(String id);
	
	/**
	 * 获取数据字典子项的个数
	 * @param parentid
	 * @return Integer
	 */
	Integer countAllChildItemsById(String parentid);
	
	/**
	 * 根据itemid查询属性值及对应的节点
	 * @param string
	 * @return DictionarySubItem
	 */
	List<DictionarySubItem> selectSubItemByItemId(String string);
	
	/**
	 * 根据id查询属性值及对应的节点
	 * @param id
	 * @return
	 */
	DictionarySubItem selectSubItemById(String id);


	/**
	 * 根据id删除数据字典项的属性
	 * @param id
	 */
	void deleteSubItemById(String id);
	
	/**
	 * 更新叶子节点属性
	 * @param subitem
	 */
	void updateSubItem(DictionarySubItem subitem);

	/**
	 * 添加叶子节点属性
	 * @param dictionarySubItem
	 */
	void addSubItem(DictionarySubItem dictionarySubItem);
	
	/**
	 * 获取数据字典子项的个数
	 * @param code
	 * @return Integer
	 */
	Integer countItemsByCode(String code);
	/**
	 * 根据所给的codes获取数据字典
	 */
	List<DictionaryItem> selectItemsByCodes(List<String> codes);
	
	/**
	 * 校单位信息加载sec_url表
	 * @param id
	 * @return
	 */
	List<DictionaryItem> selectChildItemsByIdForSec(String parentid);

}

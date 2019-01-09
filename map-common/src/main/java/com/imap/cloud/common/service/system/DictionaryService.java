package com.imap.cloud.common.service.system;

import java.util.List;

import com.imap.cloud.common.dto.Node;
import com.imap.cloud.common.entity.system.DictionaryItem;
import com.imap.cloud.common.entity.system.DictionarySubItem;
import com.imap.cloud.common.exception.DictionaryCodeRepeatException;

/**
 * 数据字典service层接口
 * @author 赵毕皓
 * @since 2016-10-20
 */
public interface DictionaryService {
	/**
	 * 根据parentid查询所有的子节点
	 * @param parentid
	 * @return List<TreeNode>
	 */
	public List<Node> findChildNodesById(String parentid);
	
	/**
	 * 保存数据字典项
	 * @param dictionaryItem
	 * @return String
	 * @throws DictionaryCodeRepeatException 
	 */
	public void save(DictionaryItem dictionaryItem) throws DictionaryCodeRepeatException;

	/**
	 * 根据id删除数据字典项
	 * @param id
	 */
	public void deleteDictionaryItem(DictionaryItem dictionaryItem);

	/**
	 * 根据id查询数据字典项
	 * @param id
	 * @return
	 */
	public DictionaryItem getDictionaryItemById(String id);

	/**
	 * 拖动树节点时,保存更新相应的数据到数据库中
	 * @param dictionaryItem
	 */
	public void dragAndSave(DictionaryItem dictionaryItem);
	
	/**
	 * 更新数据字典项信息
	 * @param dictionaryItem
	 * @throws DictionaryCodeRepeatException
	 */
	public void update(DictionaryItem dictionaryItem) throws DictionaryCodeRepeatException;

	/**
	 * 根据节点id获取其所有的属性
	 * @param itemid
	 * @return List<DictionarySubItem>
	 */
	public List<DictionarySubItem> getSubItemByItemid(String itemid);

	/**
	 * 根据数据字典项id获取其所有的子项
	 * @param id
	 * @return List<DictionaryItem>
	 */
	public List<DictionaryItem> getChildItemsById(String id);
	
	/**
	 * 根据分页参数page、rows及节点的id分页查询子项
	 * @param id
	 * @param page
	 * @param rows
	 * @param order 
	 * @param sort 
	 * @return List<DictionaryItem>
	 */
	public List<DictionaryItem> pageChildItemsById(String id, Integer page,
			Integer rows, String sort, String order);

	/**
	 * 根据数据字典属性id删除数据字典项的属性
	 * @param id
	 */
	public void deleteDictionarySubItemById(String id);

	/**
	 * 保存叶子节点属性
	 * @param dictionarySubItem
	 * @throws DictionaryCodeRepeatException 
	 */
	public void saveSubItem(DictionarySubItem dictionarySubItem) throws DictionaryCodeRepeatException;

	/**
	 * 更新叶子节点属性
	 * @param dictionarySubItem
	 * @throws DictionaryCodeRepeatException 
	 */
	public void updateSubItem(DictionarySubItem dictionarySubItem) throws DictionaryCodeRepeatException;

	/**
	 * 获取数据字典子项个数
	 * @param id
	 * @return Integer
	 */
	public Integer countChildItemsById(String id);

	/**
	 * 根据编码获取数据字典该项的子项及子项的属性
	 * @param string
	 * @return
	 */
	public List<DictionaryItem> getChildItemsByCode(String code);

	/**
	 * 分页获取数据字典叶子节点的属性
	 * @param id
	 * @param page
	 * @param rows
	 * @param order 
	 * @param sort 
	 * @return
	 */
	public List<DictionarySubItem> pageSubItemsByItemId(String id,
			Integer page, Integer rows, String sort, String order);

	/**
	 * 统计数据字典叶子节点的属性条数
	 * @param id
	 * @return
	 */
	public Integer countSubItemsByItemId(String id);

	public List<DictionaryItem> getByCodes(List<String> codes);
	
	public DictionaryItem getByCode(String code);
	
	/**
	 * 校单位信息加载sec_url表
	 * @param code
	 * @return
	 */
	public List<DictionaryItem> getChildItemByCodeForSec(String id);

}

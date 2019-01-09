package com.imap.cloud.common.service.system;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.dto.UnitDTO;
import com.imap.cloud.common.entity.system.UnitLabel;


/**
 * 单位（校园单位及POI单位）服务层接口
 * @author Bge
 * @since 2016-11-02
 */
public interface UnitService {

	/**
	 * 获取所有单位数据
	 * @return List<UnitLabel>
	 */
	List<UnitLabel> getUnitLabels();
	
	/**
	 * 根据id获取单位数据
	 * @return
	 */
	UnitLabel getUnitLabel(String id);

	/**
	 * 更新单位数据
	 * @param unitLabel
	 */
	void update(UnitDTO unitDTO);

	/**
	 * 保存单位数据
	 * @param unitLabel
	 */
	void save(UnitDTO unitDTO);

	/**
	 * 删除单位数据
	 * @param id
	 */
	void removeUnitLabel(String id);

	/**
	 * 向文件上传表和单位表的中间表中插入数据
	 * @param id
	 * @param i
	 */
	void saveImage(String labelId, String imageId);

	/**
	 * 根据单位的id获取该单位所有图片及默认图片
	 * @param labelId
	 * @return
	 */
	Map getImagesByLabelId(String labelId);

	/**
	 * 根据单位及图片的id删除他们中间表中相应的记录
	 * @param labelId
	 * @param imageId
	 */
	void delImage(String labelId, String imageId);

	/**
	 *  给单位设置默认图片
	 * @param labelId
	 * @param imageId
	 */
	void setDefaultImage(String labelId,String imageId);

	/**
	 * 根据查询参数分页查询单位数据
	 * @param conditionMap 查询参数的Map集合
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	Pager<UnitLabel> pageUnitLabelsByConditions(Integer pageNum,Integer pageSize,Map conditionMap);
	
	/**
	 * 根据传来的查询条件查询所有的单位数据
	 * @param parameterMap
	 * @return
	 */
	List<UnitLabel> listUnitLabelsByConditions(Map parameterMap);
	
	/**
	 * 根据关键字查询所有的单位数据（地址、名称）
	 * @param parameterMap
	 * @return
	 */
	Pager<UnitLabel> search(String keyWord, Integer pageSize,Integer currentPage);

	public void addImage(String labelId, String oldImageId, String title,
			String description,HttpServletRequest request);
	
	List<Map> selectSecByParentIds(List list);

}

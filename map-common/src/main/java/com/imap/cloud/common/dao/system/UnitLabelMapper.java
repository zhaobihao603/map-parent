package com.imap.cloud.common.dao.system;

import com.imap.cloud.common.dto.UnitDTO;
import com.imap.cloud.common.entity.system.UnitLabel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * 单位数据层接口
 * @author Bge
 * @since 2016-11-02
 */
@Repository
public interface UnitLabelMapper {
	/**
	 * 根据id查询单位数据
	 * @param id
	 * @return
	 */
	UnitLabel selectByPk(String id);
	
	/**
	 * 查询所有的单位数据
	 * @return List<UnitLabel>
	 */
	List<UnitLabel> selectUnitLabels();
	
	/**
	 * 插入单位数据
	 * @param unitLabel
	 */
	void insert(UnitDTO unitDTO);
	
	/**
	 * 根据id删除相应的单位数据（直接从单位表移除该条数据）
	 * @param id
	 */
	void deleteUnitLabelById(String id);
	
	/**
	 * 根据id删除相应的单位数据（仅仅更新单位表中该条单位数据的deleted标记字段）
	 * @param id
	 */
	void deleteUnitLabelById1(String id);
	
	/**
	 * 修改单位数据
	 * @param unitLabel
	 */
	void update(UnitDTO unitDTO);
	
	/**
	 * 根据实体字段相应的修改表数据
	 * @param unitLabel
	 */
	void updateByPkSelective(UnitDTO unitDTO);

	/**
	 * 向单位和图片的中间表中插入一条数据
	 * @param labelId
	 * @param imageId
	 */
	void insertImage(@Param("labelId")String labelId, @Param("imageId")String imageId);

	/**
	 * 根据单位id从中间表查询其图片的id
	 * @param labelId
	 * @return
	 */
	List<String> selectImagesIdByLabelId(String labelId);

	/**
	 * 根据单位及图片的id删除他们中间表中相应的记录
	 * @param labelId
	 * @param imageId
	 */
	void deleteUnitImage(@Param("labelId")String labelId, @Param("imageId")String imageId);
	
	/**
	 * 根据单位id删除中间表中对应的n数据
	 * @param labelId
	 */
	void deleteUnitImages(@Param("labelId")String labelId);

	/**
	 * 更新单位默认图片
	 * @param labelId
	 * @param imageId
	 */
	void updateImageId(@Param("labelId")String labelId, @Param("imageId")String imageId);

	/**
	 * 根据主次单位类型及名称查询单位数据
	 * @param conditionMap
	 * @return
	 */
	List<UnitLabel> selectUnitLabelsByConditions(Map conditionMap);
	
	/**
	 * 根据主次单位类型及名称、地址查询单位数据
	 * @param conditionMap
	 * @return
	 */
	List<UnitLabel> searchUnitLabels(Map conditionMap);
	
	/**
	 * 根据parentid查单位类别
	 * @param id
	 * @return
	 */
	List<Map> selectSecByParentIds(List list);
}

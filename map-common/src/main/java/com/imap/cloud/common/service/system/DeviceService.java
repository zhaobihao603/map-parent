package com.imap.cloud.common.service.system;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.imap.cloud.common.dto.DeviceDTO;
import com.imap.cloud.common.entity.system.Device;


/**
 * 设备管理服务层接口
 * @author Bge
 * @since 2016-11-02
 */
public interface DeviceService {

	List<Device> selectByParentType(String parentType);
	
	int countByParentType(String parentType);
	
	void updateImage(HttpServletRequest request) ;
	
	List<Device> selectByConditionMap(DeviceDTO deviceDTO);
	
	int countByConditionMap(DeviceDTO deviceDTO);
	
	List<String> comboboxType(String parentType);
	
	void deleteById(String id);
	
	void deleteByParentId(String parentid);
	
	void insert(DeviceDTO deviceDTO);
	
	void updateById(DeviceDTO deviceDTO);
	
	Map<Object,Object> treeData(); 
	
	String writeInCss(String picUrl);
	
	String removeInCss();
}

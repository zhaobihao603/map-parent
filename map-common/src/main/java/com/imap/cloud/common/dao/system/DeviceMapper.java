package com.imap.cloud.common.dao.system;

import java.util.List;
import java.util.Map;

import com.imap.cloud.common.dto.DeviceDTO;
import com.imap.cloud.common.entity.system.Device;
import org.springframework.stereotype.Repository;

/**
 * 设备数据层接口
 * @author Bge
 * @since 2016-11-02
 */
@Repository
public interface DeviceMapper {
	
	List<Device> selectByParentType(String parentType);
	
	int countByParentType(String parentType);
	
	void updateById(DeviceDTO deviceDTO);
	
	List<Device> selectById(DeviceDTO deviceDTO);
	
	List<Device> selectByConditionMap(DeviceDTO deviceDTO);
	
	int countByConditionMap(DeviceDTO deviceDTO);
	
	List<String> comboboxType(String parentType);
	
	void deleteById(String id);
	
	void deleteByParentId(String parentid);
	
	void insert(DeviceDTO deviceDTO);
	
	List<Device> selectIsStatus();
}

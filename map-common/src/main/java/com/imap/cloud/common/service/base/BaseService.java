package com.imap.cloud.common.service.base;

import java.io.Serializable;
import java.util.List;

import com.imap.cloud.common.dto.Pager;

/**
 * 父类Service
 * @author 冯林
 * @param <T> 实体对象
 */
public interface BaseService<T, PK extends Serializable> {
	
	 /**
	  * 添加单条数据
	  * @param entity 
	  * @throws Exception
	  */
	 void insert(T entity) throws Exception;
	 
	/**
	 * 添加多条数据
	 * @param entity
	 * @throws Exception
	 */
	 void insert(List<T> entity) throws Exception;
	
	 /**
	  * 根据对象主键删除,进行多条数据的删除
	  * @param id
	  * @return
	  * @throws Exception
	  */
	 void deleteByPk(PK[] id) throws Exception;
	 
	 /**
	  * 根据对象主键删除,进行 单条数据的删除
	  * @param id
	  * @return
	  * @throws Exception
	  */
	 void deleteByPk(PK id) throws Exception;
	 
	 /**
	  * 根据对象主键修改
	  * @param entity
	  * @return
	  * @throws Exception
	  */
	 void updateByPk(T entity) throws Exception;
	 
	 /**
	  * 单条数据查询
	  * @param object 查询参数
	  * @return T
	  * @throws Exception
	  */
	 T selectByPk(PK id) throws Exception;
	 
	 /**
	  * 多条数据查询
	  * @param object 查询参数
	  * @return T
	  * @throws Exception
	  */
	 List<T> selectByPkList(PK id) throws Exception;

	 /**
	  * 返回所以数据
	  * @return List<T>
	  * @throws Exception
	  */
	 List<T> findAll() throws Exception;
	 
	 /**
	 * 分页数据,根据entity的非null属性值进行过滤
	 * @param entity  过滤属性集
	 * @param pageNum 默认是1
	 * @param pageSize 默认是10
	 * @return	Pager<T>
	 */
	Pager<T> selectPage(T entity,Integer pageNum,Integer pageSize) throws Exception;

	
}

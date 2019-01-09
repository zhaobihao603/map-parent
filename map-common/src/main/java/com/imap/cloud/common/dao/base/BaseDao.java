package com.imap.cloud.common.dao.base;

import java.io.Serializable;
import java.util.List;

/**
 * 父类Dao
 * @author 冯林
 * @param <T>	实体对象
 */
public interface BaseDao<T,PK extends Serializable> {
	
	/**
     * 插入一条数据,只插入不为null的字段,不会影响有默认值的字段
     * @param record
     * @return
     */
    int insertSelective(T entity) throws Exception;
	
	/**
	  * 添加单条数据
	  * @param entity 
	  * @throws Exception
	  */
	int insert(T entity) throws Exception;
	
	/**
	  * 根据对象主键删除,进行 单条数据的删除
	  * @param id
	  * @return
	  * @throws Exception
	  */
	int deleteByPk(PK id) throws Exception;
	
	/**
	  * 根据对象主键修改
	  * @param entity
	  * @return
	  * @throws Exception
	  */
	int updateByPk(T entity) throws Exception;
	
	/**
	  * 根据对象主键定向更新,没有属性值时，会忽略更新，保有原来的值
	  * @param entity
	  * @return
	  * @throws Exception
	  */
	int updateByPkSelective(T entity);
	
	/**
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
     * 根据非null属性值进行过滤
     * @param record
     * @return
     */
	List<T> selectAll(T entity) throws Exception;
	
	/**
     * 根据非null属性值作为条件统计记录数
     * @param record
     * @return
     */
	int count(T entity) throws Exception;
	 
}

package com.imap.cloud.common.service.base.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.service.base.BaseService;

/**
 * 父类Service实现类
 * @author 冯林
 *
 * @param <T>  实体对象
 */
public abstract class BaseServiceImpl<T,PK extends Serializable> implements BaseService<T,PK> {
	
	private BaseDao<T,PK> baseDao;
	
	public void setBaseDao(BaseDao<T,PK> baseDao) {
		this.baseDao = baseDao;
	}
	
	@Transactional
	public void insert(T entity) throws Exception {
		baseDao.insertSelective(entity);
	}

	@Transactional
	public void insert(List<T> list) throws Exception {
		for(T entity:list){
			baseDao.insertSelective(entity);
		}
	}

	@Transactional
	public void deleteByPk(PK[] ids) throws Exception {
		for(PK id:ids){
			deleteByPk(id);
		}
	}

	@Transactional
	public void deleteByPk(PK id) throws Exception {
		baseDao.deleteByPk(id);
	}
	
	@Transactional
	public void updateByPk(T entity) throws Exception {
		baseDao.updateByPkSelective(entity);
	}

	public T selectByPk(PK id) throws Exception {
		return baseDao.selectByPk(id);
	}
	
	public List<T> selectByPkList(PK id) throws Exception {
		return baseDao.selectByPkList(id);
	};
	
	public List<T> findAll() throws Exception {
		return baseDao.selectAll(null);
	}
	
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	public Pager<T> selectPage(T entity,Integer pageNum,Integer pageSize) throws Exception {
		//PageHelper只对紧跟着的第一个SQL语句起作用
		Page<T> page = PageHelper.startPage(pageNum==null?1:pageNum,pageSize==null?10:pageSize,true);
        List<T> pageList = baseDao.selectAll(entity);
		//可以用PageInfo对结果进行包装，PageInfo包装过度，可以用本项目的Pager
		return new Pager(pageList);
	}

}

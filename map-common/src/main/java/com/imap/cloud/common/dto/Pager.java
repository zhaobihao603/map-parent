package com.imap.cloud.common.dto;

import java.io.Serializable;
import java.util.List;

import com.github.pagehelper.Page;

public class Pager<T> implements Serializable {
	    private static final long serialVersionUID = 8656597559014685635L;
	    private long total;        //总记录数
	    private List<T> list;    //结果集
	    private int pageNum;    // 第几页
	    private int pageSize;    // 每页记录数
	    private int pages;        // 总页数
	    private int size;        // 当前页的数量 <= pageSize，该属性来自ArrayList的size属性
	    
	    //搜索条件内容
		private String searchContent;
	    
		public Pager(){
			
		}
		
	    /**
	     * 包装Pager对象，因为直接返回Pager对象，在JSON处理以及其他情况下会被当成List来处理，
	     * 而出现一些问题。
	     * @param list          page结果(里面实际上是一个Page对象)
	     * @param navigatePages 页码数量
	     */
	    public Pager(List<T> list) {
	        if (list instanceof Page) {
	            Page<T> page = (Page<T>) list;
	            this.pageNum = page.getPageNum();
	            this.pageSize = page.getPageSize();
	            this.total = page.getTotal();
	            this.pages = page.getPages();
	            this.list = page;
	            this.size = page.size();
	        }
	    }
	    
	    /**
	     * 包装Pager对象
	     * @param list 单纯的list
	     * @param pagination 不包含结果集的Page，含有一下属性
	     * 	 pageNum 当前页码
	     * 	 pageSize 每页设定的记录条数
	     * 	 total 总共记录数
	     * 	 pages 总页数
	     * 	 size 当前页记录条数，总是<=pageSize
	     */
	    public Pager(List<T> list,Page<T> pagination) {
	    	this.pageNum = pagination.getPageNum();
            this.pageSize = pagination.getPageSize();
            this.total = pagination.getTotal();
            this.pages = pagination.getPages();
            this.list = list;
            this.size = pagination.size();
	    }
	    
	    /**
	     * 包装Pager对象
	     * @param list 单纯的list
	     * @param pageNum 当前页码
	     * @param pageSize 每页设定的记录条数
	     * @param total 总共记录数
	     * @param pages 总页数
	     * @param size 当前页记录条数，总是<=pageSize
	     */
	    public Pager(List<T> list,int pageNum,int pageSize,int total,int pages,int size) {
	            this.pageNum = pageNum;
	            this.pageSize = pageSize;
	            this.total = total;
	            this.pages = pages;
	            this.list = list;
	            this.size = size;
	    }

		public long getTotal() {
	        return total;
	    }

	    public void setTotal(long total) {
	        this.total = total;
	    }

	    public List<T> getList() {
	        return list;
	    }

	    public void setList(List<T> list) {
	        this.list = list;
	    }

	    public int getPageNum() {
	        return pageNum;
	    }

	    public void setPageNum(int pageNum) {
	        this.pageNum = pageNum;
	    }

	    public int getPageSize() {
	        return pageSize;
	    }

	    public void setPageSize(int pageSize) {
	        this.pageSize = pageSize;
	    }

	    public int getPages() {
	        return pages;
	    }

	    public void setPages(int pages) {
	        this.pages = pages;
	    }

	    public int getSize() {
	        return size;
	    }

	    public void setSize(int size) {
	        this.size = size;
	    }

		public String getSearchContent() {
			return searchContent;
		}

		public void setSearchContent(String searchContent) {
			this.searchContent = searchContent;
		}
	    
	}

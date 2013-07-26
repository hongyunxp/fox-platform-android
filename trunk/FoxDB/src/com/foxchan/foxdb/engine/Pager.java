package com.foxchan.foxdb.engine;

import java.util.List;

/**
 * 分页对象
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-13
 */
public class Pager<T> {
	
	/** 分页查询的具体数据 */
	private List<T> content;
	/** 分页的起始页 */
	private int startPage = 1;
	/** 分页的结束页 */
	private int endPage;
	/** 每一页显示的记录数 */
	private int recordsNumber = 15;
	/** 当前的页数 */
	private int currentPage = 1;
	/** 总页数 */
	private int totalPage;
	/** 总记录数 */
	private int totalRecordsNumber;
	/** 查询的其实索引值 */
	private int startIndex;
	
	public Pager(){}

	/**
	 * 构造一个分页对象
	 * @param recordsNumber	每一页显示的记录数
	 * @param currentPage	当前的页码
	 */
	public Pager(int recordsNumber, int currentPage) {
		this.recordsNumber = recordsNumber;
		this.currentPage = currentPage;
		caculateStartIndex();
	}
	
	/**
	 * 计算当前的起始查询索引
	 */
	private void caculateStartIndex(){
		this.startIndex = (currentPage - 1) * recordsNumber;
	}

	/**
	 * 获得分页查询的数据信息部分
	 * @return	返回查询到的分页信息的数据部分
	 */
	public List<T> getContent() {
		return content;
	}

	/**
	 * 设置分页查询的数据部分
	 * @param content	分页查询的结果信息
	 */
	public void setContent(List<T> content) {
		this.content = content;
	}

	/**
	 * 获得分页的起始页
	 * @return	返回分页的起始页
	 */
	public int getStartPage() {
		return startPage;
	}

	/**
	 * 设置分页的起始页
	 * @param startPage	分页的起始页
	 */
	protected void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	/**
	 * 获得分页的结束页
	 * @return	返回分页的结束页
	 */
	public int getEndPage() {
		return endPage;
	}

	/**
	 * 设置分页的结束页
	 * @param endPage	分页的结束页
	 */
	protected void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	/**
	 * 获得每一页显示的记录数
	 * @return	返回当前分页中每一页的显示记录数
	 */
	public int getRecordsNumber() {
		return recordsNumber;
	}

	/**
	 * 设置当前分页中每一页显示的记录数
	 * @param recordsNumber	当前分页中每一页显示的记录数
	 */
	protected void setRecordsNumber(int recordsNumber) {
		this.recordsNumber = recordsNumber;
	}

	/**
	 * 获得当前的页数
	 * @return	返回当前的页数
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * 设置当前的页数
	 * @param currentPage	当前的页数
	 */
	protected void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * 获得分页的总页数
	 * @return	返回分页的总页数
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * 设置分页的总页数
	 * @param totalPage	分页的总页数
	 */
	protected void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * 获得查询的总记录数
	 * @return	返回查询的总记录数
	 */
	public int getTotalRecordsNumber() {
		return totalRecordsNumber;
	}

	/**
	 * 设置查询的总记录数
	 * @param totalRecordsNumber	查询的总记录数
	 */
	public void setTotalRecordsNumber(int totalRecordsNumber) {
		this.totalRecordsNumber = totalRecordsNumber;
		totalPage = (totalRecordsNumber % recordsNumber == 0)?
				(totalRecordsNumber / recordsNumber) : (totalRecordsNumber / recordsNumber + 1);
	}
	
	public int getStartIndex() {
		return startIndex;
	}

	protected void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * 获取上一页的页码
	 * @return
	 */
	public int getPrevPage(){
		return currentPage - 1;
	}
	
	/**
	 * 获取下一页的页码
	 * @return
	 */
	public int getNextPage(){
		return currentPage + 1;
	}
	
	/**
	 * 跳转到上一页
	 */
	public void prevPage(){
		if(hasPrevPage()){
			currentPage--;
			caculateStartIndex();
		}
	}
	
	/**
	 * 跳转到下一页
	 */
	public void nextPage(){
		if(hasNextPage()){
			currentPage++;
			caculateStartIndex();
		}
	}
	
	/**
	 * 判断当前的分页对象是否还有下一页
	 * @return	如果下一页还有内容则返回true，否则返回false
	 */
	public boolean hasNextPage(){
		if(currentPage >= totalPage){
			return false;
		}
		return true;
	}
	
	/**
	 * 判断当前的分页对象是否还有上一页
	 * @return	如果还有上一页，则返回true，否则返回false
	 */
	public boolean hasPrevPage(){
		if(currentPage <= 1){
			return false;
		}
		return true;
	}
	
}
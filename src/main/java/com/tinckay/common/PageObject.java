package com.tinckay.common;

import java.util.List;

public class PageObject<T>{

	//每页数量
	private static final int size = 20;
	//总记录数
	private long totalElements;
	//总页数
	private int totalPages;
	//当前页码
	private int page;
	//当前页面包含的数据
	List<T>  dataList;
	public long getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public List<T> getDataList() {
		return dataList;
	}
	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}
	public static int getSize() {
		return size;
	}
	
	
}

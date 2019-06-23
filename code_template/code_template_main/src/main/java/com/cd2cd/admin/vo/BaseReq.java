package com.cd2cd.admin.vo;

import javax.validation.Valid;


public class BaseReq<T> {

	@Valid
	private T data;

	private Integer currPage;
	private Integer pageSize;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Integer getCurrPage() {
		return currPage;
	}

	public void setCurrPage(Integer currPage) {
		this.currPage = currPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}

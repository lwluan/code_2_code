package com.boyu.code_microservice_repository.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 公共数据分页包装对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataPageWrapper<T> {

	/** 列表数据 */
	private List<T> rows;

	/** 总页数 */
	private Integer totalPage;

	/** 总条数 */
	private Long totalCount;

	/** 当前页码 */
	private Integer currPage;

	/** 限制可访问总页数 */
	private Integer limitPage;

	/** 每页显示记录条数 */
	private Integer pageSize;

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public Integer getTotalPage() {
		if( null == totalPage && totalCount != null && pageSize != null ) {
			return (int) (totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1);
		}
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getCurrPage() {
		return currPage;
	}

	public void setCurrPage(Integer currPage) {
		this.currPage = currPage;
	}

	public Integer getLimitPage() {
		return limitPage;
	}

	public void setLimitPage(Integer limitPage) {
		this.limitPage = limitPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}

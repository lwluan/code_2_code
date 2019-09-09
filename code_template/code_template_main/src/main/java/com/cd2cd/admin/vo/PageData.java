package com.cd2cd.admin.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 公共数据分页包装对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageData<T> {

	/** 列表数据 */
	private List<T> rows;

	/** 总条数 */
	private Long total;

	/** 当前页码 */
	private Integer current;

	/** 每页显示记录条数 */
	private Integer pageSize;

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Integer getCurrent() {
		return current;
	}

	public void setCurrent(Integer current) {
		this.current = current;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}

package com.cd2cd.domain;

import java.io.Serializable;
import java.util.List;

import com.cd2cd.domain.gen.SuperProFile;

@SuppressWarnings("serial")
public class ProFile extends SuperProFile implements Serializable {
    
    private List<ProTableColumn> columns;

	public List<ProTableColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<ProTableColumn> columns) {
		this.columns = columns;
	}
    
    
}
package com.cd2cd.dom.java;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FunReturnType {

	private Set<String> typePaths = new HashSet<String>();
	private List<Long> typeIds = new ArrayList<Long>();

	public Set<String> getTypePaths() {
		return typePaths;
	}

	public void setTypePaths(Set<String> typePaths) {
		this.typePaths = typePaths;
	}

	public List<Long> getTypeIds() {
		return typeIds;
	}

	public void setTypeIds(List<Long> typeIds) {
		this.typeIds = typeIds;
	}

}

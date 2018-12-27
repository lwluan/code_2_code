package com.cd2cd.vo;

import javax.validation.constraints.NotNull;

import com.cd2cd.domain.ProFunArg;
import com.cd2cd.vo.validator.Valid;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProFunArgVo extends ProFunArg implements Valid {

	private static final long serialVersionUID = 1L;
	
	@NotNull(groups = { modify.class })
	@Override
	public Long getFunId() {
		return super.getFunId();
	}
	
	@NotNull(groups = { modify.class, add.class })
	@Override
	public String getName() {
		return super.getName();
	}
	
	@NotNull(groups = { modify.class, add.class })
    @Override
    public String getArgType() {
    	return super.getArgType();
    }

	@NotNull(groups = { modify.class, add.class })
    @Override
    public String getCollectionType() {
    	return super.getCollectionType();
    }
	
}

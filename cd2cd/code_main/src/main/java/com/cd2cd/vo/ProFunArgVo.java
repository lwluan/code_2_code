package com.cd2cd.vo;

import javax.validation.constraints.NotNull;

import com.cd2cd.domain.ProFunArg;
import com.cd2cd.vo.validator.Valid;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProFunArgVo extends ProFunArg implements Valid {
	
	// 添加验证
	public static interface addByFieldId{ }
	
	private static final long serialVersionUID = 1L;
	
	@NotNull(groups = { modify.class })
	@Override
	public Long getId() {
		return super.getId();
	}
	
	@NotNull(groups = { add.class, addByFieldId.class })
	@Override
	public Long getFunId() {
		return super.getFunId();
	}
	
	@NotNull(groups = { add.class })
	@Override
	public String getName() {
		return super.getName();
	}
	
	@NotNull(groups = { add.class })
    @Override
    public String getArgType() {
    	return super.getArgType();
    }

	@NotNull(groups = { add.class })
    @Override
    public String getCollectionType() {
    	return super.getCollectionType();
    }
	
	@NotNull(groups = {addByFieldId.class })
	@Override
	public Long getFieldId() {
		return super.getFieldId();
	}
	
	@NotNull(groups = {addByFieldId.class })
	@Override
	public Long getPid() {
		return super.getPid();
	}
	
}

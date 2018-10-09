package com.cd2cd.vo;

import javax.validation.constraints.NotNull;

import com.cd2cd.domain.ProField;
import com.fasterxml.jackson.annotation.JsonInclude;


@SuppressWarnings("serial")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProFieldVo extends ProField {

	@NotNull
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return super.getName();
	}
}

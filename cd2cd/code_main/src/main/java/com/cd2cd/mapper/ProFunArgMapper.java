package com.cd2cd.mapper;

import java.util.List;

import com.cd2cd.domain.ProFunArg;
import com.cd2cd.mapper.gen.SuperProFunArgMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProFunArgMapper extends SuperProFunArgMapper {
	
	List<ProFunArg> fetchFunArgsByFunId(@Param("funId") Long funId);
	
	List<ProFunArg> fetchFunArgsChildrenById(@Param("pid") Long pid);
	
}
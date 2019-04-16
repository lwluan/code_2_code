package com.cd2cd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cd2cd.domain.ProTable;
import com.cd2cd.mapper.gen.SuperProTableMapper;

@Mapper
public interface ProTableMapper extends SuperProTableMapper {

	List<ProTable> selectTableAndColumnByDbId(@Param("dbIds") List<Long> dbIds);
	
}
package com.cd2cd.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cd2cd.mapper.gen.SuperProDatabaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProDatabaseMapper extends SuperProDatabaseMapper {

    @Select("select db_name from pro_database where id = (select database_id from pro_table where id=#{superId})")
    @ResultType(String.class)
    String selectDbNameByTableId(@Param("superId") Long superId);

}
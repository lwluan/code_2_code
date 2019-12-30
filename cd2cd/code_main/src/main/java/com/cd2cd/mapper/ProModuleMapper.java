package com.cd2cd.mapper;

import com.cd2cd.domain.ProModule;
import com.cd2cd.mapper.gen.SuperProModuleMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProModuleMapper extends SuperProModuleMapper {


    @Select("select pro_module.*, (select name from pro_micro_service where id=micro_id) microName from pro_module where project_id=#{projectId} and del_flag = 0 order by id desc,micro_id desc")
    @ResultMap("BaseResultMap")
    List<ProModule> projectModuleList(@Param("projectId") Long projectId);

}
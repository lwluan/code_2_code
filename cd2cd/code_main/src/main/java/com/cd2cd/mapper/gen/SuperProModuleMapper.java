package com.cd2cd.mapper.gen;

import com.cd2cd.domain.ProModule;
import com.cd2cd.domain.gen.ProModuleCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuperProModuleMapper {
    long countByExample(ProModuleCriteria example);

    int deleteByExample(ProModuleCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(ProModule record);

    int insertSelective(ProModule record);

    List<ProModule> selectByExample(ProModuleCriteria example);

    ProModule selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ProModule record, @Param("example") ProModuleCriteria example);

    int updateByExample(@Param("record") ProModule record, @Param("example") ProModuleCriteria example);

    int updateByPrimaryKeySelective(ProModule record);

    int updateByPrimaryKey(ProModule record);
}
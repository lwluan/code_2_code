package com.cd2cd.mapper.gen;

import com.cd2cd.domain.ProProject;
import com.cd2cd.domain.gen.ProProjectCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuperProProjectMapper {
    long countByExample(ProProjectCriteria example);

    int deleteByExample(ProProjectCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(ProProject record);

    int insertSelective(ProProject record);

    List<ProProject> selectByExample(ProProjectCriteria example);

    ProProject selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ProProject record, @Param("example") ProProjectCriteria example);

    int updateByExample(@Param("record") ProProject record, @Param("example") ProProjectCriteria example);

    int updateByPrimaryKeySelective(ProProject record);

    int updateByPrimaryKey(ProProject record);
}
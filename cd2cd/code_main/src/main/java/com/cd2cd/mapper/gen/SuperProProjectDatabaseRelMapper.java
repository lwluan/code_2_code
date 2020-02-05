package com.cd2cd.mapper.gen;

import com.cd2cd.domain.ProProjectDatabaseRel;
import com.cd2cd.domain.gen.ProProjectDatabaseRelCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuperProProjectDatabaseRelMapper {
    long countByExample(ProProjectDatabaseRelCriteria example);

    int deleteByExample(ProProjectDatabaseRelCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(ProProjectDatabaseRel record);

    int insertSelective(ProProjectDatabaseRel record);

    List<ProProjectDatabaseRel> selectByExample(ProProjectDatabaseRelCriteria example);

    ProProjectDatabaseRel selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ProProjectDatabaseRel record, @Param("example") ProProjectDatabaseRelCriteria example);

    int updateByExample(@Param("record") ProProjectDatabaseRel record, @Param("example") ProProjectDatabaseRelCriteria example);

    int updateByPrimaryKeySelective(ProProjectDatabaseRel record);

    int updateByPrimaryKey(ProProjectDatabaseRel record);
}
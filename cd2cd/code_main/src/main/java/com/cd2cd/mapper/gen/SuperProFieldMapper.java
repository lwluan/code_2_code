package com.cd2cd.mapper.gen;

import com.cd2cd.domain.ProField;
import com.cd2cd.domain.gen.ProFieldCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuperProFieldMapper {
    long countByExample(ProFieldCriteria example);

    int deleteByExample(ProFieldCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(ProField record);

    int insertSelective(ProField record);

    List<ProField> selectByExample(ProFieldCriteria example);

    ProField selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ProField record, @Param("example") ProFieldCriteria example);

    int updateByExample(@Param("record") ProField record, @Param("example") ProFieldCriteria example);

    int updateByPrimaryKeySelective(ProField record);

    int updateByPrimaryKey(ProField record);
}
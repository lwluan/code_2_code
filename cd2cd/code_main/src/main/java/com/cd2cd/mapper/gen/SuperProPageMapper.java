package com.cd2cd.mapper.gen;

import com.cd2cd.domain.ProPage;
import com.cd2cd.domain.gen.ProPageCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuperProPageMapper {
    long countByExample(ProPageCriteria example);

    int deleteByExample(ProPageCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(ProPage record);

    int insertSelective(ProPage record);

    List<ProPage> selectByExample(ProPageCriteria example);

    ProPage selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ProPage record, @Param("example") ProPageCriteria example);

    int updateByExample(@Param("record") ProPage record, @Param("example") ProPageCriteria example);

    int updateByPrimaryKeySelective(ProPage record);

    int updateByPrimaryKey(ProPage record);
}
package com.cd2cd.mapper.gen;

import com.cd2cd.domain.ProTableColumn;
import com.cd2cd.domain.gen.ProTableColumnCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuperProTableColumnMapper {
    long countByExample(ProTableColumnCriteria example);

    int deleteByExample(ProTableColumnCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(ProTableColumn record);

    int insertSelective(ProTableColumn record);

    List<ProTableColumn> selectByExample(ProTableColumnCriteria example);

    ProTableColumn selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ProTableColumn record, @Param("example") ProTableColumnCriteria example);

    int updateByExample(@Param("record") ProTableColumn record, @Param("example") ProTableColumnCriteria example);

    int updateByPrimaryKeySelective(ProTableColumn record);

    int updateByPrimaryKey(ProTableColumn record);
}
package com.cd2cd.mapper.gen;

import com.cd2cd.domain.ProTable;
import com.cd2cd.domain.gen.ProTableCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuperProTableMapper {
    long countByExample(ProTableCriteria example);

    int deleteByExample(ProTableCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(ProTable record);

    int insertSelective(ProTable record);

    List<ProTable> selectByExample(ProTableCriteria example);

    ProTable selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ProTable record, @Param("example") ProTableCriteria example);

    int updateByExample(@Param("record") ProTable record, @Param("example") ProTableCriteria example);

    int updateByPrimaryKeySelective(ProTable record);

    int updateByPrimaryKey(ProTable record);
}
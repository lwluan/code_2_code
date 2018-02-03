package com.cd2cd.mapper.gen;

import com.cd2cd.domain.ProFile;
import com.cd2cd.domain.gen.ProFileCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuperProFileMapper {
    long countByExample(ProFileCriteria example);

    int deleteByExample(ProFileCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(ProFile record);

    int insertSelective(ProFile record);

    List<ProFile> selectByExample(ProFileCriteria example);

    ProFile selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ProFile record, @Param("example") ProFileCriteria example);

    int updateByExample(@Param("record") ProFile record, @Param("example") ProFileCriteria example);

    int updateByPrimaryKeySelective(ProFile record);

    int updateByPrimaryKey(ProFile record);
}
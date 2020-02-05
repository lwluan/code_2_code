package com.cd2cd.mapper.gen;

import com.cd2cd.domain.ProMicroService;
import com.cd2cd.domain.gen.ProMicroServiceCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuperProMicroServiceMapper {
    long countByExample(ProMicroServiceCriteria example);

    int deleteByExample(ProMicroServiceCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(ProMicroService record);

    int insertSelective(ProMicroService record);

    List<ProMicroService> selectByExample(ProMicroServiceCriteria example);

    ProMicroService selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ProMicroService record, @Param("example") ProMicroServiceCriteria example);

    int updateByExample(@Param("record") ProMicroService record, @Param("example") ProMicroServiceCriteria example);

    int updateByPrimaryKeySelective(ProMicroService record);

    int updateByPrimaryKey(ProMicroService record);
}